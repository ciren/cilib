package cilib

import scalaz.StateT

/**
 * `Scheme`s are a higher level of composition for an `Iteration`.
 * They are responsible for determining the manner in which an `Iteration`
 * is executed, or even "run".
 *
 * Within a `Scheme`, one can update the problem that is provided to
 * an `Iteration`, thereby creating a 'dynamic environment', or keeping
 * it static.
 *
 * Constraints are also provided to the `Iteration`, via the problem for which
 * the constraints are defined.
 */
object Scheme { // Are these not runners?

  // TODO: what about issues related to stack overflow?

  def static[F[_],A](i: Iteration[A], l: List[A]): StateT[Instruction, Problem[F,A], List[A]] =
    StateT { prob => i.run(l).map((prob, _)) }

  def dynamic[F[_],A](i: Iteration[A], l: List[A])
                     (update: => Problem[F,A]): StateT[Instruction, Problem[F,A], List[A]] =
    static(i, l).xmap((p: Problem[F,A]) => update)(identity _)

}
