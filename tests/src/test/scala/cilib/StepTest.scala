package cilib

import scalaz._
import scalaz.std.anyVal._
import org.scalacheck._

import scalaz.scalacheck.ScalazProperties._

object StepTest extends Spec("Step") {
  val cmp = Comparison.quality(Min)
  val eval = Eval.unconstrained((l: NonEmptyList[Int]) => l.list.foldLeft(0.0)(_ + _)).eval
  val rng = RNG.fromTime

  implicit def stepEqual = scalaz.Equal[Int].contramap((_: Step[Int,Int]).run.apply(cmp).apply(eval).run(rng)._2)
  implicit def stepSEqual = scalaz.Equal[Int].contramap((_: StepS[Int,Int,Int]).run.apply(3).run.apply(cmp).apply(eval).run(rng)._2._2)

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
