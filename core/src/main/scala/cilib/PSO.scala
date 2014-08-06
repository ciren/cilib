package cilib

import Predef.{any2stringadd => _, _}
import scalaz._
import scalaz.syntax.applicative._
import scalaz.syntax.traverse._
import scalaz.std.tuple._
import scalaz.std.list._

import spire.math._
import spire.algebra._
import spire.implicits._
import Position._

//case class Mem[A](b: Position[IList, A], v: Position[IList, A])
case class Mem[A](b: Position[List, A], v: Position[List, A])

trait Memory[A] {
  def memoryLens: Lens[A, Position[List,Double]]
}

object Memory {
  implicit object MemMemory extends Memory[Mem[Double]] {
    def memoryLens: Lens[Mem[Double],Position[List,Double]] = Lens.lensu((a,b) => a.copy(b = b), _.b)
  }
}

trait Velocity[A] {
  def velocityLens: Lens[A, Position[List,Double]]
}

object Velocity {
  implicit object MemVelocity extends Velocity[Mem[Double]] {
    def velocityLens: Lens[Mem[Double], Position[List,Double]] = Lens.lensu((a,b) => a.copy(b = b), _.b)
  }
}

object PSO {
  /*
  // Should the collection not be partially applied to the guides already?
  def velUp[S, A:Fractional](v: Lens[S, Pos[A]], local: Guide[A], global: Guide[A])(collection: IList[Pos[A]]): C[S, A] =
    Kleisli {
      case (s, a) => {
        val localG = local(collection, a)
        val globalG = global(collection, a)

        for {
          cog <- (a - localG)  traverse (x => Dist.stdUniform.map(y => x * y))
          soc <- (a - globalG) traverse (x => Dist.stdUniform.map(y => x * y))
        } yield (v.mod(_ + cog + soc, s), a)
      }
    }

  def posUp[S, A:Fractional](v: Lens[S, Pos[A]]): C[S, A] = Kleisli {
    case (s, a) => RVar.point((s, a + v.get(s)))
  }

  def barebonesVel[S, A:Fractional](v: Lens[S, Pos[A]], p: Lens[S, Pos[A]], global: Guide[A])(collection: IList[Pos[A]]): C[S, A] =
    Kleisli {
      case (s, a) =>
        val A = implicitly[Fractional[A]]
        val pbest = p.get(s)
        val gbest = global(collection, a)

        val sigmas = Zip[Pos].zipWith(pbest, gbest)((x, y) => spire.math.abs(x - y))
        val means = Zip[Pos].zipWith(pbest, gbest)((x, y) => (x + y) / 2.0)
        val vel = (means zip sigmas) traverse (x => Dist.gaussian(A.toDouble(x._1), A.toDouble(x._2))) map (x => x.map(A.fromDouble(_))) // This needs to be nicer.... A needs to disappear

        vel.map(x => (v.mod(_ => x, s), a))
    }

  def replace[S, A:Numeric](v: Lens[S, Pos[A]]): C[S, A] =
    Kleisli {
      case (s, a) => RVar.point((s, v.get(s)))
    }

  def pbestUpdate[S, A: Numeric](pbL: Lens[S, Pos[A]]): Reader[Opt, Kleisli[RVar, (S, Pos[A]), (S, Pos[A])]] =
    Reader(opt =>
      Kleisli {
        case (s, a) =>
          RVar.point((pbL.mod((pbest: Pos[A]) => Fitness.compare(pbest, a) run opt, s), a))
      }
    )


  //  def c[S, A:Numeric] = (velUp[S,A] _) >==> (posUp _)
   */

  // The below is a different approach to building up a description of the algorithm


  /**
    A Instruction is a type that models a single step in an Algorithm's operation.

    The general idea would be that you would compose different Instruction instances
    to produce the desired behaviour. Furthermore, the Instruction is parameterized
    on some input type `I` and some output type 'O'.

    Even though this is an initial pass at modeling the compuation of CI algorithms
    this way, it does provide a recursive, list-like composition allowing a multitude
    of different usages (or it is hoped to be so).

    An instruction either returns a calculated value, suspends some value or it
    does both and continues the computation. The Instruction is nothing more than a free
    monad, but suited to our uses.
    */

