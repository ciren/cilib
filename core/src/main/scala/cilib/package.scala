import scalaz._

package object cilib {

  // Type aliases
  // (S, A) => M[(S, A)] - This is the Kleisli arrow, where M = RVar
  type C[S, A] = Kleisli[RVar, (S, Pos[A]), (S, Pos[A])]
  type Pos[A] = Position[IList, A]

  type Guide[A] = List[A] => A => RVar[A] // Should expand into a typeclass?
  type Particle[S,A] = (S,Position[List,A])

  type X[A] = StateT[RVar, Problem[List,Double], A]
  type Y[A] = ReaderT[X, Opt, A]

  def positive(d: Double): Option[Double @@ Tags.Positive] =
    if (d > 0.0) Tag.subst(Some(d))
    else None

  def negative(d: Double): Option[Double @@ Tags.Negative] =
    if (d < 0.0) Tag.subst(Some(d))
    else None

}
