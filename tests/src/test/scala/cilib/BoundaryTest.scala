package cilib

import scalaz.NonEmptyList
import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

import spire.math.Interval
import spire.implicits._

object BoundaryTest extends Spec("Boundary enforcement") {

  final case class Foo(b: Interval[Double], a: Double)

  val doubleGen = Gen.choose(-1000000.0, 1000000.0)

  val intervalGen: Gen[Interval[Double]] =
      for {
        a <- doubleGen
        b <- doubleGen suchThat (_ > a)
      } yield Interval(a, b)
  implicit def arbInterval = Arbitrary { intervalGen }

  implicit def fooGen = Arbitrary {
    for {
      i <- intervalGen
      range = math.abs(i.upperValue - i.lowerValue) * 0.4
      p <- Gen.choose(i.lowerValue - range, i.upperValue + range)
    } yield Foo(i, p)
  }

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
    forAll { (a: Foo) =>
      val p = Position(NonEmptyList(a.a), NonEmptyList(a.b))
      val enforced =
        Boundary.enforce(p, Boundary.reflect[Double]).value

      enforced.list.toList.forall(x => a.b.contains(x))
    }

  property("toroidal") =
    forAll { (a: Double, b: Interval[Double]) =>
      val p = Position(NonEmptyList(a), NonEmptyList(b))
      val enforced =
        Boundary.enforce(p, Boundary.toroidal[Double]).value

      enforced.list.toList.forall(z => b.contains(z))
    }
}
