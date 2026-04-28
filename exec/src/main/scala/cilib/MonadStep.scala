package cilib
package exec

import annotation.unused
import zio.prelude._

abstract class MonadStep[M[+_]: IdentityFlatten: Covariant] {
  // mark evidences as used
  @unused val ev1 = implicitly[IdentityFlatten[M]]
  @unused val ev2 = implicitly[Covariant[M]]

  def liftR[A](r: RVar[A]): M[A]
}
