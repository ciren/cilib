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

  /** Positive integers are the set of inegers that are greater than 0 */
  def positiveInt(n: Int): Natural =
    Natural.make(n) match {
      case ZValidation.Failure(_, err) => sys.error(err.toString())
      case ZValidation.Success(_, v)   => v
    }

  implicit final class RichRVarOps[+A](rvar: RVar[A]) {
    def replicateM(n: Int): RVar[List[A]] =
      ForEach[List].forEach(List.fill(n)(rvar))(identity)
  }

}
