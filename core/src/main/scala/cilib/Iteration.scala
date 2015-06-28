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

  // iterations have the shape: [a] -> a -> Step [a]
  def sync[/*F[_]:Traverse,*/A,B](f: NonEmptyList[B] => B => Step[A,B]): Iteration[A,NonEmptyList[B]] =
    Kleisli.kleisli[Step[A,?],NonEmptyList[B],NonEmptyList[B]]((l: NonEmptyList[B]) => l traverseU f(l))

  def async[/*F[_],*/A,B](f: NonEmptyList[B] => B => Step[A,B]): Iteration[A,NonEmptyList[B]] =
    Kleisli.kleisli[Step[A,?],NonEmptyList[B],NonEmptyList[B]]((l: NonEmptyList[B]) => {
      l.tails.foldLeftM[Step[A,?], List[B]](List.empty[B]) {
        (a, c) => f(a <::: c).apply(c.copoint).map(a :+ _)
      }.map(_.toNel.getOrElse(sys.error("Not sure how to handle this... suggestions?")))
    })
}
