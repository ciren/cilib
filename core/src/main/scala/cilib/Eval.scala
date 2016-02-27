package cilib

import scalaz.NonEmptyList
import spire.math._

sealed abstract class Eval[A] { // This represents the function (NonEmpty)List[A] => Fit

  def eval(a: List[A]): Objective[A] = //(Fit, List[Constraint[A, Double]]) =
    this match {
      case Unconstrained(f)   => Single(Feasible(f(a)), List.empty)
      case Constrained(f, cs) =>
        import spire.algebra.Eq
        import spire.implicits._
//        println("violations: " +  cs.filterNot(c => Constraint.satisfies(c, a.pos.list)))
        println("a: " + a)
        val violations = cs.filter(c => !Constraint.satisfies(c, a))
        println("violations: " + violations)
//        val x = violations match {
//          case Nil => Feasible(fit.v)
//          case _   => Infeasible(fit.v, cs.filterNot(c => Constraint.satisfies(c, a)))
//        }
        violations match {
          case Nil => Single(Feasible(f(a)), List.empty)
          case xs  => Single(Infeasible(f(a), xs.length), xs)
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

final case class Unconstrained[A](f: List[A] => Double) extends Eval[A]
final case class Constrained[A](f: List[A] => Double, cs: List[Constraint[A,Double]]) extends Eval[A]
