package cilib

abstract class Violation

object Violation {

  private final case class Bool(n: Boolean) extends Violation

  def bool(n: Boolean): Violation = Bool(n)
}

import scalaz.Foldable

abstract class Eval[F[_],A] {
  def eval(a: F[A]): (Fit,List[Violation])
}

// trait Problem {
//   def eval[S](s: S): (S,Eval)
// }

object Problem {

  val s = new Eval[List,Double] {
    def eval(a: List[Double]) = (Valid(3.0),List.empty)
  }

  import scalaz.Foldable
  import spire.math._

  // def static[F[_]:Foldable,A:Numeric](f: F[A] => Fit) =
  //   new Problem[F,A] {
  //     def eval(a: F[A]) = (f(a), List.empty)
  //   }

  // def dynamic[F[_]:Foldable,A:Numeric](f: F[A] => (Fit, List[Violation])) =
  //   new Problem[F,A] {
  //     def eval(a: F[A]) = f(a)
  //   }

  //def dynamic[F[_]:Foldable1,A](f: F[A] => )

/*  def dynamic[F[_],A](f: F[A] => (RVar[Problem],Fit)) =
    new Problem {
      def eval(a: F[A]) = {
        val (prob,fit) = f(a)
        (prob, fit, Nil)
      }
    }*/

  /*
  // What if there already are violations?
  def violations[F[_],A](p: Problem[F,A], c: List[F[A] => Violation]) =
    new Problem[F,A] {
      import scalaz.syntax.apply._
      import scalaz.std.list._
      def eval(a: F[A]) = {
        val (prob, fit, _) = p.eval(a)
        val v = List(a) <*> c
        (prob, fit, v)
      }
    }

  // ????
  def penalty[F[_],A](p: Problem[F,A], c: List[F[A] => Violation]) =
    new Problem[F,A] {
      def eval(a: F[A]) = {
        //val (prob, fit, _) = p.eval(a)
        p.eval(a)
      }
    }
   */
}
