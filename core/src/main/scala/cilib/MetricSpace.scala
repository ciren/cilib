package cilib

import scalaz.{Order => _, _}
import scalaz.syntax.foldable._

import spire.math.{abs, max}
import spire.implicits._
import spire.algebra.{Semigroup => _, _}
import spire.math._

/**
  A MetricSpace is a set together with a notion of distance between elements.
  Distance is computed by a function dist which has the following four laws:

  1.  non-negative: forall x y. dist x y >= 0
  1.  identity of indiscernibles: forall x y. dist x y == 0 <=> x == y
  1.  symmetry: forall x y. dist x y == dist y x
  1.  triangle inequality: forall x y z. dist x z <= dist x y + dist y z

  See the Wikipedia article on metric spaces for more details.
  */
trait MetricSpace[A, B] { self =>
  def dist(x: A, y: A): B

  def map[C](f: B => C): MetricSpace[A, C] =
    new MetricSpace[A, C] {
      def dist(x: A, y: A) = f(self.dist(x, y))
    }

  def flatMap[C](f: B => MetricSpace[A, C]): MetricSpace[A, C] =
    new MetricSpace[A, C] {
      def dist(x: A, y: A) =
        f(self.dist(x, y)).dist(x, y)
    }
}

object MetricSpace {
  def levenshtein[B](implicit B: Integral[B]): MetricSpace[String, B] =
    new MetricSpace[String, B] {

      def dist(x: String, y: String): B = {
        def f(i: Int, j: Int): State[Map[(Int, Int), Int], Int] =
          (i, j) match {
            case (i, 0) => State(s => (s, i))
            case (0, j) => State(s => (s, j))
            case (i, j) =>
              for {
                a <- f(i - 1, j)
                b <- f(i, j - 1)
                c <- f(i - 1, j - 1)
              } yield
                List(
                  a + 1,
                  b + 1,
                  c + (if (x(i - 1) == y(j - 1)) 0 else 1)
                ).min
          }

        B.fromInt(f(x.length, y.length).eval(Map.empty))
      }
    }

  def discrete[A, B](implicit A: scalaz.Equal[A], B: Integral[B]): MetricSpace[A, B] =
    new MetricSpace[A, B] {
      def dist(x: A, y: A) = B.fromInt(if (A.equal(x, y)) 0 else 1)
    }

  def minkowski[F[_]: Foldable, A: Signed: Field, B](
      alpha: Int)(implicit A: Fractional[A], ev: Field[B]): MetricSpace[F[A], B] =
    new MetricSpace[F[A], B] {
      def dist(x: F[A], y: F[A]) =
        ev.fromDouble(
          x.toList
            .zip(y.toList)
            .map { case (ai, bi) => abs(ai - bi) ** alpha }
            .foldLeft(0.0) { (ai, bi) =>
              (ai + A.toDouble(bi)) ** (1.0 / alpha)
            })
    }

  def manhattan[F[_]: Foldable, A: Signed: Field: Fractional, B: Fractional: Field]
    : MetricSpace[F[A], B] =
    minkowski[F, A, B](1)

  def euclidean[F[_]: Foldable, A: Signed: Field: Fractional, B: Fractional: Field]
    : MetricSpace[F[A], B] =
    minkowski[F, A, B](2)

  def chebyshev[F[_]: Foldable, A: Order: Signed](implicit ev: Ring[A]): MetricSpace[F[A], A] =
    new MetricSpace[F[A], A] {
      def dist(x: F[A], y: F[A]) =
        x.toList
          .zip(y.toList)
          .map { case (ai, bi) => abs(ai - bi) }
          .foldLeft(ev.zero) { (ai, bi) =>
            max(ai, bi)
          }
    }

  def hamming[F[_]: Foldable, A]: MetricSpace[F[A], Int] =
    new MetricSpace[F[A], Int] {
      def dist(x: F[A], y: F[A]) =
        x.toList.zip(y.toList).filter { case (ai, bi) => ai != bi }.size
    }

  @deprecated("This method has been deprecated, use pure instead, it is technically better",
              "2.0.2")
  def point[A, B](a: B): MetricSpace[A, B] =
    pure(a)

  def pure[A, B](a: B): MetricSpace[A, B] =
    new MetricSpace[A, B] {
      def dist(x: A, y: A) = a
    }

  implicit def metricSpaceProfunctor: Profunctor[MetricSpace] =
    new Profunctor[MetricSpace] {
      def mapfst[A, B, C](fab: MetricSpace[A, B])(f: C => A): MetricSpace[C, B] =
        new MetricSpace[C, B] {
          def dist(x: C, y: C): B =
            fab.dist(f(x), f(y))
        }

      def mapsnd[A, B, C](fab: MetricSpace[A, B])(f: B => C): MetricSpace[A, C] =
        fab.map(f)
    }

  implicit def metricSpaceSemiGroup[A, B](implicit B: Semigroup[B]): Semigroup[MetricSpace[A, B]] =
    new Semigroup[MetricSpace[A, B]] {
      def append(f1: MetricSpace[A, B], f2: => MetricSpace[A, B]) =
        new MetricSpace[A, B] {
          def dist(x: A, y: A) = B.append(f1.dist(x, y), f2.dist(x, y))
        }
    }

  implicit def metricSpaceMonad[A]: Monad[MetricSpace[A, ?]] =
    new Monad[MetricSpace[A, ?]] {
      def point[B](a: => B): MetricSpace[A, B] =
        MetricSpace.pure[A, B](a)

      def bind[B, C](fa: MetricSpace[A, B])(f: B => MetricSpace[A, C]): MetricSpace[A, C] =
        fa.flatMap(f)
    }
}
