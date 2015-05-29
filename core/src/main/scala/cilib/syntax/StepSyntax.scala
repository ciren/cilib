package cilib
package syntax

import scalaz.StateT

final class StepIdOps[A](val self: A) extends AnyVal {

  def liftStep[F[_], B]: Step[F,B,A] = Step.point(self)

  def liftStepS[F[_], B, S]: StepS[F,B,S,A] = StepS.point(self)

}

final class StepRVarOps[A](val self: RVar[A]) extends AnyVal {
  def liftStep[F[_], B]: Step[F,B,A] = Step.pointR(self)
}

final class StepOps[F[_],A,B](val self: Step[F,A,B]) extends AnyVal {
  def liftStepS[S]: StateT[Step[F,A,?], S, B] = StepS.pointK(self)
}

trait ToStepOps {

  implicit def ToStepIdOps[A](a: A) = new StepIdOps(a)

  implicit def ToStepRVarOps[A](a: RVar[A]) = new StepRVarOps(a)

  implicit def ToStepOps[F[_],A,B](a: Step[F,A,B]) = new StepOps(a)

}
