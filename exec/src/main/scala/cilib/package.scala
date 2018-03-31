package cilib

package object exec {

  implicit def StepMonadStep[A] =
    new MonadStep[Step[A, ?]] {
      def liftR[B](r: RVar[B]): Step[A, B] =
        Step.liftR(r)
    }

  implicit def StepSMonadStep[S, A] =
    new MonadStep[StepS[S, A, ?]] {
      def liftR[B](r: RVar[B]): StepS[S, A, B] =
        StepS.liftR(r)
    }
}
