package cilib

import monocle._
import monocle.std.disjunction._
import monocle.std.option._

final case class Mem[A](b: Position[A], v: Position[A])

@annotation.implicitNotFound(
  "A HasMemory instance cannot be found for the provided state type ${S}")
trait HasMemory[S, A] {
  def _memory: Lens[S, Position[A]]
}

object HasMemory {
  @inline def apply[S, A](implicit A: HasMemory[S, A]): HasMemory[S, A] = A

  implicit val memMemory: HasMemory[Mem[Double], Double] =
    new HasMemory[Mem[Double], Double] {
      def _memory = Lens[Mem[Double], Position[Double]](_.b)(b => a => a.copy(b = b))
    }
}

trait HasVelocity[S, A] {
  def _velocity: Lens[S, Position[A]]
}

object HasVelocity {
  implicit val memVelocity: HasVelocity[Mem[Double], Double] =
    new HasVelocity[Mem[Double], Double] {
      def _velocity = Lens[Mem[Double], Position[Double]](_.v)(b => a => a.copy(v = b))
    }
}

trait HasCharge[A] {
  def _charge: Lens[A, Double]
}

trait HasPBestStagnation[A] {
  def _pbestStagnation: Lens[A, Int]
}

object Lenses {
  import scalaz.{Lens => _, Optional => _, _}

  // Base Entity lenses
  def _state[S, A]: Lens[Entity[S, A], S] =
    Lens[Entity[S, A], S](_.state)(c => e => e.copy(state = c))

  def _position[S, A]: Lens[Entity[S, A], Position[A]] =
    Lens[Entity[S, A], Position[A]](_.pos)(c => e => e.copy(pos = c))

  def _vector[A: scalaz.Equal]: Lens[Position[A], NonEmptyList[A]] =
    Lens[Position[A], NonEmptyList[A]](_.pos)(
      c =>
        e =>
          if (scalaz.Equal[NonEmptyList[A]].equal(e.pos, c)) e
          else Position(c, e.boundary))

  def _objective[A]: Getter[Position[A], Option[Objective[A]]] =
    Getter(_.objective)

  def _fitness[A]: Fold[Position[A], Fit \/ List[Fit]] =
    _objective[A]
      .composePrism(some[Objective[A]])
      .composeGetter(Getter(_.fitness))

  def _singleFitness[A]: Fold[Position[A], Fit] =
    _fitness[A].composePrism(left[Fit, List[Fit]])

  def _multiFitness[A]: Fold[Position[A], List[Fit]] =
    _fitness[A].composePrism(right[Fit, List[Fit]])

  def _feasible: Prism[Fit, Double] =
    Prism[Fit, Double](_ match {
      case Feasible(x) => Some(x)
      case _           => None
    })(x => Feasible(x))

}
