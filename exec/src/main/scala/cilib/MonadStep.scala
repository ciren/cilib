package cilib
package exec

import zio.prelude._

abstract class MonadStep[M[+_]: IdentityFlatten: Covariant] {
  def liftR[A](r: RVar[A]): M[A]
}
