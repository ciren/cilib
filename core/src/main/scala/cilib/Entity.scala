package cilib

import scala.language.higherKinds
import _root_.scala.Predef.{any2stringadd => _, _}
import scalaz._
import Scalaz._

import spire.math._

final case class Entity[S,A](state: S, pos: Position[A])

object Entity {
  // Step to evaluate the particle
  def eval[S,A](f: Position[A] => Position[A])(entity: Entity[S,A]): Step[A,Entity[S,A]] =
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

  def foldMap1[B](f: A => B)(implicit M: Semigroup[B]) =
    pos.foldMap1(f)

  def traverse1[G[_], B](f: A => G[B])(implicit G: Apply[G]): G[Position[B]] =
    pos.traverse1(f).map(Point(_, boundary))

  def pos =
    this match {
      case Point(x, _)          => x
      case Solution(x, _, _, _) => x
    }

  def fit: Maybe[Fit] =
    this match {
      case Point(_, _)          => Maybe.empty
      case Solution(_, _, f, _) => Maybe.just(f)
    }

  def violations: Maybe[List[Constraint[A,Double]]] =
    this match {
      case Point(_, _)          => Maybe.empty
      case Solution(_, _, _, v) => Maybe.just(v)
    }

  def toPoint: Position[A] =
    this match {
      case Point(_, _) => this
      case Solution(x, b, _, _) => Point(x, b)
    }

  def feasible: Option[Position[A]] =
    violations.map(_.isEmpty).getOrElse(false).option(this)

  def boundary =
    this match {
      case Point(_, b) => b
      case Solution(_, b, _, _) => b
    }
}

final case class Point[A] private[cilib] (x: NonEmptyList[A], b: NonEmptyList[Interval[Double]]) extends Position[A]
final case class Solution[A] private[cilib] (x: NonEmptyList[A], b: NonEmptyList[Interval[Double]], f: Fit, v: List[Constraint[A,Double]]) extends Position[A]

object Position {

  implicit def positionInstances: Bind[Position] with Traverse1[Position] with Align[Position] =
    new Bind[Position] with Traverse1[Position] with Align[Position] {
      override def map[A, B](fa: Position[A])(f: A => B): Position[B] =
        fa map f

      override def bind[A, B](fa: Position[A])(f: A => Position[B]): Position[B] =
        fa flatMap f

      override def traverse1Impl[G[_] : Apply, A, B](fa: Position[A])(f: A => G[B]): G[Position[B]] =
        fa traverse1 f

      override def foldMapRight1[A, B](fa: cilib.Position[A])(z: A => B)(f: (A, => B) => B): B =
        fa.pos.foldMapRight1(z)(f)

      def alignWith[A, B, C](f: A \&/ B => C) =
        (a, b) => Point(a.pos.alignWith(b.pos)(f), a.boundary)

    }

  implicit class PositionVectorOps[/*F[_],*/A](val x: Position[/*F,*/A]) extends AnyVal {
    def zeroed(implicit A: Monoid[A]): Position[A] =
      x.map(_ => A.zero)

    import spire.algebra._
    def + (other: Position[A])(implicit M: Module[Position[A],A]): Position[A] =
      M.plus(x, other)

    def - (other: Position[A])(implicit M: Module[Position[A],A]): Position[A] =
      M.minus(x, other)

    /*def * (other: Position[F, A])(implicit F: Zip[F]) = Solution(x.pos.zipWith(other.pos)((a, ob) => ob.map(_ * a).getOrElse(a))._2) */

    def *: (scalar: A)(implicit M: Module[Position[A],A]): Position[A] =
      M.timesl(scalar, x)
  }

  implicit def positionFitness[/*F[_],*/A] = new Fitness[Position[A]] {
    def fitness(a: Position[A]) =
      a.fit
  }

  def apply[/*F[_]:Foldable1,*/A](xs: NonEmptyList[A], b: NonEmptyList[Interval[Double]]): Position[A] =
    Point(xs, b)

  def createPosition[A](domain: NonEmptyList[Interval[Double]]) =
    domain.traverseU(x => Dist.uniform(x.lower.value, x.upper.value)) map (x => Position(x, domain))//.list))

  def createPositions(domain: NonEmptyList[Interval[Double]], n: Int) =
    createPosition(domain) replicateM n

  def createCollection[A](f: Position[Double] => A)(domain: NonEmptyList[Interval[Double]], n: Int): RVar[List[A]] =
    createPositions(domain,n).map(_.map(f))
}


trait NonEmpty[F[_]]
object NonEmpty {
  implicit object NonEmptyNEL extends NonEmpty[NonEmptyList]
}

sealed trait Bound[A] {
  def value: A
  def toDouble(implicit N: Numeric[A]) = N.toDouble(value)
}
case class Closed[A](value: A) extends Bound[A]
case class Open[A](value: A) extends Bound[A]

final class Interval[A] private[cilib] (val lower: Bound[A], val upper: Bound[A]) {

   // Intervals _definitely_ have at least 1 element, so invariant in the type
  def ^(n: Int): NonEmptyList[Interval[A]] =
    NonEmptyList.nel(this, (1 to n - 1).map(_ => this).toList)

}

object Interval {
  def apply[A](lower: Bound[A], upper: Bound[A]) =
    new Interval(lower, upper)
}
