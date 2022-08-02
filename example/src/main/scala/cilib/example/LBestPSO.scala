package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import zio.prelude.newtypes.Natural
import zio.{ Console, ExitCode, URIO, ZEnvironment }

object LBestPSO extends zio.ZIOAppDefault {
  val swarmSize: Natural               = positiveInt(20)
  val bounds: NonEmptyVector[Interval] = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                  = Comparison.quality(Min)
  val eval: Eval[NonEmptyVector]       = Eval.unconstrained(ExampleHelper.spherical andThen Feasible.apply)

  // LBest is a network topology where every Paricle 'x' has (n/2) neighbours
  // on each side. For example, a neighbourhood size of 3 means that there is
  // a single neighbour on both sides of the current particle.

  // Define a LBest PSO and run it for a single iteration
  val cognitive: Guide[Mem[Double], Double] = Guide.pbest
  val social: Guide[Mem[Double], Double]    = Guide.lbest[Mem[Double]](3)

  val lbestPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  ) =
    gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]]                                                 =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, swarmSize)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(lbestPSO)

  def run: URIO[Any, ExitCode] = {
    val environment = ZEnvironment((cmp, eval))
    val result      = Runner.repeat(1000, iter, swarm).provideEnvironment(environment).runAll(RNG.fromTime).toString
    Console.printLine(result).exitCode
  }

}
