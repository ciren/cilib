package cilib
package ga

import scalaz._
import scalaz.std.list._
import scalaz.syntax.traverse._

object GA {

  // There is a type error here! The number of parents would need to flow through
  // to the crossover and mutation operators etc.
  def ga[S](
      p_c: Double,
      parentSelection: NonEmptyList[Individual[S]] => RVar[List[Individual[S]]], // the number of parents should already be applied
      crossover: List[Individual[S]] => RVar[List[Individual[S]]],
      mutation: List[Individual[S]] => RVar[List[Individual[S]]]
  ): NonEmptyList[Individual[S]] => Individual[S] => Step[Double, List[Individual[S]]] =
    collection =>
      x =>
        for {
          parents <- Step.liftR(parentSelection(collection))
          r <- Step.liftR(Dist.stdUniform.map(_ < p_c))
          crossed <- if (r) Step.liftR[Double, List[Individual[S]]](crossover(parents))
          else Step.pure[Double, List[Individual[S]]](parents)
          mutated <- Step.liftR[Double, List[Individual[S]]](mutation(crossed))
          evaluated <- mutated.traverse(x => Step.eval((v: Position[Double]) => v)(x))
        } yield evaluated

  def randomSearch[S](
      parentSelection: NonEmptyList[Individual[S]] => RVar[List[Individual[S]]],
      distribution: Double => RVar[Double]
  ): NonEmptyList[Individual[S]] => Individual[S] => Step[Double, List[Individual[S]]] = {
    // The mutation method is applied to all the parents.
    // The parents are mutated by adding a random value gaussian distribution to
    // point in their position.
    def mutation(distribution: Double => RVar[Double])(
        parents: List[Individual[S]]): RVar[List[Individual[S]]] =
      parents.traverse(parent =>
        Lenses._position.modifyF((p: Position[Double]) => p.traverse(distribution))(parent))

    GA.ga(1.0, parentSelection, RVar.pure(_), mutation(distribution))
  }
}
