package cilib

import scalaz._
import Scalaz._

/**
  An `Objective` represents the result of an evaluation.

  In most cases, `Objective` values are `Single` values that contain
  the fitness and the violation count of an objective function evaluation.

  `Multi` duplicates the evaluation for multiple potential objective functions.
  */
sealed abstract class Objective[A] {
  import Objective._

  def violations: List[Constraint[A]] =
    this match {
      case Single(_, v) => v
      case Multi(xs)    => xs.foldMap(_.violations)
    }

  def violationCount: Int =
    violations.length

  def fitness: Fit \/ List[Fit] = // Should this be tail-recursive?
    this match {
      case Single(f, _) => \/.left(f)
      case Multi(xs) =>
        \/.right(xs.toList.flatMap(_.fitness match {
          case -\/(f)  => List(f)
          case \/-(fs) => fs
        }))
    }
}

object Objective {
  private final case class Single[A](f: Fit, v: List[Constraint[A]]) extends Objective[A]
  private final case class Multi[A](x: NonEmptyList[Objective[A]]) extends Objective[A]

  def single[A](f: Fit, violations: List[Constraint[A]]): Objective[A] =
    Single(f, violations)

  def multi[A](xs: NonEmptyList[Objective[A]]): Objective[A] =
    Multi(xs)
}
