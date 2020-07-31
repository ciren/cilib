package cilib
package example

import eu.timepit.refined.auto._
import scalaz._
import scalaz.effect.IO.putStrLn
import scalaz.effect._
import spire.implicits._
import spire.math.Interval

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._

object UNDXPSO extends SafeApp {
  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(
      cmp = Comparison.dominance(Min),
      eval = Eval.unconstrained((xs: NonEmptyList[Double]) => Feasible(cilib.benchmarks.Benchmarks.spherical(xs)))
    )

  val guide   = Guide.undx[Mem[Double]](1.0, 0.1)
  val undxPSO = crossoverPSO(guide)

  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter = Iteration.sync(undxPSO)

  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, iter, swarm).run(env).run(RNG.fromTime).toString)

}
