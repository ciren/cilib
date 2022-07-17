package cilib

import zio.Scope
import zio.prelude.{ Assertion => _, _ }
import zio.test._

object FitnessTest extends ZIOSpecDefault {

  object Single extends Newtype[Objective]
  type Single = Single.Type
  object Multi extends Newtype[Objective]
  type Multi = Multi.Type

  val genFeasibleFitness: Gen[Any, Feasible] =
    Gen.double(-1000, 1000).map(Feasible(_))

  val genInfeasibleFitenss: Gen[Any, Infeasible] =
    Gen.double(-1000, 1000).map(Infeasible(_))

  val genPenaltyFitness: Gen[Any, Fit] =
    genInfeasibleFitenss.map(_.adjust(identity _))

  def simpleViolationGen: Gen[Any, Constraint] =
    for {
      value      <- Gen.double(-100.0, 100.0)
      function    = ConstraintFunction(_ => 0.0)
      constraint <- Gen.oneOf(
                      Gen.const(LessThan(function, value)),
                      Gen.const(LessThanEqual(function, value)),
                      Gen.const(cilib.Equal(function, value)),
                      Gen.const(GreaterThan(function, value)),
                      Gen.const(GreaterThanEqual(function, value)),
                      Gen.const(InInterval(function, Interval(-5.12, 5.12)))
                    )
    } yield constraint

  def singleObjectiveGen: Gen[Any, Single] =
    for {
      violationCount <- Gen.int(1, 5)
      violations     <- Gen.listOfN(violationCount)(simpleViolationGen)
      obj            <- Gen.oneOf(
                          genFeasibleFitness.map(f => Single(Objective.single(f, List.empty[Constraint]))),
                          genPenaltyFitness.map(f => Single(Objective.single(f, violations))),
                          genInfeasibleFitenss.map(f => Single(Objective.single(f, violations)))
                        )
    } yield obj

  def multiObjectiveGen: Gen[Any, Multi] =
    for {
      nel <- Gen.listOfBounded(2, 10)(singleObjectiveGen)
    } yield Multi(Objective.multi(NonEmptyVector.fromIterableOption(nel).get.map(Single.unwrap)))

  implicit def idFitness: Fitness[Option, Objective, Int] =
    new Fitness[Option, Objective, Int] {
      def fitness(x: Option[Objective]): Option[Objective] =
        x
    }

  def better(opt: Opt)(x: Option[Objective], y: Option[Objective]): Option[Objective] =
    (x, y) match {
      case (Some(a), Some(b)) =>
        (a.fitness, b.fitness) match {
          case (Left(f1), Left(f2))   =>
            if (cilib.Comparison.fitCompare(opt, f1, f2, a.violationCount, b.violationCount) == Ordering.GreaterThan) x
            else y
          case (Right(f1), Right(f2)) =>
            if (
              cilib.Comparison.multiFitCompare(opt, f1, f2, a.violationCount, b.violationCount) == Ordering.GreaterThan
            ) x
            else y
          case _                      => x
        }
      case (None, None)       => x
      case (None, _)          => y
      case (_, None)          => x
    }

  val min: (Option[Objective], Option[Objective]) => Option[Objective] = better(Min) _
  val max: (Option[Objective], Option[Objective]) => Option[Objective] = better(Max) _

  def spec: Spec[Environment with TestEnvironment with Scope, Any] = suite("Fitness")(
    test("single objective min") {
      check(Gen.option(singleObjectiveGen), Gen.option(singleObjectiveGen)) { case (x, y) =>
        val a = x.map(Single.unwrap)
        val b = y.map(Single.unwrap)

        assert(cilib.Comparison.quality(Min)(a, b))(Assertion.equalTo(min(a, b)))
      }
    },
    test("single objective max") {
      check(Gen.option(singleObjectiveGen), Gen.option(singleObjectiveGen)) { case (x, y) =>
        val a = x.map(Single.unwrap)
        val b = y.map(Single.unwrap)

        assert(cilib.Comparison.quality(Max)(a, b))(Assertion.equalTo(max(a, b)))
      }
    },
    test("multi objective dominance min") {
      check(Gen.option(multiObjectiveGen), Gen.option(multiObjectiveGen)) { case (x, y) =>
        val a = x.map(Multi.unwrap)
        val b = y.map(Multi.unwrap)

        assert(cilib.Comparison.quality(Min)(a, b))(Assertion.equalTo(min(a, b)))
      }
    },
    test("multi objective dominance max") {
      check(Gen.option(multiObjectiveGen), Gen.option(multiObjectiveGen)) { case (x, y) =>
        val a = x.map(Multi.unwrap)
        val b = y.map(Multi.unwrap)

        assert(cilib.Comparison.quality(Max)(a, b))(Assertion.equalTo(max(a, b)))
      }
    }
  )

}
