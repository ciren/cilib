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
 *    List[A] => A => Instruction[List[A]]
 * }}}
 */
//final class Iteration[F[_],A,B] private (val run: ReaderT[Instruction[F,A,?], B, B]) {
//final class Iteration[M[_]:Monad,A,B] private (val run: A => M[B]) {
  // def repeat(n: Int) = // Does it not make more sense that this lives on a Scheme? Also, does the type make sense?
  //   (l: List[A]) => Range.inclusive(1, n).toStream.map(_ => run).foldLeftM(l) {
  //     (a, c) => c(a)
  //   }
//}

object Iteration {

  type Iteration[F[_],A,B] = Kleisli[Instruction[F,A,?],B,B]

  // iterations have the shape: [a] -> a -> Instruction [a]
  def sync[F[_]:Traverse,A,B](f: List[B] => B => Instruction[F,A,B]): Iteration[F,A,List[B]] =
    Kleisli.kleisli[Instruction[F,A,?],List[B],List[B]]((l: List[B]) => l traverseU f(l))

/*  def async[F[_],A,B](f: List[B] => B => Instruction[F,A,B]) = // This needs to be profiled. The drop is expensive - perhaps a zipper is better
    new Iteration((l: List[B]) =>
      l.foldLeftM(List.empty[B])((a, c) => f(a ++ l.drop(a.length)).apply(c).map(a :+ _))
    )*/

}
