package cilib

package object exec {

  implicit val StepMonadStep =
    new MonadStep[Step[+*]] {
      def liftR[A](r: RVar[A]): Step[A] =
        Step.liftR(r)
    }

  implicit def StepSMonadStep[S] =
    new MonadStep[StepS[S, +*]] {
      def liftR[A](r: RVar[A]): StepS[S, A] =
        StepS.liftR(r)
    }
}
