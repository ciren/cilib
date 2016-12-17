package cilib

import scalaz.NonEmptyList
import scalaz.std.list._
import scalaz.syntax.traverse._

final class MultiEval[A] private (objectives: List[Eval[A]]) {
  def eval(xs: NonEmptyList[A]): RVar[List[Objective[A]]] =
    objectives.traverse(_.eval(xs))
}

object MultiEval {

  // The varags sucks
  def apply[A](a: Eval[A], b: Eval[A], rest: Eval[A]*) =
    new MultiEval(List(a, b) ++ rest.toList)

}
