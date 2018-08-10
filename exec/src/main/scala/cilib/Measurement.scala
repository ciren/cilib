package cilib
package exec

final case class Measurement[A](alg: String,
                                prob: String,
                                iteration: Int,
                                env: Env,
                                seed: Long,
                                data: A)
