package cilib

import _root_.scala.Predef.{any2stringadd => _}

import scalaz._

import monocle.syntax._
import Position._

import spire.algebra._
import spire.implicits._
import spire.syntax.module._

object PSO {
  import Lenses._

  // Constrain this better - Not numeric. Operation for vector addition
  def stdPosition[S,F[_],A](
    c: Particle[S,F,A],
    v: Position[F,A]
  )(implicit A: Module[F[A],A]): Step[F,A,Particle[S,F,A]] =
    Step.point(_position.modify((_: Position[F,A]) + v)(c))

  // Dist \/ Double (scalar value)
  // This needs to be fleshed out to cater for the parameter constants // remember to extract Dists
  def stdVelocity[S,F[_]:Traverse](
    entity: Particle[S,F,Double],
    social: Position[F,Double],
    cognitive: Position[F,Double],
    w: Double,
    c1: Double,
    c2: Double
  )(implicit V: Velocity[S,F,Double], M: Module[F[Double],Double], F:Field[Double]): Step[F,Double,Position[F,Double]] =
    Step.pointR(for {
      cog <- (cognitive - entity.pos) traverse (x => Dist.stdUniform.map(_ * x))
      soc <- (social    - entity.pos) traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(entity.state)) + (c1 *: cog) + (c2 *: soc))

  // Step to evaluate the particle, without any modifications
  def evalParticle[S,F[_]:Foldable](entity: Particle[S,F,Double]) =
    Entity.eval[S,F,Double](x => x)(entity)

  def updatePBest[S,F[_]](p: Particle[S,F,Double])(implicit M: Memory[S,F,Double]): Step[F,Double,Particle[S,F,Double]] = {
    val pbestL = M._memory
    Step.liftK(Fitness.compare(p.pos, (p.state applyLens pbestL).get).map(x =>
      Entity(p.state applyLens pbestL set x, p.pos)))
  }

  def updateVelocity[S,F[_]](p: Particle[S,F,Double], v: Position[F,Double])(implicit V: Velocity[S,F,Double]): Step[F,Double,Particle[S,F,Double]] =
    Step.pointR(RVar.point(Entity(p.state applyLens V._velocity set v, p.pos)))

  def singleComponentVelocity[S,F[_]:Traverse](
    entity: Particle[S,F,Double],
    component: Position[F,Double],
    w: Double,
    c: Double
  )(implicit V: Velocity[S,F,Double], M: Memory[S,F,Double], MO: Module[F[Double],Double]): Step[F,Double,Position[F,Double]] = {
    Step.pointR(for {
      comp <- (component - entity.pos) traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(entity.state)) + (c *: comp))
  }

  case class GCParams(p: Double, successes: Int, failures: Int, e_s: Double, e_f: Double)

  def defaultGCParams = GCParams(1.0, 0, 0, 15, 5)

  def gcVelocity[S,F[_]:Traverse](
    entity: Particle[S,F,Double],
    nbest: Position[F,Double],
    w: Double,
    s: GCParams
  )(implicit V: Velocity[S,F,Double], M: Module[F[Double],Double]): Step[F,Double,Position[F,Double]] =
    Step.pointR(
      nbest traverse (_ => Dist.stdUniform.map(x => s.p * (1 - 2*x))) map (a =>
        -1.0 *: entity.pos + nbest + w *: V._velocity.get(entity.state) + a
      ))

  def barebones[S,F[_]:Traverse:Zip](p: Particle[S,F,Double], global: Position[F,Double])(implicit M: Memory[S,F,Double]) =
    Step.pointR {
      val pbest = M._memory.get(p.state)
      val zipped = pbest.zip(global)
      val sigmas = zipped map { case (x,y) => math.abs(x - y) }
      val means  = zipped map { case (x,y) => (x + y) / 2.0 }

      (means zip sigmas) traverse (x => Dist.gaussian(x._1, x._2))
    }

  def quantum[S,F[_]:Traverse](
    collection: List[Particle[S,F,Double]],
    x: Particle[S,F,Double],
    center: Position[F,Double],
    r: Double
  )(implicit M: Module[F[Double],Double]): Step[F,Double,Position[F,Double]] =
    Step.pointR(
      for {
        u <- Dist.stdUniform
        rand_x <- x.pos.traverse(_ => Dist.stdNormal)
      } yield {
        import scalaz.syntax.foldable._
        val sum_sq = rand_x.pos.foldLeft(0.0)((a,c) => a + c*c)
        val scale: Double = r * math.pow(u, 1.0 / x.pos.pos.length) / math.sqrt(sum_sq)
        (scale *: rand_x) + center
      }
    )

  def acceleration[S,F[_]:Functor](
    collection: List[Particle[S,F,Double]],
    x: Particle[S,F,Double],
    distance: (Position[F,Double], Position[F,Double]) => Double,
    rp: Double,
    rc: Double)(
    implicit C: Charge[S], MO: Module[F[Double],Double]): Step[F,Double,Position[F,Double]] = {
    def charge(x: Particle[S,F,Double]) =
      C._charge.get(x.state)

    Step.point(
      collection
        .filter(z => charge(z) > 0.0)
        .foldLeft(x.pos.zeroed) { (p1, p2) => {
          val d = distance(x.pos, p2.pos)
          if (d > rp || (x eq p2)) p1
          else (charge(x) * charge(p2) / (d * (if (d < rc) (rc * rc) else (d * d)))) *: (x.pos - p2.pos) + p1
      }})
  }

  // Naming?
  def replace[S,F[_]](entity: Particle[S,F,Double], p: Position[F,Double]): Step[F,Double,Particle[S,F,Double]] =
    Step.point(entity applyLens _position set p)

  def createParticle[S,F[_]](f: Position[F,Double] => Particle[S,F,Double])(pos: Position[F,Double]): Particle[S,F,Double] =
    f(pos)
}


/*
next pso work:
==============
- vepso / dvepso (robert afer moo & dmoo)
- cooperative & variations
- heterogenous filipe

- niching (less important for now)

commonalities:
- subswarms

functions:
- moo & dmoo functions (benchmarks) robert
*/

/*
 Stopping conditions:
 ====================
 iteration based stopping conditions
 fitness evaluations
 dimension based updates
 # of position updates (only defined if change is some epislon based on the position vector)
 # of dimensional updates > epsilon
 */
