package cilib

import zio.prelude._
import zio.prelude.newtypes.Natural

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

  def boundary: NonEmptyVector[Interval] =
    this match {
      case Point(_, b)       => b
      case Solution(_, b, _) => b
    }

  def forall(f: A => Boolean): Boolean =
    pos.forall(f)
}

object Position {
  private final case class Point[A](x: NonEmptyVector[A], b: NonEmptyVector[Interval]) extends Position[A]
  private final case class Solution[A](x: NonEmptyVector[A], b: NonEmptyVector[Interval], o: Objective)
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

  implicit def positionDotProd[A](implicit A: scala.math.Numeric[A]): algebra.DotProd[Position, A] =
    new algebra.DotProd[Position, A] {
      def dot(a: Position[A], b: Position[A]): Double =
        // FIXME: Is this actually wrong?
        A.toDouble(a.zip(b).pos.foldLeft(A.zero) { case (a, b) => A.plus(a, A.times(b._1, b._2)) })
    }

  implicit def positionPointwise[A](implicit A: scala.math.Numeric[A]): algebra.Pointwise[Position, A] =
    new algebra.Pointwise[Position, A] {
      def pointwise(a: Position[A], b: Position[A]) =
        a.zip(b).map(x => A.times(x._1, x._2))
    }

  implicit def positionVectorOps[A]: algebra.VectorOps[Position, A] =
    new algebra.VectorOps[Position, A] {
      def zeroed(a: Position[A])(implicit A: scala.math.Numeric[A]): Position[A] =
        a.map(_ => A.zero)

      def +(a: Position[A], b: Position[A])(implicit M: scala.math.Numeric[A]): Position[A] = {
        val combined =
          a.pos.zipAllWith(b.pos.toChunk)(identity, identity)(M.plus(_, _))

        Point(combined, a.boundary)
      }

      def -(a: Position[A], b: Position[A])(implicit M: scala.math.Numeric[A]): Position[A] = {
        val combined =
          a.pos.zipAllWith(b.pos.toChunk)(identity, identity)(M.minus(_, _))

        Point(combined, a.boundary)
      }

      def *:(scalar: A, a: Position[A])(implicit M: scala.math.Numeric[A]): Position[A] =
        a.map(x => M.times(scalar, x))

      def unary_-(a: Position[A])(implicit M: scala.math.Numeric[A]): Position[A] =
        a.map(x => M.negate(x))

      def isZero(a: Position[A])(implicit R: scala.math.Numeric[A]): Boolean =
        a.pos.forall(_ == R.zero)
    }

  implicit class PositionVectorOps[A](private val x: Position[A]) extends AnyVal {
    def zeroed(implicit A: scala.math.Numeric[A]): Position[A] =
      x.map(_ => A.zero)

    def +(other: Position[A])(implicit M: algebra.VectorOps[Position, A], A: scala.math.Numeric[A]): Position[A] =
      M.+(x, other)

    def -(other: Position[A])(implicit M: algebra.VectorOps[Position, A], A: scala.math.Numeric[A]): Position[A] =
      M.-(x, other)

    def *:(scalar: A)(implicit M: algebra.VectorOps[Position, A], A: scala.math.Numeric[A]): Position[A] =
      M.*:(scalar, x)

    def unary_-(implicit M: algebra.VectorOps[Position, A], A: scala.math.Numeric[A]): Position[A] =
      M.unary_-(x)

    def isZero(implicit R: scala.math.Numeric[A]): Boolean =
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
        e.eval(x).map(s => Solution(x, b, s))

      case x @ Solution(_, _, _) =>
        RVar.pure(x)
    }

  def apply[A](xs: NonEmptyVector[A], b: NonEmptyVector[Interval]): Position[A] =
    Point(xs, b)

  def createPosition[A](domain: NonEmptyVector[Interval]): RVar[Position[Double]] =
    ForEach[NonEmptyVector]
      .forEach(domain)(Dist.uniform)
      .map(z => Position(z, domain))

  def createPositions(
    domain: NonEmptyVector[Interval],
    n: Natural
  ): RVar[NonEmptyVector[Position[Double]]] =
    createPosition(domain)
      .replicateM(Natural.unwrap(n))
      .map(list =>
        NonEmptyVector
          .fromIterableOption(list)
          .getOrElse(sys.error("Impossible -> refinement requires n to be positive, i.e. n > 0"))
      )

  def createCollection[A](
    f: Position[Double] => A
  )(domain: NonEmptyVector[Interval], n: Natural): RVar[NonEmptyVector[A]] =
    createPositions(domain, n).map(_.map(f))
}
