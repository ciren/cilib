import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric._
import scalaz._
import spire.math.Interval
import spire.math.interval.{ Bound, ValueBound }

package object cilib extends EvalInstances {

  type RVar[+A] = zio.prelude.State[RNG, A]

  //type Eval[A] = RVar[NonEmptyList[A] => Objective[A]]

  // Should expand into a typeclass? Getter?
  type Selection[A]          = NonEmptyList[A] => List[A]
  type IndexSelection[A]     = (NonEmptyList[A], A) => List[A]
  type RandSelection[A]      = NonEmptyList[A] => RVar[List[A]]
  type RandIndexSelection[A] = (NonEmptyList[A], A) => RVar[List[A]]

  type Crossover[A] = NonEmptyList[Position[A]] => RVar[NonEmptyList[Position[A]]]

  // Find a better home for this - should this even exist? it is unlawful
  implicit object DoubleMonoid extends Monoid[Double] {
    def zero                            = 0.0
    def append(a: Double, b: => Double) = a + b
  }

  implicit class IntervalOps[A](private val interval: Interval[A]) extends AnyVal {
    def ^(n: Int): NonEmptyList[Interval[A]] =
      NonEmptyList.nel(interval, IList.fill(n - 1)(interval))

    private def getValue(b: Bound[A]) =
      ValueBound.unapply(b).getOrElse(sys.error("Empty and Unbounded bounds are not supported"))

    def lowerValue: A = getValue(interval.lowerBound)
    def upperValue: A = getValue(interval.upperBound)
  }

  implicit def intervalEqual[A]: scalaz.Equal[Interval[A]] =
    scalaz.Equal.equalA[Interval[A]]

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

  def preludeNel2Scalaz[A](zioNel: zio.prelude.NonEmptyList[A]) =
    scalaz.NonEmptyList.fromSeq(zioNel.head, zioNel.tail)

  def scalazNel2Prelude[A](scalazNel: scalaz.NonEmptyList[A]): zio.prelude.NonEmptyList[A] =
    zio.prelude.NonEmptyList.fromIterable(scalazNel.head, scalazNel.tail.toList)

}
