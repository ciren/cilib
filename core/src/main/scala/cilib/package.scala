import scalaz._

package object cilib {

  // Type aliases
  // (S, A) => M[(S, A)] - This is the Kleisli arrow, where M = RVar
  type C[S, A] = Kleisli[RVar, (S, Position[IList, A]), (S, Position[IList, A])]
  type Pos[A] = Position[IList, A]
  type Guide[A] = (IList[Pos[A]], Pos[A]) => Pos[A] // Should expand into a typeclass?

  def positive(d: Double): Option[Double @@ Tags.Positive] =
    if (d > 0.0) Tag.subst(Some(d))
    else None

  def negative(d: Double): Option[Double @@ Tags.Negative] =
    if (d < 0.0) Tag.subst(Some(d))
    else None

}
