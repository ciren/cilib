package cilib

//import scalaz.NonEmptyList
//import scalaz.std.list._
//import scalaz.syntax.traverse._

final case class MultiEval[F[_],A](objectives: List[Eval[F,A]]) {
  // def eval(xs: NonEmptyList[A]): RVar[List[Objective[A]]] = {
  //   implicit val a = implicitly[spire.algebra.Eq[Double]]
  //   objectives.traverse(_.eval)
  // }
}

object MultiEval {

  // Varags suck
  def apply[F[_],A](a: Eval[F,A], b: Eval[F,A], rest: Eval[F,A]*) =
    new MultiEval(List(a, b) ++ rest.toList)

}

/*
sealed trait Collection[A]
final case class Single[A](x: List[A]) extends Collection[A]
final case class Multiple[A](main: List[A], subs: List[Single[A]]) extends Collection[A]

object Collection {
  def merge[A](a: Collection[A], b: Collection[A]): Collection[A] =
    (a, b) match {
      case (Single(xa), Single(xb)) => Single(xa ++ xb)
      case (xa @ Single(_), Multiple(main, subs)) => Multiple(main, subs :+ xa)
      case (Multiple(main, subs), xb @ Single(_)) => Multiple(main, subs :+ xb)
        //case (Multiple(main1, subs1), Multiple(main2, subs2)) => ??? // Not really sure how this could be? Illegal state?
      case _ => ???
    }

  // Create a subswarm, given some predicate?
  def split[A](a: Collection[A], p: A => Boolean): Multiple[A] =
    a match {
      case Single(xa) =>
        val (main, sub) = xa.partition(p)
        Multiple(main, List(Single(sub)))
      case Multiple(main, subs) =>
        val (m, s) = main.partition(p)
        Multiple(m, subs :+ Single(s))
    }
}
*/
