package cilib

import scalaz.{Foldable1,ICons, INil, NonEmptyList}
import spire.math._

trait Input[F[_]] {
  def toInput[A](a: NonEmptyList[A]): F[A]
}

sealed abstract class Eval[A] { // This represents the function (NonEmpty)List[A] => Fit
  def eval(a: NonEmptyList[A]): Objective[A]

  def constrainBy(cs: List[Constraint[A,Double]]): Eval[A]

  def unconstrain: Eval[A]
}

object Eval {
  def unconstrained[F[_],A](f: F[A] => Double)(implicit F: Input[F]): Eval[A] = new Eval[A] {
    def eval(a: NonEmptyList[A]): Objective[A] =
      Single(Feasible(f(F.toInput(a))), List.empty)

    def constrainBy(cs: List[Constraint[A,Double]]): Eval[A] =
      constrained(f, cs)

    def unconstrain = this
  }

  def constrained[F[_],A](f: F[A] => Double, cs: List[Constraint[A,Double]])(implicit F: Input[F]): Eval[A] = new Eval[A] {
    import spire.algebra.Eq
    import spire.implicits._

    def eval(a: NonEmptyList[A]): Objective[A] =
      cs.filter(c => !Constraint.satisfies(c, a)) match {
        case Nil => Single(Feasible(f(F.toInput(a))), List.empty)
        case xs  => Single(Infeasible(f(F.toInput(a)), xs.length), xs)
      }

    def constrainBy(css: List[Constraint[A,Double]]): Eval[A] =
      constrained(f, css)

    def unconstrain =
      unconstrained(f)
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
