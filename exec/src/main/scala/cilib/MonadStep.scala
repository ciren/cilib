package cilib
package exec

import scalaz.Monad

abstract class MonadStep[M[_]: Monad] {
  @deprecated("This method has been deprecated, use liftR instead, it is technically more accurate",
              "2.0.2")
  def pointR[A](r: RVar[A]): M[A] =
    liftR(r)

  def liftR[A](r: RVar[A]): M[A]
}
