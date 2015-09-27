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

  //type Iteration[M[_],A] = Kleisli[M,List[A],A]

  // iterations have the shape: [a] -> a -> Step [a]
  def sync_[M[_]: Applicative,A](f: List[A] => A => M[Result[A]]): Kleisli[M,List[A],Result[A]] =
    Kleisli.kleisli((l: List[A]) => Functor[M].map(l traverseU f(l))(_.suml))

  def sync[F[_],A,B](f: List[B] => B => Step[F,A,Result[B]]) =
    sync_[Step[F,A,?], B](f)

  def syncS[F[_],A,S,B](f: List[B] => B => StepS[F,A,S,Result[B]]) = {
    implicit val S = StateT.stateTMonadState[S, Step[F,A,?]]
    sync_[StepS[F,A,S,?], B](f)
  }

  import scalaz.syntax.monoid._

  // This needs to be profiled. The drop is expensive - perhaps a zipper is better
  def async_[M[_]: Monad,A](f: List[A] => A => M[Result[A]]): Kleisli[M,List[A],Result[A]] =
    Kleisli.kleisli((l: List[A]) =>
      l.foldLeftM[M, Result[A]](Zero()) { (a, c) =>
        val p1: List[A] = a.toList
        val p2: List[A] = l.drop(p1.length)
        f(p1 ++ p2).apply(c).map(a |+| _)
      })

   def async[F[_],A,B](f: List[B] => B => Step[F,A,Result[B]]) =
     async_[Step[F,A,?], B](f)

   def asyncS[F[_],A,S,B](f: List[B] => B => StepS[F,A,S,Result[B]]) = {
     implicit val S = StateT.stateTMonadState[S, Step[F,A,?]]
     async_[StepS[F,A,S,?], B](f)
   }

}
