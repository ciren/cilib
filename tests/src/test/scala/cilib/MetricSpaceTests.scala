package cilib

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen

import scalaz.std.list._

import spire.algebra._
import spire.implicits._

object MetricSpaceTest extends Spec("MetricSpace") {

  implicit def arbMetricSpace: Arbitrary[MetricSpace[Int,Int]] = Arbitrary {
    Arbitrary.arbitrary[Int].map(x => MetricSpace.pure[Int,Int](x))
  }

  implicit def arbMetricSpaceFunc: Arbitrary[MetricSpace[Int, Int => Int]] = Arbitrary {
    Arbitrary.arbitrary[Int => Int].map(MetricSpace.pure[Int, Int => Int])
  }

  implicit val doubleGen = Gen.choose(-1000000.0, 1000000.0)

  val listTuple2 = Gen.sized { size =>
    for {
      x <- Gen.listOfN(size, doubleGen)
      y <- Gen.listOfN(size, doubleGen)
    } yield (x, y)
  }

  val listTuple3 = Gen.sized { size =>
    for {
      x <- Gen.listOfN(size, doubleGen)
      y <- Gen.listOfN(size, doubleGen)
      z <- Gen.listOfN(size, doubleGen)
    } yield (x, y, z)
  }

  val euclidean = MetricSpace.euclidean[List,Double,Double]
  val manhattan = MetricSpace.manhattan[List,Double,Double]
  val chebyshev = MetricSpace.chebyshev[List,Double]
  val hamming = MetricSpace.hamming[List,Double]

  def nonnegative[A,B](m: MetricSpace[A,B], x: A, y: A)(implicit ev: IsReal[B]): Boolean =
    ev.toDouble(m.dist(x, y)) >= 0.0

  def indisc[A,B](m: MetricSpace[A,B], x: A)(implicit E: Eq[B], F: Field[B]): Boolean =
    E.eqv(m.dist(x, x), F.zero)

  def symmetry[A,B](m: MetricSpace[A,B], x: A, y: A)(implicit E: Eq[B]): Boolean =
    E.eqv(m.dist(x, y), m.dist(y, x))

  // Some discussion: https://en.wikipedia.org/wiki/Triangle_inequality
  // Look at section on Metric Spaces
  def triangle[A,B](m: MetricSpace[A,B], a: A, b: A, c: A)(implicit F: Field[B], O: Order[B]) =
    O.lteqv(m.dist(a, c), m.dist(a, b) + m.dist(b, c))

  property("non-negativity") = forAll(listTuple2) { case (x, y) =>
    nonnegative(euclidean, x, y) &&
    nonnegative(manhattan, x, y) &&
    nonnegative(chebyshev, x, y)
  }

  property("hamming metric space") = forAll { (x: List[Double], y: List[Double], z: List[Double]) =>
    nonnegative(hamming, x, y) &&
    symmetry(hamming, x, y) &&
    hamming.dist(x, x) == 0
  }

  property("identity of indiscernibles") = forAll { (l: List[Double]) =>
    indisc(euclidean, l) &&
    indisc(manhattan, l) &&
    indisc(chebyshev, l)
  }

  property("identity") = forAll(listTuple2) { case (x, y) =>
    euclidean.dist(x, x) === 0.0 &&
    manhattan.dist(x, x) === 0.0 &&
    chebyshev.dist(x, x) === 0.0 &&
//    hamming.dist(x, x) === 0 &&
      (x =!= y) ==> (euclidean.dist(x, y) =!= 0.0) &&
      (x =!= y) ==> (manhattan.dist(x, y) =!= 0.0) &&
      (x =!= y) ==> (chebyshev.dist(x, y) =!= 0.0) //&&
  //    (x =!= y) ==> (hamming.dist(x, y) =!= 0)
  }

  property("symmetry") = forAll(listTuple2) { case (x, y) =>
    symmetry(euclidean, x, y) &&
    symmetry(manhattan, x, y) &&
    symmetry(chebyshev, x, y)
  }

  property("triangle-inequality") = forAll(listTuple3) { t =>
    val (x, y, z) = t

    triangle(euclidean, x, y, z) &&
    triangle(manhattan, x, y, z) &&
    triangle(chebyshev, x, y, z)
  }

  //checkAll("MetricSpace", monad.laws[MetricSpace[Int,?]])

}
