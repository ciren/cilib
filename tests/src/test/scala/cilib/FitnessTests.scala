package cilib

import spire.implicits._

import scalaz._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

object FitnessTest extends Properties("Fitness") {

  val genFeasibleFitness: Gen[Fit] =
    Gen.choose(-1000, 1000).map(x => Feasible(x.toDouble))

  val genInfeasibleFitenss: Gen[Fit] =
    Gen.choose(-1000, 1000).map(x => Infeasible(x.toDouble))

  val genPenaltyFitness: Gen[Fit] =
    genInfeasibleFitenss.map(_.adjust(identity _))

  def simpleViolationGen: Gen[Constraint[Int]] =
    for {
      value <- Gen.choose(-100.0, 100.0)
      function = ConstraintFunction((_: NonEmptyList[Int]) => 0.0)
      constraint <- Gen.oneOf(
        LessThan(function, value),
        LessThanEqual(function, value),
        Equal(function, value),
        GreaterThan(function, value),
        GreaterThanEqual(function, value),
        InInterval(function, spire.math.Interval(-5.12, 5.12))
      )
    } yield constraint

  def objectiveGen: Gen[Objective[Int]] =
    for {
      violationCount <- Gen.choose(1, 3)
      violations <- Gen.listOfN(violationCount, simpleViolationGen)
      obj <- Gen.oneOf(
        genFeasibleFitness.map(f => Objective.single(f, List.empty[Constraint[Int]])),
        genPenaltyFitness.map(f => Objective.single(f, violations)),
        genInfeasibleFitenss.map(f => Objective.single(f, violations))
      )
    } yield obj


  implicit def arbObjective = Arbitrary { objectiveGen }

  implicit def idFitness: Fitness[Option, Objective[Int], Int] =
    new Fitness[Option, Objective[Int], Int] {
      def fitness(x: Option[Objective[Int]]): Option[Objective[Int]] =
        x
    }

  implicit def idFitness3: Fitness[Objective, Int, Int] =
    new Fitness[Objective, Int, Int] {
      def fitness(x: Objective[Int]): Option[Objective[Int]] =
        Some(x)
    }

  def better(opt: Opt)(x: Option[Objective[Int]], y: Option[Objective[Int]]) =
    (x, y) match {
      case (Some(a), Some(b)) =>
        if (Comparison.fitter(a, b).apply(Comparison.quality(opt))) x else y
      case (None, Some(_)) => y
      case (Some(_), None) => x
      case _ => x
    }

  property("min") = {
    def min(x: Option[Objective[Int]], y: Option[Objective[Int]]) = better(Min)(x, y)

    forAll { (x: Option[Objective[Int]], y: Option[Objective[Int]]) =>
      Comparison.quality(Min)(x, y) == min(x, y)
    }
  }

  property("max") = {
    def max(x: Option[Objective[Int]], y: Option[Objective[Int]]) = better(Max)(x, y)

    forAll { (x: Option[Objective[Int]], y: Option[Objective[Int]]) =>
      Comparison.quality(Max)(x, y) == max(x, y)
    }
  }
}
