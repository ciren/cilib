package cilib

import zio.Chunk
import zio.prelude._

object Boundary {

  final case class Enforce[F[_], A](f: (A, Interval) => F[A])
  final case class EnforceTo[F[_], A](f: (A, Interval, A) => F[A])

  def enforce[F[+_]: AssociativeBoth: Covariant](
    x: Position[Double],
    f: Enforce[F, Double]
  ): F[NonEmptyVector[Double]] =
    NonEmptyForEach[NonEmptyVector].forEach1(x.pos.zip(x.boundary))(f.f.tupled)

  def enforceTo[F[+_], A, B](x: Position[A], z: Position[A], f: EnforceTo[F, A])(implicit
    F: Covariant[F] with IdentityBoth[F]
  ): F[NonEmptyVector[A]] =
    x.pos
      .zip(x.boundary)
      .zipWith(z.pos) { case ((a, b), c) => f.f(a, b, c) }
      .reduceMapRight(F.map(NonEmptyVector.single))((f, fas) => f.zipWith(fas)((h, t) => Chunk(h) +: t))

  def clamp: Enforce[Id, Double] =
    absorb

  def projection: Enforce[Id, Double] =
    absorb

  def absorb: Enforce[Id, Double] =
    Enforce((a: Double, b: Interval) =>
      Id {
        if (a < b.lowerValue) b.lowerValue
        else if (a > b.upperValue) b.upperValue
        else a
      }
    )

  def random: Enforce[RVar, Double] =
    Enforce((a: Double, b: Interval) =>
      if (b.contains(a)) RVar.pure(a)
      else Dist.uniform(b)
    )

  /**
   *    J. Ronkkonen, S. Kukkonen, and K. Price, “Real-parameter optimization
   *    with  differential  evolution,”  in Evolutionary  Computation,  2005.  The
   *    2005 IEEE Congress on, vol. 1, Sept 2005, pp. 506–513 Vol.1
   */
  def reflect: Enforce[Id, Double] =
    Enforce((a: Double, b: Interval) =>
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

        go(a)
      }
    )

  def wrap: Enforce[Id, Double] =
    toroidal

  def periodic: Enforce[Id, Double] =
    toroidal

  def toroidal: Enforce[Id, Double] =
    Enforce((a: Double, b: Interval) =>
      Id {
        val range = math.abs(b.upperValue - b.lowerValue)

        if (a < b.lowerValue) b.upperValue - (b.lowerValue - a) % range
        else if (a > b.upperValue) b.lowerValue + (a - b.upperValue) % range
        else a
      }
    )

  def midpoint: Enforce[Id, Double] =
    Enforce((a: Double, b: Interval) =>
      Id {
        if (b.contains(a)) a
        else (b.upperValue + b.lowerValue) / 2.0
      }
    )

  def evolutionary: EnforceTo[RVar, Double] =
    EnforceTo { (a: Double, b: Interval, target: Double) =>
      if (a < b.lowerValue)
        Dist.stdUniform.map(x => x * b.lowerValue + (1.0 - x) * target)
      else if (a > b.upperValue)
        Dist.stdUniform.map(x => x * b.upperValue + (1.0 - x) * target)
      else RVar.pure(a)
    }

  def around: EnforceTo[RVar, Double] =
    evolutionary

}
