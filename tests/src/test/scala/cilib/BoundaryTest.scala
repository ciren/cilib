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

  property("absorb") =
    forAll { (a: Double, b: Interval[Double]) =>
      val p = Position(NonEmptyList(a), NonEmptyList(b))
      val enforced =
        Boundary.enforce(p, Boundary.absorb[Double]).value

      enforced.list.toList.forall(x => b.contains(x))
    }

  property("random") =
    forAll { (a: Double, b: Interval[Double], rng: Long) =>
      val p = Position(NonEmptyList(a), NonEmptyList(b))
      val enforced =
        Boundary.enforce(p, Boundary.random[Double])
          .eval(RNG.init(rng))

      enforced.list.toList.forall(x => b.contains(x))
    }

  property("reflect") =
    forAll { (a: Double, b: Interval[Double]) =>
      val p = Position(NonEmptyList(a), NonEmptyList(b))
      val enforced =
        Boundary.enforce(p, Boundary.reflect[Double]).value

      enforced.list.toList.forall(x => b.contains(x))
    }

  property("toroidal") =
    forAll { (a: Double, b: Interval[Double]) =>
      val p = Position(NonEmptyList(a), NonEmptyList(b))
      val enforced =
        Boundary.enforce(p, Boundary.toroidal[Double]).value

      enforced.list.toList.forall(z => b.contains(z))
    }
}
