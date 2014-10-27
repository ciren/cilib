package cilib

import scala.language.higherKinds
import _root_.scala.Predef.{any2stringadd => _, _}
import scalaz._
import Scalaz._

// Transformer of some sort, over the type F
sealed abstract class Position[F[_], A] {
  import Position._

  def map[B](f: A => B)(implicit F: Monad[F]): Position[F, B] =
    Point(pos map f)

  def flatMap[B](f: A => Position[F, B])(implicit F: Monad[F]): Position[F, B] =
    Point(F.bind(pos)(f(_).pos))

  def zip[B](other: Position[F, B])(implicit F: Zip[F]): Position[F, (A, B)] =
    Point(F.zip(pos, other.pos))

  def traverse[G[_]: Applicative, B](f: A => G[B])(implicit F: Traverse[F]): G[Position[F, B]] =
    F.traverse(pos)(f).map(Point(_))

  def pos =
    this match {
      case Point(x)          => x
      case Solution(x, _, _) => x
    }

  def fit: Option[Fit] =
    this match {
      case Point(_)          => None
      case Solution(_, f, _) => Some(f)
    }

  def violations: Option[List[Violation]] =
    this match {
      case Point(_)          => None
      case Solution(_, _, v) => Some(v)
    }

  def eval: State[Problem[F, A], Position[F, A]] =
    State(problem => {
      this match {
        case Point(x) =>
          val (np, fit, vio) = problem.eval(x)
          (np, Solution(x, fit, vio))
        case Solution(_, _, _) =>
          (problem, this)
      }
    })

  def toPoint: Position[F, A] =
    this match {
      case Point(x) => this
      case Solution(x, _, _) => Point(x)
    }
}

object Position {
  import spire.math.{ Interval => _, _ }
  import spire.implicits._

  implicit def positionInstances[F[_]](implicit F0: Monad[F], F1: Zip[F]): Bind[({type λ[α] = Position[F,α]})#λ] with Zip[({type λ[α] = Position[F,α]})#λ] =
    new Bind[({type λ[α] = Position[F,α]})#λ] with Zip[({type λ[α] = Position[F,α]})#λ] {
      def point[A](a: => A): cilib.Position[F,A] =
        Point(Applicative[F].point(a))

      def map[A, B](fa: Position[F, A])(f: A => B): Position[F, B] =
        fa map f

      def bind[A, B](fa: Position[F, A])(f: A => Position[F,B]): Position[F, B] =
        fa flatMap f

      def zip[A, B](a: => Position[F, A], b: => Position[F, B]): Position[F, (A, B)] =
        a zip b
    }

  private final case class Point[F[_],A](x: F[A]) extends Position[F,A]
  private final case class Solution[F[_],A](x: F[A], f: Fit, v: List[Violation]) extends Position[F,A]

  implicit class ToPositionVectorOps[F[_], A: Fractional](x: Position[F, A]) {
    def + (other: Position[F, A])(implicit Z: Zip[F], F: Functor[F]): Position[F, A] = Point {
      Z.zipWith(x.pos, other.pos)(_ + _)
    }
    def - (other: Position[F, A])(implicit Z: Zip[F], F: Functor[F]): Position[F, A] = Point(Z.zipWith(x.pos, other.pos)(_ - _))
    /*def * (other: Position[F, A])(implicit F: Zip[F]) = Solution(x.pos.zipWith(other.pos)((a, ob) => ob.map(_ * a).getOrElse(a))._2) */
    def *:(scalar: A)(implicit F: Functor[F]): Position[F, A] = Point(x.pos.map(_ * scalar))
  }

  implicit def positionFitness[F[_], A] = new Fitness[Position[F, A]] {
    def fitness(a: Position[F, A]) =
      a.fit
  }

  def apply[A](xs: List[A]): Position[List, A] =
    Point(xs)

  def createPosition[A: Numeric](domain: List[Interval[A]]) =
    domain.traverseU(x => Dist.uniform(x.lower.value.toDouble, x.upper.value.toDouble)) map (Position(_))

  def createPositions[A: Numeric](domain: List[Interval[A]], n: Int) =
    createPosition(domain) replicateM n

  def createCollection[A, B: Numeric](f: Pos[Double] => A)(domain: List[Interval[B]], n: Int) =
    createPositions(domain, n) map (_ map f)

}

sealed trait Bound[A] {
  def value: A
}
case class Closed[A](value: A) extends Bound[A]
case class Open[A](value: A) extends Bound[A]

final class Interval[A] private[cilib] (val lower: Bound[A], val upper: Bound[A]) {

  def ^(n: Int): List[Interval[A]] =
    (1 to n).map(_ => this).toList

}

object Interval {
  def apply[A](lower: Bound[A], upper: Bound[A]) =
    new Interval(lower, upper)

}
