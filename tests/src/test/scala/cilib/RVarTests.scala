package cilib

import scalaz._
import scalaz.std.anyVal._
import org.scalacheck._
import org.scalacheck.Prop._

import org.specs2.scalaz._
import scalaz.scalacheck.ScalazProperties._
import scalaz.scalacheck.ScalazArbitrary._

// Should we look at using Discipline or scalaz's way of testing? I'm not sure...
object RVarTests extends Spec {

  val rng = RNG.fromTime

  implicit def rngEqual = scalaz.Equal[Int].contramap((_: RVar[Int]).run(rng)._2)

  implicit def arbRVar: Arbitrary[RVar[Int]] = Arbitrary {
    for {
      i <- Arbitrary.arbitrary[Int]
    } yield RVar.point(i)
  }

  implicit def arbRVarFunc: Arbitrary[RVar[Int => Int]] = Arbitrary {
    for {
      i <- Arbitrary.arbitrary[Int => Int]
    } yield RVar.point(i)
  }

  checkAll(equal.laws[RVar[Int]])
  checkAll(monad.laws[RVar])

}
