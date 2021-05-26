package cilib

import spire.algebra.{ Semigroup => _, _ }
import spire.implicits._
import spire.math._
import spire.math.{ abs, max }
import zio.prelude._

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
      type Memo = Map[(Int, Int), Int]

      def dist(x: String, y: String): B = {
        def f(i: Int, j: Int): State[Memo, Int] =
          (i, j) match {
            case (i, 0) => zio.prelude.fx.ZPure.succeed(i)
            case (0, j) => zio.prelude.fx.ZPure.succeed(j)
            case (i, j) =>
              zio.prelude.fx.ZPure.modify(memo =>
                memo.get((i, j)) match {
                  case Some(value) => (memo, value)
                  case None =>
                    (for {
                      a <- f(i - 1, j)
                      b <- f(i, j - 1)
                      c <- f(i - 1, j - 1)
                    } yield NonEmptyList(
                      a + 1,
                      b + 1,
                      c + (if (x(i - 1) == y(j - 1)) 0 else 1)
                    ).min).run(memo)
                }
              )
          }

        B.fromInt(f(x.length, y.length).runResult(Map.empty))
      }
    }

  def discrete[A, B](implicit A: scalaz.Equal[A], B: Integral[B]): MetricSpace[A, B] =
    new MetricSpace[A, B] {
      def dist(x: A, y: A) = B.fromInt(if (A.equal(x, y)) 0 else 1)
    }

  def minkowski[F[+_], A: Signed: Field, B](
    alpha: Int
  )(implicit F: ForEach[F], A: Fractional[A], ev: Field[B]): MetricSpace[F[A], B] =
    new MetricSpace[F[A], B] {
      def dist(x: F[A], y: F[A]) =
        ev.fromDouble(
          F.toList(x)
            .zip(F.toList(y))
            .map { case (ai, bi) => abs(ai - bi) ** alpha }
            .foldLeft(0.0) { (ai, bi) =>
              (ai + A.toDouble(bi)) ** (1.0 / alpha)
            }
        )
    }

  def manhattan[F[+_]: ForEach, A: Signed: Field: Fractional, B: Fractional: Field]: MetricSpace[F[A], B] =
    minkowski[F, A, B](1)

  def euclidean[F[+_]: ForEach, A: Signed: Field: Fractional, B: Fractional: Field]: MetricSpace[F[A], B] =
    minkowski[F, A, B](2)

  def chebyshev[F[+_], A: Order: Signed](implicit ev: Ring[A], F: ForEach[F]): MetricSpace[F[A], A] =
    new MetricSpace[F[A], A] {
      def dist(x: F[A], y: F[A]) =
        F.toList(x)
          .zip(F.toList(y))
          .map { case (ai, bi) => abs(ai - bi) }
          .foldLeft(ev.zero) { (ai, bi) =>
            max(ai, bi)
          }
    }

  def hamming[F[+_], A](implicit F: ForEach[F]): MetricSpace[F[A], Int] =
    new MetricSpace[F[A], Int] {
      def dist(x: F[A], y: F[A]) =
        F.toList(x).zip(F.toList(y)).filter { case (ai, bi) => ai != bi }.size
    }

  def pure[A, B](a: B): MetricSpace[A, B] =
    new MetricSpace[A, B] {
      def dist(x: A, y: A) = a
    }

  implicit def MetricSpaceAssociative[A, B](implicit B: Associative[B]): Associative[MetricSpace[A, B]] =
    new Associative[MetricSpace[A, B]] {
      def combine(l: => MetricSpace[A, B], r: => MetricSpace[A, B]): MetricSpace[A, B] =
        new MetricSpace[A, B] {
          def dist(x: A, y: A) = B.combine(l.dist(x, y), r.dist(x, y))
        }
    }

  // implicit def metricSpaceMonad[A]: Monad[MetricSpace[A, *]] =
  //   new Monad[MetricSpace[A, *]] {
  //     def point[B](a: => B): MetricSpace[A, B] =
  //       MetricSpace.pure[A, B](a)
  //
  //     def bind[B, C](fa: MetricSpace[A, B])(f: B => MetricSpace[A, C]): MetricSpace[A, C] =
  //       fa.flatMap(f)
  //   }
}
