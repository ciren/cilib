package cilib

import scalaz._
import Scalaz._

import spire.math._
import spire.algebra._
import Position._

// These are some typeclasses that are needed to make the code much more generic and simpler, and can be extended.
trait Memory[F[_], S, A] {
  def mem: Lens[S, Position[F, A]]
}

object Memory {
  implicit def basicMemory[F[_], A] = new Memory[F, Mem[F, A], A] {
    def mem: Lens[Mem[F, A], Position[F, A]] = Lens.lensu((a, b) => a.copy(b = b), _.b)
  }
}

trait Neighbour[F[_], A] {
  type Collection = List[Position[F, A]]

  def neighbour: (Collection, Position[F, A]) => Position[F, A]
}

object Neighbour {
  def gbest[F[_], A] = new Neighbour[F, A] {
    def neighbour = (col, x) => x
  }
}

trait StepSize[F[_], S, A] {
  def step: Lens[S, Position[F, A]]
}

object StepSize {
  implicit def memStepSize[F[_], A] = new StepSize[F, Mem[F, A], A] {
    def step: Lens[Mem[F, A], Position[F, A]] = Lens.lensu((a, b) => a.copy(v = b), _.v)
  }
}

case class Mem[F[_], A](b: Position[F, A], v: Position[F, A])

object PSO {

  // the ideal is the following:
  // There is a single function that creates the initial "state" which is just a Memory instance for the computation
  // If there are situations where "extra" information is required in the state, then the initial state needs to be transformed into the required state type
  type PSOState[S, A] = (S, A) => (S, A)

  // What about the parameters? c1, c2, w? Are they stored in S?
  def stdVel[F[_]: Zip: Functor, A: Numeric, S](globalG: Neighbour[F, A], M: Memory[F, S, A], V: StepSize[F, S, A])(w: Double, c1: Double, c2: Double): PSOState[S, Position[F, A]] =
    (s: S, x: Position[F, A]) => {
      val G = globalG.neighbour(List.empty, x)
      val localG = M.mem
      val vel = V.step
      val n = implicitly[Numeric[A]]
      (vel.set(s, n.fromDouble(w) *: vel.get(s) + localG.get(s) + G), x)
    }

  def stdPos[F[_]: Zip: Functor, A: Numeric, S](vel: StepSize[F, S, A]): PSOState[S, Position[F, A]] =
    (s: S, x: Position[F, A]) => (s, x + vel.step.get(s))

  def stdPSOIter[F[_]:Zip:Functor, A:Numeric] = (s: Mem[F, A], x: Position[F, A]) => {
    val a = stdVel[F, A, Mem[F, A]](Neighbour.gbest, Memory.basicMemory, StepSize.memStepSize)(0.8, 1.4, 1.4)
    val b = stdPos[F, A, Mem[F, A]](StepSize.memStepSize)

    b.tupled(a(s,x))
  }

  def pbestL[F[_], A] = Lens.lensu[Mem[F, A], Position[F, A]]((a, b) => a.copy(b = b), _.b)
  def velL[F[_], A] = Lens.lensu[Mem[F, A], Position[F, A]]((a, b) => a.copy(v = b), _.v)
}

object Guide {
  //import PSO.{ Guide, Particle, Memory }

  //def pbest[F[_], A]: Guide[F, A] =

//  def nbest[F[_], A]: Guide[F, A] = // This is nothing more than the gbest particle

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
