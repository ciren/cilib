package cilib

import monocle._

case class Mem[/*F[_],*/A](b: Position[A], v: Position[A])

trait Memory[S,/*F[_],*/A] {
  def _memory: Lens[S, Position[A]]
}

object Memory {
  implicit def memMemory/*[F[_]]*/ = new Memory[Mem[Double],Double] {
    def _memory = Lens[Mem[Double],Position[Double]](_.b)(b => a => a.copy(b = b))
  }
}

trait Velocity[S,/*F[_],*/A] {
  def _velocity: Lens[S, Position[A]]
}

object Velocity {
  implicit def memVelocity/*[F[_]]*/ = new Velocity[Mem[Double],Double] {
    def _velocity = Lens[Mem[Double], Position[Double]](_.v)(b => a => a.copy(v = b))
  }
}

trait Charge[A] {
  def _charge: Lens[A,Double]
}


object Lenses {
  // Base Entity lenses
  def _state[S,/*F[_],*/A]    = Lens[Entity[S,A], S](_.state)(c => e => e.copy(state = c))
  def _position[S,/*F[_],*/A] = Lens[Entity[S,A], Position[A]](_.pos)(c => e => e.copy(pos = c))

  def _solutionPrism[/*F[_],*/A]: Prism[Position[A],Solution[A]] =
    Prism.apply[Position[A],Solution[A]]{
      case x@Solution(_, _, _) => Some(x)
      case _ => None
    }(identity)

  def _fitness[/*F[_],*/A]: Optional[Position[A],Fit] =
    _solutionPrism[A] composeLens Lens[Solution[A], Fit](_.f)(c => e => e.copy(f = c))

/*=======
  def _solutionPrism[F[_], A]: Prism[Position[F,A],Solution[F,A]] =
    Prism.apply[Position[F,A],Solution[F,A]]{
      case x @ Solution(_, _, _) => Some(x)
      case _                     => None
    }(identity)

  def _fitness[F[_],A]: Optional[Position[F,A],Fit] =
    _solutionPrism[F,A] composeLens Lens[Solution[F,A], Fit](_.f)(c => e => e.copy(f = c))
>>>>>>> non-empty-interval
 */
}
