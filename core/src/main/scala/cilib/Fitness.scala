package cilib

import scalaz._

sealed trait Fit
final case object Invalid extends Fit
final case class Penalty(v: Double, p: Double) extends Fit
final case class Valid(v: Double) extends Fit

@annotation.implicitNotFound("Cannot find instance of type class Fitness[${A}]")
trait Fitness[A] {
  def fitness(a: A): Fit
}

object Fitness {

  implicit object EntityFitness extends Fitness[Entity] {
    def fitness(a: Entity) = a.f
  }

}
