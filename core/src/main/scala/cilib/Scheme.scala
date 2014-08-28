package cilib

import scalaz._
import Scalaz._
import Kleisli.kleisli

final case class Scheme[A](run: List[A] => PSO.Instruction[List[A]]) extends KleisliFunctions {
//final case class Scheme[A](run: Kleisli[Y, List[A], List[A]]) extends KleisliFunctions {
  def >=>(s: Scheme[A]): Scheme[A] =
    Scheme(kleisli(run) >=> kleisli(s.run))

  def repeat(n: Int) = {
    val k = kleisli(run)
    Scheme((1 until n).foldLeft(k)((a, _) => a >=> k))
  }
}

object Scheme {

  // algorithms have the shape: [a] -> a -> Instruction a
  def sync[A](f: List[A] => A => PSO.Instruction[A]) =
    Scheme((l: List[A]) => l traverse (f(l)))
}
