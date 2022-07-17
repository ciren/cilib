package cilib
package exec

final case class Measurement[A](algorithm: Name, problem: Name, iteration: Int, env: Env, seed: Long, data: A) {
  @deprecated("Renamed to 'algorithm', please update sources", "2.1.0")
  val alg = algorithm

  @deprecated("Renamed to 'problem', please update sources", "2.1.0")
  val prob = problem
}
