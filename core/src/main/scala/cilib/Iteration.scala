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

  type Iteration[F[_],A,B] = Kleisli[Step[F,A,?],B,B]

  // iterations have the shape: [a] -> a -> Step [a]
  def sync[F[_]:Traverse,A,B](f: List[B] => B => Step[F,A,B]): Iteration[F,A,List[B]] =
    Kleisli.kleisli[Step[F,A,?],List[B],List[B]]((l: List[B]) => l traverseU f(l))

  // This needs to be profiled. The drop is expensive - perhaps a zipper is better
  def async[F[_],A,B](f: List[B] => B => Step[F,A,B]) =
    Kleisli.kleisli[Step[F,A,?],List[B],List[B]]((l: List[B]) =>
      l.foldLeftM[Step[F,A,?], List[B]](List.empty[B]) {
        (a, c) => f(a ++ l.drop(a.length)).apply(c).map(a :+ _)
      })

}
