package cilib

import spire.implicits._
import spire.math.Interval
import zio.prelude._
import zio.random.Random
import zio.test._

object BoundarySpec extends DefaultRunnableSpec {

  val intervalGen: Gen[Random, Interval[Double]] =
    for {
      a <- Gen.anyDouble
      b <- Gen.anyDouble
      if b > a
    } yield Interval(a, b)

  val intervalPairGen: Gen[Random, (Double, Interval[Double])] =
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
          Id.unwrap(Boundary.enforce(p, Boundary.absorb[Double]))

        assert(enforced.forall(interval.contains))(Assertion.isTrue)
      }
    },
    testM("random") {
      check(Gen.anyDouble, intervalGen, Gen.anyLong) { case (a, b, seed) =>
        val p        = Position(NonEmptyVector(a), NonEmptyVector(b))
        val enforced =
          Boundary
            .enforce(p, Boundary.random[Double])
            .runResult(RNG.init(seed))

        assert(enforced.forall(b.contains))(Assertion.isTrue)
      }
    },
    testM("reflect") {
      check(intervalPairGen) { case ((double, interval)) =>
        val p        = Position(NonEmptyVector(double), NonEmptyVector(interval))
        val enforced =
          Id.unwrap(Boundary.enforce(p, Boundary.reflect[Double]))

        assert(enforced.forall(interval.contains))(Assertion.isTrue)
      }
    },
    testM("toroidal") {
      check(Gen.anyDouble, intervalGen) { case (double, interval) =>
        val p        = Position(NonEmptyVector(double), NonEmptyVector(interval))
        val enforced =
          Id.unwrap(Boundary.enforce(p, Boundary.toroidal[Double]))

        assert(enforced.forall(interval.contains))(Assertion.isTrue)
      }
    }
  )
}
