package cilib
package pso

import scalaz._
import monocle._, Monocle._
import monocle.syntax._
import Position._

import spire.algebra._

object PSO {
  import Lenses._

  // Constrain this better - Not numeric. Operation for vector addition
  def stdPosition[S,A](
    c: Particle[S,A],
    v: Position[A]
  )(implicit A: Module[Position[A],A]): Step[A,Particle[S,A]] =
    Step.point(_position.modify((_: Position[A]) + v)(c))

  // Dist \/ Double (scalar value)
  // This needs to be fleshed out to cater for the parameter constants // remember to extract Dists
  def stdVelocity[S](
    entity: Particle[S,Double],
    social: Position[Double],
    cognitive: Position[Double],
    w: Double,
    c1: Double,
    c2: Double
  )(implicit V: Velocity[S,Double], M: Module[Position[Double],Double], F:Field[Double]): Step[Double,Position[Double]] =
    Step.pointR(for {
      cog <- (cognitive - entity.pos) traverse (x => Dist.stdUniform.map(_ * x))
      soc <- (social    - entity.pos) traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(entity.state)) + (c1 *: cog) + (c2 *: soc))

  // Step to evaluate the particle, without any modifications
  def evalParticle[S](entity: Particle[S,Double]) =
    Entity.eval[S,Double](x => x)(entity)

  def updatePBest[S](p: Particle[S,Double])(implicit M: Memory[S,Double]): Step[Double,Particle[S,Double]] = {
    val pbestL = M._memory
    Step.liftK(Comparison.compare(p.pos, (p.state applyLens pbestL).get)).map(x =>
      Entity(p.state applyLens pbestL set x, p.pos))
  }

  def updatePBestBounds[S](p: Particle[S,Double])(implicit M: Memory[S,Double]): Step[Double,Particle[S,Double]] = {
    val b = (p.pos.pos zip p.pos.boundary.list.toList).foldLeft(true)((a,c) => a && (c._2.contains(c._1)))
    if (b) updatePBest(p) else Step.point(p)
  }

  def updateVelocity[S](p: Particle[S,Double], v: Position[Double])(implicit V: Velocity[S,Double]): Step[Double,Particle[S,Double]] =
    Step.pointR(RVar.point(Entity(p.state applyLens V._velocity set v, p.pos)))

  def singleComponentVelocity[S](
    entity: Particle[S,Double],
    component: Position[Double],
    w: Double,
    c: Double
  )(implicit V: Velocity[S,Double], M: Memory[S,Double], MO: Module[Position[Double],Double]): Step[Double,Position[Double]] =
    Step.pointR(
      for {
        comp <- (component - entity.pos) traverse (x => Dist.stdUniform.map(_ * x))
      } yield (w *: V._velocity.get(entity.state)) + (c *: comp))

  final case class GCParams(p: Double, successes: Int, failures: Int, e_s: Double, e_f: Double)

  def defaultGCParams =
    GCParams(p = 1.0, successes = 0, failures = 0, e_s = 15, e_f = 5)

  def gcVelocity[S](
    entity: Particle[S,Double],
    nbest: Position[Double],
    w: Double,
    s: GCParams
  )(implicit V: Velocity[S,Double], M: Module[Position[Double],Double]): Step[Double,Position[Double]] =
    Step.pointR(
      nbest traverse (_ => Dist.stdUniform.map(x => s.p * (1 - 2*x))) map (a =>
        -1.0 *: entity.pos + nbest + w *: V._velocity.get(entity.state) + a
      ))

  def barebones[S](p: Particle[S,Double], global: Position[Double])(implicit M: Memory[S,Double]) =
    Step.pointR {
      val pbest = M._memory.get(p.state)
      val zipped = pbest.zip(global)
      val sigmas = zipped map { case (x,y) => math.abs(x - y) }
      val means  = zipped map { case (x,y) => (x + y) / 2.0 }

      (means zip sigmas) traverse (x => Dist.gaussian(x._1, x._2))
    }

  def quantum[S](
    collection: List[Particle[S,Double]],
    x: Particle[S,Double],
    center: Position[Double],
    r: Double
  )(implicit M: Module[Position[Double],Double]): Step[Double,Position[Double]] =
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

  def acceleration[S](
    collection: List[Particle[S,Double]],
    x: Particle[S,Double],
    distance: (Position[Double], Position[Double]) => Double,
    rp: Double,
    rc: Double)(
    implicit C: Charge[S], MO: Module[Position[Double],Double]): Step[Double,Position[Double]] = {
    def charge(x: Particle[S,Double]) =
      C._charge.get(x.state)

    Step.point(
      collection
//        .list
        .filter(z => charge(z) > 0.0)
        .foldLeft(x.pos.zeroed) { (p1, p2) => {
          val d = distance(x.pos, p2.pos)
          if (d > rp || (x eq p2)) p1
          else (charge(x) * charge(p2) / (d * (if (d < rc) (rc * rc) else (d * d)))) *: (x.pos - p2.pos) + p1
      }})
  }

  // Naming?
  def replace[S](entity: Particle[S,Double], p: Position[Double]): Step[Double,Particle[S,Double]] =
    Step.point(entity applyLens _position set p)

  def createParticle[S](f: Position[Double] => Particle[S,Double])(pos: Position[Double]): Particle[S,Double] =
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


trait PopAlg[A] {
  def run: List[A] => A => Step[Double,A]
}
