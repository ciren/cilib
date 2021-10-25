package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import spire.implicits._
import spire.math.Interval
import zio.console._
import zio.{ ExitCode, URIO }
import zio.prelude.newtypes.Natural

object VonNeumannPSO extends zio.App {

  val swarmSize: Natural.subtype.Type with Natural.Tag = positiveInt(20)
  val bounds: NonEmptyVector[Interval[Double]]         = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                                  = Comparison.dominance(Min)
  val eval: Eval[NonEmptyVector]                       = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive: Guide[Mem[Double], Double] = Guide.pbest[Mem[Double], Double]
  val social: Guide[Mem[Double], Double]    = Guide.vonNeumann[Mem[Double]]
  val gbestPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  )                                         = gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  // RVar
  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]]                                                 =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, swarmSize)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(gbestPSO)

  def run(args: List[String]): URIO[Console with Console, ExitCode] =
    putStrLn(Runner.repeat(1000, iter, swarm).provide((cmp, eval)).runAll(RNG.fromTime).toString).exitCode

}
