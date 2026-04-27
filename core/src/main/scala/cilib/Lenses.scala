package cilib

case class Lens[S, A](
  get: S => A,
  set: (S, A) => S
) {

  def modify(f: A => A): S => S =
    s => set(s, f(get(s)))

  def compose[B](that: Lens[A, B]): Lens[S, B] =
    Lens[S, B](
      get = s => that.get(this.get(s)),
      set = (s, b) => {
        val a    = this.get(s)
        val newA = that.set(a, b)
        this.set(s, newA)
      }
    )
}

case class Prism[S, A](
  getOption: S => Option[A],
  reverseGet: A => S
) {

  def modify(f: A => A): S => S =
    s =>
      getOption(s) match {
        case Some(a) => reverseGet(f(a))
        case None    => s
      }

  def compose[B](that: Prism[A, B]): Prism[S, B] =
    Prism[S, B](
      getOption = s => this.getOption(s).flatMap(that.getOption),
      reverseGet = b => this.reverseGet(that.reverseGet(b))
    )
}

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
        mem => mem.b,
        (mem, pos) => mem.copy(b = pos)
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
        mem => mem.v,
        (mem, vel) => mem.copy(v = vel)
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

  // Base Entity lenses
  def _state[S, A]: Lens[Entity[S, A], S] =
    Lens[Entity[S, A], S](
      entity => entity.state,
      (entity, newState) => entity.copy(state = newState)
    )

  def _position[S, A]: Lens[Entity[S, A], Position[A]] =
    Lens[Entity[S, A], Position[A]](
      entity => entity.pos,
      (entity, newPos) => entity.copy(pos = newPos)
    )

  def _vector[A: zio.prelude.Equal]: Lens[Position[A], NonEmptyVector[A]] =
    Lens[Position[A], NonEmptyVector[A]](
      position => position.pos,
      (position, newPosition) =>
        if (implicitly[zio.prelude.Equal[NonEmptyVector[A]]].equal(position.pos, newPosition)) position
        else Position(newPosition, position.boundary)
    )
}
