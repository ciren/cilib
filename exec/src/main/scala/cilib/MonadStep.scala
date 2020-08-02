package cilib
package exec

import scalaz.Monad

abstract class MonadStep[M[_]: Monad] {
  def liftR[A](r: RVar[A]): M[A]
}
