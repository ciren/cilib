package cilib

final case class Entity[S, A](state: S, pos: Position[A])

object Entity {

  implicit def entityEqual[S, A: scalaz.Equal]: scalaz.Equal[Entity[S, A]] =
    new scalaz.Equal[Entity[S, A]] {
      import Position._
      def equal(x: Entity[S, A], y: Entity[S, A]): Boolean =
        scalaz.Equal[Position[A]].equal(x.pos, y.pos)
    }

  implicit def entityFitness[S, A]: Fitness[Entity[S, ?], A, A] =
    new Fitness[Entity[S, ?], A, A] {
      def fitness(a: Entity[S, A]) =
        a.pos.objective
    }
}
