package cilib
package syntax

import scalaz.{IndexedStateT,StateT,LensFamily}

object algorithm {
  final implicit class ToAlgorithmOps[A,B,C](val self: List[B] => B => Step[A,C]) extends AnyVal {
    def map[D](g: C => D): List[B] => B => Step[A,D] =
      xs => x => self(xs)(x).map(g)

    def flatMap[D](g: C => Step[A,D]): List[B] => B => Step[A,D] =
      xs => x => self(xs)(x).flatMap(g)

    def liftStepS[S]: List[B] => B => StepS[A,S,C] =
      xs => x => StepS.pointS(self(xs)(x))
  }

  implicit class ToAlgorithmSOps[A,B,C,S](val self: List[B] => B => StateT[Step[A,?], S, C]) extends AnyVal {
    def map[D](g: C => D): List[B] => B => StateT[Step[A,?], S, D] =
      xs => x => self(xs)(x).map(g)

    def flatMap[D](g: C => StateT[Step[A,?], S, D]): List[B] => B => StateT[Step[A,?], S, D] =
      xs => x => self(xs)(x).flatMap(g)

    def zoom[S0, S3](l: LensFamily[S0, S3, S, S]): List[B] => B => IndexedStateT[Step[A,?],S0,S3,C] =
      xs => x => self(xs)(x).zoom(l: LensFamily[S0, S3, S, S])
  }
}
