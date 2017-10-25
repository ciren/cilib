package cilib
package ga


import cilib.Lenses._
import scalaz.std.list._
import scalaz.syntax.traverse._

object RandomSearch {

  def ga[S](
             parentSelection: List[Individual] => RVar[List[Individual]]): List[Individual] => Individual => Step[Double, List[Individual]] =
    collection => x => for {
      parents <- Step.pointR(parentSelection(collection))
      mutated <- Step.pointR[Double, List[Individual]](mutation(parents))
      evaluated <- mutated.traverseU(x => Entity.eval((v: Position[Double]) => v)(x))
    } yield evaluated

  def mutation(parents: List[Individual]): RVar[List[Individual]] = {
    parents.traverse(parent => {
      _position.get(parent).traverse(positions => for {
        newPositions <- Dist.stdNormal.flatMap(Dist.gaussian(0, _)).map(_ + positions)
      } yield newPositions).map(a => _position.set(a)(parent))
    })
  }
}
