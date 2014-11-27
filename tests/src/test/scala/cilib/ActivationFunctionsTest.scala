package cilib

import cilib.ActivationFunctions._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

object ActivationFunctionsTest extends Properties("ActivationFunctions") {

  property("linear") = forAll { (x: Double) => linear(1.0)(x) == x }

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

  property("step") = forAll { (x: Double) =>
    if (x >= 0.0) step(0.0)(x) == 1.0 else step(0.0)(x) == 0.0
  }

}
