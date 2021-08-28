package cilib

import zio.prelude.fx.ZPure
package object exec {

  implicit val StepMonadStep: MonadStep[ZPure[Nothing, RNG, RNG, Environment, Exception, +*]] =
    new MonadStep[Step[+*]] {
      def liftR[A](r: RVar[A]): Step[A] =
        Step.liftR(r)
    }

  implicit def StepSMonadStep[S]: MonadStep[ZPure[Nothing, (RNG, S), (RNG, S), Environment, Exception, +*]] =
    new MonadStep[StepS[S, +*]] {
      def liftR[A](r: RVar[A]): StepS[S, A] =
        StepS.liftR(r)
    }
}
