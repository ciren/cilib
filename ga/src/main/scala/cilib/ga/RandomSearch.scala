package cilib
package ga

import scalaz.std.list._
import scalaz.syntax.traverse._

object RandomSearch {

  // There is a type error here! The number of parents would need to flow through
  // to the crossover and mutation operators etc.
  def ga[S](
    p_c: Double,
    parentSelection: List[Individual] => RVar[List[Individual]], // the number of parents should already be applied
    crossover: List[Individual] => RVar[List[Individual]],
    mutation: List[Individual] => RVar[List[Individual]]
  ): List[Individual] => Individual => Step[Double,List[Individual]] =
    collection => x => for {
      parents   <- Step.pointR(parentSelection(collection))
      r         <- Step.pointR(Dist.stdUniform.map(_ < p_c))
      crossed   <- if (r) Step.pointR[Double,List[Individual]](crossover(parents))
                   else Step.point[Double,List[Individual]](parents)
      mutated   <- Step.pointR[Double,List[Individual]](mutation(crossed))
      evaluated <- mutated.traverseU(x => Entity.eval((v: Position[Double]) => v)(x))
    } yield evaluated
}
