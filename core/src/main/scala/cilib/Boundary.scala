package cilib

import scalaz._
import Scalaz._
import spire.math.Interval

object Boundary {

  final case class Enforce[F[_], A](f: (A, Interval[Double]) => F[A])
  final case class EnforceTo[F[_], A](f: (A, Interval[Double], A) => F[A])

  def enforce[F[_]: Applicative, A: spire.math.Numeric](x: Position[A],
                                                        f: Enforce[F, A]): F[NonEmptyList[A]] =
    x.pos.zip(x.boundary).traverse { case (a, b) => f.f(a, b) }

  def enforceTo[F[_], A: spire.math.Numeric, B](x: Position[A], z: Position[A], f: EnforceTo[F, A])(
      implicit F: Applicative[F]): F[NonEmptyList[A]] =
    x.pos.zip(x.boundary).zip(z.pos).traverse {
      case ((a, b), c) => f.f(a, b, c)
    }

  def clamp[A](implicit N: spire.math.Numeric[A]) =
    absorb

  def absorb[A](implicit N: spire.math.Numeric[A]) =
    Enforce((a: A, b: Interval[Double]) =>
      Need {
        val z = N.toDouble(a)

        if (z < b.lowerValue) N.fromDouble(b.lowerValue)
        else if (z > b.upperValue) N.fromDouble(b.upperValue)
        else a
    })

  def random[A](implicit N: spire.math.Numeric[A]) =
    Enforce(
      (a: A, b: Interval[Double]) =>
        if (b.contains(N.toDouble(a))) RVar.pure(a)
        else Dist.uniform(b).map(N.fromDouble))

  def reflect[A](implicit N: spire.math.Numeric[A]) =
    Enforce((a: A, b: Interval[Double]) =>
      Need {
        val z = N.toDouble(a)

        if (z < b.lowerValue) N.fromDouble(b.lowerValue - (z - b.lowerValue))
        else if (z > b.upperValue) N.fromDouble(b.upperValue - (z - b.upperValue))
        else a
    })

  def wrap[A](implicit N: spire.math.Numeric[A]) =
    toroidal

  def periodic[A](implicit N: spire.math.Numeric[A]) =
    toroidal

  def toroidal[A](implicit N: spire.math.Numeric[A]) =
    Enforce((a: A, b: Interval[Double]) =>
      Need {
        val z = N.toDouble(a)
        val range = math.abs(b.upperValue - b.lowerValue)

        if (z < b.lowerValue) N.fromDouble(b.upperValue - (b.lowerValue - z) % range)
        else if (z > b.upperValue) N.fromDouble(b.lowerValue + (z - b.upperValue) % range)
        else a
    })

  def midpoint[A](implicit N: spire.math.Numeric[A]) =
    Enforce((a: A, b: Interval[Double]) =>
      Need {
        if (b.contains(N.toDouble(a))) a
        else N.fromDouble((b.upperValue + b.lowerValue) / 2.0)
    })

  def around[A](implicit N: spire.math.Numeric[A]) =
    EnforceTo((a: A, b: Interval[Double], target: A) => {
      val z = N.toDouble(a)

      if (z < b.lowerValue)
        Dist.stdUniform.map(x => N.fromDouble(x * b.lowerValue + (1.0 - x) * N.toDouble(target)))
      else if (z > b.upperValue)
        Dist.stdUniform.map(x => N.fromDouble(x * b.upperValue + (1.0 - x) * N.toDouble(target)))
      else RVar.pure(a)
    })

}
