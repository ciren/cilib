package cilib

sealed abstract class Bound {
  def value: Option[Double] =
    this match {
      case Bound.Closed(value) => Some(value)
      case Bound.Open(value)   => Some(value)
      case Bound.Unbound()     => None
      case Bound.EmptyBound()  => None
    }

  def toLeftPositionString: String =
    this match {
      case Bound.Closed(value) => "[" ++ value.toString()
      case Bound.Open(value)   => "(" ++ value.toString()
      case Bound.Unbound()     => "(-infinity"
      case Bound.EmptyBound()  => "{"
    }

  def toRightPositionString: String =
    this match {
      case Bound.Closed(value) => value.toString() ++ "]"
      case Bound.Open(value)   => value.toString() ++ ")"
      case Bound.Unbound()     => "infinity)"
      case Bound.EmptyBound()  => "}"
    }

}

object Bound {
  final case class Closed(private val v: Double) extends Bound
  final case class Open(private val v: Double)   extends Bound
  final case class Unbound()                     extends Bound
  final case class EmptyBound()                  extends Bound
}

sealed abstract class Interval {
  import Bound._

  override def toString(): String =
    this match {
      case Interval.Bounded(lower, upper) => lower.toLeftPositionString + "," + upper.toRightPositionString
      case Interval.Empty()               => "Empty boundary"
    }

  def lowerBound: Bound =
    this match {
      case Interval.Empty()           => EmptyBound()
      case Interval.Bounded(lower, _) => lower
    }

  def lowerValue: Double =
    lowerBound match {
      case Bound.Closed(v)    => v
      case Bound.Open(v)      => v
      case Bound.Unbound()    => Double.NegativeInfinity
      case Bound.EmptyBound() => Double.NegativeInfinity
    }

  def upperBound: Bound =
    this match {
      case Interval.Empty()           => EmptyBound()
      case Interval.Bounded(_, upper) => upper
    }

  def upperValue: Double =
    upperBound match {
      case Bound.Closed(v)    => v
      case Bound.Open(v)      => v
      case Bound.Unbound()    => Double.PositiveInfinity
      case Bound.EmptyBound() => Double.PositiveInfinity
    }

  def contains(point: Double): Boolean = {
    val lhs =
      lowerBound match {
        case Bound.Closed(v)    => point >= v
        case Bound.Open(v)      => point > v
        case Bound.Unbound()    => true
        case Bound.EmptyBound() => false
      }

    val rhs =
      upperBound match {
        case Bound.Closed(v)    => point <= v
        case Bound.Open(v)      => point < v
        case Bound.Unbound()    => true
        case Bound.EmptyBound() => false
      }

    lhs && rhs
  }

  def ^(n: Int): NonEmptyVector[Interval] =
    NonEmptyVector.fromIterable(this, List.fill(n - 1)(this))
}

object Interval {
  import Bound._

  final case class Bounded(lower: Bound, upper: Bound) extends Interval
  final case class Empty()                             extends Interval

  def apply(lower: Double, upper: Double): Interval =
    closed(lower, upper)

  def closed(lower: Double, upper: Double): Interval =
    if (lower < upper) Bounded(Closed(lower), Closed(upper))
    else if (upper > lower) Bounded(Closed(upper), Closed(lower))
    else Empty()

  implicit val intervalEqualZio: zio.prelude.Equal[Interval] =
    zio.prelude.Equal.make((l, r) => l == r)

}
