package cilib

import scalaz._
import scalaz.syntax.comonad._
import scalaz.syntax.traverse._
import scalaz.syntax.std.list._
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
 *    NonEmptyList[B] => B => Step[A,NonEmptyList[B]]
 * }}}
 *
 * NB: Should consider trying to define this based on the Free monad?
 */
object Iteration {

/*<<<<<<< HEAD
  // iterations have the shape: [a] -> a -> Step [a]
  def sync[/*F[_]:Traverse,*/A,B](f: NonEmptyList[B] => B => Step[A,B]): Iteration[A,NonEmptyList[B]] =
    Kleisli.kleisli[Step[A,?],NonEmptyList[B],NonEmptyList[B]]((l: NonEmptyList[B]) => l traverseU f(l))

  def async[/*F[_],*/A,B](f: NonEmptyList[B] => B => Step[A,B]): Iteration[A,NonEmptyList[B]] =
    Kleisli.kleisli[Step[A,?],NonEmptyList[B],NonEmptyList[B]]((l: NonEmptyList[B]) => {
      l.tails.foldLeftM[Step[A,?], List[B]](List.empty[B]) {
        (a, c) => f(a <::: c).apply(c.copoint).map(a :+ _)
      }.map(_.toNel.getOrElse(sys.error("Not sure how to handle this... suggestions?")))
    })
=======*/
  type Iteration[M[_],A] = Kleisli[M,A,A]

  // iterations have the shape: [a] -> a -> Step [a]
  def sync_[M[_]: Applicative,A](f: List[A] => A => M[A]): Iteration[M,List[A]] =
    Kleisli.kleisli[M,List[A],List[A]]((l: List[A]) => l traverseU f(l))

  def sync[A,B](f: List[B] => B => Step[A,B]) =
    sync_[Step[A,?], B](f)

  def syncS[A,S,B](f: List[B] => B => StepS[A,S,B]) = {
    implicit val S = StateT.stateTMonadState[S, Step[A,?]]
    sync_[StepS[A,S,?], B](f)
  }

  // This needs to be profiled. The drop is expensive - perhaps a zipper is better
  def async_[M[_],A](f: List[A] => A => M[A])(implicit M: Monad[M]) =
    Kleisli.kleisli[M,List[A],List[A]]((l: List[A]) =>
      l.foldLeftM[M, List[A]](List.empty[A]) {
        (a, c) => M.map(f(a ++ l.drop(a.length)).apply(c))(a :+ _)
      })

  def async[A,B](f: List[B] => B => Step[A,B]) =
    async_[Step[A,?], B](f)

  def asyncS[A,S,B](f: List[B] => B => StepS[A,S,B]) = {
    implicit val S = StateT.stateTMonadState[S, Step[A,?]]
    async_[StepS[A,S,?], B](f)
  }

}
