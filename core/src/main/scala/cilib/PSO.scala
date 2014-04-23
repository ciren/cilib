package cilib

import scalaz._
import Scalaz._

import spire.math._
import spire.algebra._
import Position._

object PSO {
  case class Memory[F[_], A](b: Position[F, A], v: Position[F, A])
  case class Particle[F[_]: Traverse, A: Numeric](x: Position[F, A], m: Memory[F, A]) {
    def + (other: Position[F, A]) =
      Particle(x + other, m)

    def - (other: Position[F, A]) =
      Particle(x - other, m)

    def *: (scalar: A) =
      Particle(scalar *: x, m)
  }

  object Particle {
    implicit def particleFitness[F[_], A] = new Fitness[Particle[F, A]] {
      def fitness(a: Particle[F, A]) =
        a.x.fit
    }
  }

  type Guide[F[_], A] = Particle[F, A] => NonEmptyList[Particle[F, A]] => Reader[Opt, Position[F, A]]

  def pso_update[F[_]: Monad: Traverse, A: Numeric](guides: List[Guide[F, A]]): Particle[F, A] => NonEmptyList[Particle[F, A]] => ReaderT[RVar, Opt, Particle[F, A]] =
    current => collection => for {
      velocity <- stdVelocity(current, guides.traverseU(x => x(current)(collection)))
    } yield current + velocity

    // params injected, from something
  def stdVelocity[F[_]: Traverse, A](x: Particle[F, A], guides: Reader[Opt, List[Position[F, A]]])(implicit N: Numeric[A]): ReaderT[RVar, Opt, Position[F, A]] = {
    val diffs: Reader[Opt, List[RVar[Position[F, A]]]] = guides map { _.map { p => Dist.stdUniform.map(r => N.fromDouble(r) *: (p - x.x)) } } // random per dimension - needs to be vector or scalar

    Kleisli[RVar, Opt, Position[F, A]] { opt => diffs.map(d => d.sequence.map(_.foldLeft(N.fromDouble(0.8) *: x.m.v)(_ + _))) run opt }
  }

  def stdStep[F[_]: Traverse, A](x: Particle[F, A]) =
    Step({

      })


  // 1 -> Need to extract a notion of a step size calculation (like the velocity)
}

object Guide {
  import PSO.{ Guide, Particle, Memory }

  def pbest[F[_], A]: Guide[F, A] =
    x => collection => Reader(o => x.m.b)

  def nbest[F[_], A]: Guide[F, A] = // This is nothing more than the gbest particle
    x => collection => {
      Reader(o => collection.foldLeft(x)(Fitness.compare(_, _).run(o)).x)
    }
}

case class Step[F[_]: Traverse, A, B](run: ReaderT[RVar, B, Position[F, A]])


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
