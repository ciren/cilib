package cilib

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen

import scalaz.std.list._
import spire.implicits._

object DistanceTests extends Properties("Distance") {

  val listTuple2 = Gen.sized { size =>
    for {
      x <- Gen.listOfN(size, Arbitrary.arbitrary[Double])
      y <- Gen.listOfN(size, Arbitrary.arbitrary[Double])
    } yield (x, y)
  }

  val listTuple3 = Gen.sized { size =>
    for {
      x <- Gen.listOfN(size, Arbitrary.arbitrary[Double])
      y <- Gen.listOfN(size, Arbitrary.arbitrary[Double])
      z <- Gen.listOfN(size, Arbitrary.arbitrary[Double])
    } yield (x, y, z)
  }

  val euclidean = Distance.euclidean[List,Double]
  val manhattan = Distance.manhattan[List,Double]


  property("non-negativity") = forAll(listTuple2) { case (x, y) =>
    euclidean(x, y) >= 0.0 &&
    manhattan(x, y) >= 0.0
  }

  property("identity") = forAll(listTuple2) { case (x, y) =>
    euclidean(x, x) === 0.0 &&
    manhattan(x, x) === 0.0 &&
    (x =!= y) ==> (euclidean(x, y) =!= 0.0) &&
    (x =!= y) ==> (manhattan(x, y) =!= 0.0)
  }

  property("symmetry") = forAll(listTuple2) { case (x, y) =>
    euclidean(x, y) === euclidean(y, x) &&
    manhattan(x, y) === manhattan(y, x)
  }

  property("triangle-inequality") = forAll(listTuple3) { case (x, y, z) =>
    euclidean(x, z) <= euclidean(x, y) + euclidean(y, z) &&
    manhattan(x, z) <= manhattan(x, y) + manhattan(y, z)
  }

}
