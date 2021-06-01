package cilib

import zio.prelude._

final case class Entity[S, A](state: S, pos: Position[A])

object Entity {

  implicit def entityEqual[S, A: zio.prelude.Equal]: zio.prelude.Equal[Entity[S, A]] =
    zio.prelude.Equal.make((x, y) => x.pos === y.pos)

  implicit def entityFitness[S, A]: Fitness[Entity[S, *], A, A] =
    new Fitness[Entity[S, *], A, A] {
      def fitness(a: Entity[S, A]) =
        a.pos.objective
    }
}
