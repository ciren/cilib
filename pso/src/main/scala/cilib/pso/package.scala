package cilib

package object pso {
  type Particle[S,A] = Entity[S,A]
  type Guide[S,A] = (List[Particle[S,A]], Particle[S,A]) => Step[A,Position[A]]


  type Pool[A] = List[PoolItem[A]]
}
