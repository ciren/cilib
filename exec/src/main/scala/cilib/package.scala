package cilib

package object exec {

  implicit def StepMonadStep[A] =
    new MonadStep[Step[A, ?]] {
      def pointR[B](r: RVar[B]): Step[A, B] =
        Step.pointR(r)
    }

  implicit def StepSMonadStep[S, A] =
    new MonadStep[StepS[S, A, ?]] {
      def pointR[B](r: RVar[B]): StepS[S, A, B] =
        StepS.pointR(r)
    }
}
