package cilib
package ga

import cilib.Lenses._

import scalaz.std.list._
import scalaz.syntax.traverse._

// Author: Kyle Erwin
object RandomSearch {

    def ga[S](
        parentSelection: List[Individual] => RVar[List[Individual]],
        distribution: Double => RVar[Double]): List[Individual] => Individual => Step[Double, List[Individual]] =
        GA.ga(1.0, parentSelection, crossover, mutation(distribution))


    // The mutation method is applied to all the parents.
    // The parents are mutated by adding a random value gaussian distribution to
    // point in their position.
    def mutation(distribution: Double => RVar[Double])(parents: List[Individual]): RVar[List[Individual]] = {
        parents.traverse(parent => {
            _position.get(parent).traverse(position => for {
                newPosition <- distribution(position)
            } yield newPosition).map(a => _position.set(a)(parent))
        })
    }
    def crossover(selection: List[Individual]): RVar[List[Individual]] = RVar.point(selection)
}
