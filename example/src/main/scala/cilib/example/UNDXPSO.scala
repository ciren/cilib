package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import zio.prelude.newtypes.Natural
import zio.{ Console, IO, ZEnvironment }

import java.io.IOException

object UNDXPSO extends zio.ZIOAppDefault {
  val swarmSize: Natural               = positiveInt(20)
  val bounds: NonEmptyVector[Interval] = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                  = Comparison.dominance(Min)
  val eval: Eval[NonEmptyVector]       = Eval.unconstrained(ExampleHelper.spherical andThen Feasible.apply)

  val guide: Guide[Mem[Double], Double] = Guide.undx[Mem[Double]](1.0, 0.1)
  val undxPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  ) = crossoverPSO(guide)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]]                                                 =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, swarmSize)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(undxPSO)

  def run: IO[IOException, Unit] = {
    val env = ZEnvironment((cmp, eval))
    Console.printLine(Runner.repeat(1000, iter, swarm).provideEnvironment(env).runAll(RNG.fromTime).toString)
  }

}
