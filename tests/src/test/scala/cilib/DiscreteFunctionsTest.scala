package cilib

import cilib.DiscreteFunctions._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

object DiscreteFunctionsTest extends Properties("DiscreteFunctions") {

  val a = List('a', 'b', 'c', 'd', 'e', 'f')
  val b = List(1, 2, 3, 4, 5, 6)

  val genChar =
    Gen.containerOfN[List, Char](a.length, Arbitrary.arbitrary[Char])
  val genInt = Gen.containerOfN[List, Int](b.length, Arbitrary.arbitrary[Int])

  val hammingA = hammingDistance(a)
  val hammingB = hammingDistance(b)

  property("hammingDistance") = {
    forAll(genChar) { g => hammingA(g).forall(_ >= 0) } &&
    forAll(genInt) { g => hammingB(g).forall(_ >= 0) } &&
    hammingA(a) == Some(0.0) &&
    hammingB(b) == Some(0.0) &&
    hammingA(a.reverse) == Some(a.length) &&
    hammingB(b.reverse) == Some(b.length)
  }

  property("onemax") = forAll { (g: List[Boolean]) =>
    onemax(g).forall(_ >= 0) &&
    onemax(g).forall(_ <= g.length)
  }

  val genOrder3 =
    Gen.containerOfN[List, Boolean](18, Arbitrary.arbitrary[Boolean])
  def intToBool(x: List[Int]) = x.map(_ != 0)

  property("order3Bipolar") = forAll(genOrder3) { g =>
    order3Bipolar(g).forall(_ >= 0.0)
    order3Bipolar(g).forall(_ <= g.length / 6)
  } && {
    order3Bipolar(intToBool(List(1, 0, 0, 0, 0, 0))) == Some(0.0) &&
    order3Bipolar(intToBool(List(1, 1, 1, 1, 1, 1))) == Some(1.0)
  }

  property("order3Deceptive") = forAll(genOrder3) { g =>
    order3Deceptive(g).forall(_ >= 0.3)
    order3Deceptive(g).forall(_ <= g.length / 3)
  } && {
    order3Deceptive(intToBool(List(1, 1, 1))) == Some(1.0) &&
    order3Deceptive(intToBool(List(0, 0, 0))) == Some(0.9)
  }
}
