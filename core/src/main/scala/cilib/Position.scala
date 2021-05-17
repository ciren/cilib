package cilib

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import spire.algebra.{ LeftModule, Ring }
import spire.implicits._
import spire.math._
import zio.prelude._

sealed abstract class Position[A] {
  import Position._

  def map[B](f: A => B): Position[B] =
    Point(pos.map(f), boundary)

  def flatMap[B](f: A => Position[B]): Position[B] =
    Point(pos.flatMap(f(_).pos), boundary)

  def zip[B](other: Position[B]): Position[(A, B)] =
    Point(pos.zip(other.pos), boundary)

  def traverse[G[+_]: IdentityBoth: Covariant, B](f: A => G[B]): G[Position[B]] =
    ForEach[NonEmptyList].forEach(pos)(f).map(Point(_, boundary))

  def take(n: Int): List[A] =
    pos.take(n)

  def drop(n: Int): List[A] =
    pos.drop(n)

  def pos: NonEmptyList[A] =
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

  def boundary: NonEmptyList[Interval[Double]] =
    this match {
      case Point(_, b)       => b
      case Solution(_, b, _) => b
    }

  def forall(f: A => Boolean): Boolean =
    pos.forall(f)
}

object Position {
  private final case class Point[A](x: NonEmptyList[A], b: NonEmptyList[Interval[Double]]) extends Position[A]
  private final case class Solution[A](x: NonEmptyList[A], b: NonEmptyList[Interval[Double]], o: Objective)
      extends Position[A]

  implicit def positionEqual[A: zio.prelude.Equal]: zio.prelude.Equal[Position[A]] =
    zio.prelude.Equal.make[Position[A]]((l, r) => {
      l.pos === r.pos && l.boundary === r.boundary
    })

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
      def zero                   = Position(NonEmptyList(scalar.zero), NonEmptyList(spire.math.Interval(0.0, 0.0)))

      def plus(x: Position[A], y: Position[A]) = {
        ???
        // import scalaz.syntax.align._
        // x.align(y)
        //   .map(
        //     _.fold(
        //       s = x => x,
        //       t = x => x,
        //       q = scalar.plus(_, _)
        //     )
        //   )
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

    def isZero(implicit R: Ring[A]): Boolean = {
      @annotation.tailrec
      def test(xs: List[A]): Boolean =
        xs match {
          case Nil      => true
          case x :: xss => if (x != R.zero) false else test(xss)
        }

      test(x.pos.toList)
    }
  }

  implicit def positionFitness[A]: Fitness[Position, A, A] =
    new Fitness[Position, A, A] {
      def fitness(a: Position[A]) =
        a.objective
    }

  def eval[A](e: Eval[NonEmptyList], pos: Position[A]): RVar[Position[A]] =
    pos match {
      case Point(x, b) =>
        e.eval.map { f =>
          val s: Objective = f.apply(x)
          Solution(x, b, s)
        }

      case x @ Solution(_, _, _) =>
        RVar.pure(x)
    }

  def apply[A](xs: NonEmptyList[A], b: NonEmptyList[Interval[Double]]): Position[A] =
    Point(xs, b)

  def createPosition[A](domain: NonEmptyList[Interval[Double]]): RVar[Position[Double]] = {
    val rvarZioNEL = zio.prelude.ForEach[zio.prelude.NonEmptyList].forEach(domain)(Dist.uniform)

    rvarZioNEL.map(znel => Position(znel, domain))
    //domain.traverse(Dist.uniform).map(x => Position(x, domain))
  }

  def createPositions(
    domain: NonEmptyList[Interval[Double]],
    n: Int Refined Positive
  ): RVar[NonEmptyList[Position[Double]]] =
    createPosition(domain)
      .replicateM(n.value)
      .map(list => NonEmptyList.fromIterableOption(list).getOrElse(sys.error("Impossible -> refinement requires n to be positive, i.e. n > 0")))

  def createCollection[A](
    f: Position[Double] => A
  )(domain: NonEmptyList[Interval[Double]], n: Int Refined Positive): RVar[NonEmptyList[A]] =
    createPositions(domain, n).map(_.map(f))
}
