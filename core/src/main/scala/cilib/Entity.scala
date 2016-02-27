package cilib

import cilib.algebra._

import _root_.scala.Predef.{any2stringadd => _, _}
import scala.language.higherKinds
import scalaz._
import Scalaz._

import spire.algebra.{Field,Module,NRoot,Ring}
import spire.implicits._
import spire.math._

final case class Entity[S,A](state: S, pos: Position[A])

object Entity {
  // Step to evaluate the Entity
  def eval[S,A:Numeric](f: Position[A] => Position[A])(entity: Entity[S,A]): Step[A,Entity[S,A]] =
    Step.evalF(f(entity.pos)).map(p => Lenses._position.set(p)(entity))

}

sealed abstract class Position[A] {
  import Position._

  def map[B](f: A => B): Position[B] =
    Point(pos map f, boundary)

  def flatMap[B](f: A => Position[B]): Position[B] =
    Point(pos flatMap (f(_).pos), boundary)

  def zip[B](other: Position[B]): Position[(A, B)] =
    Point(pos.zip(other.pos), boundary)

  def traverse[G[_]: Applicative, B](f: A => G[B]): G[Position[B]] =
    pos.traverse(f).map(Point(_, boundary))

  def pos =
    this match {
      case Point(x, _)       => x
      case Solution(x, _, _) => x
    }

  def toPoint: Position[A] =
    this match {
      case Point(_, _)       => this
      case Solution(x, b, _) => Point(x, b)
    }

  def objective: Maybe[Objective[A]] =
    this match {
      case Point(_,_)        => Maybe.empty
      case Solution(_, _, o) => Maybe.just(o)
    }

  def boundary =
    this match {
      case Point(_, b)       => b
      case Solution(_, b, _) => b
    }
}

final case class Point[A] private[cilib] (x: List[A], b: NonEmptyList[Interval[Double]]) extends Position[A]
final case class Solution[A] private[cilib] (x: List[A], b: NonEmptyList[Interval[Double]], o: Objective[A]) extends Position[A]

object Position {

  implicit def positionInstances: Bind[Position] with Traverse[Position] with Align[Position] =
    new Bind[Position] with Traverse[Position] with Align[Position] {
      override def map[A, B](fa: Position[A])(f: A => B): Position[B] =
        fa map f

      override def bind[A, B](fa: Position[A])(f: A => Position[B]): Position[B] =
        fa flatMap f

      override def traverseImpl[G[_]: Applicative, A, B](fa: Position[A])(f: A => G[B]): G[Position[B]] =
        fa traverse f

      def alignWith[A, B, C](f: A \&/ B => C) =
        (a, b) => Point(a.pos.alignWith(b.pos)(f), a.boundary)

    }

  implicit def positionDotProd[A](implicit A: Numeric[A]): algebra.DotProd[Position, A] =
    new algebra.DotProd[Position, A] {
      def dot(a: Position[A], b: Position[A]): Double =
        a.zip(b).pos.foldLeft(A.zero) { case (a, b) => a + (b._1 * b._2) }.toDouble
    }

  implicit def positionPointwise[A](implicit A: Numeric[A]): algebra.Pointwise[Position, A] =
    new algebra.Pointwise[Position, A] {
      def pointwise(a: Position[A], b: Position[A]) =
        (a zip b).map(x => x._1 * x._2)
    }

  implicit class PositionVectorOps[A](val x: Position[A]) extends AnyVal {
    def zeroed(implicit A: Ring[A]): Position[A] =
      x.map(_ => A.zero)

    def + (other: Position[A])(implicit M: Module[Position[A],A]): Position[A] =
      M.plus(x, other)

    def - (other: Position[A])(implicit M: Module[Position[A],A]): Position[A] =
      M.minus(x, other)

    def *: (scalar: A)(implicit M: Module[Position[A],A]): Position[A] =
      M.timesl(scalar, x)

    def unary_-(implicit M: Module[Position[A],A]): Position[A] =
      M.negate(x)

    def isZero(implicit R: Ring[A]) = x.pos.forall(_ == R.zero)

  }

  implicit def positionFitness[A] = new Fitness[Position,A] {
    def fitness(a: Position[A]) =
      a.objective
  }

  implicit def positionEqual[A:scalaz.Equal] =
    scalaz.Equal.equal[Position[A]]((a, b) => (a.pos === b.pos) && (a.boundary === b.boundary))

  def apply[A](xs: NonEmptyList[A], b: NonEmptyList[Interval[Double]]): Position[A] =
    Point(xs.list.toList, b)

  def createPosition[A](domain: NonEmptyList[Interval[Double]]) =
    domain.traverseU(x => Dist.uniform(Interval(x.lowerValue, x.upperValue))) map (x => Position(x, domain))

  def createPositions(domain: NonEmptyList[Interval[Double]], n: Int) =
    createPosition(domain) replicateM n

  def createCollection[A](f: Position[Double] => A)(domain: NonEmptyList[Interval[Double]], n: Int): RVar[List[A]] =
    createPositions(domain,n).map(_.map(f))

}
