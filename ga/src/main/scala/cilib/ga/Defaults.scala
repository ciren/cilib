package cilib
package ga

import scalaz._
import scalaz.std.list._
import scalaz.syntax.traverse._


object GADefaults {

    // Author: Kyle Erwin
    // The mutation method is applied to all the parents.
    // The parents are mutated by adding a random value gaussian distribution to
    // point in their position.
    def randomSearchGA[S](
        parentSelection: NonEmptyList[Individual[S]] => RVar[List[Individual[S]]],
        distribution: Double => RVar[Double]
        ): NonEmptyList[Individual[S]] => Individual[S] => Step[Double,List[Individual[S]]] = {

            def mutation(distribution: Double => RVar[Double])(parents: List[Individual[S]]): RVar[List[Individual[S]]] = 
                parents.traverse(parent =>
                    Lenses._position.modifyF((p: Position[Double]) => p.traverse(distribution))(parent))


            collection => x => for {
                parents   <- Step.pointR(parentSelection(collection))
                mutated   <- Step.pointR[Double,List[Individual[S]]](mutation(distribution)(parents))
                evaluated <- mutated.traverseU(x => Step.eval((v: Position[Double]) => v)(x))
            } yield evaluated
        }
}
