package cilib

import scalaz._

abstract class Problem[F[_], A] {
  def eval(a: F[A]): (Problem[F, A], Fit)
}

object Problem {

  def static[F[_],A](f: F[A] => Fit) =
    new Problem[F,A] {
      def eval(a: F[A]) =
        (this, f(a))
    }

}
