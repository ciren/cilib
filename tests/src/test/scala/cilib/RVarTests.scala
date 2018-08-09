package cilib

import scalaz._
import Scalaz._

import org.scalacheck._
import org.scalacheck.Prop._

import eu.timepit.refined._
import eu.timepit.refined.numeric.Positive

import scalaz.scalacheck.ScalazProperties._
import scalaz.scalacheck.ScalazArbitrary._

// Should we look at using Discipline or scalaz's way of testing? I'm not sure...
object RVarTests extends Spec("RVar") {

  val rng = RNG.fromTime

  implicit def rngEqual = scalaz.Equal[Int].contramap((_: RVar[Int]).run(rng)._2)

  implicit def arbRVar: Arbitrary[RVar[Int]] = Arbitrary {
    Arbitrary.arbitrary[Int].map(RVar.pure(_))
  }

  implicit def arbRVarFunc: Arbitrary[RVar[Int => Int]] = Arbitrary {
    Arbitrary.arbitrary[Int => Int].map(RVar.pure(_))
  }

  checkAll(equal.laws[RVar[Int]])
  checkAll(monad.laws[RVar])
  checkAll(bindRec.laws[RVar])

  property("shuffle") =
    forAll { (xs: NonEmptyList[Int]) =>
      val shuffled = RVar.shuffle(xs).run(RNG.fromTime)._2
      shuffled.length == xs.length && shuffled.sorted == xs.sorted
    }

  property("sampling") =
    forAll { (n: Int, xs: List[Int]) =>
      (n > 0) ==> (refineV[Positive](n) match {
        case Left(error) => false :| error
        case Right(value) =>
          val selected: List[Int] = RVar.sample(value, xs).run(rng)._2.getOrElse(List.empty)

          if (xs.length < n) selected.isEmpty :| s"Resulting list [$selected] does not have length $n"
          else (selected.length == n) :| s"Error => n: $n, $selected, length: ${selected.length}, xs.length: ${xs.length}, xs: $xs"
      })
    }
}
