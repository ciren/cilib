package cilib

import zio.prelude._

final class MultiEval[F[+_]](objectives: NonEmptyVector[Eval[F]]) {

  def eval[A](p: Position[A]): RVar[Objective] =
    ForEach[NonEmptyVector]
      .forEach(objectives)(x => x.eval.map(_.apply(p.pos)))
      .map(Objective.multi(_))

}

object MultiEval {

  def apply[F[+_]](a: NonEmptyVector[Eval[F]]) =
    new MultiEval(a)

}
