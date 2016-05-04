package cilib

import scalaz.{Order => _, _}
import scalaz.syntax.foldable._

import spire.math.{abs,max}
import spire.implicits._
import spire.algebra._
import spire.math._

/**
  A MetricSpace is a set together with a notion of distance between elements.
  Distance is computed by a function dist which has the following four laws:

  1.  non-negative: forall x y. dist x y >= 0
  2.  identity of indiscernibles: forall x y. dist x y == 0 <=> x == y
  3.  symmetry: forall x y. dist x y == dist y x
  4.  triangle inequality: forall x y z. dist x z <= dist x y + dist y z

  See the Wikipedia article on metric spaces for more details.
  */
trait MetricSpace[A,B] {
  def dist(x: A, y: A): B
}

object MetricSpace {
  //def levenshtein[B](implicit I: Integral[B]): MetricSpace[String,B] =

  def discrete[A,B](implicit A: scalaz.Equal[A], B: Integral[B]) =
    new MetricSpace[A,B] {
      def dist(x: A, y: A) = B.fromInt(if (A.equal(x,y)) 0 else 1)
    }

  def minkowski[F[_]:Foldable,A:Signed:Field,B](alpha: Int)(implicit A: Fractional[A], B: Fractional[B], ev: Field[B]) =
    new MetricSpace[F[A],B] {
      def dist(x: F[A], y: F[A]) =
        ev.fromDouble(
          (x.toList zip y.toList)
            .map { case (ai, bi) => abs(ai - bi) ** alpha }
            .foldLeft(0.0) { (ai, bi) => (ai + A.toDouble(bi)) ** (1.0 / alpha) })
    }

  def euclidean[F[_]:Foldable,A:Signed:Field:Fractional,B:Fractional:Field] = minkowski[F,A,B](1)
  def manhattan[F[_]:Foldable,A:Signed:Field:Fractional,B:Fractional:Field] = minkowski[F,A,B](2)

  def chebyshev[F[_]:Foldable,A:Order:Signed](implicit ev: Ring[A]) =
    new MetricSpace[F[A],A] {
      def dist(x: F[A], y: F[A]) =
        (x.toList zip y.toList)
          .map { case (ai, bi) => abs(ai - bi) }
          .foldLeft(ev.zero) { (ai, bi) => max(ai, bi) }
    }

  def hamming[F[_]:Foldable,A] =
    new MetricSpace[F[A],Int] {
      def dist(x: F[A], y: F[A]) =
        (x.toList zip y.toList).filter { case (ai, bi) => ai != bi }.size
    }
}
