import scalaz._

package object cilib {

  // Really want this? Should we use Show instances instead?
  def println[A](a: A) = System.out.println(a)

  // Type aliases
  // (S, A) => M[(S, A)] - This is the Kleisli arrow, where M = RVar
  type Z[S, A] = Kleisli[RVar, (S, Pos[A]), (S, Pos[A])]
  type Pos[A] = Position[List, A]

  type Particle[S,A] = (S,Pos[A])
  type Guide[S,A] = (List[Particle[S,A]], Particle[S,A]) => Instruction[Pos[A]] // Should expand into a typeclass? Getter?

  type Selection[A] = (List[A], A) => List[A]

  type Y[A] = ReaderT[RVar, Opt, A]

  //type Problem[A] = List[A] => (Fit, List[Violation])

  def positive(d: Double): Maybe[Double @@ Tags.Positive] =
    if (d > 0.0) Tag.subst(Maybe.just(d))
    else Maybe.empty

  def negative(d: Double): Maybe[Double @@ Tags.Negative] =
    if (d < 0.0) Tag.subst(Maybe.just(d))
    else Maybe.empty


  // Use Spire for this!
  def closed[A](point: A): Bound[A] =
    Closed(point)

  def open[A](point: A): Bound[A] =
    Open(point)


  // Find a better home for this
  implicit object DoubleMonoid extends Monoid[Double] {
    def zero = 0.0
    def append(a: Double, b: => Double) = a + b
  }

}
