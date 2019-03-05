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

object VonNeumannPSO extends SafeApp {

  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(cmp = Comparison.dominance(Min),
                eval = Eval.unconstrained((xs: NonEmptyList[Double]) =>
                  Feasible(cilib.benchmarks.Benchmarks.spherical(xs))))

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[Double], Double]
  val social = Guide.vonNeumann[Mem[Double]]
  val gbestPSO = gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  // RVar
  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter = Iteration.sync(gbestPSO)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  override val runc: IO[Unit] = {
    val result = Runner.repeat(1000, iter, swarm).run(env).run(RNG.fromTime)

    result._2 match {
      case -\/(error) =>
        throw error

      case \/-(value) =>
        val positions = value.map(x => Lenses._position.get(x))
        putStrLn(positions.toString)
    }
  }

}
