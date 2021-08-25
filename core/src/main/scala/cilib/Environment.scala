package cilib

final case class Environment(cmp: Comparison, eval: Eval[NonEmptyVector])
