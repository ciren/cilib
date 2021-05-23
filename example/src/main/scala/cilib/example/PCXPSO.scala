package cilib
package example
/*
import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
import zio.console._

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._

object PCXPSO extends zio.App {
  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(
      cmp = Comparison.dominance(Min),
      eval = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)
    )

  val guide  = Guide.pcx[Mem[Double]](2.0, 2.0)
  val pcxPSO = crossoverPSO(guide)

  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter = Iteration.sync(pcxPSO)

  def run(args: List[String]) =
    putStrLn(Runner.repeat(1000, iter, swarm).run(env).run(RNG.fromTime).toString).exitCode

}
 */
