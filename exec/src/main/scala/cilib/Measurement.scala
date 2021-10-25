package cilib
package exec

final case class Measurement[A](alg: Name, prob: Name, iteration: Int, env: Env, seed: Long, data: A)
