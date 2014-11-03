package cilib

abstract class Problem[F[_], A] {
  def eval(a: F[A]): (Problem[F, A], Option[Fit])
}

object Problem {

  def static[F[_],A](f: F[A] => Option[Fit]) =
    new Problem[F,A] {
      def eval(a: F[A]) =
        (this, f(a))
    }

}
