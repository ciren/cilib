package cilib

import spire.implicits._

import scalaz._
import scalaz.Ordering._
import scalaz.scalacheck.ScalazArbitrary._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

object FitnessTest extends Properties("Fitness") {

  trait Single
  trait Multi

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

  def singleObjectiveGen: Gen[Objective[Int] @@ Single] =
    for {
      violationCount <- Gen.choose(1,5)
      violations <- Gen.listOfN(violationCount, simpleViolationGen)
      obj <- Gen.oneOf(
        genFeasibleFitness.map(f => Tag[Objective[Int], Single](Objective.single(f, List.empty[Constraint[Int]]))),
        genPenaltyFitness.map(f => Tag[Objective[Int], Single](Objective.single(f, violations))),
        genInfeasibleFitenss.map(f => Tag[Objective[Int], Single](Objective.single(f, violations)))
      )
    } yield obj

  implicit def arbSingleObjective = Arbitrary { singleObjectiveGen }


  def multiObjectiveGen: Gen[Objective[Int] @@ Multi] =
    for {
      nel <- arbitrary[NonEmptyList[Objective[Int] @@ Single]]
    } yield Tag[Objective[Int], Multi](Objective.multi(nel.map(Tag.unwrap)))

  implicit def arbMultiObjective = Arbitrary { multiObjectiveGen }

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

  def better(opt: Opt)(x: Option[Objective[Int]], y: Option[Objective[Int]]): Option[Objective[Int]] =
    (x, y) match {
      case (Some(a), Some(b)) =>
        (a.fitness, b.fitness) match {
          case (-\/(f1), -\/(f2)) =>
            if (Comparison.fitCompare(opt, f1, f2, a.violationCount, b.violationCount) == GT) x else y
          case (\/-(f1), \/-(f2)) =>
            if (Comparison.multiFitCompare(opt, f1, f2, a.violationCount, b.violationCount) == GT) x else y
          case _ => x
        }
      case (None, None) => x
      case (None, _) => y
      case (_, None) => x
    }

  val min = better(Min) _
  val max = better(Max) _

  property("single objective min") =
    forAll { (x: Option[Objective[Int] @@ Single], y: Option[Objective[Int] @@ Single]) =>
      val a = x.map(Tag.unwrap)
      val b = y.map(Tag.unwrap)

      Comparison.quality(Min)(a, b) == min(a, b)
    }

  property("single objective max") =
    forAll { (x: Option[Objective[Int] @@ Single], y: Option[Objective[Int] @@ Single]) =>
      val a = x.map(Tag.unwrap)
      val b = y.map(Tag.unwrap)

      Comparison.quality(Max)(a, b) == max(a, b)
    }

  property("multi objective dominance min") =
    forAll { (x: Option[Objective[Int] @@ Multi], y: Option[Objective[Int] @@ Multi]) =>
      val a = x.map(Tag.unwrap)
      val b = y.map(Tag.unwrap)

      Comparison.quality(Min)(a, b) == min(a, b)
    }

  property("multi objective dominance max") =
    forAll { (x: Option[Objective[Int] @@ Multi], y: Option[Objective[Int] @@ Multi]) =>
      val a = x.map(Tag.unwrap)
      val b = y.map(Tag.unwrap)

      Comparison.quality(Max)(a, b) == max(a, b)
    }

}
