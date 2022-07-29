package cilib

import zio.prelude._

/**
 * An `Iteration` is an atomic action that applies a given "algorithm" for each
 * item within the provided `List[A]`.
 *
 * An `Iteration` may either run synchronously or asynchronously (but not in terms
 * of concurrency - at least, not yet)
 *
 * The `Algorithm` passed to an `Iteration` scheme has the shape:
 * {{{
 *    NonEmptyList[B] => B => Step[A,NonEmptyList[B]]
 * }}}
 *
 * NB: Should consider trying to define this based on the Free monad?
 */
object Iteration {

  def sync_[M[+_]: Covariant: IdentityBoth, A, B](
    f: NonEmptyVector[A] => A => M[B]
  ): NonEmptyVector[A] => M[NonEmptyVector[B]] =
    (l: NonEmptyVector[A]) => ForEach[NonEmptyVector].forEach(l)(f(l))

  def sync[A, B](f: NonEmptyVector[A] => A => Step[B]): NonEmptyVector[A] => Step[NonEmptyVector[B]] =
    sync_[zio.prelude.fx.ZPure[Nothing, RNG, RNG, (cilib.Comparison, Eval[NonEmptyVector]), Exception, +*], A, B](f)

  def syncS[S, A, B](f: NonEmptyVector[A] => A => StepS[S, B]): NonEmptyVector[A] => StepS[S, NonEmptyVector[B]] =
    sync_[StepS[S, +*], A, B](f)

  def async_[M[+_]: Covariant: IdentityBoth: IdentityFlatten, A](
    f: NonEmptyVector[A] => A => M[A]
  ): NonEmptyVector[A] => M[NonEmptyVector[A]] =
    (l: NonEmptyVector[A]) => {
      val intermediate: M[List[A]] =
        ForEach[NonEmptyVector].foldLeftM(l)(List.empty[A]) { (a, c) =>
          val p1: List[A] = a
          val p2: List[A] = l.toChunk.drop(p1.length).toList
          val nel         = NonEmptyVector.fromIterableOption(p1 ++ p2).getOrElse(sys.error("asdasd"))

          f(nel).apply(c).map(x => a ++ List(x))
        }

      intermediate.map(x => NonEmptyVector.fromIterableOption(x).getOrElse(sys.error("")))
    }

  def async[A](f: NonEmptyVector[A] => A => Step[A]): NonEmptyVector[A] => Step[NonEmptyVector[A]] =
    async_[Step[+*], A](f)

  def asyncS[S, A](f: NonEmptyVector[A] => A => StepS[S, A]): NonEmptyVector[A] => StepS[S, NonEmptyVector[A]] =
    async_[
      zio.prelude.fx.ZPure[Nothing, (RNG, S), (RNG, S), (cilib.Comparison, Eval[NonEmptyVector]), Exception, +*],
      A
    ](f)

}
