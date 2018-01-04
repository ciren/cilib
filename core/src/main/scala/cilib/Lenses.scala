package cilib

import monocle._

case class Mem[A](b: Position[A], v: Position[A])

@annotation.implicitNotFound("A HasMemory instance cannot be found for the provided state type ${S}")
trait HasMemory[S,A] {
  def _memory: Lens[S, Position[A]]
}

object HasMemory {
  @inline def apply[S,A](implicit A: HasMemory[S,A]) = A

  implicit val memMemory = new HasMemory[Mem[Double],Double] {
    def _memory = Lens[Mem[Double],Position[Double]](_.b)(b => a => a.copy(b = b))
  }
}

trait HasVelocity[S,A] {
  def _velocity: Lens[S, Position[A]]
}

object HasVelocity {
  implicit val memVelocity = new HasVelocity[Mem[Double],Double] {
    def _velocity = Lens[Mem[Double], Position[Double]](_.v)(b => a => a.copy(v = b))
  }
}

trait HasCharge[A] {
  def _charge: Lens[A,Double]
}

trait HasPBestStagnation[A] {
  def _pbestStagnation: Lens[A, Int]
}

object Lenses {
  import scalaz.{ Lens => _, Optional => _, _ }

  // Base Entity lenses
  def _state[S,A]    = Lens[Entity[S,A], S](_.state)(c => e => e.copy(state = c))
  def _position[S,A] = Lens[Entity[S,A], Position[A]](_.pos)(c => e => e.copy(pos = c))

  def _vector[A:scalaz.Equal] = Lens[Position[A],NonEmptyList[A]](_.pos)(c => e => e match {
    case Point(_, b) => Point(c, b)
    case Solution(x, b, _) =>
      if (scalaz.Equal[NonEmptyList[A]].equal(x, c)) e
      else Point(c, b)
  })

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
      _singleObjective composeLens
      _singleFit

  def _feasible: Prism[Fit,Double] =
    Prism[Fit,Double](_ match {
      case Feasible(x) => Some(x)
      case _ => None
    })(x => Feasible(x))

  // def _infeasible: Prism[Fit,Double] =
  //   Prism[Fit,Double](_ match {
  //     case Infeasible(x,_) => Some(x)
  //     case _ => None
  //   })(x => Infeasible(x))

//  def _penalty: Prism[] =

}
