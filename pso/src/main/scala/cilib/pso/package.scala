package cilib

import scalaz.NonEmptyList

package object pso {
  type Particle[S, A] = Entity[S, A]
  type Guide[S, A] = (NonEmptyList[Particle[S, A]], Particle[S, A]) => Step[A, Position[A]]

  type Pool[A] = NonEmptyList[PoolItem[A]]
}
