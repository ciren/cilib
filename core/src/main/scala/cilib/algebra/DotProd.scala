package cilib
package algebra

import scalaz._
import Scalaz._

import spire.algebra._
import spire.implicits._

trait DotProd[F[_], A] {
  def dot(a: F[A], b: F[A]): Double
  def ∙(a: F[A], b: F[A]): Double = dot(a, b)

  def normsqr(a: F[A]): Double = dot(a, a)
  def norm(a: F[A]): Double = math.sqrt(normsqr(a))
  def lensqr(a: F[A]): Double = normsqr(a)
  def len(a: F[A]): Double = norm(a)
}

trait CrossProd[F[_], A] {
  def cross(a: F[A], b: F[A]): F[A]
}

trait Pointwise[F[_], A] {
  def pointwise(a: F[A], b: F[A]): F[A] // Pointwise multiplication
}

trait Orthongonal { // Need to examine the name of this typeclass??

}

object Algebra {

  def normalize[F[_], A](a: F[A])(implicit M: Module[F[A], Double], D: DotProd[F, A]): F[A] = {
    val mag = D.norm(a)
    if (mag === 0.0) a
    else (1.0 / mag) *: a
  }

  def magnitude[F[_], A](a: F[A])(implicit D: DotProd[F, A]): Double =
    D.norm(a)

  def distance[F[_], A](a: F[A], b: F[A])(implicit D: DotProd[F, A],
                                          M: Module[F[A], Double]): Double =
    D.norm(M.minus(a, b))

  def pointwise[F[_], A](a: F[A], b: F[A])(implicit P: Pointwise[F, A]): F[A] =
    P.pointwise(a, b)

  def vectorSum[F[_], A](xs: NonEmptyList[F[A]])(implicit M: Module[F[A], Double]): F[A] =
    xs.foldLeft1(M.plus)

  def meanVector[F[_], A](xs: NonEmptyList[F[A]])(implicit M: Module[F[A], Double]): F[A] =
    (1.0 / xs.length) *: vectorSum(xs)

  def orthogonalize[F[_]: Functor, A](x: F[A], vs: List[F[A]])(implicit D: DotProd[F, A],
                                                               F: Field[A],
                                                               M: Module[F[A], Double]): F[A] =
    vs.foldLeft(x)((a, b) => a - project(a, b))

  def project[F[_], A](x: F[A], other: F[A])(implicit D: DotProd[F, A],
                                             F: Functor[F],
                                             F2: Field[A],
                                             M: Module[F[A], Double]): F[A] =
    if (D.dot(other, other) == F2.zero) F.map(other)(_ => F2.zero)
    else (D.dot(x, other) / D.dot(other, other)) *: other

  def orthonormalize[F[_]: Functor: Foldable1, A: NRoot](vs: NonEmptyList[F[A]])(
      implicit D: DotProd[F, A],
      M: Module[F[A], Double],
      A: Field[A]): NonEmptyList[F[A]] = {
    val bases = vs.foldLeft(NonEmptyList(vs.head)) { (ob, v) =>
      val ui = ob.foldLeft(v) { (u, o) =>
        u - project(v, o)
      }
      if (ui.foldLeft(A.zero)(A.plus) == A.zero) ob
      else ob.append(NonEmptyList(ui))
    }

    bases.map(normalize(_))
  }

}
