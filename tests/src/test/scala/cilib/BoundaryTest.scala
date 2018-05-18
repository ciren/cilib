package cilib

import scalaz.NonEmptyList
import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

import spire.math.Interval
import spire.implicits._

object BoundaryTest extends Spec("Boundary enforcement") {

  val doubleGen = Gen.choose(-1000000.0, 1000000.0)

  val intervalGen: Gen[Interval[Double]] =
      for {
        a <- doubleGen
        b <- doubleGen suchThat (_ > a)
      } yield Interval(a, b)

  implicit def arbInterval = Arbitrary { intervalGen }

  val intervalPairGen =
    for {
      b <- intervalGen
      a <- Gen.choose(b.lowerValue, b.upperValue)
      scale <- Gen.choose(1,50)
    } yield (a*scale, b)

  property("absorb") =
    forAll { (a: Double, b: Interval[Double]) =>
      val p = Position(NonEmptyList(a), NonEmptyList(b))
      val enforced =
        Boundary.enforce(p, Boundary.absorb[Double]).value

      enforced.list.toList.forall(b.contains)
    }

  property("random") =
    forAll { (a: Double, b: Interval[Double], rng: Long) =>
      val p = Position(NonEmptyList(a), NonEmptyList(b))
      val enforced =
        Boundary.enforce(p, Boundary.random[Double])
          .eval(RNG.init(rng))

      enforced.list.toList.forall(b.contains)
    }

  property("reflect") =
    forAll(intervalPairGen) { (x: (Double, Interval[Double])) =>
      val p = Position(NonEmptyList(x._1), NonEmptyList(x._2))
      val enforced =
        Boundary.enforce(p, Boundary.reflect[Double]).value

      enforced.list.toList.forall(x._2.contains)
    }

  property("toroidal") =
    forAll { (a: Double, b: Interval[Double]) =>
      val p = Position(NonEmptyList(a), NonEmptyList(b))
      val enforced =
        Boundary.enforce(p, Boundary.toroidal[Double]).value

      enforced.list.toList.forall(b.contains)
    }
}
