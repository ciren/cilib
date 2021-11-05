package cilib
package syntax

import cilib.algebra._
import zio.prelude._

object dotprod {
  implicit class DotProdSyntax[F[_], A](private val x: F[A]) extends AnyVal {
    def ∙(a: F[A])(implicit D: DotProd[F, A]): Double = D.dot(x, a)
  }

  implicit class AlgebraSyntax[F[+_]](private val x: F[Double]) extends AnyVal {
    def normalize(implicit D: DotProd[F, Double], F: Covariant[F]): F[Double] =
      Algebra.normalize(x)

    def norm(implicit D: DotProd[F, Double]): Double =
      D.norm(x)

    def orthonormalize(implicit
      F: Covariant[F],
      F2: ForEach[F],
      D: DotProd[F, Double],
      M: VectorOps[F, Double]
    ): NonEmptyVector[F[Double]] =
      Algebra.orthonormalize(NonEmptyVector(x))
  }

}
