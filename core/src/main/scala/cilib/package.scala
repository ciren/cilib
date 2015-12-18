import scalaz._
import spire.math.Interval
import spire.math.interval.{Bound,ValueBound}

package object cilib {
  type Particle[S,F[_],A] = Entity[S,F,A]

  // Should expand into a typeclass? Getter?
  type Guide[S,F[_],A] = (List[Particle[S,F,A]], Particle[S,F,A]) => Step[F,A,Position[F,A]]

  type Selection[A] = (List[A], A) => List[A]

  def positive(d: Double): Maybe[Double @@ Tags.Positive] =
    if (d > 0.0) Tag.subst(Maybe.just(d))
    else Maybe.empty

  def negative(d: Double): Maybe[Double @@ Tags.Negative] =
    if (d < 0.0) Tag.subst(Maybe.just(d))
    else Maybe.empty

  type StepS[F[_],A,S,B] = StateT[Step[F,A,?],S,B]

  // Find a better home for this
  implicit object DoubleMonoid extends Monoid[Double] {
    def zero = 0.0
    def append(a: Double, b: => Double) = a + b
  }

  implicit class IntervalOps[A](interval: Interval[A]) {
    def ^(n: Int): NonEmptyList[Interval[A]] =
      NonEmptyList.nel(interval, (1 until n).map(_ => interval).toList)

    private def getValue(b: Bound[A]) =
      ValueBound.unapply(b).getOrElse(sys.error("Empty and Unbounded bounds are not supported"))

    lazy val lowerValue = getValue(interval.lowerBound)
    lazy val upperValue = getValue(interval.upperBound)
  }

}
