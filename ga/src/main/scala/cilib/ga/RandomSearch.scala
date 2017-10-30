package cilib
package ga

import cilib.Lenses._
import scalaz.std.list._
import scalaz.syntax.traverse._

// Author: Kyle Erwin
object RandomSearch {

    def ga[S](parentSelection: List[Individual] => RVar[List[Individual]], deviation: Double): List[Individual] => Individual => Step[Double, List[Individual]] =
        collection => x => for {
            parents <- Step.pointR(parentSelection(collection))
            mutated <- Step.pointR[Double, List[Individual]](mutation(parents, deviation))
            evaluated <- mutated.traverseU(x => Entity.eval((v: Position[Double]) => v)(x))
        } yield evaluated

    // The mutation method is applied to all the parents.
    // The parents are mutated by adding a random value gaussian distribution to
    // point in their position.
    def mutation(parents: List[Individual], deviation: Double): RVar[List[Individual]] = {
        parents.traverse(parent => {
            _position.get(parent).traverse(positions => for {
                newPositions <- Dist.stdNormal.flatMap(x => Dist.gaussian(0, deviation)).map(_ + positions)
            } yield newPositions).map(a => _position.set(a)(parent))
        })
    }
}
