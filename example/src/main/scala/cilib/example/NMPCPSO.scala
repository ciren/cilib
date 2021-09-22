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

object NMPCPSO extends zio.App {

  val bounds: NonEmptyVector[Interval[Double]] = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                          = Comparison.quality(Min)
  val eval: Eval[NonEmptyVector]               = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)

  val guide: Guide[Mem[Double], Double] = Guide.nmpc[Mem[Double]](0.5)
  val nmpcPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  )                                     = nmpc(guide)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]]                                                 =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(nmpcPSO)

  def run(args: List[String]): URIO[Console with Console, ExitCode] =
    putStrLn(Runner.repeat(1000, iter, swarm).provide((cmp, eval)).runAll(RNG.fromTime).toString).exitCode

}
