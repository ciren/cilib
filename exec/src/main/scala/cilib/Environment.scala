package cilib
package exec

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

//import com.sksamuel.avro4s._

sealed abstract class Env
final case object Unchanged extends Env
final case object Change extends Env

/*
final case class Algorithm[F[_], A, B](name: String Refined NonEmpty,
                                       alg: Kleisli[Step[A, ?], F[B], F[B]])

final case class Problem[F[_], A](
    name: String Refined NonEmpty,
    initial: F[A],
    change: A => F[A],
    eval: A => Eval[NonEmptyList, Double]
)

final case class Measurement[A: SchemaFor](alg: String,
                                           prob: String,
                                           iteration: Int,
                                           env: Env,
                                           seed: Long,
//                               max: Double,
                                           //                             error: Double,
                                           data: A)
 */

object Env {
  def constant: Stream[Env] =
    Stream(Unchanged) #::: constant

  def frequency[A](n: Int Refined Positive): Stream[Env] =
    (constant.take(n - 1) #::: Stream[Env](Change)) #::: frequency(n)
}
