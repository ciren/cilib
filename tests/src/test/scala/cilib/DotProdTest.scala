package cilib

import spire.implicits._
import spire.math.Interval
import zio.prelude._
import zio.test._

import cilib.algebra._

object DotProdTest extends DefaultRunnableSpec {

  val interval           = Interval(-10.0, 10.0)
  def boundary(dim: Int) = interval ^ dim

  val dimGen = Gen.anyInt.map(_ >= 1)

  def nelGen(dim: Int) =
    for {
      head <- Gen.int(-10, 10)
      tail <- Gen.listOfN(dim - 1)(Gen.int(-10, 10))
    } yield NonEmptyList.fromIterable(head, tail)

  def positionGen(dim: Int) = nelGen(dim).map(Position(_, boundary(dim)))

  val D = implicitly[DotProd[Position, Int]]
  val N = implicitly[Numeric[Int]]

  override def spec: ZSpec[Environment,Failure] = suite("dot product")(
    testM("commutativity") {
      check(positionGen(10), positionGen(10)) { case (a,b) =>
        assert(D.dot(a, b) == D.dot(b, a))(Assertion.isTrue)
      }
    },

    testM("distributive") {
      check(positionGen(10), positionGen(10), positionGen(10)) { case (a, b, c) =>
        assert(D.dot(a, b + c) == D.dot(a, b) + D.dot(a, c))(Assertion.isTrue)
      }
    },

    testM("bilinear") {
      check(positionGen(10), positionGen(10), positionGen(10), Gen.int(-10000, 10000)) {
        case (a, b, c, r) =>
          val result = D.dot(a, r *: b + c) == r * D.dot(a, b) + D.dot(a, c)

          assert(result)(Assertion.isTrue)
      }
    },

    testM("scalar multiplication") {
      check(positionGen(10), positionGen(10), Gen.int(-1000, 1000), Gen.int(-1000, 1000)) {
        case (a, b, c1, c2) =>
          val result = D.dot(c1 *: a, c2 *: b) == c1 * c2 * D.dot(a, b)

          assert(result)(Assertion.isTrue)
      }
    }
  )

}
