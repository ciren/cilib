package cilib

import scalaz._
import Scalaz._

import spire.math._

final case class Entity[S,A](state: S, pos: Position[A])

object Entity {
  // Step to evaluate the Entity
  def eval[S,A:Numeric](f: Position[A] => Position[A])(entity: Entity[S,A]): Step[A,Entity[S,A]] =
    Step.evalF(f(entity.pos)).map(p => Lenses._position.set(p)(entity))

  implicit def entityEqual[S,A:scalaz.Equal]: scalaz.Equal[Entity[S,A]] =
    new scalaz.Equal[Entity[S,A]] {
      import Position._
      def equal(x: Entity[S,A], y: Entity[S,A]): Boolean =
        scalaz.Equal[Position[A]].equal(x.pos, y.pos)
    }

  implicit def entityFitness[S,A]: Fitness[Entity[S,?],A] =
    new Fitness[Entity[S,?],A] {
      def fitness(a: Entity[S,A]) =
        a.pos.objective
    }

}
