package cilib

import scalaz._
import scalaz.std.anyVal._
import scalaz.syntax.foldable._
import scalaz.std.list._
import org.scalacheck._
import org.scalacheck.Prop._

import scalaz.scalacheck.ScalazProperties._

object StepTest extends Spec("Step") {

  val cmp = Comparison.quality(Min)
  val eval = Unconstrained((l: List[Int]) => l.foldLeft(0.0)(_ + _))
  val rng = RNG.fromTime

  implicit def stepEqual = scalaz.Equal[Int].contramap((_: Step[Int,Int]).run.apply(cmp).apply(eval).run(rng)._2)

  implicit def arbStep: Arbitrary[Step[Int,Int]] = Arbitrary {
    Arbitrary.arbitrary[Int].map(Step.point)
  }

  implicit def arbStepFunc: Arbitrary[Step[Int, Int => Int]] = Arbitrary {
    Arbitrary.arbitrary[Int => Int].map(Step.point)
  }

  checkAll(equal.laws[Step[Int,Int]])
  checkAll(monad.laws[Step[Int,?]])

}
