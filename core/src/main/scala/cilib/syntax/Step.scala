package cilib
package syntax

import scalaz.StateT

object step {
  final implicit class StepOps[A, B](val self: Step[A, B]) extends AnyVal {
    def liftStepS[S]: StepS[A, S, B] = StepS(StateT[Step[A, ?], S, B](s => self.map((s, _))))
  }
}
