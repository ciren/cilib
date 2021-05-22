package cilib

import zio.prelude.NonEmptyList

final case class Environment private (val cmp: Comparison, val eval: Eval[NonEmptyList])

object Environment {

  def apply(cmp: Comparison, eval: Eval[NonEmptyList]): Environment =
    new Environment(cmp, eval)

}
