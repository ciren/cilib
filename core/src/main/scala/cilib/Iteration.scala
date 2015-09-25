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
sealed trait Iteration[M[_],A] {
  def run(l: List[A])(implicit M: Monad[M]): ListT[M, A]// M[List[A]]
}

object Iteration {

  // TODO: Typeclass or ADT?
  final case class SyncIter[M[_],A] private[Iteration] (f: List[A] => A => M[List[A]]) extends Iteration[M,A] {
//    def par = ParSyncIter(f)
    def run(l: List[A])(implicit M: Monad[M]): ListT[M,A] =//M[List[A]] =
      ListT(M.map(l traverseU f(l))(Merge.id.merge))
  }
 /* final case class ASyncIter[M[_],A] private[Iteration] (f: List[A] => A => M[A]) extends Iteration[M,A] {
    def run(l: List[A])(implicit M: Monad[M]) =
      l.foldLeftM[M, List[A]](List.empty[A]) {
        (a, c) => M.map(f(a ++ l.drop(a.length)).apply(c))(a :+ _)
      }
  }
  final case class ParSyncIter[M[_],A] private[Iteration] (f: List[A] => A => M[A]) extends Iteration[M,A] {
    def unpar = SyncIter(f)
    def run(l: List[A])(implicit M: Monad[M]) = {
      import scalaz.concurrent.Task
      val applied: List[Task[M[A]]] = l.map(x => Task.delay { f(l)(x) })
      Nondeterminism[Task].gather(applied).run.sequence
    }
  }*/

  // iterations have the shape: [a] -> a -> Step [a]
  def sync_[M[_]: Applicative,A](f: List[A] => A => M[List[A]]) =
    SyncIter(f)

  def sync[A,B](f: List[B] => B => Step[A,List[B]]) =
    sync_[Step[A,?], B](f)

  def syncS[A,S,B](f: List[B] => B => StepS[A,S,List[B]]) = {
    implicit val S = StateT.stateTMonadState[S, Step[A,?]]
    sync_[StepS[A,S,?], B](f)
  }
/*
  def parSync_[M[_],A](f: List[A] => A => M[A]) =
    ???//ParSyncIter(f)

  // This needs to be profiled. The drop is expensive - perhaps a zipper is better
  def async_[M[_],A](f: List[A] => A => M[A]) =
    ???//ASyncIter(f)

  def async[A,B](f: List[B] => B => Step[A,B]) =
    async_[Step[A,?], B](f)

  def asyncS[A,S,B](f: List[B] => B => StepS[A,S,B]) = {
    implicit val S = StateT.stateTMonadState[S, Step[A,?]]
    async_[StepS[A,S,?], B](f)
  }*/

}
