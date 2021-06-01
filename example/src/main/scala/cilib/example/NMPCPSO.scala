package cilib
package example

import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
import zio.console._

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._

object NMPCPSO extends zio.App {

  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(
      cmp = Comparison.quality(Min),
      eval = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)
    )

  val guide   = Guide.nmpc[Mem[Double]](0.5)
  val nmpcPSO = nmpc(guide)

  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter = Iteration.sync(nmpcPSO)

  def run(args: List[String]) =
    putStrLn(Runner.repeat(1000, iter, swarm).provide(env).runAll(RNG.fromTime).toString).exitCode

}
