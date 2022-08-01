package cilib

import cilib.algebra._
import cilib.syntax.dotprod._
import zio.Scope
import zio.prelude.{ Assertion => _, _ }
import zio.test._

import Predef.{ any2stringadd => _, assert => _ }

object PositionTests extends ZIOSpecDefault {

  val interval: Interval                           = Interval(-10.0, 10.0)
  def boundary(dim: Int): NonEmptyVector[Interval] = interval ^ dim

  val dimGen: Gen[Any, Int] = Gen.int(1, 100)

  def nonEmptyVectorGen(dim: Int): Gen[Any, NonEmptyVector[Int]] =
    for {
      head <- Gen.int(-10, 10)
      tail <- Gen.listOfN(dim - 1)(Gen.int(-10, 10))
    } yield NonEmptyVector.fromIterable(head, tail)

  def positionGen: Gen[Any, Position[Int]] = nonEmptyVectorGen(10).map(Position(_, boundary(10)))

  val positionsGen: Gen[Any, NonEmptyVector[Position[Int]]] = for {
    dim   <- dimGen
    bounds = boundary(dim)
    head  <- nonEmptyVectorGen(dim)
    tail  <- Gen.listOfN(dim - 1)(nonEmptyVectorGen(dim))
  } yield NonEmptyVector.fromIterable(Position(head, bounds), tail.map(Position(_, bounds)))

  val positionTuple: Gen[Any, (Position[Int], Position[Int], Position[Int])] = for {
    dim   <- dimGen
    bounds = boundary(dim)
    a     <- nonEmptyVectorGen(dim)
    b     <- nonEmptyVectorGen(dim)
    c     <- nonEmptyVectorGen(dim)
  } yield (Position(a, bounds), Position(b, bounds), Position(c, bounds))

  val one: Position[Double]  = Position(NonEmptyVector(1.0, 1.0, 1.0), boundary(3))
  val two: Position[Double]  = Position(NonEmptyVector(2.0, 2.0, 2.0), boundary(3))
  val zero: Position[Double] = Position(NonEmptyVector(0.0, 0.0, 0.0), boundary(3))

  // def conditionalApproxEqual[A: Numeric](referenceA: A, referenceB: A, tolerance: A): Assertion[A] =
  //   Assertion.assertion("conditionalApproxEqual")(param(referenceA), param(referenceB), param(tolerance)) { actual =>
  //     val referenceType = implicitly[Numeric[A]]
  //     val maxA          = referenceType.plus(referenceA, tolerance)
  //     val minA          = referenceType.minus(referenceA, tolerance)
  //     val maxB          = referenceType.plus(referenceB, tolerance)
  //     val minB          = referenceType.minus(referenceB, tolerance)

  //     val testA = referenceType.gteq(actual, minA) && referenceType.lteq(actual, maxA)
  //     val testB = referenceType.gteq(actual, minB) && referenceType.lteq(actual, maxB)

  //     testA || testB
  //   }

  def spec: Spec[Environment with TestEnvironment with Scope, Any] = suite("Position")(
    test("addition") {
      check(positionGen, positionGen, positionGen) { case (a, b, c) =>
        assert(a + b)(Assertion.equalTo(b + a)) &&
        assert((a + b) + c)(Assertion.equalTo(a + (b + c))) &&
        assert(a + a.zeroed)(Assertion.equalTo(a)) &&
        assert(a + (-a))(Assertion.equalTo(a.zeroed))
      }
    },
    test("subtraction") {
      check(positionGen) { case a =>
        assert(a - a.zeroed)(Assertion.equalTo(a)) &&
        assert(a - a)(Assertion.equalTo(a.zeroed)) &&
        assert(a - (-a))(Assertion.equalTo(a + a))
      }
    },
    test("scalar multiplication") {
      check(positionGen, positionGen, Gen.int, Gen.int) { case (a, b, n, m) =>
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
    test("negation") {
      check(positionGen) { case a =>
        assert(-a)(Assertion.not(Assertion.equalTo(a))) &&
        assert(-a)(Assertion.equalTo(a.map(_ * -1))) &&
        assert(a + (-a))(Assertion.equalTo(a.zeroed))
      }
    },
    test("is zero") {
      check(positionGen) { case a =>
        assert(a.zeroed.sum)(Assertion.equalTo(0)) &&
        assert(zero.sum)(Assertion.equalTo(0.0)) &&
        assert(one.sum)(Assertion.not(Assertion.equalTo(0.0)))
      }
    },
    test("dot product") {
      check(positionGen, positionGen, positionGen) { case (a, b, c) =>
        assert(a ∙ b)(Assertion.equalTo(b ∙ a)) &&
        assert(a ∙ (b + c))(Assertion.equalTo((a ∙ b) + (a ∙ c))) &&
        assert(a ∙ ((2 *: b) + c))(Assertion.equalTo(2 * (a ∙ b) + (a ∙ c))) &&
        assert((2 *: a) ∙ (3 *: b))(Assertion.equalTo(2 * 3 * (a ∙ b)))
      }
    },
    // test("norm") {
    //   check(positionGen) { case a =>
    //     val norm = Algebra.norm(a)

    //     assert(norm)(Assertion.isGreaterThanEqualTo(0.0)) &&
    //     assert(zero.norm)(Assertion.equalTo(0.0)) &&
    //     assert(one.norm)(Assertion.equalTo(3.0))
    //   }
    // },
    // test("normalize") {
    //   check(positionGen.map(_.map(_.toDouble))) { case a =>
    //     assert(a.normalize.norm)(conditionalApproxEqual(0.0, 1.0, 0.0001)) &&
    //       assert(zero.normalize.norm)(Assertion.approximatelyEquals(0.0, 0.0001))
    //   }
    // },
    test("mean") {
      val ps   = NonEmptyVector(zero, one, two)
      val mean = Algebra.meanVector(ps)

      assert(mean)(Assertion.equalTo(one)) &&
      assert(ps.forall(_.boundary === mean.boundary))(Assertion.isTrue) &&
      assert(ps.forall(_.pos.length === mean.pos.length))(Assertion.isTrue)
    },
    test("orthonormalize") {
      val ps   = NonEmptyVector(zero, one, two)
      val orth = Algebra.orthonormalize(ps)

      assert(orth.forall(_.boundary === one.boundary))(Assertion.isTrue) &&
      assert(orth.forall(_.pos.length === one.pos.length))(Assertion.isTrue)
    }
  )
}
