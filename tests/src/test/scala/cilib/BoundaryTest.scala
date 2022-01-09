package cilib

import zio.prelude.{ Assertion => _, _ }
import zio.random.Random
import zio.test._

object BoundarySpec extends DefaultRunnableSpec {

  val intervalGen: Gen[Random, Interval] =
    for {
      a <- Gen.anyDouble
      b <- Gen.anyDouble
      if b > a
    } yield Interval(a, b)

  val intervalPairGen: Gen[Random, (Double, Interval)] =
    for {
      b     <- intervalGen
      a     <- Gen.elements(b.lowerValue, b.upperValue)
      scale <- Gen.elements(1, 50)
    } yield (a * scale, b)

  override def spec: ZSpec[Environment, Failure] = suite("Boundary enforcement")(
    testM("absorb") {
      check(Gen.anyDouble, intervalGen) { case (double, interval) =>
        val p        = Position(NonEmptyVector(double), NonEmptyVector(interval))
        val enforced =
          Id.unwrap(Boundary.enforce(p, Boundary.absorb))

        assert(enforced.forall(interval.contains))(Assertion.isTrue)
      }
    },
    testM("random") {
      check(Gen.anyDouble, intervalGen, Gen.anyLong) { case (a, b, seed) =>
        val p        = Position(NonEmptyVector(a), NonEmptyVector(b))
        val enforced =
          Boundary
            .enforce(p, Boundary.random)
            .runResult(RNG.init(seed))

        assert(enforced.forall(b.contains))(Assertion.isTrue)
      }
    },
    testM("reflect") {
      check(intervalPairGen) { case ((double, interval)) =>
        val p        = Position(NonEmptyVector(double), NonEmptyVector(interval))
        val enforced =
          Id.unwrap(Boundary.enforce(p, Boundary.reflect))

        assert(enforced.forall(interval.contains))(Assertion.isTrue)
      }
    },
    testM("toroidal") {
      check(Gen.anyDouble, intervalGen) { case (double, interval) =>
        val p        = Position(NonEmptyVector(double), NonEmptyVector(interval))
        val enforced =
          Id.unwrap(Boundary.enforce(p, Boundary.toroidal))

        assert(enforced.forall(interval.contains))(Assertion.isTrue)
      }
    }
  )
}
