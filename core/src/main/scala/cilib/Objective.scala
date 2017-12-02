package cilib

/**
An `Objective` represents the result of an evaluation.

In most cases, `Objective` values are `Single` values that contain
the fitness and the violation count of an objective function evaluation.

`Multi` duplicates the evaluation for multiple potential objective functions.
  */
sealed abstract class Objective[A] {
  def violations: List[Constraint[A]] =
    this match {
      case Single(_, v) => v
      case Multi(xs)    => xs.foldLeft(List.empty[Constraint[A]])(_ ++ _.v)
    }

  def violationCount =
    violations.length
}
final case class Single[A](f: Fit, v: List[Constraint[A]]) extends Objective[A]
final case class Multi[A](x: List[Single[A]]) extends Objective[A]
