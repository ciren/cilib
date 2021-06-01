package cilib

import zio.prelude.NonEmptyList

final case class Environment(cmp: Comparison, eval: Eval[NonEmptyList])
