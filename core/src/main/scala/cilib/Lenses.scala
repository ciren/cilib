package cilib

import monocle._

case class Mem[/*F[_],*/A](b: Position[A], v: Position[A])

trait Memory[S,/*F[_],*/A] {
  def _memory: Lens[S, Position[A]]
}

object Memory {
  @inline def apply[S,A](implicit A: Memory[S,A]) = A

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

trait PBestStagnation[A] {
  def _pbestStagnation: Lens[A, Int]
}

object Lenses {
  // Base Entity lenses
  def _state[S,A]    = Lens[Entity[S,A], S](_.state)(c => e => e.copy(state = c))
  def _position[S,A] = Lens[Entity[S,A], Position[A]](_.pos)(c => e => e.copy(pos = c))

  def _solutionPrism[A]: Prism[Position[A],Solution[A]] =
    Prism[Position[A],Solution[A]] {
      case x@Solution(_, _, _) => Some(x)
      case _ => None
    }(identity)

  def _objectiveLens[A]: Lens[Solution[A],Objective[A]] =
    Lens[Solution[A],Objective[A]](_.o)(o => s => s.copy(o = o))

  def _singleObjective[A]: Prism[Objective[A],Single[A]] =
    Prism[Objective[A],Single[A]](_ match {
      case x@Single(_,_) => Some(x)
      case _ => None
    })(x => x)

  def _multiObjective[A]: Prism[Objective[A],Multi[A]] =
    Prism[Objective[A],Multi[A]](_ match {
      case x@Multi(_) => Some(x)
      case _ => None
    })(x => x)

  def _singleFit[A]: Lens[Single[A],Fit] =
    Lens[Single[A],Fit](_.f)(f => s => s.copy(f = f))

  def _singleFitness[A]: Optional[Position[A], Fit] =
    _solutionPrism[A] composeLens
      _objectiveLens[A] composePrism
      _singleObjective[A] composeLens
      _singleFit[A]

}
