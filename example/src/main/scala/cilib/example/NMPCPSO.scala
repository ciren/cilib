package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import zio.console._
import zio.prelude.newtypes.Natural
import zio.{ ExitCode, URIO }

object NMPCPSO extends zio.App {

  val swarmSize: Natural               = positiveInt(20)
  val bounds: NonEmptyVector[Interval] = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                  = Comparison.quality(Min)
  val eval: Eval[NonEmptyVector]       = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)

  val guide: Guide[Mem[Double], Double] = Guide.nmpc[Mem[Double]](0.5)
  val nmpcPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  )                                     = nmpc(guide)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]]                                                 =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, swarmSize)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(nmpcPSO)

  def run(args: List[String]): URIO[Console with Console, ExitCode] =
    putStrLn(Runner.repeat(1000, iter, swarm).provide((cmp, eval)).runAll(RNG.fromTime).toString).exitCode

}
