package cilib

import scalaz.std.anyVal._
import org.scalacheck._

import scalaz.scalacheck.ScalazProperties._

// Should we look at using Discipline or scalaz's way of testing? I'm not sure...
object RVarTests extends Spec("RVar") {

  val rng = RNG.fromTime

  implicit def rngEqual = scalaz.Equal[Int].contramap((_: RVar[Int]).run(rng)._2)

  implicit def arbRVar: Arbitrary[RVar[Int]] = Arbitrary {
    Arbitrary.arbitrary[Int].map(RVar.point(_))
  }

  implicit def arbRVarFunc: Arbitrary[RVar[Int => Int]] = Arbitrary {
    Arbitrary.arbitrary[Int => Int].map(RVar.point(_))
  }

  checkAll(equal.laws[RVar[Int]])
  checkAll(monad.laws[RVar])

}
