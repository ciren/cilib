package cilib

import scala.language.higherKinds
import _root_.scala.Predef.{any2stringadd => _, _}
import scalaz._
import Scalaz._

import spire.math._

sealed abstract class Position[F[_],A] { // Transformer of some sort, over the type F?
  import Position._

  def map[B](f: A => B)(implicit F: Monad[F]): Position[F,B] =
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

  def fit: Maybe[Fit] =
    this match {
      case Point(_)          => Maybe.empty
      case Solution(_, f, _) => Maybe.just(f)
    }

  def violations: Maybe[List[Violation]] =
    this match {
      case Point(_)          => Maybe.empty
      case Solution(_, _, v) => Maybe.just(v)
    }

  //  def eval: StateT[RVar, Problem, Position[F,A]] =
  def eval(f: Eval[F,A])(implicit F: Foldable[F], A: Numeric[A]): RVar[Position[F,A]] =
/*    StateT(problem => {
      this match {
        case Point(x) =>
          val (np, fit, vio) = problem.eval(x)
          np.map((_, Solution(x, fit, vio)))
          //(np, Solution(x, fit, vio))
        case Solution(_, _, _) =>
          RVar.point((problem, this))
          //(problem, this)
      }
 })*/
    RVar.point(
      this match {
        case Point(x) =>
          val (fit, vio) = f.eval(x)
          Solution(x, fit, vio)
        case x @ Solution(_, _, _) =>
          x
      })


  def toPoint: Position[F, A] =
    this match {
      case Point(x) => this
      case Solution(x, _, _) => Point(x)
    }

  def feasible: Option[Position[F,A]] =
    violations.map(_.forall(_ => true)).getOrElse(false).option(this)
}

object Position {

  private final case class Point[F[_],A](x: F[A]) extends Position[F,A]
  private final case class Solution[F[_],A](x: F[A], f: Fit, v: List[Violation]) extends Position[F,A]

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

  implicit class PositionVectorOps[F[_],A](val x: Position[F,A]) extends AnyVal {
    import spire.algebra._
    def + (other: Position[F,A])(implicit M: Module[F[A],A]): Position[F, A] =
      Point(M.plus(x.pos, other.pos))

    def - (other: Position[F, A])(implicit M: Module[F[A],A]): Position[F,A] =
      Point(M.minus(x.pos, other.pos))

    /*def * (other: Position[F, A])(implicit F: Zip[F]) = Solution(x.pos.zipWith(other.pos)((a, ob) => ob.map(_ * a).getOrElse(a))._2) */

    def *:(scalar: A)(implicit M: Module[F[A],A]): Position[F, A] =
      Point(M.timesl(scalar, x.pos))
  }

  implicit def positionFitness[F[_], A] = new Fitness[Position[F, A]] {
    def fitness(a: Position[F, A]) =
      a.fit
  }

  def apply[F[_],A](xs: F[A]): Position[F, A] =
    Point(xs)

   def createPosition[A](domain: List[Interval[A]])(implicit A: Numeric[A]) =
     domain.traverseU(x => Dist.uniform(A.toDouble(x.lower.value), A.toDouble(x.upper.value))) map (Position(_))

   def createPositions[A: Numeric](domain: List[Interval[A]], n: Int) =
     createPosition(domain) replicateM n

   def createCollection[A, B: Numeric](f: Position[List,Double] => A)(domain: List[Interval[B]], n: Int): RVar[List[A]] =
     createPositions(domain,n).map(_.map(f))

}


sealed trait Bound[A] {
  def value: A
  def toDouble(implicit N: Numeric[A]) = N.toDouble(value)
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

