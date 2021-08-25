package cilib

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import spire.algebra.{ LeftModule, Ring }
import spire.implicits._
import spire.math._
import zio.prelude._

sealed abstract class Position[+A] {
  import Position._

  def map[B](f: A => B): Position[B] =
    Point(pos.map(f), boundary)

  def flatMap[B](f: A => Position[B]): Position[B] =
    Point(pos.flatMap(f(_).pos), boundary)

  def zip[B](other: Position[B]): Position[(A, B)] =
    Point(pos.zip(other.pos), boundary)

  def traverse[G[+_]: IdentityBoth: Covariant, B](f: A => G[B]): G[Position[B]] =
    this.forEach(f)

  def take(n: Int): List[A] =
    pos.toChunk.take(n).toList

  def drop(n: Int): List[A] =
    pos.toChunk.drop(n).toList

  def pos: NonEmptyVector[A] =
    this match {
      case Point(x, _)       => x
      case Solution(x, _, _) => x
    }

  def toPoint: Position[A] =
    this match {
      case Point(_, _)       => this
      case Solution(x, b, _) => Point(x, b)
    }

  def objective: Option[Objective] =
    this match {
      case Point(_, _)       => None
      case Solution(_, _, o) => Some(o)
    }

  def boundary: NonEmptyVector[Interval[Double]] =
    this match {
      case Point(_, b)       => b
      case Solution(_, b, _) => b
    }

  def forall(f: A => Boolean): Boolean =
    pos.forall(f)
}

object Position {
  private final case class Point[A](x: NonEmptyVector[A], b: NonEmptyVector[Interval[Double]]) extends Position[A]
  private final case class Solution[A](x: NonEmptyVector[A], b: NonEmptyVector[Interval[Double]], o: Objective)
      extends Position[A]

  implicit def positionEqual[A: zio.prelude.Equal]: zio.prelude.Equal[Position[A]] =
    zio.prelude.Equal.make[Position[A]] { (l, r) =>
      l.pos === r.pos && l.boundary === r.boundary
    }

  implicit val positionForEach: ForEach[Position] =
    new ForEach[Position] {
      def forEach[G[+_]: IdentityBoth: Covariant, A, B](fa: Position[A])(f: A => G[B]): G[Position[B]] =
        ForEach[NonEmptyVector].forEach(fa.pos)(f).map(Point(_, fa.boundary))
    }

  implicit val positionNonEmptyForEach: NonEmptyForEach[Position] =
    new NonEmptyForEach[Position] {
      def forEach1[G[+_]: AssociativeBoth: Covariant, A, B](fa: Position[A])(f: A => G[B]): G[Position[B]] =
        NonEmptyForEach[NonEmptyVector].forEach1(fa.pos)(f).map(Point(_, fa.boundary))
    }

  implicit def positionDotProd[A](implicit A: Numeric[A]): algebra.DotProd[Position, A] =
    new algebra.DotProd[Position, A] {
      import spire.implicits._

      def dot(a: Position[A], b: Position[A]): Double =
        a.zip(b).pos.foldLeft(A.zero) { case (a, b) => a + (b._1 * b._2) }.toDouble
    }

  implicit def positionPointwise[A](implicit A: Numeric[A]): algebra.Pointwise[Position, A] =
    new algebra.Pointwise[Position, A] {
      import spire.implicits._

      def pointwise(a: Position[A], b: Position[A]) =
        a.zip(b).map(x => x._1 * x._2)
    }

  implicit def positionModule[A](implicit sc: Ring[A]): LeftModule[Position[A], A] =
    new LeftModule[Position[A], A] {
      def scalar: Ring[A] = sc

      def negate(x: Position[A]) = x.map(scalar.negate)
      def zero                   = Position(NonEmptyVector(scalar.zero), NonEmptyVector(spire.math.Interval(0.0, 0.0)))

      def plus(x: Position[A], y: Position[A]) = {
        val combined =
          x.pos.zipAllWith(y.pos.toChunk)(identity, identity)(scalar.plus(_, _))

        Point(combined, x.boundary)
      }

      def timesl(r: A, v: Position[A]): Position[A] =
        v.map(scalar.times(r, _))
    }

  implicit class PositionVectorOps[A](private val x: Position[A]) extends AnyVal {
    def zeroed(implicit A: Ring[A]): Position[A] =
      x.map(_ => A.zero)

    def +(other: Position[A])(implicit M: LeftModule[Position[A], A]): Position[A] =
      M.plus(x, other)

    def -(other: Position[A])(implicit M: LeftModule[Position[A], A]): Position[A] =
      M.minus(x, other)

    def *:(scalar: A)(implicit M: LeftModule[Position[A], A]): Position[A] =
      M.timesl(scalar, x)

    def unary_-(implicit M: LeftModule[Position[A], A]): Position[A] =
      M.negate(x)

    def isZero(implicit R: Ring[A]): Boolean =
      x.forall(_ == R.zero)
  }

  implicit def positionFitness[A]: Fitness[Position, A, A] =
    new Fitness[Position, A, A] {
      def fitness(a: Position[A]) =
        a.objective
    }

  def eval[A](e: Eval[NonEmptyVector], pos: Position[A]): RVar[Position[A]] =
    pos match {
      case Point(x, b) =>
        e.eval.map { f =>
          val s: Objective = f.apply(x)
          Solution(x, b, s)
        }

      case x @ Solution(_, _, _) =>
        RVar.pure(x)
    }

  def apply[A](xs: NonEmptyVector[A], b: NonEmptyVector[Interval[Double]]): Position[A] =
    Point(xs, b)

  def createPosition[A](domain: NonEmptyVector[Interval[Double]]): RVar[Position[Double]] =
    ForEach[NonEmptyVector]
      .forEach(domain)(Dist.uniform)
      .map(z => Position(z, domain))

  def createPositions(
    domain: NonEmptyVector[Interval[Double]],
    n: Int Refined Positive
  ): RVar[NonEmptyVector[Position[Double]]] =
    createPosition(domain)
      .replicateM(n.value)
      .map(list =>
        NonEmptyVector
          .fromIterableOption(list)
          .getOrElse(sys.error("Impossible -> refinement requires n to be positive, i.e. n > 0"))
      )

  def createCollection[A](
    f: Position[Double] => A
  )(domain: NonEmptyVector[Interval[Double]], n: Int Refined Positive): RVar[NonEmptyVector[A]] =
    createPositions(domain, n).map(_.map(f))
}
