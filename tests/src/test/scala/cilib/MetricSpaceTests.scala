package cilib

import zio.prelude.{ Assertion => _, _ }
import zio.random.Random
import zio.test.{ TestResult, _ }

import Predef.{ any2stringadd => _, assert => _ }

object MetricSpaceTest extends DefaultRunnableSpec {

  val doubleGen: Gen[Random, Double]                      = Gen.double(-1000000.0, 1000000.0)
  val doubleListGen: Gen[Random with Sized, List[Double]] = Gen.listOf(doubleGen)

  val listTuple2: Gen[Sized with Random, (List[Double], List[Double])] = Gen.sized { size =>
    for {
      x <- Gen.listOfN(size)(doubleGen)
      y <- Gen.listOfN(size)(doubleGen)
    } yield (x, y)
  }

  val listTuple3: Gen[Sized with Random, (List[Double], List[Double], List[Double])] = Gen.sized { size =>
    for {
      x <- Gen.listOfN(size)(doubleGen)
      y <- Gen.listOfN(size)(doubleGen)
      z <- Gen.listOfN(size)(doubleGen)
    } yield (x, y, z)
  }

  val euclidean: MetricSpace[List[Double], Double] = MetricSpace.euclidean[List, Double]
  val manhattan: MetricSpace[List[Double], Double] = MetricSpace.manhattan[List, Double]
  val chebyshev: MetricSpace[List[Double], Double] = MetricSpace.chebyshev[List, Double]
  val hamming: MetricSpace[List[Double], Int]      = MetricSpace.hamming[List, Double]

  val doubleEq: zio.prelude.Equal[Double] = zio.prelude.Equal.DoubleEqualWithEpsilon()

  def nonnegative[A, B: Ord](m: MetricSpace[A, B], x: A, y: A)(implicit ev: scala.math.Numeric[B]): TestResult =
    assert(ev.toDouble(m.dist(x, y)))(Assertion.isGreaterThanEqualTo(0.0))

  def indisc[A, B](m: MetricSpace[A, B], x: A)(implicit F: scala.math.Numeric[B]): TestResult =
    assert(doubleEq.equal(F.toDouble(m.dist(x, x)), F.toDouble(F.zero)))(Assertion.isTrue)

  def symmetry[A, B](m: MetricSpace[A, B], x: A, y: A)(implicit F: scala.math.Numeric[B]): TestResult =
    assert(doubleEq.equal(F.toDouble(m.dist(x, y)), F.toDouble(m.dist(y, x))))(Assertion.isTrue)

  // Some discussion: https://en.wikipedia.org/wiki/Triangle_inequality
  // Look at section on Metric Spaces
  def triangle[A, B](m: MetricSpace[A, B], a: A, b: A, c: A)(implicit O: Ord[B], F: scala.math.Numeric[B]): Boolean =
    O.lessOrEqual(m.dist(a, c), F.plus(m.dist(a, b), m.dist(b, c)))

  override def spec: ZSpec[Environment, Failure] = suite("metric space")(
    testM("non-negativity") {
      check(listTuple2) { case (x, y) =>
        nonnegative(euclidean, x, y) &&
          nonnegative(manhattan, x, y) &&
          nonnegative(chebyshev, x, y)
      }
    },
    testM("hamming metric space") {
      check(doubleListGen, doubleListGen) { case (x, y) =>
        nonnegative(hamming, x, y) &&
          symmetry(hamming, x, y) &&
          assert(hamming.dist(x, x))(Assertion.equalTo(0))
      }
    },
    testM("identity of indiscernibles") {
      check(doubleListGen) { case l =>
        indisc(euclidean, l) &&
          indisc(manhattan, l) &&
          indisc(chebyshev, l)
      }
    },
    testM("identity") {
      check(listTuple2) { case (x, _) =>
        assert(euclidean.dist(x, x))(Assertion.equalTo(0.0)) &&
          assert(manhattan.dist(x, x))(Assertion.equalTo(0.0)) &&
          assert(chebyshev.dist(x, x))(Assertion.equalTo(0.0))

      // TODO: Another test to verify
      //    hamming.dist(x, x) === 0 &&
      // (x =!= y) ==> (euclidean.dist(x, y) =!= 0.0) &&
      //     (x =!= y) ==> (manhattan.dist(x, y) =!= 0.0) &&
      //     (x =!= y) ==> (chebyshev.dist(x, y) =!= 0.0) //&&
      // (x =!= y) ==> (hamming.dist(x, y) =!= 0)
      }
    },
    testM("symmetry") {
      check(listTuple2) { case (x, y) =>
        symmetry(euclidean, x, y) &&
          symmetry(manhattan, x, y) &&
          symmetry(chebyshev, x, y)
      }
    },
    testM("triangle-inequality") {
      check(listTuple3) { case (x, y, z) =>
        assert(triangle(euclidean, x, y, z))(Assertion.isTrue) &&
          assert(triangle(manhattan, x, y, z))(Assertion.isTrue) &&
          assert(triangle(chebyshev, x, y, z))(Assertion.isTrue)
      }
    }
  )

}
