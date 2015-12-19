package cilib

/**
  A Result is a structure that represents zero, one or many results. The data type
  allows for the abstraction of the final result of an algorithm, where it may be
  a single result (as in the case of a PSO, where each particle generates its own
  replacement) or multiple (as in the GA where two or more individuals may be generated
  from the reproduction process)
  */
/*sealed trait Result[A] {
  def toList =
    this match {
      case Zero()  => List.empty
      case One(a)  => List(a)
      case Many(l) => l
    }

  def sortWith(f: (A,A) => Boolean): Result[A] =
    this match {
      case Zero() => Zero()
      case One(a) => One(a)
      case Many(l) => Many(l.sortWith(f))
    }

  def take(n: Int): Result[A] =
    this match {
      case Zero() => Zero()
      case One(a) => One(a)
      case Many(l) => Many(l take n)
    }
}
case class Zero[A]() extends Result[A] // Is this case useful? Many(List.empty)? mzero?
case class One[A](a: A) extends Result[A]
case class Many[A](a: /*NonEmpty*/List[A]) extends Result[A]

object Result {
  import scalaz.{Functor,Monoid}

  implicit def resultMonoid[A] = new Monoid[Result[A]] {
    def zero = Zero()
    def append(f1: Result[A], f2: => Result[A]) =
      (f1, f2) match {
        case (Zero(),   _)        => f2
        case (One(a),   Zero())   => f1
        case (One(a),   One(b))   => Many(List(a, b))
        case (One(a),   Many(as)) => Many(a :: as)
        case (Many(as), Zero())   => f1
        case (Many(as), One(b))   => Many(as :+ b)
        case (Many(as), Many(bs)) => Many(as ++ bs)
      }
  }

  implicit def resultFunctor = new Functor[Result] {
    def map[A,B](fa: Result[A])(f: A => B): Result[B] =
      fa match {
        case Zero() => Zero()
        case One(a) => One(f(a))
        case Many(l) => Many(l map f)
      }
  }
}
 */
