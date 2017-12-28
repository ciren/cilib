package cilib

import scalaz._
import scalaz.std.anyVal._
import org.scalacheck._

import scalaz.scalacheck.ScalazProperties._

import spire.math.Interval
import spire.implicits._

object StepTest extends Spec("Step") {
  val rng = RNG.fromTime
  val env = Environment(
    cmp = Comparison.quality(Min),
    eval = Eval.unconstrained((l: NonEmptyList[Int]) => l.list.foldLeft(0.0)(_ + _)).eval,
    bounds = NonEmptyList(Interval(-5.12,5.12)))

  implicit def stepEqual = scalaz.Equal[Int].contramap((_: Step[Int,Int]).run.apply(env).run(rng)._2)
  implicit def stepSEqual = scalaz.Equal[Int].contramap((_: StepS[Int,Int,Int]).run.apply(3).run.apply(env).run(rng)._2._2)

  implicit def arbStep: Arbitrary[Step[Int,Int]] = Arbitrary {
    Arbitrary.arbitrary[Int].map(Step.point)
  }

  implicit def arbStepFunc: Arbitrary[Step[Int, Int => Int]] = Arbitrary {
    Arbitrary.arbitrary[Int => Int].map(Step.point[Int, Int => Int])
  }

  implicit def arbStepS: Arbitrary[StepS[Int, Int, Int]] = Arbitrary {
    Arbitrary.arbitrary[Step[Int,Int]].map(StepS.pointS)
  }

  implicit def arbStepSFunc: Arbitrary[StepS[Int, Int, Int => Int]] = Arbitrary {
    Arbitrary.arbitrary[Step[Int,Int => Int]].map(StepS.pointS)
  }

  checkAll("Step", equal.laws[Step[Int,Int]])
  checkAll("Step", monad.laws[Step[Int,?]])

  checkAll("StepS", equal.laws[StepS[Int,Int,Int]])
  checkAll("StepS", monad.laws[StepS[Int, Int, ?]])
}
