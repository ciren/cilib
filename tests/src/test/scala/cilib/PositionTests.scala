package cilib

import scala.Predef.{ any2stringadd => _, assert => _ }

import spire.implicits._
import spire.math.{ sqrt, Interval }
import zio.prelude._
import zio.test._

import cilib.algebra._
import cilib.syntax.dotprod._

object PositionTests extends DefaultRunnableSpec {

  val interval           = Interval(-10.0, 10.0)
  def boundary(dim: Int) = interval ^ dim

  val dimGen = Gen.int(1, 100)

  def nelGen(dim: Int) =
    for {
      head <- Gen.int(-10, 10)
      tail <- Gen.listOfN(dim - 1)(Gen.int(-10, 10))
    } yield NonEmptyList.fromIterable(head, tail)

  def positionGen = nelGen(10).map(Position(_, boundary(10)))

  val positionsGen = for {
    dim    <- dimGen
    bounds = boundary(dim)
    head   <- nelGen(dim)
    tail   <- Gen.listOfN(dim - 1)(nelGen(dim))
  } yield NonEmptyList.fromIterable(Position(head, bounds), tail.map(Position(_, bounds)))

  val positionTuple = for {
    dim    <- dimGen
    bounds = boundary(dim)
    a      <- nelGen(dim)
    b      <- nelGen(dim)
    c      <- nelGen(dim)
  } yield (Position(a, bounds), Position(b, bounds), Position(c, bounds))

  val one  = Position(NonEmptyList(1.0, 1.0, 1.0), boundary(3))
  val two  = Position(NonEmptyList(2.0, 2.0, 2.0), boundary(3))
  val zero = Position(NonEmptyList(0.0, 0.0, 0.0), boundary(3))

  // implicit val arbPosition       = Arbitrary { dimGen.flatMap(dim => positionGen(dim)) }
  // implicit val arbPositions      = Arbitrary { positionsGen }
  // implicit val arbPositionTuple  = Arbitrary { positionTuple }
  // implicit val arbPositionDouble = Arbitrary { Gen.oneOf(zero, one, two) }

  override def spec: ZSpec[Environment, Failure] = suite("Position")(
    testM("addition") {
      check(positionGen, positionGen, positionGen) {
        case (a, b, c) =>
          assert(a + b)(Assertion.equalTo(b + a)) &&
            assert((a + b) + c)(Assertion.equalTo(a + (b + c))) &&
            assert(a + a.zeroed)(Assertion.equalTo(a)) &&
            assert(a + (-a))(Assertion.equalTo(a.zeroed))
      }
    },
    testM("subtraction") {
      check(positionGen) {
        case a =>
          assert(a - a.zeroed)(Assertion.equalTo(a)) &&
            assert(a - a)(Assertion.equalTo(a.zeroed)) &&
            assert(a - (-a))(Assertion.equalTo(a + a))
      }
    },
    testM("scalar multiplication") {
      check(positionGen, positionGen, Gen.anyInt, Gen.anyInt) {
        case (a, b, n, m) =>
          assert((n *: a).boundary)(Assertion.equalTo(a.boundary)) &&
            assert(1 *: a)(Assertion.equalTo(a)) &&
            assert(2 *: a)(Assertion.equalTo(a + a)) &&
            assert(2.0 *: zero)(Assertion.equalTo(zero)) &&
            assert(0 *: a)(Assertion.equalTo(a.zeroed)) &&
            assert(-1 *: a)(Assertion.equalTo(-a)) &&
            assert((n + m) *: a)(Assertion.equalTo((n *: a) + (m *: a))) &&
            assert(n *: (a + b))(Assertion.equalTo((n *: a) + (n *: b)))
      }
    },
    testM("negation") {
      check(positionGen) {
        case a =>
          assert(-a)(Assertion.not(Assertion.equalTo(a))) &&
            assert(-a)(Assertion.equalTo(a.map(_ * -1))) &&
            assert(a + (-a))(Assertion.equalTo(a.zeroed))
      }
    },
    testM("is zero") {
      check(positionGen) {
        case a =>
          assert(a.zeroed.sum)(Assertion.equalTo(0)) &&
            assert(zero.sum)(Assertion.equalTo(0.0)) &&
            assert(one.sum)(Assertion.not(Assertion.equalTo(0.0)))
      }
    },
    testM("dot product") {
      check(positionGen, positionGen, positionGen) {
        case (a, b, c) =>
          assert(a ∙ b)(Assertion.equalTo(b ∙ a)) &&
            assert(a ∙ (b + c))(Assertion.equalTo((a ∙ b) + (a ∙ c))) &&
            assert(a ∙ ((2 *: b) + c))(Assertion.equalTo(2 * (a ∙ b) + (a ∙ c))) &&
            assert((2 *: a) ∙ (3 *: b))(Assertion.equalTo(2 * 3 * (a ∙ b)))
      }
    },
    testM("magnitude") {
      check(positionGen) {
        case a =>
          assert(a.magnitude)(Assertion.isGreaterThanEqualTo(0.0)) &&
            assert(zero.magnitude)(Assertion.equalTo(0.0)) &&
            assert(one.magnitude)(Assertion.equalTo(sqrt(3.0)))
      }
    },
    testM("normalize") {
      check(positionGen.map(_.map(_.toDouble))) {
        case a =>
          assert(a.normalize.magnitude)(Assertion.approximatelyEquals(1.0, 0.0001)) &&
            //assert(a.normalize.pos.toIterable)(Assertion.forall(Assertion.isLessThanEqualTo(0.0))) &&
            assert(zero.normalize.magnitude)(Assertion.approximatelyEquals(0.0, 0.0001))
      }
    },
    test("mean") {
      val ps   = NonEmptyList(zero, one, two)
      val mean = Algebra.meanVector(ps)

      assert(mean)(Assertion.equalTo(one)) &&
      assert(ps.forall(_.boundary === mean.boundary))(Assertion.isTrue) &&
      assert(ps.forall(_.pos.length === mean.pos.length))(Assertion.isTrue)
    },
    test("orthonormalize") {
      val ps   = NonEmptyList(zero, one, two)
      val orth = Algebra.orthonormalize(ps)

      assert(orth.forall(_.boundary === one.boundary))(Assertion.isTrue) &&
      assert(orth.forall(_.pos.length === one.pos.length))(Assertion.isTrue)
    }
  )
}
