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
  ): NonEmptyList[Individual[S]] => Individual[S] => Step[Double,List[Individual[S]]] =
    collection => x => for {
      parents   <- Step.pointR(parentSelection(collection))
      r         <- Step.pointR(Dist.stdUniform.map(_ < p_c))
      crossed   <- if (r) Step.pointR[Double,List[Individual[S]]](crossover(parents))
                   else Step.point[Double,List[Individual[S]]](parents)
      mutated   <- Step.pointR[Double,List[Individual[S]]](mutation(crossed))
      evaluated <- mutated.traverseU(x => Entity.eval((v: Position[Double]) => v)(x))
    } yield evaluated
}
