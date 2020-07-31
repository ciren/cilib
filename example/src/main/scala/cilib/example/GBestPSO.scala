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

object GBestPSO extends SafeApp {
  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(
      cmp = Comparison.dominance(Min),
      eval = Eval.unconstrained((xs: NonEmptyList[Double]) => Feasible(cilib.benchmarks.Benchmarks.spherical(xs)))
    )

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[Double], Double]
  val social    = Guide.gbest[Mem[Double]]
  val gbestPSO  = gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  // RVar
  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter = Iteration.sync(gbestPSO)

  val problemStream = Runner.staticProblem("spherical", env.eval)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  override val runc: IO[Unit] = {
    val t = Runner.foldStep(
      env,
      RNG.fromTime,
      swarm,
      Runner.staticAlgorithm("gbestPSO", iter),
      problemStream,
      (x: NonEmptyList[Particle[Mem[Double], Double]], _: Eval[NonEmptyList, Double]) => RVar.pure(x)
    )

    putStrLn(t.take(1000).runLast.unsafePerformSync.toString)
  }
}
