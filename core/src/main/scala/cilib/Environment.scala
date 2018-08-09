package cilib

import scalaz.NonEmptyList
import monocle._

final class Environment[A] private (val cmp: Comparison, val eval: Eval[NonEmptyList, A])

object Environment {

  def apply[A](cmp: Comparison, eval: Eval[NonEmptyList, A]): Environment[A] =
    new Environment(cmp, eval)

  def _eval[A]: Lens[Environment[A], Eval[NonEmptyList, A]] =
    Lens[Environment[A], Eval[NonEmptyList, A]](_.eval)(b => a => new Environment(a.cmp, b))
}
