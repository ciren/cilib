package cilib
package example

import cilib.exec._
import cilib.ga._
import zio.prelude.fx.ZPure
import zio.prelude.newtypes.Natural
import zio.{ Console, ZEnvironment, ZIO }

object RandomSearchGA extends zio.ZIOAppDefault {
  type Ind = Individual[Unit]

  val populationSize: Natural          = positiveInt(20)
  val bounds: NonEmptyVector[Interval] = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                  = Comparison.dominance(Opt.Min)
  val eval: Eval[NonEmptyVector]       = Eval.unconstrained(ExampleHelper.spherical andThen Fit.feasible)

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

  def run: ZIO[Environment & zio.ZIOAppArgs & zio.Scope, Any, Any] = {
    val env = ZEnvironment((cmp, eval))
    Runner
      .repeat(1000, myGA, swarm)
      .provideEnvironment(env)
      .toZIOWith(RNG.fromTime)
      .fold(
        failure = ex => ZIO.die(ex),
        success = a => Console.printLine(a.toString())
      )
  }
}
