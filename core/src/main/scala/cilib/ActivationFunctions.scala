package cilib

import spire.math._
import spire.algebra._
import spire.implicits._

object ActivationFunctions {

  def hyperbolic[T: Field : Trig](lambda: T = 1.0) = (x: T) => {
    val lx = lambda * x
    (exp(lx) - exp(-lx)) / ((exp(lx) + exp(-lx)))
  }

  def linear[T: Field](lambda: T = 1.0) = (x: T) => lambda * x

  def sigmoid[T: Field : Trig](lambda: T = 1.0, gamma: T = 1.0, offset: T = 0.0) =
    (x: T) => gamma / (1.0 + exp(-1.0 * lambda * (x - offset)))

  def step[T: Order](threshold: T = 0.0) =
    (x: T ) => if (x.compare(threshold) < 0) 0.0 else 1.0

}
