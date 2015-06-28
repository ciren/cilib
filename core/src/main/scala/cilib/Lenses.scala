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


object Lenses {
  // Base Entity lenses
  def _state[S,F[_],A]    = Lens[Entity[S,F,A], S](_.state)(c => e => e.copy(state = c))
  def _position[S,F[_],A] = Lens[Entity[S,F,A], Position[F,A]](_.pos)(c => e => e.copy(pos = c))

  def _solutionPrism[F[_], A]: Prism[Position[F,A],Solution[F,A]] =
    Prism.apply[Position[F,A],Solution[F,A]]{
      case x @ Solution(_, _, _) => Some(x)
      case _                     => None
    }(identity)

  def _fitness[F[_],A]: Optional[Position[F,A],Fit] =
    _solutionPrism[F,A] composeLens Lens[Solution[F,A], Fit](_.f)(c => e => e.copy(f = c))
}
