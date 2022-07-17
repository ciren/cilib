package cilib

import cilib.algebra._
import zio.Scope
import zio.test._

import Predef.{ any2stringadd => _, assert => _, _ }

object DotProdTest extends ZIOSpecDefault {

  val interval: Interval                           = Interval(-10.0, 10.0)
  def boundary(dim: Int): NonEmptyVector[Interval] = interval ^ dim

  def nonEmptyVectorGen(dim: Int): Gen[Any, NonEmptyVector[Int]] =
    for {
      head <- Gen.int(-10, 10)
      tail <- Gen.listOfN(dim - 1)(Gen.int(-10, 10))
    } yield NonEmptyVector.fromIterable(head, tail)

  def positionGen(dim: Int): Gen[Any, Position[Int]] = nonEmptyVectorGen(dim).map(Position(_, boundary(dim)))

  val D: DotProd[Position, Int] = implicitly[DotProd[Position, Int]]
  val N: Numeric[Int]           = implicitly[Numeric[Int]]

  def spec: Spec[Environment with TestEnvironment with Scope, Any] = suite("dot product")(
    test("commutativity") {
      check(positionGen(10), positionGen(10)) { case (a, b) =>
        assert(D.dot(a, b) == D.dot(b, a))(Assertion.isTrue)
      }
    },
    test("distributive") {
      check(positionGen(10), positionGen(10), positionGen(10)) { case (a, b, c) =>
        assert(D.dot(a, b + c) == D.dot(a, b) + D.dot(a, c))(Assertion.isTrue)
      }
    },
    test("bilinear") {
      check(positionGen(10), positionGen(10), positionGen(10), Gen.int(-10000, 10000)) { case (a, b, c, r) =>
        val result = D.dot(a, r *: b + c) == r * D.dot(a, b) + D.dot(a, c)

        assert(result)(Assertion.isTrue)
      }
    },
    test("scalar multiplication") {
      check(positionGen(10), positionGen(10), Gen.int(-1000, 1000), Gen.int(-1000, 1000)) { case (a, b, c1, c2) =>
        val result = D.dot(c1 *: a, c2 *: b) == c1 * c2 * D.dot(a, b)

        assert(result)(Assertion.isTrue)
      }
    }
  )

}
