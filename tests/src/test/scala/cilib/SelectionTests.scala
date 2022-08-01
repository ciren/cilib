package cilib

import zio.Scope
import zio.test._

object SelectionTests extends ZIOSpecDefault {

  val star: (NonEmptyVector[Int], Int) => List[Int]                                                                  = Selection.star[Int]
  val ring: (NonEmptyVector[Int], Int) => List[Int]                                                                  = Selection.indexNeighbours[Int](3)
  val wheel: (NonEmptyVector[Int], Int) => List[Int]                                                                 = Selection.wheel[Int]
  val ringDistance: (NonEmptyVector[NonEmptyVector[Double]], NonEmptyVector[Double]) => List[NonEmptyVector[Double]] =
    Selection.distanceNeighbours[NonEmptyVector, Double](MetricSpace.euclidean)(3)

  def nelGen(dim: Int): Gen[Any, NonEmptyVector[Int]] =
    Gen
      .listOfN(dim)(Gen.int(-10, 10))
      .map(x => NonEmptyVector.fromIterableOption(x).get)

  def spec: Spec[Environment with TestEnvironment with Scope, Any] = suite("selection")(
    test("star") {
      check(nelGen(10)) { case (a) =>
        val selection = star(a, a.head)

        assert(selection.length == a.length)(Assertion.isTrue) &&
        assert(selection)(Assertion.hasSubset(a.toChunk))
      }
    },
    test("star units") {
      assert(star(NonEmptyVector(1), 1))(Assertion.equalTo(List(1))) &&
      assert(star(NonEmptyVector(1, 2), 1))(Assertion.equalTo(List(1, 2))) &&
      assert(star(NonEmptyVector(1, 2, 3), 1))(Assertion.equalTo(List(1, 2, 3))) &&
      assert(star(NonEmptyVector(1, 2, 3, 4, 5), 3))(Assertion.equalTo(List(1, 2, 3, 4, 5)))
    },
    test("indexNeighbours") {
      check(nelGen(10)) { case (a) =>
        val selection = ring(a, a.head)

        assert(selection.length)(Assertion.equalTo(3))
      // assert(selection)(Assertion.hasSubset(a))
      // assert(selection)(Assertion.contains(a.head))
      }
    },
    test("ring units") {
      assert(ring(NonEmptyVector(1), 1))(Assertion.equalTo(List(1, 1, 1))) &&
      assert(ring(NonEmptyVector(1, 2), 1))(Assertion.equalTo(List(2, 1, 2))) &&
      assert(ring(NonEmptyVector(1, 2, 3), 1))(Assertion.equalTo(List(3, 1, 2))) &&
      assert(ring(NonEmptyVector(1, 2, 3, 4, 5), 3))(Assertion.equalTo(List(2, 3, 4))) &&
      assert(ring(NonEmptyVector(1, 2, 3, 4, 5), 5))(Assertion.equalTo(List(4, 5, 1)))
    },
    test("wheel") {
      check(nelGen(10)) { case a =>
        val wheelHead = wheel(a, a.head)

        assert(wheelHead.length)(Assertion.equalTo(a.length)) &&
        assert(wheelHead)(Assertion.hasSubset(a.toChunk)) // && assert(wheel(a, a.last))(Assertion.hasSubset(a))
      }
    },
    test("wheel units") {
      assert(wheel(NonEmptyVector(1), 1))(Assertion.equalTo(List(1))) &&
      assert(wheel(NonEmptyVector(1, 2), 1))(Assertion.equalTo(List(1, 2))) &&
      assert(wheel(NonEmptyVector(1, 2, 3), 1))(Assertion.equalTo(List(1, 2, 3))) &&
      assert(wheel(NonEmptyVector(1, 2, 3, 4, 5), 3))(Assertion.equalTo(List(1, 3))) &&
      assert(wheel(NonEmptyVector(1, 2, 3, 4, 5), 5))(Assertion.equalTo(List(1, 5)))
    },
    // TODO: Fix this test and generator
    // test("distance neighbours") {
    //   check(nelGen(10)) { case a =>
    //     val selection: Int = ringDistance(a, a.head)
    //
    //     assert(selection.length)(Assertion.lessOrEqual(3)) &&
    //     assert(selection)(Assertion.hasSubset(a)) &&
    //     assert(selection)(Assertion.contains(a.head))
    //   }
    // },
    test("ring distance units") {
      val a = NonEmptyVector(1.0, 2.0)
      val b = NonEmptyVector(3.0, 4.0)
      val c = NonEmptyVector(5.0, 6.0)
      val d = NonEmptyVector(7.0, 8.0)
      val e = NonEmptyVector(9.0, 10.0)
      val l = NonEmptyVector(a, b, c, d, e)

      assert(ringDistance(l, a))(Assertion.equalTo(List(a, b, c))) &&
      assert(ringDistance(l, c))(Assertion.equalTo(List(c, b, d))) &&
      assert(ringDistance(l, e))(Assertion.equalTo(List(e, d, c)))
    }
  )

}
