package cilib
package example

import cilib.exec._
import cilib.ga._
import spire.implicits._
import spire.math.Interval
import zio.console._
import zio.prelude.fx.ZPure
import zio.prelude.newtypes.Natural
import zio.{ ExitCode, URIO }

object RandomSearchGA extends zio.App {
  type Ind = Individual[Unit]

  val populationSize: Natural.subtype.Type with Natural.Tag = positiveInt(20)
  val bounds: NonEmptyVector[Interval[Double]]              = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                                       = Comparison.dominance(Min)
  val eval: Eval[NonEmptyVector]                            = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)

  val randomSelection: NonEmptyVector[Ind] => ZPure[Nothing, RNG, RNG, Any, Nothing, List[Ind]] =
    (l: NonEmptyVector[Ind]) => RVar.sample(positiveInt(2), l).map(_.getOrElse(List.empty))
  val distribution: Double => ZPure[Nothing, RNG, RNG, Any, Nothing, Double]                    =
    (position: Double) => Dist.stdNormal.flatMap(_ => Dist.gaussian(0, 1.25)).map(_ + position)

  val ga: NonEmptyVector[Individual[Unit]] => (Individual[Unit] => Step[List[Individual[Unit]]]) =
    GA.randomSearch(randomSelection, distribution)

  val swarm: RVar[NonEmptyVector[Ind]]                       =
    Position.createCollection[Ind](x => Entity((), x))(bounds, populationSize)
  val myGA: NonEmptyVector[Ind] => Step[NonEmptyVector[Ind]] =
    (collection: NonEmptyVector[Ind]) =>
      Iteration
        .sync(ga)
        .apply(collection)
        .map(_.toChunk.toList.flatten)
        .flatMap(r =>
          Step
            .withCompare(o => r.sortWith((x, y) => Comparison.fitter(x.pos, y.pos).apply(o)))
            .map(offspring =>
              NonEmptyVector.fromIterableOption(offspring.take(20)).getOrElse(sys.error("Impossible -> List is empty?"))
            )
        )

  def run(args: List[String]): URIO[Console with Console, ExitCode] =
    putStrLn(Runner.repeat(1000, myGA, swarm).provide((cmp, eval)).runAll(RNG.fromTime).toString).exitCode
}
