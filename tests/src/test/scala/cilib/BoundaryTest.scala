package cilib

import zio.Scope
import zio.prelude.Id
import zio.test._

object BoundarySpec extends ZIOSpecDefault {

  val intervalGen: Gen[Any, Interval] =
    for {
      a <- Gen.double
      b <- Gen.double
      if b > a
    } yield Interval(a, b)

  val intervalPairGen: Gen[Any, (Double, Interval)] =
    for {
      b     <- intervalGen
      a     <- Gen.elements(b.lowerValue, b.upperValue)
      scale <- Gen.elements(1, 50)
    } yield (a * scale, b)

  def spec: Spec[Environment with TestEnvironment with Scope, Any] = suite("Boundary enforcement")(
    test("absorb") {
      check(Gen.double, intervalGen) { case (double, interval) =>
        val p        = Position(NonEmptyVector(double), NonEmptyVector(interval))
        val enforced = Id.unwrap(Boundary.enforce(p, Boundary.absorb))

        assert(enforced.forall(interval.contains(_)))(Assertion.isTrue)
      }
    },
    test("random") {
      check(Gen.double, intervalGen, Gen.long) { case (a, b, seed) =>
        val p        = Position(NonEmptyVector(a), NonEmptyVector(b))
        val enforced =
          Boundary
            .enforce(p, Boundary.random)
            .runResult(RNG.init(seed))

        assert(enforced.forall(b.contains))(Assertion.isTrue)
      }
    },
    test("reflect") {
      check(intervalPairGen) { case ((double, interval)) =>
        val p        = Position(NonEmptyVector(double), NonEmptyVector(interval))
        val enforced =
          Id.unwrap(Boundary.enforce(p, Boundary.reflect))

        assert(enforced.forall(interval.contains))(Assertion.isTrue)
      }
    },
    test("toroidal") {
      check(Gen.double, intervalGen) { case (double, interval) =>
        val p        = Position(NonEmptyVector(double), NonEmptyVector(interval))
        val enforced =
          Id.unwrap(Boundary.enforce(p, Boundary.toroidal))

        assert(enforced.forall(interval.contains))(Assertion.isTrue)
      }
    }
  )

}
