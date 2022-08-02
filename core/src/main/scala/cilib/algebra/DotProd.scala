package cilib
package algebra

import zio.Chunk
import zio.prelude._

trait DotProd[F[_], A] {
  def dot(a: F[A], b: F[A]): Double

  def normsqr(a: F[A]): Double = dot(a, a)
  def norm(a: F[A]): Double    = math.sqrt(dot(a, a))
  def lensqr(a: F[A]): Double  = normsqr(a)
  def len(a: F[A]): Double     = norm(a)
}

trait CrossProd[F[_], A] {
  def cross(a: F[A], b: F[A]): F[A]
}

trait Pointwise[F[_], A] {
  def pointwise(a: F[A], b: F[A]): F[A] // Pointwise multiplication
}

trait VectorOps[F[_], A] {

  def zeroed(a: F[A]): F[A]

  def plus(a: F[A], b: F[A]): F[A]

  def minus(a: F[A], b: F[A]): F[A]

  def scalarMultiply(scalar: A, a: F[A]): F[A]

  def isZero(a: F[A]): Boolean
}

object VectorOps {
  @inline def apply[F[_], A](implicit V: VectorOps[F, A]) =
    V
}

trait Orthongonal {} // Need to examine the name of this typeclass??

object Algebra {

  def normalize[F[+_]: Covariant](a: F[Double])(implicit D: DotProd[F, Double]): F[Double] = {
    val mag = D.norm(a)
    if (mag === 0.0) a
    else Covariant[F].map((x: Double) => x * (1.0 / mag))(a)
  }

  def norm[F[_], A](a: F[A])(implicit D: DotProd[F, A]): Double =
    D.norm(a)

  def distance[F[_], A: scala.math.Numeric](a: F[A], b: F[A])(implicit D: DotProd[F, A], M: VectorOps[F, A]): Double =
    D.norm(M.minus(a, b))

  def pointwise[F[_], A](a: F[A], b: F[A])(implicit P: Pointwise[F, A]): F[A] =
    P.pointwise(a, b)

  def vectorSum[F[_], A: scala.math.Numeric](xs: NonEmptyVector[F[A]])(implicit M: VectorOps[F, A]): F[A] =
    NonEmptyForEach[NonEmptyVector].reduceAll(xs)(M.plus)

  def meanVector[F[+_]: Covariant, A](
    xs: NonEmptyVector[F[A]]
  )(implicit M: VectorOps[F, A], A: scala.math.Numeric[A]): F[Double] = {
    val l = 1.0 / xs.length

    Covariant[F].map((x: A) => A.toDouble(x) * l)(vectorSum(xs))
  }

  def orthogonalize[F[+_]: Covariant](
    x: F[Double],
    vs: List[F[Double]]
  )(implicit D: DotProd[F, Double], M: VectorOps[F, Double]): F[Double] =
    vs.foldLeft(x)((a, b) => M.minus(a, project(a, b)))

  def project[F[+_]](
    x: F[Double],
    other: F[Double]
  )(implicit D: DotProd[F, Double], F: Covariant[F], M: VectorOps[F, Double]): F[Double] =
    if (D.dot(other, other) == 0.0) F.map((_: Double) => 0.0)(other)
    else F.map((a: Double) => a * (D.dot(x, other) / D.dot(other, other)))(other)

  def orthonormalize[F[+_]: Covariant: ForEach](
    vs: NonEmptyVector[F[Double]]
  )(implicit D: DotProd[F, Double], M: VectorOps[F, Double]): NonEmptyVector[F[Double]] = {
    val bases = vs.foldLeft(NonEmptyVector(vs.head)) { (ob, v) =>
      val ui = ob.foldLeft(v) { (u, o) =>
        M.minus(u, project(v, o))
      }
      if (ui.sum == 0.0) ob
      else ob ++ Chunk(ui)
    }

    bases.map(normalize(_))
  }

}
