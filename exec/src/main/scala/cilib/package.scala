package cilib

import zio.prelude.fx.ZPure
package object exec {

  implicit val StepMonadStep: MonadStep[ZPure[Nothing, RNG, RNG, (Comparison, Eval[NonEmptyVector]), Exception, +*]] =
    new MonadStep[Step[+*]] {
      def liftR[A](r: RVar[A]): Step[A] =
        Step.liftR(r)
    }

  implicit def StepSMonadStep[S]
    : MonadStep[ZPure[Nothing, (RNG, S), (RNG, S), (Comparison, Eval[NonEmptyVector]), Exception, +*]] =
    new MonadStep[StepS[S, +*]] {
      def liftR[A](r: RVar[A]): StepS[S, A] =
        StepS.liftR(r)
    }
}
