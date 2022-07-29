package cilib

import zio.optics._


final case class Mem[A](b: Position[A], v: Position[A])

@annotation.implicitNotFound("A HasMemory instance cannot be found for the provided state type ${S}")
trait HasMemory[S, A] {
  def _memory: Lens[S, Position[A]]
}

object HasMemory {
  @inline def apply[S, A](implicit A: HasMemory[S, A]): HasMemory[S, A] = A

  implicit val memMemory: HasMemory[Mem[Double], Double] =
    new HasMemory[Mem[Double], Double] {
      def _memory = Lens[Mem[Double], Position[Double]](
        mem => Right(mem.b),
        pos => mem => Right(mem.copy(b = pos))
      )
    }
}

trait HasVelocity[S, A] {
  def _velocity: Lens[S, Position[A]]
}

object HasVelocity {
  implicit val memVelocity: HasVelocity[Mem[Double], Double] =
    new HasVelocity[Mem[Double], Double] {
      def _velocity = Lens[Mem[Double], Position[Double]](
        mem => Right(mem.v),
        vel => mem => Right(mem.copy(v = vel))
      )
    }
}

trait HasCharge[A] {
  def _charge: Lens[A, Double]
}

trait HasPBestStagnation[A] {
  def _pbestStagnation: Lens[A, Int]
}


object Lenses {
  import zio.prelude._

  // Base Entity lenses
  def _state[S, A]: Lens[Entity[S, A], S] =
    Lens[Entity[S, A], S](
      entity => Right(entity.state),
      newState => entity => Right(entity.copy(state = newState))
    )

  def _position[S, A]: Lens[Entity[S, A], Position[A]] =
    Lens[Entity[S, A], Position[A]](
      entity => Right(entity.pos),
      newPos => entity => Right(entity.copy(pos = newPos))
    )

  def _vector[A: zio.prelude.Equal]: Lens[Position[A], NonEmptyVector[A]] =
    Lens[Position[A], NonEmptyVector[A]](
      position => Right(position.pos),
      newPosition => position => Right(if (position.pos === newPosition) position else Position(newPosition, position.boundary))
    )
}
