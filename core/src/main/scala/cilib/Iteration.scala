package cilib

import scalaz._
import scalaz.syntax.traverse._
import scalaz.std.list._

/**
 * An `Iteration` is an atomic action that applies a given "algorithm" for each
 * item within the provided `List[A]`.
 *
 * An `Iteration` may either run synchronously or asynchronously (but not in terms
 * of concurrency - at least, not yet)
 *
 * The `Algorithm` passed to an `Iteration` scheme has the shape:
 * {{{
 *    List[A] => A => Step[List[A]]
 * }}}
 *
 * NB: Should consider trying to define this based on the Free monad
 */
object Iteration {

  type Iteration[M[_],A] = Kleisli[M,A,A]

  // iterations have the shape: [a] -> a -> Step [a]
  def sync_[M[_]: Applicative,A](f: List[A] => A => M[A]): Iteration[M,List[A]] =
    Kleisli.kleisli[M,List[A],List[A]]((l: List[A]) => l traverseU f(l))

  def sync[F[_],A,B](f: List[B] => B => Step[F,A,B]) =
    sync_[Step[F,A,?], B](f)

  def syncS[F[_],A,S,B](f: List[B] => B => StepS[F,A,S,B]) = {
    implicit val S = StateT.stateTMonadState[S, Step[F,A,?]]
    sync_[StepS[F,A,S,?], B](f)
  }

  // This needs to be profiled. The drop is expensive - perhaps a zipper is better
  def async_[M[_]: Monad,A](f: List[A] => A => M[A]) =
    Kleisli.kleisli[M,List[A],List[A]]((l: List[A]) =>
      l.foldLeftM[M, List[A]](List.empty[A]) {
        (a, c) => f(a ++ l.drop(a.length)).apply(c).map(a :+ _)
      })

  def async[F[_],A,B](f: List[B] => B => Step[F,A,B]) =
    async_[Step[F,A,?], B](f)

  def asyncS[F[_],A,S,B](f: List[B] => B => StepS[F,A,S,B]) = {
    implicit val S = StateT.stateTMonadState[S, Step[F,A,?]]
    async_[StepS[F,A,S,?], B](f)
  }

}
