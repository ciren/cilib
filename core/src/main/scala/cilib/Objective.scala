package cilib

import scalaz.Scalaz._
import scalaz._

/**
  An `Objective` represents the result of an evaluation.

  In most cases, `Objective` values are `Single` values that contain
  the fitness and the violation count of an objective function evaluation.

  `Multi` duplicates the evaluation for multiple potential objective functions.
 */
sealed abstract class Objective {
  import Objective._

  def violations: List[Constraint] =
    this match {
      case Single(_, v) => v
      case Multi(xs)    => xs.foldMap(_.violations)
    }

  def violationCount: Int =
    violations.length

  def fitness: Either[Fit, List[Fit]] = // Should this be tail-recursive?
    this match {
      case Single(f, _) => Left(f)
      case Multi(xs) =>
        Right(xs.toList.flatMap(_.fitness match {
          case Left(f)  => List(f)
          case Right(fs) => fs
        }))
    }
}

object Objective {
  private final case class Single[A](f: Fit, v: List[Constraint]) extends Objective
  private final case class Multi[A](x: NonEmptyList[Objective]) extends Objective

  def single[A](f: Fit, violations: List[Constraint]): Objective =
    Single(f, violations)

  def multi[A](xs: NonEmptyList[Objective]): Objective =
    Multi(xs)
}
