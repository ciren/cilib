package cilib

import _root_.scala.Predef.{any2stringadd => _}

import scalaz._

import monocle.syntax._
import Position._

import spire.algebra._
import spire.implicits._
import spire.syntax.module._

object PSO {
  def stdPosition[S,F[_],A](c: Particle[S,F,A], v: Position[F,A])(implicit A: Module[F[A],A]): Instruction[F,A,Particle[S,F,A]] = // Constrain this better - Not numeric. Operation for vector addition
    Instruction.point((c._1, c._2 + v))

  // Dist \/ Double (scalar value)
  // This needs to be fleshed out to cater for the parameter constants // remember to extract Dists
  def stdVelocity[S,F[_]:Traverse](entity: Particle[S,F,Double], social: Position[F,Double], cognitive: Position[F,Double], w: Double, c1: Double, c2: Double)
    (implicit V: Velocity[S,F,Double], M: Module[F[Double],Double], F:Field[Double]): Instruction[F,Double,Position[F,Double]] = {
    val (state,pos) = entity
    Instruction.pointR(for {
      cog <- (cognitive - entity._2) traverse (x => Dist.stdUniform.map(_ * x))
      soc <- (social - entity._2)    traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(entity._1)) + (c1 *: cog) + (c2 *: soc))
  }

  // Instruction to evaluate the particle // what about cooperative?
  def evalParticle[S,F[_]:Foldable](entity: Particle[S,F,Double]): Instruction[F,Double,Particle[S,F,Double]] =
    Instruction(Kleisli((e: (Opt,Eval[F,Double])) => entity._2.eval(e._2).map(x => (entity._1, x))))

  def updatePBest[S,F[_]](p: Particle[S,F,Double])(implicit M: Memory[S,F,Double]): Instruction[F,Double,Particle[S,F,Double]] = {
    val pbestL = M._memory
    val (state, pos) = p
    Instruction.liftK(Fitness.compare(pos, (state applyLens pbestL).get).map(x => (state applyLens pbestL set x, pos)))
  }

  def updateVelocity[S,F[_]](p: Particle[S,F,Double], v: Position[F,Double])(implicit V: Velocity[S,F,Double]): Instruction[F,Double,Particle[S,F,Double]] =
    Instruction.pointR(RVar.point((p._1 applyLens V._velocity set v, p._2)))

  def createParticle[S,F[_]](f: Position[F,Double] => Particle[S,F,Double])(pos: Position[F,Double]): Particle[S,F,Double] =
    f(pos)

  def singleComponentVelocity[S,F[_]:Traverse](entity: Particle[S,F,Double], component: Position[F,Double], w: Double, c: Double)(implicit V: Velocity[S,F,Double], M: Memory[S,F,Double], MO: Module[F[Double],Double]): Instruction[F,Double,Position[F,Double]] = {
    val (state,pos) = entity
    Instruction.pointR(for {
      comp <- (component - pos) traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(state)) + (c *: comp))
  }

  case class GCParams(p: Double = 1.0, successes: Int = 0, failures: Int = 0, e_s: Double = 15, e_f: Double = 5)
  def gcVelocity[S,F[_]:Traverse](entity: Particle[S,F,Double], nbest: Position[F,Double], w: Double, s: GCParams)(implicit V: Velocity[S,F,Double], M: Module[F[Double],Double]): Instruction[F,Double,Position[F,Double]] =
    Instruction.pointR(
      nbest traverse (_ => Dist.stdUniform.map(x => s.p * (1 - 2*x))) map (a =>
        -1.0 *: entity._2 + nbest + w *: V._velocity.get(entity._1) + a
      ))

  def barebones[S,F[_]:Monad:Traverse:Zip](p: Particle[S,F,Double], global: Position[F,Double])(implicit M: Memory[S,F,Double]) =
    Instruction.pointR {
      val (state,x) = p
      val pbest = M._memory.get(state)
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
  )(implicit M: Module[F[Double],Double]): Instruction[F,Double,Position[F,Double]] =
    Instruction.pointR(
      for {
        u <- Dist.uniform(0,1)
        rand_x <- x._2.traverse(_ => Dist.stdNormal)
      } yield {
        import scalaz.syntax.foldable._
        val sum_sq = rand_x.pos.foldLeft(0.0)((a,c) => a + c*c)
        val scale: Double = r * math.pow(u, 1.0 / x._2.pos.length) / math.sqrt(sum_sq)
        (scale *: rand_x) + center
      }
    )

  def acceleration[S,F[_]:Monad]( // Why must this be a Monad?? Surely Functor is enough?
    collection: List[Particle[S,F,Double]],
    x: Particle[S,F,Double],
    distance: (Position[F,Double], Position[F,Double]) => Double,
    rp: Double,
    rc: Double
  )(implicit C: Charge[S], MO: Module[F[Double],Double]): Instruction[F,Double,Position[F,Double]] = {
    def charge(x: Particle[S,F,Double]) =
      C._charge.get(x._1)

    Instruction.point(
      collection
        .filter(z => charge(z) > 0.0)
        .foldLeft(x._2.map(_ => 0.0)) { (p1, p2) => {
          val d = distance(x._2, p2._2)
          if (d > rp || (x eq p2))
            p1
          else
            (charge(x) * charge(p2) / (d * (if (d < rc) (rc * rc) else (d * d)))) *: (x._2 - p2._2) + p1
      }})
  }

  // Naming?
  def replace[S,F[_]](entity: Particle[S,F,Double], p: Position[F,Double]): Instruction[F,Double,Particle[S,F,Double]] =
    Instruction.point((entity._1, p))
}

object Guide {

  def identity[S,F[_],A]: Guide[S,F,A] =
    (collection, x) => Instruction.point(x._2)

  def pbest[S,F[_],A](implicit M: Memory[S,F,A]): Guide[S,F,A] =
    (collection, x) => Instruction.point(M._memory.get(x._1))

  def nbest[S,F[_]](selection: Selection[Particle[S,F,Double]])(implicit M: Memory[S,F,Double]): Guide[S,F,Double] = {
    (collection, x) => new Instruction(Kleisli[RVar, (Opt,Eval[F,Double]), Position[F,Double]]((o: (Opt,Eval[F,Double])) => RVar.point {
      selection(collection, x).map(e => M._memory.get(e._1)).reduceLeft((a, c) => Fitness.compare(a, c) run o._1)
    }))
  }

  def gbest[S,F[_]](implicit M: Memory[S,F,Double]): Guide[S,F,Double] = nbest((c, _) => c)
  def lbest[S,F[_]](n: Int)(implicit M: Memory[S,F,Double]) = nbest(Selection.indexNeighbours[Particle[S,F,Double]](n))

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
