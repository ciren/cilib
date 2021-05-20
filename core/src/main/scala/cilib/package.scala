import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._

import spire.math.Interval
import spire.math.interval.{ Bound, ValueBound }

import zio.prelude.NonEmptyList

package object cilib extends EvalInstances {

  type RVar[+A]     = zio.prelude.State[RNG, A] /// zio.prelude.fx.ZPure[Nothing, S, S, Any, Nothing, A]
  type Step[+A]     = zio.prelude.fx.ZPure[Nothing, RNG, RNG, Environment, Exception, A]
  type StepS[S, +A] = zio.prelude.fx.ZPure[Nothing, (RNG, S), (RNG, S), Environment, Exception, A]

  //type Eval[A] = RVar[NonEmptyList[A] => Objective[A]]

  // Should expand into a typeclass? Getter?
  type Selection[A]          = zio.prelude.NonEmptyList[A] => List[A]
  type IndexSelection[A]     = (zio.prelude.NonEmptyList[A], A) => List[A]
  type RandSelection[A]      = zio.prelude.NonEmptyList[A] => RVar[List[A]]
  type RandIndexSelection[A] = (zio.prelude.NonEmptyList[A], A) => RVar[List[A]]

  type Crossover[A] = zio.prelude.NonEmptyList[Position[A]] => RVar[zio.prelude.NonEmptyList[Position[A]]]

  implicit class IntervalOps[A](private val interval: Interval[A]) extends AnyVal {
    def ^(n: Int): NonEmptyList[Interval[A]] =
      NonEmptyList.fromIterable(interval, List.fill(n - 1)(interval))

    private def getValue(b: Bound[A]) =
      ValueBound.unapply(b).getOrElse(sys.error("Empty and Unbounded bounds are not supported"))

    def lowerValue: A = getValue(interval.lowerBound)
    def upperValue: A = getValue(interval.upperBound)
  }

  implicit def intervalEqualZio[A]: zio.prelude.Equal[Interval[A]] =
    zio.prelude.Equal.make((l, r) => l == r)

  /* Refinement definitions */
  def refine[A, B, C](a: A)(f: A Refined B => C)(implicit ev: eu.timepit.refined.api.Validate[A, B]): C =
    refineV[B](a) match {
      case Left(error)  => sys.error(error)
      case Right(value) => f(value)
    }

  /** Positive integers are the set of inegers that are greater than 0 */
  def positiveInt[A](n: Int)(f: Int Refined Positive => A): A =
    refine(n)((x: Int Refined Positive) => f(x))


  implicit final class RichRVarOps[+A](rvar: RVar[A]) {
    import zio.prelude._

    def replicateM(n: Int): RVar[List[A]] =
      ForEach[List].forEach(List.fill(n)(rvar))(identity)
  }

}
