package cilib
package exec

import com.sksamuel.avro4s._

final case class Measurement[A: SchemaFor](alg: String,
                                           prob: String,
                                           iteration: Int,
                                           env: Env,
                                           seed: Long,
                                           data: A)
