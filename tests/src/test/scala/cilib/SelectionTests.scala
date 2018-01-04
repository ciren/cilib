package cilib

import org.scalacheck._
import org.scalacheck.Prop._

import spire.implicits._

import scalaz._
import Scalaz._
import scalaz.scalacheck.ScalazArbitrary._

object SelectionTests extends Properties("Selection") {

  val star = Selection.star[Int]
  val ring = Selection.indexNeighbours[Int](3)
  val wheel = Selection.wheel[Int]
  val ringDistance = Selection.distanceNeighbours[NonEmptyList,Double](MetricSpace.euclidean)(3)

  property("star") = forAll { (a: NonEmptyList[Int]) =>
    {
      val selection = star(a, a.head)
      selection.length === a.length &&
      selection.forall(e => a.toList.contains(e)) &&
      selection.contains(a.head)
    }
  } && {
    star(NonEmptyList(1), 1)             === List(1) &&
    star(NonEmptyList(1, 2), 1)          === List(1, 2) &&
    star(NonEmptyList(1, 2, 3), 1)       === List(1, 2, 3) &&
    star(NonEmptyList(1, 2, 3, 4, 5), 3) === List(1, 2, 3, 4, 5)
  }

  property("indexNeighbours") = forAll { (a: NonEmptyList[Int]) =>
    {
      val selection = ring(a, a.head)
      selection.length === 3 &&
      selection.forall(e => a.toList.contains(e)) &&
      selection.contains(a.head)
    }
  } && {
    ring(NonEmptyList(1), 1)             === List(1, 1, 1) &&
    ring(NonEmptyList(1, 2), 1)          === List(2, 1, 2) &&
    ring(NonEmptyList(1, 2, 3), 1)       === List(3, 1, 2) &&
    ring(NonEmptyList(1, 2, 3, 4, 5), 3) === List(2, 3, 4) &&
    ring(NonEmptyList(1, 2, 3, 4, 5), 5) === List(4, 5, 1)
  }

  property("wheel") = forAll { (a: NonEmptyList[Int]) =>
    (a.length >= 2 && a.head != a.last) ==> {
      wheel(a, a.head).length === a.length &&
      wheel(a, a.last).length === 2 &&
      wheel(a, a.head).forall(e => a.toList.contains(e)) &&
      wheel(a, a.last).forall(e => a.toList.contains(e))

    }
  } && {
    wheel(NonEmptyList(1), 1)             === List(1) &&
    wheel(NonEmptyList(1, 2), 1)          === List(1, 2) &&
    wheel(NonEmptyList(1, 2, 3), 1)       === List(1, 2, 3) &&
    wheel(NonEmptyList(1, 2, 3, 4, 5), 3) === List(1, 3) &&
    wheel(NonEmptyList(1, 2, 3, 4, 5), 5) === List(1, 5)
  }

  property("distanceNeighbours") = forAll { (a: NonEmptyList[NonEmptyList[Double]]) =>
    {
      val selection = ringDistance(a, a.head)
      selection.length <= 3 &&
      selection.forall(e => a.toList.contains(e)) &&
      selection.contains(a.head)
    }
  } && {
    val a = NonEmptyList(1.0, 2.0)
    val b = NonEmptyList(3.0, 4.0)
    val c = NonEmptyList(5.0, 6.0)
    val d = NonEmptyList(7.0, 8.0)
    val e = NonEmptyList(9.0, 10.0)
    val l = NonEmptyList(a, b, c, d, e)
    ringDistance(l, a) === List(a, b, c) &&
    ringDistance(l, c) === List(c, b, d) &&
    ringDistance(l, e) === List(e, d, c)
  }

}
