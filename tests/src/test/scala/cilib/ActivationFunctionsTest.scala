package cilib

import cilib.ActivationFunctions._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import spire.implicits._

object ActivationFunctionsTest extends Properties("ActivationFunctions") {

  val lin = linear(1.0)
  property("linear") = forAll { (x: Double) => lin(x) == x }

  val sig = sigmoid(1.0, 1.0, 0.0)
  property("sigmoid") = forAll { (x: Double) =>
    sig(x) >= 0.0 &&
    sig(x) <= 1.0
  } && sig(0.0) == 0.5

  val hyper = hyperbolic(1.0)
  property("hyperbolic") = forAll(Gen.choose(-100.0, 100.0)) { x =>
    hyper(x) >= -1.0 &&
    hyper(x) <= 1.0
  } && hyper(0.0) == 0.0

  val st = step(0.0)
  property("step") = forAll { (x: Double) =>
    if (x >= 0.0) st(x) == 1.0 else st(x) == 0.0
  }

}
