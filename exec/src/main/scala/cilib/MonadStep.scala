package cilib
package exec

import scalaz.Monad

abstract class MonadStep[M[_]: Monad] {
  def pointR[A](r: RVar[A]): M[A]
}
