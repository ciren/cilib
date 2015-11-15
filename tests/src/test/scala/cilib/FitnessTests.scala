package cilib

import scalaz._
import Scalaz._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

object FitnessTest extends Properties("Fitness") {

  case class Id[A](val underlying: A) extends AnyVal {
    def min(other: Id[A])(implicit A:Numeric[A]) =
      Id(A.min(underlying, other.underlying))

    def max(other: Id[A])(implicit A:Numeric[A]) =
      Id(A.max(underlying, other.underlying))

    def ===(other: Id[A])(implicit A:scalaz.Equal[A]) =
      A.equal(underlying, other.underlying)
  }

  implicit def arbId[A:Arbitrary]: Arbitrary[Id[A]] =
    Arbitrary { arbitrary[A].map(Id(_)) }

  implicit val intFitness = new Fitness[Id, Int] {
    def fitness(a: Id[Int]) =
      Maybe.Just(Single(Feasible(a.underlying.toDouble), List.empty))
    //    def quality(a: Int) = (Maybe.just(Feasible(a.toDouble)), ViolationCount.zero)
  }

  property("Minimization quality compare") =
    forAll { (x: Id[Int], y: Id[Int]) =>
      Comparison.quality(Min)(x, y) === (x min y)
    }

  property("Maximization quality compare") =
    forAll { (x: Id[Int], y: Id[Int]) =>
      Comparison.quality(Max)(x, y) === (x max y)
    }

}
