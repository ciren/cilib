package cilib

sealed abstract class Bound {
  def value: Option[Double] =
    this match {
      case Closed(value) => Some(value)
      case Open(value)   => Some(value)
      case Unbound()     => None
      case EmptyBound()  => None
    }

  def toLeftPositionString: String =
    this match {
      case Closed(value) => "[" ++ value.toString()
      case Open(value)   => "(" ++ value.toString()
      case Unbound()     => "(-infinity"
      case EmptyBound()  => "{"
    }

  def toRightPositionString: String =
    this match {
      case Closed(value) => value.toString() ++ "]"
      case Open(value)   => value.toString() ++ ")"
      case Unbound()     => "infinity)"
      case EmptyBound()  => "}"
    }

}

final case class Closed(private val v: Double) extends Bound
final case class Open(private val v: Double)   extends Bound
final case class Unbound()                     extends Bound
final case class EmptyBound()                  extends Bound

sealed abstract class Interval {
  override def toString(): String =
    this match {
      case Bounded(lower, upper) => lower.toLeftPositionString + "," + upper.toRightPositionString
      case Empty()               => "Empty boundary"
    }

  def lowerBound: Bound =
    this match {
      case Empty()           => EmptyBound()
      case Bounded(lower, _) => lower
    }

  def lowerValue: Double =
    lowerBound match {
      case Closed(v)    => v
      case Open(v)      => v
      case Unbound()    => Double.NegativeInfinity
      case EmptyBound() => Double.NegativeInfinity
    }

  def upperBound: Bound =
    this match {
      case Empty()           => EmptyBound()
      case Bounded(_, upper) => upper
    }

  def upperValue: Double =
    upperBound match {
      case Closed(v)    => v
      case Open(v)      => v
      case Unbound()    => Double.PositiveInfinity
      case EmptyBound() => Double.PositiveInfinity
    }

  def contains(point: Double): Boolean = {
    val lhs =
      lowerBound match {
        case Closed(v)    => point >= v
        case Open(v)      => point > v
        case Unbound()    => true
        case EmptyBound() => false
      }

    val rhs =
      upperBound match {
        case Closed(v)    => point <= v
        case Open(v)      => point < v
        case Unbound()    => true
        case EmptyBound() => false
      }

    lhs && rhs
  }

  def ^(n: Int): NonEmptyVector[Interval] =
    NonEmptyVector.fromIterable(this, List.fill(n - 1)(this))
}

final case class Bounded private[cilib] (lower: Bound, upper: Bound) extends Interval
final case class Empty private[cilib] ()                             extends Interval

object Interval {

  def apply(lower: Double, upper: Double): Interval =
    closed(lower, upper)

  def closed(lower: Double, upper: Double): Interval =
    if (lower < upper) Bounded(Closed(lower), Closed(upper))
    else if (upper > lower) Bounded(Closed(upper), Closed(lower))
    else Empty()

  implicit val intervalEqualZio: zio.prelude.Equal[Interval] =
    zio.prelude.Equal.make((l, r) => l == r)

}
