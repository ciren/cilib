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
    def mutation(distribution: Double => RVar[Double])(parents: List[Individual[S]]): RVar[List[Individual[S]]] = 
        parents.traverse(parent =>
            Lenses._position.modifyF((p: Position[Double]) => p.traverse(distribution))(parent))
            
    def crossover(selection: List[Individual]): RVar[List[Individual]] = RVar.point(selection)
}
