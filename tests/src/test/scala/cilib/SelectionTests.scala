package cilib

import spire.implicits._
import zio.test._
import zio.prelude._

object SelectionTests extends DefaultRunnableSpec {

  val star         = Selection.star[Int]
  val ring         = Selection.indexNeighbours[Int](3)
  val wheel        = Selection.wheel[Int]
  val ringDistance = Selection.distanceNeighbours[NonEmptyList, Double](MetricSpace.euclidean)(3)

  def nelGen(dim: Int) =
    Gen.listOfN(dim)(Gen.int(-10, 10))
      .map(x => NonEmptyList.fromIterableOption(x).get)


  override def spec: ZSpec[Environment,Failure] = suite("selection")(
    testM("star") {
      check(nelGen(10)) { case (a) =>
        val selection = star(a, a.head)

        assert(selection.length == a.length)(Assertion.isTrue) &&
        assert(selection)(Assertion.hasSubset(a))
      }
    },
    test("star units") {
      assert(star(NonEmptyList(1), 1))(Assertion.equalTo(List(1))) &&
      assert(star(NonEmptyList(1, 2), 1))(Assertion.equalTo(List(1, 2))) &&
      assert(star(NonEmptyList(1, 2, 3), 1))(Assertion.equalTo(List(1, 2, 3))) &&
      assert(star(NonEmptyList(1, 2, 3, 4, 5), 3))(Assertion.equalTo(List(1, 2, 3, 4, 5)))
    },

    testM("indexNeighbours") {
      check(nelGen(10)) { case (a) =>
        val selection = ring(a, a.head)

        assert(selection.length)(Assertion.equalTo(3))
        //assert(selection)(Assertion.hasSubset(a))
        //assert(selection)(Assertion.contains(a.head))
      }
    },
    test("ring units") {
      assert(ring(NonEmptyList(1), 1))(Assertion.equalTo(List(1, 1, 1))) &&
      assert(ring(NonEmptyList(1, 2), 1))(Assertion.equalTo(List(2, 1, 2))) &&
      assert(ring(NonEmptyList(1, 2, 3), 1))(Assertion.equalTo(List(3, 1, 2))) &&
      assert(ring(NonEmptyList(1, 2, 3, 4, 5), 3))(Assertion.equalTo(List(2, 3, 4))) &&
      assert(ring(NonEmptyList(1, 2, 3, 4, 5), 5))(Assertion.equalTo(List(4, 5, 1)))
    },

    testM("wheel") {
      check(nelGen(10)) { case a =>
        val wheelHead = wheel(a, a.head)

        assert(wheelHead.length)(Assertion.equalTo(a.length)) &&
        assert(wheelHead)(Assertion.hasSubset(a)) // && assert(wheel(a, a.last))(Assertion.hasSubset(a))
      }
    },
    test("wheel units") {
      assert(wheel(NonEmptyList(1), 1))(Assertion.equalTo(List(1))) &&
      assert(wheel(NonEmptyList(1, 2), 1))(Assertion.equalTo(List(1, 2))) &&
      assert(wheel(NonEmptyList(1, 2, 3), 1))(Assertion.equalTo(List(1, 2, 3))) &&
      assert(wheel(NonEmptyList(1, 2, 3, 4, 5), 3))(Assertion.equalTo(List(1, 3))) &&
      assert(wheel(NonEmptyList(1, 2, 3, 4, 5), 5))(Assertion.equalTo(List(1, 5)))
    },

    // TODO: Fix this test and generator
    // testM("distance neighbours") {
    //   check(nelGen(10)) { case a =>
    //     val selection: Int = ringDistance(a, a.head)
    //
    //     assert(selection.length)(Assertion.lessOrEqual(3)) &&
    //     assert(selection)(Assertion.hasSubset(a)) &&
    //     assert(selection)(Assertion.contains(a.head))
    //   }
    // },
    test("ring distance units") {
      val a = NonEmptyList(1.0, 2.0)
      val b = NonEmptyList(3.0, 4.0)
      val c = NonEmptyList(5.0, 6.0)
      val d = NonEmptyList(7.0, 8.0)
      val e = NonEmptyList(9.0, 10.0)
      val l = NonEmptyList(a, b, c, d, e)

      assert(ringDistance(l, a))(Assertion.equalTo(List(a, b, c))) &&
      assert(ringDistance(l, c))(Assertion.equalTo(List(c, b, d))) &&
      assert(ringDistance(l, e))(Assertion.equalTo(List(e, d, c)))
    }
  )

}
