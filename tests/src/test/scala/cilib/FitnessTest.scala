package cilib

import scalaz._
import Scalaz._

import org.scalacheck._
import org.scalacheck.Prop._

object FitnessTest extends Properties("Fitness") {

  implicit object IntFitness extends Fitness[Int] {
    def fitness(a: Int) = Some(Valid(a.toDouble))
  }

  property("Minimization compare") = forAll { (x: Int, y: Int) =>
    Fitness.compare(x, y).run(Min) === (x min y)
  }

  property("Maximization compare") = forAll { (x: Int, y: Int) =>
    Fitness.compare(x, y).run(Max) === (x max y)
  }

}
