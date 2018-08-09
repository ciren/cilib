package cilib

import scalaz._
import Scalaz._

final class MultiEval[F[_], A](objectives: NonEmptyList[Eval[F, A]]) {

  def eval(p: Position[A]): RVar[Objective[A]] =
    objectives
      .traverse(x => x.eval.map(_.apply(p.pos)))
      .map(Objective.multi)

}

object MultiEval {

  def apply[F[_], A](a: NonEmptyList[Eval[F, A]]) =
    new MultiEval(a)

}
