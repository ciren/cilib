package cilib

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
final class Iteration[A] private (val run: List[A] => Instruction[List[A]]) {
  // def repeat(n: Int) = // Does it not make more sense that this lives on a Scheme? Also, does the type make sense?
  //   (l: List[A]) => Range.inclusive(1, n).toStream.map(_ => run).foldLeftM(l) {
  //     (a, c) => c(a)
  //   }
}

object Iteration {

  // iterations have the shape: [a] -> a -> Instruction a
  def sync[A](f: List[A] => A => Instruction[A]) =
    new Iteration((l: List[A]) => l traverse f(l))

  def async[A](f: List[A] => A => Instruction[A]) = // This needs to be profiled. The drop is expensive - perhaps a zipper is better
    new Iteration((l: List[A]) =>
      l.foldLeftM(List.empty[A])((a, c) => f(a ++ l.drop(a.length)).apply(c).map(a :+ _))
    )

}
