package cilib

import monocle._

case class Mem[F[_],A](b: Position[F,A], v: Position[F,A])

trait Memory[S,F[_],A] {
  def _memory: Lens[S, Position[F,A]]
}

object Memory {
  implicit def memMemory[F[_]] = new Memory[Mem[F,Double],F,Double] {
    def _memory = Lens[Mem[F,Double],Position[F,Double]](_.b)(b => a => a.copy(b = b))
  }
}

trait Velocity[S,F[_],A] {
  def _velocity: Lens[S, Position[F,A]]
}

object Velocity {
  implicit def memVelocity[F[_]] = new Velocity[Mem[F,Double],F,Double] {
    def _velocity = Lens[Mem[F,Double], Position[F,Double]](_.v)(b => a => a.copy(v = b))
  }
}

trait Charge[A] {
  def _charge: Lens[A,Double]
}
