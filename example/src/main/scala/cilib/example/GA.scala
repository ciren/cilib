package cilib
package example

import cilib.exec._
import cilib.ga._
import zio.prelude.newtypes.Natural
import zio.prelude.{ Comparison => _, _ }
import zio.{ Console, ExitCode, URIO, ZEnvironment }

import Lenses._

object GAExample extends zio.ZIOAppDefault {
  type Ind = Individual[Unit]

  val populationSize: Natural = positiveInt(20)

  val bounds: NonEmptyVector[Interval] = Interval(-5.12, 5.12) ^ 30

  val cmp: Comparison            = Comparison.dominance(Min)
  val eval: Eval[NonEmptyVector] = Eval.unconstrained(ExampleHelper.spherical andThen Feasible.apply)

  def onePoint(xs: List[Ind]): RVar[List[Ind]] =
    xs match {
      case a :: b :: _ =>
        val point: RVar[Int] = Dist.uniformInt(0, a.pos.pos.size - 1)
        point.map(p =>
          List(
            a.pos.take(p) ++ b.pos.drop(p),
            b.pos.take(p) ++ a.pos.drop(p)
          ).forEach(x => NonEmptyVector.fromIterableOption(x).map(x => Entity((), Position(x, a.pos.boundary))))
            .getOrElse(List.empty[Ind])
        )
      case _           => sys.error("Incorrect number of parents")
    }

  def mutation(p_m: Double)(xs: List[Ind]): RVar[List[Ind]] =
    xs.forEach { x =>
      val newPos = x.pos.forEach { z =>
        for {
          za <- Dist.stdUniform.map(_ < p_m)
          zb <- if (za) Dist.stdNormal.flatMap(Dist.gaussian(0, _)).map(_ * z) else RVar.pure(z)
        } yield zb
      }

      newPos.map(p => _position.set(p)(x).toOption.get)
    }

  val randomSelection: NonEmptyVector[Ind] => RVar[List[Ind]] =
    (l: NonEmptyVector[Ind]) => RVar.sample(positiveInt(2), l).map(_.getOrElse(List.empty))

  val ga: NonEmptyVector[Ind] => Ind => Step[List[Ind]] =
    GA.ga(0.7, randomSelection, onePoint, mutation(0.2))

  val swarm: RVar[NonEmptyVector[Ind]] = Position.createCollection[Ind](x => Entity((), x))(bounds, populationSize)

  /* We need to convert the produced lists of Individuals that are
   * produced from the collection into a single container type to
   * match the expected type signature for an algorithm
   */
  val cullingGA: NonEmptyVector[Ind] => Step[NonEmptyVector[Ind]] =
    (collection: NonEmptyVector[Ind]) =>
      Iteration
        .sync(ga)
        .apply(collection)
        .map(_.toChunk.toList.flatten)
        .flatMap(offspring =>
          Step
            .withCompare(o =>
              (collection.toChunk.toList ++ offspring).sortWith((x, y) => Comparison.fitter(x.pos, y.pos).apply(o))
            )
            .map(offspring =>
              NonEmptyVector.fromIterableOption(offspring.take(populationSize)).getOrElse(sys.error("asdas"))
            )
        )

  // Our IO[Unit] that runs at the end of the world
  def run: URIO[Any, ExitCode] = {
    val env = ZEnvironment((cmp, eval))

    Console
      .printLine(
        exec.Runner
          .repeat(1000, cullingGA, swarm)
          .provideEnvironment(env)
          .runAll(RNG.fromTime)
          .toString
      )
      .exitCode
  }
}
