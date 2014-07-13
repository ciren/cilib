package cilib

import scalaz._

trait Problem[F[_], A] {
  def eval(a: F[A]): (Problem[F, A], Fit)
}

object Problem {

}
