package cilib
package exec

final case class Kleisli[F[_], -A, B](run: A => F[B])
