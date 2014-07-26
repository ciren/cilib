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
  sealed trait Instruction[F[_],A] {
    def flatMap[B](f: A => Instruction[F,B]): Instruction[F,B] =
      FlatMap(this, f)
    def map[B](f: A => B): Instruction[F,B] =
      flatMap(f andThen (Return(_)))
  }

  case class Return[F[_],A](x: A) extends Instruction[F,A]
  case class Suspend[F[_],A](s: F[A]) extends Instruction[F,A]
  case class FlatMap[F[_],A,B](sub: Instruction[F,A], k: A => Instruction[F,B]) extends Instruction[F,B]

  object Instruction {
    implicit def instrucitonMonad[F[_]]: Monad[({type f[a] = Instruction[F,a]})#f] =
      new Monad[({type f[a]=Instruction[F,a]})#f] {
        def point[A](a: => A): Instruction[F,A] = Return(a)
        def bind[A, B](fa: Instruction[F,A])(f: A => Instruction[F,B]): Instruction[F,B] = FlatMap(fa, f)
      }

    def suspend[F[_],A](s: F[A]): Instruction[F,A] = // This should go into the package object
      Suspend(s)
  }

  import Instruction._

  def createPosition(n: Int/*, domain: List[Interval]*/) = //: Instruction[RVar, Position[List,Double]] =
    suspend(Dist.uniform(-5.12, 5.12).replicateM(n) map (Position(_)))

  def createCollection(n: Int, d: Int) =
    createPosition(d) replicateM n

  // This should not be a direct funciton, possibly usage via `map`?
  def createParticle[S](f: Position[List,Double] => (S, Position[List,Double]))(pos: Position[List,Double]) =
    f(pos)

  // Instruction to evaluate the particle // what about cooperative?
  def evalParticle[F[_],S](entity: (S,Position[List,Double])): State[Problem[List,Double],(S,Position[List,Double])] = {
    State(p => {
      val r = entity._2.eval(p)
      (r._1, (entity._1, r._2))
    })
  }

  def updatePosition[S](c: (S,Position[List,Double]), v: Position[List,Double]) =
    suspend(RVar.point((c._1, c._2 + v)))

  // Dist \/ Double (scalar value)
  // This needs to be fleshed out to cater for the parameter constants // remember to extract Dists
  def updateVelocity[S](entity: (S,Position[List,Double]), social: Position[List,Double], cognitive: Position[List, Double], w: Double, c1: Double, c2: Double)(implicit V: Velocity[S]) = {
    val (state,pos) = entity
    suspend(for {
      cog <- (cognitive - pos) traverse (x => Dist.stdUniform.map(_ * x))
      soc <- (social - pos) traverse (x => Dist.stdUniform.map(_ * x))
    } yield (w *: pos) + (c1 *: cog) + (c2 *: soc))
  }

  // The following function needs a lot of work... the biggest issue is the case of the state 'S' and how to get the values out of it and how to update again??? Lenses? Typeclasses?
  def updatePBest[S](p: (S,Position[List,Double]))(implicit M: Memory[S]): Reader[Opt, (S,Position[List,Double])] = {
    val pbestL = M.memoryLens
    val (state, pos) = p
    Fitness.compare(pos, pbestL.get(state)).map(x => (pbestL.set(state, x), pos))
  }


  type Guide[A] = List[A] => A => A
  type Particle[S,A] = (S,Position[List,A])

  // The funciton bwlow needs the guides for the particle, for the standard PSO update and will eventually live in the simulator
  def gbest[S:Velocity](
    x: (S,Position[List,Double]),
    cognitive: Particle[S,Double] => Particle[S,Double],
    social: Particle[S,Double] => Particle[S,Double]
  ): Instruction[RVar, State[Problem[List,Double], (S,Position[List,Double])]] =
    for {
      v <- updateVelocity(x, social(x)._2, cognitive(x)._2, 0.8, 1.4, 1.4)
      p <- updatePosition(x, v)
    } yield evalParticle(p)

  def syncUpdate[S:Velocity](collection: List[(S,Position[List,Double])],
    f: ((S,Position[List,Double])) => Instruction[RVar, State[Problem[List,Double], List[(S,Position[List,Double])]]]) =
    collection.traverseU(f)

  // Some helper code for translating Instruction to some monad F
  @annotation.tailrec
  def step[F[_],A](inst: Instruction[F,A])(implicit F: Monad[F]): Instruction[F,A] = inst match {
    case FlatMap(FlatMap(x,f), g) => step(x flatMap (a => f(a) flatMap g))
    case FlatMap(Return(x), f) => step(f(x))
    case _ => inst
  }

  def interpret[F[_],A](inst: Instruction[F,A])(implicit F: Monad[F]): F[A] = run(inst)

  def run[F[_],A](inst: Instruction[F,A])(implicit F: Monad[F]): F[A] = step(inst) match {
    case Return(a) => F.point(a)
    case Suspend(r) => F.bind(r)(a => run(Return[F,A](a)))
    case FlatMap(x, f) => x match {
      case Suspend(r) => F.bind(r)(a => run(f(a)))
      case _ => sys.error("Impossible")
    }
  }
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
