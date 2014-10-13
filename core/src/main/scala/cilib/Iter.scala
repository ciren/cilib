package cilib

import scalaz._
import Scalaz._

final case class SchemeS[S,A](run: List[A] => Instruction[State[S,List[A]]])

final case class Iter[A](run: List[A] => Instruction[List[A]]) {
  def repeat(n: Int) =
    (l: List[A]) => (1 to n).toStream.map(_ => run).foldLeftM(l) {
      (a, c) => c(a)
    }
}

object Iter {

  // algorithms have the shape: [a] -> a -> Instruction a
  def sync[A](f: List[A] => A => Instruction[A]) =
    Iter((l: List[A]) => l traverse f(l))

  def async[A](f: List[A] => A => Instruction[A]) = // This needs to be profiled. The drop is expensive - perhaps a zipper is better
    Iter((l: List[A]) =>
      l.foldLeftM(List.empty[A])((a, c) => f(a ++ l.drop(a.length)).apply(c).map(a :+ _))
    )

  def syncS[S,A](f: List[A] => A => Instruction[State[S,A]]): SchemeS[S,A] =
    SchemeS((l: List[A]) => {
      val s = Applicative[({type l[a] = State[S,a]})#l]
      val i: Instruction[List[State[S,A]]] = l traverse f(l)
      i map (s.sequence(_))
    })
}
