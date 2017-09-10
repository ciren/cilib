package cilib

import scalaz._
import scalaz.syntax.traverse._
import scalaz.syntax.monadPlus._
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
  def run(l: List[A])(implicit M: Monad[M]): ListT[M, A]
}

object Iteration {

  // iterations have the shape: [a] -> a -> Step [a]
  //def sync_[M[_]:Applicative,A,B:Monoid](f: List[A] => A => M[B]): Kleisli[M,List[A],List[B]] =
  //Kleisli.kleisli((l: List[A]) => l traverseU f(l))//Functor[M].map(l traverseU f(l))(x => x))

  def sync_[M[_]:Applicative,A,B](f: List[A] => A => M[B]): Kleisli[M,List[A],List[B]] = //List[A] => M[List[B]] =
//    (l: List[A]) => Functor[M].map(l traverseU f(l))(_.suml)
    Kleisli.kleisli((l: List[A]) => l traverse f(l))

  def sync[A,B,C](f: List[B] => B => Step[A,C]) =
    sync_[Step[A,?],B,C](f)

  def syncS[A,S,B,C](f: List[B] => B => StepS[A,S,C]) = {
    sync_[StepS[A,S,?], B,C](f)
  }

  def async_[M[_]: Monad,A](f: List[A] => A => M[A]): Kleisli[M,List[A],List[A]] =
    Kleisli.kleisli((l: List[A]) =>
      l.foldLeftM[M, List[A]](List.empty) { (a, c) =>
        val p1: List[A] = a.toList
        val p2: List[A] = l.drop(p1.length)
        Functor[M].map(f(p1 ++ p2).apply(c))(x => a <+> List(x))
      })

   def async[A,B](f: List[B] => B => Step[A,B]) =
     async_[Step[A,?], B](f)

   def asyncS[A,S,B](f: List[B] => B => StepS[A,S,B]) = {
     async_[StepS[A,S,?], B](f)
   }

}
