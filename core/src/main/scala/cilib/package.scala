import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import scalaz._
import spire.algebra.{Monoid => _, _}
import spire.math.Interval
import spire.math.interval.{Bound, ValueBound}

package object cilib extends EvalInstances {

  //type Eval[A] = RVar[NonEmptyList[A] => Objective[A]]

  // Should expand into a typeclass? Getter?
  type Selection[A] = NonEmptyList[A] => List[A]
  type IndexSelection[A] = (NonEmptyList[A], A) => List[A]
  type RandSelection[A] = NonEmptyList[A] => RVar[List[A]]
  type RandIndexSelection[A] = (NonEmptyList[A], A) => RVar[List[A]]

  type Crossover[A] = NonEmptyList[Position[A]] => RVar[NonEmptyList[Position[A]]]

  // Find a better home for this - should this even exist? it is unlawful
  implicit object DoubleMonoid extends Monoid[Double] {
    def zero = 0.0
    def append(a: Double, b: => Double) = a + b
  }

  implicit def PositionModule[A](implicit sc: Rng[A]): Module[Position[A], A] =
    new Module[Position[A], A] {
      import spire.implicits._

      implicit def scalar: Rng[A] = sc

      def negate(x: Position[A]) = x.map(scalar.negate)
      def zero = Position(NonEmptyList(scalar.zero), NonEmptyList(spire.math.Interval(0.0, 0.0)))

      def plus(x: Position[A], y: Position[A]) = {
        import scalaz.syntax.align._
        x.align(y)
          .map(
            _.fold(
              s = x => x,
              t = x => x,
              q = scalar.plus(_, _)
            ))
      }

      def timesl(r: A, v: Position[A]): Position[A] =
        v.map(scalar.times(r, _))
    }

  implicit class IntervalOps[A](val interval: Interval[A]) extends AnyVal {
    def ^(n: Int): NonEmptyList[Interval[A]] =
      NonEmptyList.nel(interval, IList.fill(n - 1)(interval))

    private def getValue(b: Bound[A]) =
      ValueBound.unapply(b).getOrElse(sys.error("Empty and Unbounded bounds are not supported"))

    def lowerValue: A = getValue(interval.lowerBound)
    def upperValue: A = getValue(interval.upperBound)
  }

  implicit def intervalEqual[A]: scalaz.Equal[Interval[A]] =
    scalaz.Equal.equalA[Interval[A]]

  /* Refinement definitions */
  def refine[A, B, C](a: A)(f: A Refined B => C)(
      implicit ev: eu.timepit.refined.api.Validate[A, B]): C =
    refineV[B](a) match {
      case Left(error)  => sys.error(error)
      case Right(value) => f(value)
    }

  /** Positive integers are the set of inegers that are greater than 0 */
  def positiveInt[A](n: Int)(f: Int Refined Positive => A): A =
    refine(n)((x: Int Refined Positive) => f(x))

}
