package cilib
package syntax

import scalaz.NonEmptyList

object algorithm {
  final implicit class ToAlgorithmOps[A, B, C](val self: NonEmptyList[B] => B => Step[A, C])
      extends AnyVal {
    def map[D](g: C => D): NonEmptyList[B] => B => Step[A, D] =
      xs => x => self(xs)(x).map(g)

    def flatMap[D](g: C => Step[A, D]): NonEmptyList[B] => B => Step[A, D] =
      xs => x => self(xs)(x).flatMap(g)

    def liftStepS[S]: NonEmptyList[B] => B => StepS[A, S, C] =
      xs => x => StepS.pointS(self(xs)(x))
  }

  implicit class ToAlgorithmSOps[A, B, C, S](val self: NonEmptyList[B] => B => StepS[A, S, C])
      extends AnyVal {
    def map[D](g: C => D): NonEmptyList[B] => B => StepS[A, S, D] =
      xs => x => self(xs)(x).map(g)

    def flatMap[D](g: C => StepS[A, S, D]): NonEmptyList[B] => B => StepS[A, S, D] =
      xs => x => self(xs)(x).flatMap(g)

    def zoom[S0](l: scalaz.Lens[S0, S]): NonEmptyList[B] => B => StepS[A, S0, C] =
      xs => x => self(xs)(x).zoom(StepS.lensIso.get(l))
  }
}
