package cilib

import spire.implicits._
import spire.math.Interval
import zio.prelude._
import zio.prelude.NonEmptyList._

object Boundary {

  final case class Enforce[F[_], A](f: (A, Interval[Double]) => F[A])
  final case class EnforceTo[F[_], A](f: (A, Interval[Double], A) => F[A])

  def enforce[F[+_]: IdentityBoth : Covariant, A: spire.math.Numeric](x: Position[A], f: Enforce[F, A]): F[NonEmptyList[A]] = {
    val F = implicitly[Covariant[F]]
    val combined: NonEmptyList[F[A]] = x.pos.zipWith(x.boundary)(f.f)

    combined.reduceMapRight(F.map(single))((f, fas) => f.zipWith(fas)(cons))
  }

  def enforceTo[F[+_], A: spire.math.Numeric, B](x: Position[A], z: Position[A], f: EnforceTo[F, A])(
    implicit F: Covariant[F] with IdentityBoth[F]
  ): F[NonEmptyList[A]] =
    x.pos.zip(x.boundary).zipWith(z.pos) { case ((a,b), c) => f.f(a,b,c) }
      .reduceMapRight(F.map(single))((f, fas) => f.zipWith(fas)(cons))


  def clamp[A](implicit N: spire.math.Numeric[A]): Enforce[Id, A] =
    absorb

  def projection[A](implicit N: spire.math.Numeric[A]): Enforce[Id, A] =
    absorb

  def absorb[A](implicit N: spire.math.Numeric[A]): Enforce[Id, A] =
    Enforce((a: A, b: Interval[Double]) =>
      Id {
        val z = N.toDouble(a)

        if (z < b.lowerValue) N.fromDouble(b.lowerValue)
        else if (z > b.upperValue) N.fromDouble(b.upperValue)
        else a
      }
    )

  def random[A](implicit N: spire.math.Numeric[A]): Enforce[RVar, A] =
    Enforce((a: A, b: Interval[Double]) =>
      if (b.contains(N.toDouble(a))) RVar.pure(a)
      else Dist.uniform(b).map(N.fromDouble)
    )

  /**
    J. Ronkkonen, S. Kukkonen, and K. Price, “Real-parameter optimization
    with  differential  evolution,”  in Evolutionary  Computation,  2005.  The
    2005 IEEE Congress on, vol. 1, Sept 2005, pp. 506–513 Vol.1
   */
  def reflect[A](implicit N: spire.math.Numeric[A]): Enforce[Id, A] =
    Enforce((a: A, b: Interval[Double]) =>
      Id {
        @annotation.tailrec
        def go(c: Double): Double =
          if (b.contains(c)) c
          else
            go(
              if (c < b.lowerValue)
                2 * b.lowerValue - c
              else
                2 * b.upperValue - c
            )

        N.fromDouble(go(N.toDouble(a)))
      }
    )

  def wrap[A](implicit N: spire.math.Numeric[A]): Enforce[Id, A] =
    toroidal

  def periodic[A](implicit N: spire.math.Numeric[A]): Enforce[Id, A] =
    toroidal

  def toroidal[A](implicit N: spire.math.Numeric[A]): Enforce[Id, A] =
    Enforce((a: A, b: Interval[Double]) =>
      Id {
        val z     = N.toDouble(a)
        val range = math.abs(b.upperValue - b.lowerValue)

        if (z < b.lowerValue) N.fromDouble(b.upperValue - (b.lowerValue - z) % range)
        else if (z > b.upperValue) N.fromDouble(b.lowerValue + (z - b.upperValue) % range)
        else a
      }
    )

  def midpoint[A](implicit N: spire.math.Numeric[A]): Enforce[Id, A] =
    Enforce((a: A, b: Interval[Double]) =>
      Id {
        if (b.contains(N.toDouble(a))) a
        else N.fromDouble((b.upperValue + b.lowerValue) / 2.0)
      }
    )

  def evolutionary[A](implicit N: spire.math.Numeric[A]): EnforceTo[RVar, A] =
    EnforceTo { (a: A, b: Interval[Double], target: A) =>
      val z = N.toDouble(a)

      if (z < b.lowerValue)
        Dist.stdUniform.map(x => N.fromDouble(x * b.lowerValue + (1.0 - x) * N.toDouble(target)))
      else if (z > b.upperValue)
        Dist.stdUniform.map(x => N.fromDouble(x * b.upperValue + (1.0 - x) * N.toDouble(target)))
      else RVar.pure(a)
    }

  def around[A](implicit N: spire.math.Numeric[A]): EnforceTo[RVar, A] =
    evolutionary

}
