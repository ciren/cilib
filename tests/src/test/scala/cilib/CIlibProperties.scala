package cilib

import org.scalacheck._
import org.scalacheck.Prop._

import spire.implicits._
import spire.algebra._
import spire.math._

import cilib.algebra._

object CIlibProperties {

  object dotprod {
    def commutativity[F[_],A](implicit D: DotProd[F,A], arb: Arbitrary[F[A]]) =
      forAll {
        (a: F[A], b: F[A]) => D.dot(a, b) == D.dot(b, a)
      }

    def distributive[F[_],A](implicit D: DotProd[F,A], arb: Arbitrary[F[A]], M: Module[F[A],A]) =
      forAll {
        (a: F[A], b: F[A], c: F[A]) => D.dot(a, b + c) == D.dot(a, b) + D.dot(a, c)
      }

    def bilinear[F[_],A](implicit D: DotProd[F,A], arb: Arbitrary[F[A]], arb2: Arbitrary[A], M: Module[F[A],A], N: Numeric[A]) =
      forAll { (a: F[A], b: F[A], c: F[A], r: A) =>
        (N.toDouble(r) < 10000.0 && N.toDouble(r) >= -10000.0) ==>
          (D.dot(a, r *: b + c) === N.times(r, N.fromDouble(D.dot(a, b))).toDouble + D.dot(a, c))
      }

    def scalarMultiplication[F[_],A](
      implicit N: Numeric[A], D: DotProd[F,A], arb: Arbitrary[F[A]], c: Arbitrary[A], M: Module[F[A],A]) =
      forAll {
        (a: F[A], b: F[A], c1: A, c2: A) =>
          (N.toInt(N.times(c1, c2)) <= 1000000 && N.toInt(N.times(c1, c2)) >= -1000000) ==>
            (D.dot(c1 *: a, c2 *: b) === N.toDouble(N.times(c1, c2)) * D.dot(a, b))
      }

  }
}
