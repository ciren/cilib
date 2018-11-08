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

  def distinctListOf[A:Arbitrary:scalaz.Order] =
    distinctListOfGen(Arbitrary.arbitrary[A])

  def distinctListOfGen[A](gen: Gen[A])(implicit E: scalaz.Order[A]): Gen[List[A]] = {
    @annotation.tailrec
    def go(discarded: Int, acc: List[A]): List[A] = {
      if (discarded >= 10) acc
      else
        gen.sample match {
          case Some(x) if !acc.exists(a => E.equal(a, x)) =>
            go(discarded, acc :+ x)
          case _    => go(discarded + 1, acc)
        }
    }

    Gen.choose(0, 10).flatMap(n => Gen.const(go(0, List.empty[A])))
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
    forAll(Gen.choose(1, 10), distinctListOf[Int]) { (n: Int, xs: List[Int]) =>
      refineV[Positive](n) match {
        case Left(error) => false :| s"Cannot refine: $error"
        case Right(value) =>
          val selected: List[Int] =
            // Using distinct here is safe as we are comparing Int values and not more complex data types
            RVar.sample(value, xs).run(rng)._2.getOrElse(List.empty).distinct

          if (xs.length < n) selected.isEmpty :| s"Resulting list [$selected] does not have length $n"
          else (selected.length == n) :| s"Error => $n, selected length: ${selected.length}, xs.length: ${xs.length}, xs: $xs\nselected: $selected"
      }
    }
}
