package cilib

import org.scalacheck._
import org.scalacheck.Prop._

import spire.implicits._

import scalaz.std.list._

object SelectionTests extends Properties("Selection") {

  val star = Selection.star[Int]
  val ring = Selection.indexNeighbours[Int](3)
  val wheel = Selection.wheel[Int]
  val ringDistance = Selection.distanceNeighbours[List,Double](MetricSpace.euclidean)(3)

  property("star") = forAll { (a: List[Int]) =>
    (a.length >= 1) ==> {
      val selection = star(a, a.head)
      selection.length === a.length &&
      selection.forall(e => a.contains(e)) &&
      selection.contains(a.head)
    }
  } && {
    star(List(1), 1)             === List(1) &&
    star(List(1, 2), 1)          === List(1, 2) &&
    star(List(1, 2, 3), 1)       === List(1, 2, 3) &&
    star(List(1, 2, 3, 4, 5), 3) === List(1, 2, 3, 4, 5)
  }

  property("indexNeighbours") = forAll { (a: List[Int]) =>
    (a.length >= 1) ==> {
      val selection = ring(a, a.head)
      selection.length === 3 &&
      selection.forall(e => a.contains(e)) &&
      selection.contains(a.head)
    }
  } && {
    ring(List(1), 1)             === List(1, 1, 1) &&
    ring(List(1, 2), 1)          === List(2, 1, 2) &&
    ring(List(1, 2, 3), 1)       === List(3, 1, 2) &&
    ring(List(1, 2, 3, 4, 5), 3) === List(2, 3, 4) &&
    ring(List(1, 2, 3, 4, 5), 5) === List(4, 5, 1)
  }

  property("wheel") = forAll { (a: List[Int]) =>
    (a.length >= 2 && a.head != a.last) ==> {
      wheel(a, a.head).length === a.length &&
      wheel(a, a.last).length === 2 &&
      wheel(a, a.head).forall(e => a.contains(e)) &&
      wheel(a, a.last).forall(e => a.contains(e))

    }
  } && {
    wheel(List(1), 1)             === List(1) &&
    wheel(List(1, 2), 1)          === List(1, 2) &&
    wheel(List(1, 2, 3), 1)       === List(1, 2, 3) &&
    wheel(List(1, 2, 3, 4, 5), 3) === List(1, 3) &&
    wheel(List(1, 2, 3, 4, 5), 5) === List(1, 5)
  }

  property("distanceNeighbours") = forAll { (a: List[List[Double]]) =>
    (a.length >= 1) ==> {
      val selection = ringDistance(a, a.head)
      selection.length <= 3 &&
      selection.forall(e => a.contains(e)) &&
      selection.contains(a.head)
    }
  } && {
    val a = List(1.0, 2.0)
    val b = List(3.0, 4.0)
    val c = List(5.0, 6.0)
    val d = List(7.0, 8.0)
    val e = List(9.0, 10.0)
    val l = List(a, b, c, d, e)
    ringDistance(l, a) === List(a, b, c) &&
    ringDistance(l, c) === List(c, b, d) &&
    ringDistance(l, e) === List(e, d, c)
  }
}
