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

  def stdVelocity[S, F[_]: Zip: Traverse, A: Numeric](globalG: Neighbour[F, A], M: Memory[F, S, A], V: StepSize[F, S, A])(w: Double, c1: Double, c2: Double)(x: (S, Position[F, A])): RVar[(S, Position[F, A])] = {
    val n = implicitly[Numeric[A]]
    val newVel: RVar[Position[F, A]] = for {
      b <- (M.mem.get(x._1) - x._2).traverse(q => Dist.stdUniform.map(r => n.fromDouble(r * c1 * n.toDouble(q))))
      a <- (globalG.neighbour(List.empty, x._2) - x._2).traverse(q => Dist.stdUniform.map(r => n.fromDouble(r * c2 * n.toDouble(q))))
    } yield n.fromDouble(w) *: x._2 + b + a

    newVel.map(y => (V.step.set(x._1, y), x._2))
  }

  def stdPos[F[_]: Zip: Functor, A: Numeric, S](vel: StepSize[F, S, A])(x: (S, Position[F, A])): RVar[(S, Position[F, A])] =
    RVar.point((x._1, x._2 + vel.step.get(x._1)))

  // Iterations are of the form: A => RVar[A], where A = (Mem[F,A], Position[F,A])
  def stdPSOIter[F[_]:Zip:Traverse, A:Numeric] = (s: Mem[F, A], x: Position[F, A]) => for {
    vel <- stdVelocity[Mem[F, A], F, A](Neighbour.gbest, Memory.basicMemory, StepSize.memStepSize)(0.8, 1.4, 1.4)((s, x))
    pos <- stdPos[F, A, Mem[F, A]](StepSize.memStepSize)(vel)
  } yield pos


}

case class Result[S, A](run: RVar[(S, A)]) {
  def mapfst[X](f: S => X) =
    Result(run map { x => (f(x._1), x._2) })

  def mapsnd[B](f: A => B) =
    Result(run map { x => (x._1, f(x._2)) })
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
