package cilib
package ga

/*
import cilib.Lenses._
import scalaz.std.list._
import scalaz.syntax.traverse._*/

object RandomSearch {

    /*def mutation(xs: List[Individual]): RVar[List[Individual]] = {
        val stdO = 2.0
        val step = 3.1
        val myList = List(12.1, 40.1, 78.35)
        println(myList.map(x => x + (stdO * step)))

        xs.traverse(x => {
            _position.get(x).traverse(z => for {
                za <- Dist.stdUniform.map(_ < p_m)
                zb <- if (za) Dist.stdNormal.flatMap(Dist.gaussian(0,_)).map(_ * z) else RVar.point(z)
            } yield zb).map(a => _position.set(a)(x))
        })
    }*/
/*
    def ga[S](
    parentSelection: List[Individual] => RVar[List[Individual]]
  ): List[Individual] => Individual => Step[Double,List[Individual]] =
    collection => x => for {
      parents   <- Step.pointR(parentSelection(collection))
      mutated   <- Step.pointR[Double,List[Individual]](mutation(parents))
      evaluated <- mutated.traverseU(x => Entity.eval((v: Position[Double]) => v)(x))
    } yield evaluated*/
}
