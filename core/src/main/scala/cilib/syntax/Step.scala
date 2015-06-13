package cilib
package syntax

import scalaz.StateT

object step {
  final implicit class StepId[A](val self: A) extends AnyVal {
    def liftStep[F[_], B]: Step[F,B,A] = Step.point(self)
    //def liftStepS[F[_], B, S]: StepS[F,B,S,A] = StepS.point(self)
  }

  final implicit class StepRVar[A](val self: RVar[A]) extends AnyVal {
    def liftStep[F[_], B]: Step[F,B,A] = Step.pointR(self)
  }

/*  final implicit class StepOps[F[_],A,B](val self: Step[F,A,B]) extends AnyVal {
    def liftStepS[S]: StateT[Step[F,A,?], S, B] = StepS.pointK(self)
  }*/
}

