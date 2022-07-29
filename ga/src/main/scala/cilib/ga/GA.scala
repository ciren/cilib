package cilib
package ga

import zio.prelude._

object GA {

  // There is a type error here! The number of parents would need to flow through
  // to the crossover and mutation operators etc.
  def ga[S](
    p_c: Double,
    parentSelection: NonEmptyVector[Individual[S]] => RVar[
      List[Individual[S]]
    ], // the number of parents should already be applied
    crossover: List[Individual[S]] => RVar[List[Individual[S]]],
    mutation: List[Individual[S]] => RVar[List[Individual[S]]]
  ): NonEmptyVector[Individual[S]] => Individual[S] => Step[List[Individual[S]]] =
    collection =>
      _ =>
        for {
          parents   <- Step.liftR(parentSelection(collection))
          r         <- Step.liftR(Dist.stdUniform.map(_ < p_c))
          crossed   <- if (r) Step.liftR[List[Individual[S]]](crossover(parents))
                       else Step.pure[List[Individual[S]]](parents)
          mutated   <- Step.liftR[List[Individual[S]]](mutation(crossed))
          evaluated <- ForEach[List].forEach(mutated)(x => Step.eval(x)(identity))
        } yield evaluated

  def randomSearch[S](
    parentSelection: NonEmptyVector[Individual[S]] => RVar[List[Individual[S]]],
    distribution: Double => RVar[Double]
  ): NonEmptyVector[Individual[S]] => Individual[S] => Step[List[Individual[S]]] = {
    // The mutation method is applied to all the parents.
    // The parents are mutated by adding a random value gaussian distribution to
    // point in their position.
    def mutation(distribution: Double => RVar[Double])(parents: List[Individual[S]]): RVar[List[Individual[S]]] =
      parents.forEach { parent =>
        val newPos: RVar[NonEmptyVector[Double]] = parent.pos.pos.forEach(distribution)

        newPos.map { p =>
          parent.copy(pos = Lenses._vector[Double].set(p)(parent.pos).toOption.get)
        }
      }

    GA.ga(1.0, parentSelection, RVar.pure(_), mutation(distribution))
  }
}
