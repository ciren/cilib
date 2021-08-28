package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
import zio.console._
import zio.{ ExitCode, URIO }

object PCXPSO extends zio.App {
  val bounds: NonEmptyVector[Interval[Double]] = Interval(-5.12, 5.12) ^ 30
  val env: Environment =
    Environment(
      cmp = Comparison.dominance(Min),
      eval = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)
    )

  val guide: Guide[Mem[Double], Double] = Guide.pcx[Mem[Double]](2.0, 2.0)
  val pcxPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  ) = crossoverPSO(guide)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(pcxPSO)

  def run(args: List[String]): URIO[Console with Console, ExitCode] =
    putStrLn(Runner.repeat(1000, iter, swarm).provide(env).runAll(RNG.fromTime).toString).exitCode

}
