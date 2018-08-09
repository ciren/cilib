package cilib

import scalaz._
import Scalaz._

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
  def run(l: NonEmptyList[A])(implicit M: Monad[M]): ListT[M, A]
}

object Iteration {

  // iterations have the shape: [a] -> a -> Step [a]
  //def sync_[M[_]:Applicative,A,B:Monoid](f: List[A] => A => M[B]): Kleisli[M,List[A],List[B]] =
  //Kleisli.kleisli((l: List[A]) => l traverseU f(l))//Functor[M].map(l traverseU f(l))(x => x))

  def sync_[M[_]: Applicative, A, B](f: NonEmptyList[A] => A => M[B])
    : Kleisli[M, NonEmptyList[A], NonEmptyList[B]] = //List[A] => M[List[B]] =
//    (l: List[A]) => Functor[M].map(l traverseU f(l))(_.suml)
    Kleisli.kleisli((l: NonEmptyList[A]) => l.traverse(f(l)))

  def sync[A, B, C](f: NonEmptyList[B] => B => Step[A, C])
    : Kleisli[Step[A, ?], NonEmptyList[B], NonEmptyList[C]] =
    sync_[Step[A, ?], B, C](f)

  def syncS[A, S, B, C](f: NonEmptyList[B] => B => StepS[A, S, C])
    : Kleisli[StepS[A, S, ?], NonEmptyList[B], NonEmptyList[C]] =
    sync_[StepS[A, S, ?], B, C](f)

  def async_[M[_]: Monad, A](
      f: NonEmptyList[A] => A => M[A]): Kleisli[M, NonEmptyList[A], NonEmptyList[A]] =
    Kleisli.kleisli((l: NonEmptyList[A]) => {
      val list = l.toList
      val intermediate: M[List[A]] = list.foldLeftM[M, List[A]](List.empty) { (a, c) =>
        val p1: List[A] = a
        val p2: List[A] = list.drop(p1.length)
        val nel = (p1 ++ p2).toNel.getOrElse(sys.error("asdasd"))
        Functor[M].map(f(nel).apply(c))(x => a <+> List(x))
      }

      intermediate.map(_.toNel.getOrElse(sys.error("")))
    })

  def async[A, B](f: NonEmptyList[B] => B => Step[A, B])
    : Kleisli[Step[A, ?], NonEmptyList[B], NonEmptyList[B]] =
    async_[Step[A, ?], B](f)

  def asyncS[A, S, B](f: NonEmptyList[B] => B => StepS[A, S, B])
    : Kleisli[StepS[A, S, ?], NonEmptyList[B], NonEmptyList[B]] =
    async_[StepS[A, S, ?], B](f)

}
