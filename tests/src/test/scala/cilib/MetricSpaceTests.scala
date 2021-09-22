package cilib

import spire.algebra.{ MetricSpace => _, _ }
import spire.implicits._
import zio.random.Random
import zio.test.{ TestResult, _ }

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

  val euclidean: MetricSpace[List[Double], Double] = MetricSpace.euclidean[List, Double, Double]
  val manhattan: MetricSpace[List[Double], Double] = MetricSpace.manhattan[List, Double, Double]
  val chebyshev: MetricSpace[List[Double], Double] = MetricSpace.chebyshev[List, Double]
  val hamming: MetricSpace[List[Double], Int]      = MetricSpace.hamming[List, Double]

  def nonnegative[A, B](m: MetricSpace[A, B], x: A, y: A)(implicit ev: IsReal[B]): TestResult =
    assert(ev.toDouble(m.dist(x, y)))(Assertion.isGreaterThanEqualTo(0.0))

  def indisc[A, B](m: MetricSpace[A, B], x: A)(implicit E: Eq[B], F: Field[B]): TestResult =
    assert(E.eqv(m.dist(x, x), F.zero))(Assertion.isTrue)

  def symmetry[A, B](m: MetricSpace[A, B], x: A, y: A)(implicit E: Eq[B]): TestResult =
    assert(E.eqv(m.dist(x, y), m.dist(y, x)))(Assertion.isTrue)

  // Some discussion: https://en.wikipedia.org/wiki/Triangle_inequality
  // Look at section on Metric Spaces
  def triangle[A, B](m: MetricSpace[A, B], a: A, b: A, c: A)(implicit F: Field[B], O: Order[B]): Boolean =
    O.lteqv(m.dist(a, c), m.dist(a, b) + m.dist(b, c))

  override def spec: ZSpec[Environment, Failure] = suite("metric space")(
    testM("non-negativity") {
      check(listTuple2) {
        case (x, y) =>
          nonnegative(euclidean, x, y) &&
            nonnegative(manhattan, x, y) &&
            nonnegative(chebyshev, x, y)
      }
    },
    testM("hamming metric space") {
      check(doubleListGen, doubleListGen) {
        case (x, y) =>
          nonnegative(hamming, x, y) &&
            symmetry(hamming, x, y) &&
            assert(hamming.dist(x, x))(Assertion.equalTo(0))
      }
    },
    testM("identity of indiscernibles") {
      check(doubleListGen) {
        case l =>
          indisc(euclidean, l) &&
            indisc(manhattan, l) &&
            indisc(chebyshev, l)
      }
    },
    testM("identity") {
      check(listTuple2) {
        case (x, _) =>
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
      check(listTuple2) {
        case (x, y) =>
          symmetry(euclidean, x, y) &&
            symmetry(manhattan, x, y) &&
            symmetry(chebyshev, x, y)
      }
    },
    testM("triangle-inequality") {
      check(listTuple3) {
        case (x, y, z) =>
          assert(triangle(euclidean, x, y, z))(Assertion.isTrue) &&
            assert(triangle(manhattan, x, y, z))(Assertion.isTrue) &&
            assert(triangle(chebyshev, x, y, z))(Assertion.isTrue)
      }
    }
  )

}
