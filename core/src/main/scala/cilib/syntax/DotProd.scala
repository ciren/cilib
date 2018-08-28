package cilib
package syntax

import scalaz.{Foldable1, Functor, NonEmptyList}
import spire.implicits._
import spire.algebra._

import cilib.algebra._

object dotprod {
  implicit class DotProdSyntax[F[_], A](val x: F[A]) extends AnyVal {
    def ∙(a: F[A])(implicit D: DotProd[F, A]): Double = D.dot(x, a)
  }

  implicit class AlgebraSyntax[F[_], A](val x: F[A]) extends AnyVal {
    def normalize(implicit M: Module[F[A], Double], D: DotProd[F, A]): F[A] = Algebra.normalize(x)

    def magnitude(implicit D: DotProd[F, A]): Double = Algebra.magnitude(x)

    def orthonormalize(implicit F: Functor[F],
                       F2: Foldable1[F],
                       F3: Field[A],
                       A: NRoot[A],
                       D: DotProd[F, A],
                       M: Module[F[A], Double]): NonEmptyList[F[A]] =
      Algebra.orthonormalize(NonEmptyList(x))
  }

}