  // This should be extracted to use transformers - Does this mean we need RVarT?
  trait InstructionFunctions {

  }



  object Instruction {
    import scalaz._, Scalaz._

    type X[A] = StateT[RVar, Problem[List,Double], A]

    //case class Instruction[F[_],D,A](run: RVar[State[Problem[F,D],Reader[Opt,A]]]) {
    //case class Instruction[F[_],D,A](run: StateT[RVar, Problem[F,D], Reader[Opt,A]]) {
    final class Instruction[F[_],D,A](val run: ReaderT[X, Opt, A]) {

      def map[B](f: A => B): Instruction[F,D,B] =
        new Instruction(run map f)

      def flatMap[B](f: A => Instruction[F,D,B]): Instruction[F,D,B] =
        new Instruction(run flatMap (f(_).run))
    }

    def point[A](a: A): Instruction[List,Double,A] =
      new Instruction(Kleisli[X, Opt, A]((o: Opt) => StateT((p: Problem[List,Double]) => RVar.point((p, a)))))

    def pointR[A](a: RVar[A]): Instruction[List,Double,A] =
      new Instruction(Kleisli[X,Opt,A]((o: Opt) => StateT((p: Problem[List,Double]) => a.map(x => (p,x)))))

    def pointS[A](a: StateT[RVar, Problem[List,Double],A]): Instruction[List,Double,A] =
      new Instruction(Kleisli[X,Opt,A]((o: Opt) => a))

    def liftK[A](a: Reader[Opt, A]): Instruction[List,Double,A] =
      new Instruction(Kleisli[X, Opt, A]((o: Opt) => StateT((p: Problem[List,Double]) => RVar.point((p, a.run(o))))))
  }

  import Instruction._

  def updatePosition[S](c: (S,Position[List,Double]), v: Position[List,Double]): Instruction[List,Double,(S,Position[List,Double])] =
    Instruction.point((c._1, c._2 + v))

  // Dist \/ Double (scalar value)
  // This needs to be fleshed out to cater for the parameter constants // remember to extract Dists
  def updateVelocity[S](entity: (S,Position[List,Double]), social: Position[List,Double], cognitive: Position[List, Double], w: Double, c1: Double, c2: Double)(implicit V: Velocity[S]): Instruction[List,Double,Position[List,Double]] = {
    val (state,pos) = entity
    Instruction.pointR(for {
      cog <- (cognitive - pos) traverse (x => Dist.stdUniform.map(_ * x))
      soc <- (social - pos) traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: pos) + (c1 *: cog) + (c2 *: soc))
  }

  // Instruction to evaluate the particle // what about cooperative?
  def evalParticle[F[_],S](entity: (S,Position[List,Double])): Instruction[List,Double,(S,Position[List,Double])] = {
    Instruction.pointS(StateT(p => {
      val r = entity._2.eval(p)
      RVar.point((r._1, (entity._1, r._2)))
    }))
  }

  // The following function needs a lot of work... the biggest issue is the case of the state 'S' and how to get the values out of it and how to update again??? Lenses? Typeclasses?
  def updatePBest[S](p: (S,Position[List,Double]))(implicit M: Memory[S]): Instruction[List,Double,Particle[S,Double]] = {
    val pbestL = M.memoryLens
    val (state, pos) = p
    Instruction.liftK(Fitness.compare(pos, pbestL.get(state)).map(x => (pbestL.set(state, x), pos)))
  }

  type Guide[A] = List[A] => A => A
  type Particle[S,A] = (S,Position[List,A])

  // The function below needs the guides for the particle, for the standard PSO update and will eventually live in the simulator
  def gbest[S:Memory:Velocity](w: Double, c1: Double, c2: Double,
    cognitive: Guide[Particle[S,Double]],
    social: Guide[Particle[S,Double]]
  ): List[Particle[S, Double]] => Particle[S,Double] => Instruction[List,Double,Particle[S,Double]] =
    collection => x => for {
      v <- updateVelocity(x, social(collection)(x)._2, cognitive(collection)(x)._2, w, c1, c2)
      p <- updatePosition(x, v)
      p2 <- evalParticle(p)
      updated <- updatePBest(p2)
    } yield updated

}


/*
next pso work:
==============
- dynamic psos (quantum, charged, etc) bennie
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
