package cilib

import _root_.scala.Predef.{any2stringadd => _}

import scalaz.{Kleisli,NonEmptyList,State}
import scalaz.std.list._

import monocle._
import monocle.syntax._
import Position._

case class Mem[A](b: Position[List, A], v: Position[List, A])

trait Memory[A] {
  def _memory: Lens[A, Position[List,Double]]
}

object Memory {
  implicit object MemMemory extends Memory[Mem[Double]] {
    def _memory = Lens[Mem[Double],Position[List,Double]](_.b)(b => a => a.copy(b = b))
  }
}

trait Velocity[A] {
  def _velocity: Lens[A, Position[List,Double]]
}

object Velocity {
  implicit object MemVelocity extends Velocity[Mem[Double]] {
    def _velocity = Lens[Mem[Double], Position[List,Double]](_.v)(b => a => a.copy(v = b))
  }
}

trait Charge[A] {
  def _charge: Lens[A,Double]
}

object PSO {
  def stdPosition[S](c: Particle[S,Double], v: Position[List,Double]): Instruction[Particle[S,Double]] =
    Instruction.point((c._1, c._2 + v))

  // Dist \/ Double (scalar value)
  // This needs to be fleshed out to cater for the parameter constants // remember to extract Dists
  def stdVelocity[S](entity: Particle[S,Double], social: Position[List,Double], cognitive: Position[List, Double], w: Double, c1: Double, c2: Double)(implicit V: Velocity[S]): Instruction[Position[List,Double]] = {
    val (state,pos) = entity
    Instruction.pointR(for {
      cog <- (cognitive - entity._2) traverse (x => Dist.stdUniform.map(_ * x))
      soc <- (social - entity._2)    traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(entity._1)) + (c1 *: cog) + (c2 *: soc))
  }

  // Instruction to evaluate the particle // what about cooperative?
  import scalaz.std.list._
  def evalParticle[S](entity: Particle[S,Double]): Instruction[Particle[S,Double]] = {
    Instruction(Kleisli((e: Env) => entity._2.eval(e.prob).map(x => (entity._1, x))))
//    Instruction.pointR(entity._2.eval(problem).map(x => (entity._1, x)))
/*    Instruction.pointS(StateT(p => {
      //val r: RVar[(Problem[List,Double], Position[List,Double])] = entity._2.eval(p)
      entity._2.eval(p).map(x => (x._1, (entity._1, x._2)))
      //RVar.point((r._1, (entity._1, r._2)))
    }))*/
  }

  def updatePBest[S](p: Particle[S,Double])(implicit M: Memory[S]): Instruction[Particle[S,Double]] = {
    val pbestL = M._memory
    val (state, pos) = p
    Instruction.liftK(Fitness.compare(pos, (state applyLens pbestL).get).map(x => (state applyLens pbestL set x, pos)))
  }

  def updateVelocity[S](p: Particle[S,Double], v: Position[List,Double])(implicit V: Velocity[S]) =
    Instruction.pointR(RVar.point((p._1 applyLens V._velocity set v, p._2)))

  def createParticle[S](f: Position[List,Double] => Particle[S,Double])(pos: Position[List,Double]) =
    f(pos)

  def singleComponentVelocity[S](entity: (S,Position[List,Double]), component: Position[List,Double], w: Double, c: Double)(implicit V: Velocity[S], M: Memory[S]) = {
    val (state,pos) = entity
    Instruction.pointR(for {
      comp <- (component - pos) traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(state)) + (c *: comp))
  }

  case class GCParams(p: Double = 1.0, successes: Int = 0, failures: Int = 0, e_s: Double = 15, e_f: Double = 5)
  def gcVelocity[S](entity: Particle[S,Double], nbest: Position[List,Double], w: Double, s: GCParams)(implicit V: Velocity[S]): Instruction[Pos[Double]] =
    Instruction.pointR(
      nbest traverse (_ => Dist.stdUniform.map(x => s.p * (1 - 2*x))) map (a =>
        -1.0 *: entity._2 + nbest + w *: V._velocity.get(entity._1) + a
      ))

  def barebones[S](p: Particle[S,Double], global: Position[List,Double])(implicit M: Memory[S], V: Velocity[S]) =
    Instruction.pointR {
      import scalaz.Zip
      type P[A] = Position[List,A]

      val (state,x) = p
      val pbest = M._memory.get(state)
      val sigmas = Zip[P].zipWith(pbest, global)((x, y) => math.abs(x - y))
      val means  = Zip[P].zipWith(pbest, global)((x, y) => (x + y) / 2.0)

      (means zip sigmas) traverse (x => Dist.gaussian(x._1, x._2))
    }

  def quantum[S](
    collection: List[Particle[S,Double]],
    x: Particle[S,Double],
    center: Position[List,Double],
    r: Double
  ): Instruction[Position[List,Double]] =
    Instruction.pointR(
      for {
        u <- Dist.uniform(0,1)
        rand_x <- x._2.traverse(_ => Dist.stdNormal)
      } yield {
        import scalaz.std.list._
        val sum_sq = rand_x.pos.foldLeft(0.0)((a,c) => a + c*c)
        val scale: Double = r * math.pow(u, 1.0 / x._2.pos.length) / math.sqrt(sum_sq)
        (scale *: rand_x) + center
      }
    )

  def acceleration[S](
    collection: List[Particle[S,Double]],
    x: Particle[S,Double],
    distance: (Position[List,Double], Position[List,Double]) => Double,
    rp: Double,
    rc: Double
  )(implicit C: Charge[S]): Instruction[Position[List,Double]] = {
    def charge(x: Particle[S,Double]) =
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
  def replace[S](entity: Particle[S,Double], p: Position[List,Double]): Instruction[Particle[S,Double]] =
    Instruction.point((entity._1, p))
}

object Guide {

  def identity[S,A]: Guide[S,A] =
    (collection, x) => Instruction.point(x._2)

  def pbest[S](implicit M: Memory[S]): Guide[S,Double] =
    (collection, x) => Instruction.point(M._memory.get(x._1))

  def nbest[S](selection: Selection[Particle[S,Double]])(implicit M: Memory[S]): Guide[S,Double] = {
    (collection, x) => new Instruction(Kleisli[RVar, Env, Pos[Double]]((o: Env) => RVar.point {
      selection(collection, x).map(e => M._memory.get(e._1)).reduceLeft((a, c) => Fitness.compare(a, c) run o.opt)
    }))
  }

  def gbest[S:Memory]: Guide[S,Double] = nbest((c, _) => c)
  def lbest[S:Memory](n: Int) = nbest(Selection.indexNeighbours[Particle[S,Double]](n))

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
