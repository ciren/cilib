package cilib

import scala.language.higherKinds
import _root_.scala.Predef.{any2stringadd => _, _}
import scalaz._
import Scalaz._

import spire.math._

final case class Entity[S,F[_],A](state: S, pos: Position[F,A])

object Entity {
  // Step to evaluate the particle
  def eval[S,F[_]:Foldable,A](f: Position[F,A] => Position[F,A])(entity: Entity[S,F,A]): Step[F,A,Entity[S,F,A]] =
    Step.evalF(f(entity.pos)).map(p => Lenses._position.set(p)(entity))
}

sealed abstract class Position[F[_],A] { // Transformer of some sort, over the type F?
  import Position._

  def map[B](f: A => B)(implicit F: Functor[F]): Position[F,B] =
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

  def violations: Maybe[List[Constraint[A,Double]]] =
    this match {
      case Point(_)          => Maybe.empty
      case Solution(_, _, v) => Maybe.just(v)
    }

  def toPoint: Position[F, A] =
    this match {
      case Point(_) => this
      case Solution(x, _, _) => Point(x)
    }

  def feasible: Option[Position[F,A]] =
    violations.map(_.isEmpty).getOrElse(false).option(this)
}

final case class Point[F[_],A] private[cilib] (x: F[A]) extends Position[F,A]
final case class Solution[F[_],A] private[cilib] (x: F[A], f: Fit, v: List[Constraint[A,Double]]) extends Position[F,A]

object Position {
  implicit def positionInstances[F[_]](implicit F0: Monad[F], F1: Zip[F]): Bind[Position[F,?]] /*with Traverse[Position[F,?]]*/ with Zip[Position[F,?]] =
    new Bind[Position[F,?]] /*with Traverse[Position[F,?]]*/ with Zip[Position[F,?]] {
      override def map[A, B](fa: Position[F, A])(f: A => B): Position[F, B] =
        fa map f

      override def bind[A, B](fa: Position[F, A])(f: A => Position[F,B]): Position[F, B] =
        fa flatMap f

      override def zip[A, B](a: => Position[F, A], b: => Position[F, B]): Position[F, (A, B)] =
        a zip b
    }

  implicit class PositionVectorOps[F[_],A](val x: Position[F,A]) extends AnyVal {
    def zeroed(implicit F: Functor[F], A: Monoid[A]): Position[F,A] =
      x.map(_ => A.zero)

    import spire.algebra._
    def + (other: Position[F,A])(implicit M: Module[F[A],A]): Position[F,A] =
      Point(M.plus(x.pos, other.pos))

    def - (other: Position[F,A])(implicit M: Module[F[A],A]): Position[F,A] =
      Point(M.minus(x.pos, other.pos))

    /*def * (other: Position[F, A])(implicit F: Zip[F]) = Solution(x.pos.zipWith(other.pos)((a, ob) => ob.map(_ * a).getOrElse(a))._2) */

    def *: (scalar: A)(implicit M: Module[F[A],A]): Position[F,A] =
      Point(M.timesl(scalar, x.pos))
  }

  implicit def positionFitness[F[_],A] = new Fitness[Position[F,A]] {
    def fitness(a: Position[F,A]) =
      a.fit
  }

  def apply[F[_]:SolutionRep,A](xs: F[A]): Position[F, A] =
    Point(xs)

  def createPosition[A](domain: NonEmptyList[Interval[Double]])(implicit F: SolutionRep[List]) =
    domain.traverseU(x => Dist.uniform(x.lower.value, x.upper.value)) map (x => Position(x.list))

  def createPositions(domain: NonEmptyList[Interval[Double]], n: Int)(implicit ev: SolutionRep[List]) =
    createPosition(domain) replicateM n

  def createCollection[A](f: Position[List,Double] => A)(domain: NonEmptyList[Interval[Double]], n: Int)(implicit ev: SolutionRep[List]): RVar[List[A]] =
    createPositions(domain,n).map(_.map(f))
}

trait SolutionRep[F[_]]

object SolutionRep {
  implicit object ListRep extends SolutionRep[List]
  implicit object VectorRep extends SolutionRep[Vector]
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
