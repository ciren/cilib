package cilib

import scalaz.Foldable
import scalaz.syntax.foldable._

sealed abstract class Eval[F[_], A] { // This represents the function F[A] => Fit

  def eval(a: F[A])(implicit ev: Foldable[F]): (Fit, List[Constraint[A, Double]]) =
    this match {
      case Unconstrained(f) => (f(a), List.empty)
      case Constrained(f, cs) =>
        import spire.algebra.Eq
        import spire.implicits._
        (f(a), cs.filter(c => Constraint.satisfies(c, a.toList)))
    }

  def constrainBy(cs: List[Constraint[A,Double]])(implicit ev: Foldable[F]) =
    this match {
      case Unconstrained(f) => Constrained(f, cs)
      case Constrained(f, _) => Constrained(f, cs)
    }

  def unconstrain =
    this match {
      case x @ Unconstrained(_) => x
      case Constrained(f, _) => Unconstrained(f)
    }
}

final case class Unconstrained[F[_],A](f: F[A] => Fit) extends Eval[F,A]
final case class Constrained[F[_]:Foldable,A](f: F[A] => Fit, cs: List[Constraint[A,Double]]) extends Eval[F,A]
