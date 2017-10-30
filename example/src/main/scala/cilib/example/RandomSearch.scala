package cilib
package example

import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.std.list._
import scalaz.syntax.traverse._
import spire.implicits._
import spire.math.Interval

import cilib.ga._

// Author: Kyle Erwin
object RandomSearchExample extends SafeApp {

    // Our problem
    ///////////////////////////////////////////
    val sum = Problems.spherical

    // Creating the GA
    ///////////////////////////////////////////
    val randomSelection = (l: List[Individual]) => RVar.sample(2, l).getOrElse(List.empty[Individual])
    val ga = RandomSearch.ga(randomSelection, 1.25)

    // Executing the RandomSearch
    ///////////////////////////////////////////
    val swarm = Position.createCollection[Individual](x => Entity((), x))(Interval(-5.12, 5.12) ^ 30, 20)
    val myGA = Iteration.sync(ga).map(_.suml)
            .flatMapK(r => Step.withCompare(o => RVar.point(r.sortWith((x, y) => Comparison.fittest(x.pos, y.pos).apply(o))) map (_.take(20))))

    override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, myGA, swarm).run(Comparison.dominance(Min))(sum).run(RNG.fromTime).toString)
}