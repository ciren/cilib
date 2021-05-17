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
sealed trait Iteration[M[_], A] {
  def run(l: NonEmptyList[A]): M[List[A]]
}

object Iteration {

  // iterations have the shape: [a] -> a -> Step [a]
  //def sync_[M[_]:Applicative,A,B:Monoid](f: List[A] => A => M[B]): Kleisli[M,List[A],List[B]] =
  //Kleisli.kleisli((l: List[A]) => l traverseU f(l))//Functor[M].map(l traverseU f(l))(x => x))

  def sync_[M[+_]: Covariant : IdentityBoth, A, B](f: NonEmptyList[A] => A => M[B]): NonEmptyList[A] => M[NonEmptyList[B]] =
    (l: NonEmptyList[A]) => ForEach[NonEmptyList].forEach(l)(f(l))

  def sync[A, B](f: NonEmptyList[A] => A => Step[B]): NonEmptyList[A] => Step[NonEmptyList[B]] =
    sync_[zio.prelude.fx.ZPure[Nothing, RNG, RNG, Environment, Exception, +*], A, B](f)

  def syncS[S, A, B](f: NonEmptyList[A] => A => StepS[S, B]):  NonEmptyList[A] => StepS[S, NonEmptyList[B]] =
    sync_[StepS[S, +*], A, B](f)

  def async_[M[+_]: Covariant : IdentityBoth : IdentityFlatten, A](f: NonEmptyList[A] => A => M[A]): NonEmptyList[A] => M[NonEmptyList[A]] =
    (l: NonEmptyList[A]) => {
      val intermediate: M[List[A]] =
        ForEach[NonEmptyList].foldLeftM(l)(List.empty[A]) { (a, c) =>
          val p1: List[A] = a
          val p2: List[A] = l.drop(p1.length)
          val nel         = NonEmptyList.fromIterableOption(p1 ++ p2).getOrElse(sys.error("asdasd"))

          f(nel).apply(c).map(x => a ++ List(x))
        }

      intermediate.map(x => NonEmptyList.fromIterableOption(x).getOrElse(sys.error("")))
    }

  def async[A](f: NonEmptyList[A] => A => Step[A]): NonEmptyList[A] => Step[NonEmptyList[A]] =
    async_[Step[+*], A](f)

  def asyncS[S, A](f: NonEmptyList[A] => A => StepS[S, A]): NonEmptyList[A] => StepS[S, NonEmptyList[A]] =
    async_[zio.prelude.fx.ZPure[Nothing, (RNG, S), (RNG, S), Environment, Exception, +*], A](f)

}
