package cilib
package syntax

import scalaz.{StateT,LensFamily}

object step {
  final implicit class StepId[A](val self: A) extends AnyVal {
// <<<<<<< HEAD
//     def liftStep[B]: Step[B,A] = Step.point(self)
//     //def liftStepS[F[_], B, S]: StepS[F,B,S,A] = StepS.point(self)
//   }

//   final implicit class StepRVar[A](val self: RVar[A]) extends AnyVal {
//     def liftStep[B]: Step[B,A] = Step.pointR(self)
// =======
//     def liftStep[F[_], B]: Step[F,B,A] = Step.point(self)
//     def liftStepS[F[_], B, S]: StepS[F,B,S,A] = StepS.point(self)
//   }

//   final implicit class StepRVar[A](val self: RVar[A]) extends AnyVal {
//     def liftStep[F[_], B]: Step[F,B,A] = Step.pointR(self)
//     def liftStepS[F[_], B, S]: StepS[F,B,S,A] = StepS.pointR(self)
// >>>>>>> series/2.0.x
  }

  final implicit class StepOps[A,B](val self: Step[A,B]) extends AnyVal {
    def liftStepS[S]: StateT[Step[A,?], S, B] = StateT[Step[A,?],S,B](s => self.map((s, _)))
  }
}
