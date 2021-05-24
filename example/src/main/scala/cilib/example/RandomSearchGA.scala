package cilib
package example

import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
import zio.console._
import zio.prelude.{ Comparison => _, _ }

import cilib.exec._
import cilib.ga._

object RandomSearchGA extends zio.App {
  type Ind = Individual[Unit]

  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(
      cmp = Comparison.dominance(Min),
      eval = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)
    )

  val randomSelection = (l: NonEmptyList[Ind]) => RVar.sample(2, l).map(_.getOrElse(List.empty))
  val distribution    = (position: Double) => Dist.stdNormal.flatMap(_ => Dist.gaussian(0, 1.25)).map(_ + position)

  val ga = GA.randomSearch(randomSelection, distribution)

  val swarm = Position.createCollection[Ind](x => Entity((), x))(bounds, 20)
  val myGA: NonEmptyList[Ind] => Step[NonEmptyList[Ind]] =
    (collection: NonEmptyList[Ind]) => {
      Iteration.sync(ga).apply(collection)
        .map(_.toList.flatten)
        .flatMap(r =>
          Step
            .withCompare(o => r.sortWith((x, y) => Comparison.fitter(x.pos, y.pos).apply(o)))
            .map(offspring => NonEmptyList.fromIterableOption(offspring.take(20)).getOrElse(sys.error("Impossible -> List is empty?")))
        )
    }

  def run(args: List[String]) =
    putStrLn(Runner.repeat(1000, myGA, swarm).provide(env).runAll(RNG.fromTime).toString).exitCode
}
