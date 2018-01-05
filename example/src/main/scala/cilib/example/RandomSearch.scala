package cilib
package example

import scalaz._
import Scalaz._
import scalaz.effect._
import scalaz.effect.IO.putStrLn

import eu.timepit.refined.auto._

import spire.implicits._
import spire.math.Interval

import cilib.ga._

// Author: Kyle Erwin
object RandomSearchExample extends SafeApp {

    type Ind = Individual[Unit]

    // Our problem
    ///////////////////////////////////////////
    val env = Environment(
        cmp = Comparison.dominance(Min),
        eval = Eval.unconstrained(cilib.benchmarks.Benchmarks.spherical[NonEmptyList, Double]).eval
        bounds = Interval(-5.12,5.12)^3
    )

    // Creating the GA
    ///////////////////////////////////////////
    val randomSelection: NonEmptyList[Ind] => RVar[List[Ind]] =
        (l: NonEmptyList[Ind]) => RVar.sample(2, l).getOrElse(List.empty[Ind])
    val distribution = (position: Double) => Dist.gaussian(0, 1.25).map(_ + position)

    val ga = GADefaults.randomSearchGA(randomSelection, distribution)
    // Executing the RandomSearch
    ///////////////////////////////////////////
    val swarm = Position.createCollection[Ind](x => Entity((), x))(env.bounds, 10)
    val myGA: Kleisli[Step[Double,?],NonEmptyList[Ind],NonEmptyList[Ind]] =
        Iteration.sync(ga).map(_.suml)
        .flatMapK(r => Step.withCompare(o =>
            r.sortWith((x,y) => Comparison.fittest(x.pos,y.pos).apply(o)))
            .map(_.take(20).toNel.getOrElse(sys.error("Error"))))


    override val runc: IO[Unit] =
        putStrLn(Runner.repeat(1000, myGA, swarm).run(env).eval(RNG.fromTime).toString)
}