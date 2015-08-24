package cilib

import scalaz.NonEmptyList

sealed abstract class Eval[/*F[_],*/A] { // This represents the function NonEmptyList[A] => Fit

  def eval(a: NonEmptyList[A]): (Fit, List[Constraint[A, Double]]) = {
    this match {
      case Unconstrained(f)   => (f(a), List.empty)
      case Constrained(f, cs) =>
        import spire.algebra.Eq
        import spire.implicits._
//        println("violations: " +  cs.filterNot(c => Constraint.satisfies(c, a.pos.list)))
//        println("a: " + a)
        (f(a), cs.filterNot(c => Constraint.satisfies(c, a.list)))
    }
  }

  def constrainBy(cs: List[Constraint[A,Double]]) =
    this match {
      case Unconstrained(f)  => Constrained(f, cs)
      case Constrained(f, _) => Constrained(f, cs)
    }

  def unconstrain =
    this match {
      case x @ Unconstrained(_) => x
      case Constrained(f, _)    => Unconstrained(f)
    }
}

final case class Unconstrained[/*F[_],*/A](f: NonEmptyList[A] => Fit) extends Eval[A]
final case class Constrained[/*F[_]:Foldable,*/A](f: NonEmptyList[A] => Fit, cs: List[Constraint[A,Double]]) extends Eval[A]
