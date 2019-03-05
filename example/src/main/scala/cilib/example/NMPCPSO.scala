package cilib
package example

import cilib.pso._
import cilib.pso.Defaults._
import cilib.exec._

import eu.timepit.refined.auto._

import scalaz._
import scalaz.effect._
import scalaz.effect.IO.putStrLn
import spire.implicits._
import spire.math.Interval

object NMPCPSO extends SafeApp {

  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(cmp = Comparison.quality(Min),
                eval = Eval.unconstrained((xs: NonEmptyList[Double]) =>
                  Feasible(cilib.benchmarks.Benchmarks.spherical(xs))))

  val guide = Guide.nmpc[Mem[Double]](0.5)
  val nmpcPSO = nmpc(guide)

  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter = Iteration.sync(nmpcPSO)

  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, iter, swarm).run(env).run(RNG.fromTime).toString)

}
