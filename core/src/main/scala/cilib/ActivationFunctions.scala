package cilib

import spire.math._

object ActivationFunctions {

  def hyperbolic(lambda: Double = 1.0) = (x: Double) => {
    val lx = lambda * x
    (exp(lx) - exp(-lx)) / ((exp(lx) + exp(-lx)))
  }

  def linear(lambda: Double) = (x: Double) => lambda * x

  def sigmoid(lambda: Double = 1.0, gamma: Double = 1.0, offset: Double = 0.0) =
    (x: Double) => gamma / (1.0 + exp(-1.0 * lambda * (x - offset)))

  def step(threshold: Double = 0.0) =
    (x: Double ) => if (x >= threshold) 1.0 else 0.0

}
