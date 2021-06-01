package cilib

import zio.prelude.NonEmptyList

package object pso {
  type Particle[S, A] = Entity[S, A]
  type Guide[S, A]    = (NonEmptyList[Particle[S, A]], Particle[S, A]) => Step[Position[A]]

//  type Pool[A] = NonEmptyList[PoolItem[A]]
}
