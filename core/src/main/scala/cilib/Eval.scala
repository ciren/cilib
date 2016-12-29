package cilib

import scalaz.{Foldable1,ICons, INil, NonEmptyList}
import spire.math._

trait Input[F[_]] {
  def toInput[A](a: NonEmptyList[A]): F[A]
}

sealed abstract class Eval[A] { // This represents the function (NonEmpty)List[A] => RVar[Fit]
  def eval(a: NonEmptyList[A]): RVar[Objective[A]]

  def constrainBy(cs: List[Constraint[A,Double]]): Eval[A]

  def unconstrain: Eval[A]
}

object Eval {

  def unconstrained[F[_]:Input, A](f: F[A] => Double) =
    unconstrainedNamed(f, "unnamed")

  def unconstrainedNamed[F[_],A](f: F[A] => Double, name: String)(implicit F: Input[F]): Eval[A] =
    new Eval[A] {
      def eval(a: NonEmptyList[A]): RVar[Objective[A]] = RVar.point {
        Single(Feasible(f(F.toInput(a))), List.empty)
      }

      def constrainBy(cs: List[Constraint[A,Double]]): Eval[A] =
        constrainedNamed(f, cs, name)

      def unconstrain = this

      override def toString = "Unconstrained(" + name + ")"
    }

  def constrained[F[_]:Input, A](f: F[A] => Double, cs: List[Constraint[A,Double]]): Eval[A] =
    constrainedNamed(f, cs, "unnamed")

  def constrainedNamed[F[_],A](f: F[A] => Double, cs: List[Constraint[A,Double]], name: String)(implicit F: Input[F]): Eval[A] =
    new Eval[A] {
      import spire.algebra.Eq
      import spire.implicits._

      def eval(a: NonEmptyList[A]): RVar[Objective[A]] = RVar.point {
        cs.filter(c => !Constraint.satisfies(c, a)) match {
          case Nil => Single(Feasible(f(F.toInput(a))), List.empty)
          case xs  => Single(Infeasible(f(F.toInput(a)), xs.length), xs)
        }
      }

      def constrainBy(css: List[Constraint[A,Double]]): Eval[A] =
        constrainedNamed(f, css, name)

      def unconstrain =
        unconstrainedNamed(f, name)

      override def toString = "Constrained(" + name + ")"
    }

  implicit val nelInput = new Input[NonEmptyList] {
    def toInput[A](a: NonEmptyList[A]): NonEmptyList[A] = a
  }

  implicit def pairInput = new Input[Lambda[x => (x, x)]] {
    def toInput[A](a: NonEmptyList[A]): (A,A) =
      a.list match {
        case ICons(a, ICons(b, _)) => (a, b)
        case _ => sys.error("error producing a pair")
      }
  }

}
