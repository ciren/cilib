package cilib
package exec

final case class Measurement[A](algorithm: Name, problem: Name, iteration: Int, env: Env, seed: Long, data: A)
