package cilib

import monocle._
import zio.prelude.NonEmptyList

final class Environment private (val cmp: Comparison, val eval: Eval[NonEmptyList])

object Environment {

  def apply(cmp: Comparison, eval: Eval[NonEmptyList]): Environment =
    new Environment(cmp, eval)

  def _eval: Lens[Environment, Eval[NonEmptyList]] =
    Lens[Environment, Eval[NonEmptyList]](_.eval)(b => a => new Environment(a.cmp, b))
}
