import scala.language.implicitConversions

import zio.prelude.newtypes.Natural
import zio.prelude.{ ForEach, ZValidation }

package object cilib {

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

  type NonEmptyVector[+A] = zio.NonEmptyChunk[A]
  val NonEmptyVector = zio.NonEmptyChunk

  /*
   * Implicits for the conversion of a NenEmptyVector to the needed Input type.
   */
  implicit val nonEmptyListInput: Input[zio.prelude.NonEmptyList] =
    new Input[zio.prelude.NonEmptyList] {
      def toInput[A](a: NonEmptyVector[A]): zio.prelude.NonEmptyList[A] =
        // Safe as there _will always be_ at least 1 element
        zio.prelude.NonEmptyList.fromIterableOption(a.toChunk.toList).get
    }

  implicit val nonEmptyVectorInput: Input[NonEmptyVector] = new Input[NonEmptyVector] {
    def toInput[A](a: NonEmptyVector[A]): NonEmptyVector[A] = a
  }

  implicit val pairInput: Input[[x] =>> (x, x)] =
    new Input[[x] =>> (x, x)] {
      def toInput[A](a: NonEmptyVector[A]): (A, A) = {
        val grouped = a.toChunk.toList.grouped(2)
        if (grouped.hasNext) {
          grouped.next().toList match {
            case a :: b :: _ => (a, b)
            case _           => sys.error("error producing a pair")
          }
        } else sys.error("Too few elements provided. Need at least 2.")
      }
    }

  type Type[F[_]] <: (Any { type T })

  implicit def wrap[F[_], A](value: F[A]): Type[F] =
    value.asInstanceOf[Type[F]]

  implicit def unwrap[F[_]](value: Type[F]): F[value.T] =
    value.asInstanceOf[F[value.T]]

}
