package cilib

package object pso {
  type Particle[S, A] = Entity[S, A]
  type Guide[S, A]    = (NonEmptyVector[Particle[S, A]], Particle[S, A]) => Step[Position[A]]
}
