package cilib

import scalaz.NonEmptyList
import scalaz.syntax.bind._
import scalaz.std.list._
import scalaz.syntax.traverse._

object GA {

  type Individual = Entity[Unit, List, Double]

  // There is a type error here! The number of parents somehow would need to flow through
  // to the crossover and mutation operators etc.
  def ga[S](
    p_c: Double,
    parentSelection: List[Individual] => RVar[List[Individual]], // the number of parents should already be applied
    crossover: List[Individual] => RVar[List[Individual]],
    mutation: List[Individual] => RVar[List[Individual]]
  ): List[Individual] => Individual => Step[List,Double,Result[Individual]] =
    collection => x => for {
      parents   <- Step.pointR(parentSelection(collection))
      r         <- Step.pointR(Dist.stdUniform.map(_ < p_c))
      crossed   <- if (r) Step.pointR[List,Double,List[Individual]](crossover(parents))
                   else Step.point[List,Double,List[Individual]](parents)
      mutated   <- Step.pointR[List,Double,List[Individual]](mutation(crossed))
      evaluated <- mutated.traverseU(x => Entity.eval((v: Position[List,Double]) => v)(x))
    } yield Many(evaluated)
}
