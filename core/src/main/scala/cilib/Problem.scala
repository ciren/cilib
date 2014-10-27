package cilib

final case class Violation()

abstract class Problem[F[_],A] {
  def eval(a: F[A]): (Problem[F,A], Fit, List[Violation])
}

object Problem {

  def static[F[_],A](f: F[A] => Fit) =
    new Problem[F,A] {
      def eval(a: F[A]) =
        (this, f(a), Nil)
    }

  def dynamic[F[_],A](f: F[A] => (Problem[F,A],Fit)) =
    new Problem[F,A] {
      def eval(a: F[A]) = {
        val (prob, fit) = f(a)
        (prob, fit, Nil)
      }
    }

  def constrain[F[_],A](p: Problem[F,A], c: List[F[A] => Violation]) =
    new Problem[F,A] {
      import scalaz.syntax.apply._
      import scalaz.std.list._
      import scalaz.std.tuple._
      def eval(a: F[A]) =
        p.eval(a).map(_ => List(a) <*> c)
    }

}
