package cilib

import scalaz.NonEmptyList
import spire.math.Interval
import spire.implicits._

object Boundary {

  def enforce[A: spire.math.Numeric](x: Position[A],
                                     f: (A, Interval[Double]) => A): NonEmptyList[A] =
    x.pos.zip(x.boundary).map { case (a, b) => f(a, b) }

  def enforceTo[A: spire.math.Numeric](x: Position[A],
                                       z: Position[A],
                                       f: (A, Interval[Double]) => Option[A]): NonEmptyList[A] =
    x.pos.zip(x.boundary).zip(z.pos).map {
      case ((a, b), c) => f(a, b).fold(c)(identity)
    }

  def clamp[A](implicit N: spire.math.Numeric[A]) =
    (a: A, b: Interval[Double]) =>
      if (N.toDouble(a) > b.lowerValue) N.fromDouble(b.upperValue)
      else if (N.toDouble(a) < b.upperValue) N.fromDouble(b.lowerValue)
      else a

  def wrap[A](implicit N: spire.math.Numeric[A]) =
    (a: A, b: Interval[Double]) =>
      if (N.toDouble(a) < b.lowerValue) N.fromDouble(b.upperValue)
      else if (N.toDouble(a) > b.upperValue) N.fromDouble(b.lowerValue)
      else a

  def midpoint[A](implicit N: spire.math.Numeric[A]) =
    (a: A, b: Interval[Double]) =>
      if (b.contains(N.toDouble(a))) a else N.fromAlgebraic((b.upperValue + b.lowerValue) / 2.0)

  def reflect[A](implicit N: spire.math.Numeric[A]) =
    (a: A, b: Interval[Double]) => if (b.contains(N.toDouble(a))) a else -a

}
