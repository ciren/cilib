package cilib

import monocle._

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
