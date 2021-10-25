import spire.math.Interval
import spire.math.interval.{ Bound, ValueBound }
import zio.prelude.newtypes.Natural
import zio.prelude.{ ForEach, ZValidation }

package object cilib extends EvalInstances {

  type RVar[+A]     = zio.prelude.State[RNG, A] /// zio.prelude.fx.ZPure[Nothing, S, S, Any, Nothing, A]
  type Step[+A]     = zio.prelude.fx.ZPure[Nothing, RNG, RNG, (cilib.Comparison, Eval[NonEmptyVector]), Exception, A]
  type StepS[S, +A] =
    zio.prelude.fx.ZPure[Nothing, (RNG, S), (RNG, S), (cilib.Comparison, Eval[NonEmptyVector]), Exception, A]

  // Should expand into a typeclass? Getter?
  type Selection[A]          = NonEmptyVector[A] => List[A]
  type IndexSelection[A]     = (NonEmptyVector[A], A) => List[A]
  type RandSelection[A]      = NonEmptyVector[A] => RVar[List[A]]
  type RandIndexSelection[A] = (NonEmptyVector[A], A) => RVar[List[A]]

  implicit class IntervalOps[A](private val interval: Interval[A]) extends AnyVal {
    def ^(n: Int): NonEmptyVector[Interval[A]] =
      NonEmptyVector.fromIterable(interval, List.fill(n - 1)(interval))

    private def getValue(b: Bound[A]) =
      ValueBound.unapply(b).getOrElse(sys.error("Empty and Unbounded bounds are not supported"))

    def lowerValue: A = getValue(interval.lowerBound)
    def upperValue: A = getValue(interval.upperBound)
  }

  implicit def intervalEqualZio[A]: zio.prelude.Equal[Interval[A]] =
    zio.prelude.Equal.make((l, r) => l == r)


  /** Positive integers are the set of inegers that are greater than 0 */
  def positiveInt(n: Int) =
    Natural.make(n) match {
      case ZValidation.Failure(_, err) => sys.error(err.toString())
      case ZValidation.Success(_, v)   => v
    }
 
  implicit final class RichRVarOps[+A](rvar: RVar[A]) {
    def replicateM(n: Int): RVar[List[A]] =
      ForEach[List].forEach(List.fill(n)(rvar))(identity)
  }

}
