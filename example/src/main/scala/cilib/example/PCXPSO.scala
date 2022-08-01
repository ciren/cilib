package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import zio.prelude.newtypes.Natural
import zio.{ Console, ExitCode, URIO, ZEnvironment }

object PCXPSO extends zio.ZIOAppDefault {
  val swarmSize: Natural               = positiveInt(20)
  val bounds: NonEmptyVector[Interval] = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                  = Comparison.dominance(Min)
  val eval: Eval[NonEmptyVector]       = Eval.unconstrained(ExampleHelper.spherical andThen Feasible.apply)

  val guide: Guide[Mem[Double], Double] = Guide.pcx[Mem[Double]](2.0, 2.0)
  val pcxPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  ) = crossoverPSO(guide)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]]                                                 =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, swarmSize)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(pcxPSO)

  def run: URIO[Any, ExitCode] = {
    val env = ZEnvironment((cmp, eval))
    Console.printLine(Runner.repeat(1000, iter, swarm).provideEnvironment(env).runAll(RNG.fromTime).toString).exitCode
  }

}
