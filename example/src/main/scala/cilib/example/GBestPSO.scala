package cilib
package example

import cilib.pso._
import cilib.pso.Defaults._

import eu.timepit.refined.auto._

import scalaz._
import scalaz.effect._
import scalaz.effect.IO.putStrLn
import spire.implicits._
import spire.math.Interval

object GBestPSO extends SafeApp {

  val env =
    Environment(
      cmp = Comparison.dominance(Min),
      eval = Eval.unconstrained(cilib.benchmarks.Benchmarks.spherical[NonEmptyList, Double]).eval,
      bounds = Interval(-5.12, 5.12) ^ 30)

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[Double], Double]
  val social = Guide.gbest[Mem[Double]]
  val gbestPSO = gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  // RVar
  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(env.bounds, 20)
  val iter = Iteration.sync(gbestPSO)

  val problemStream = exec.Runner.staticProblem(env.eval, RNG.init(123L)).take(1000)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  override val runc: IO[Unit] = {
    /*val result = Runner.repeat(1000, iter, swarm).run(env).run(RNG.fromTime)

    result._2 match {
      case -\/(error) =>
        throw error

      case \/-(value) =>
        val positions = value.map(x => Lenses._position.get(x))
        putStrLn(positions.toString)
    }*/

    // val result = Runner.repeat(1000, iter, swarm).run(env).run(RNG.fromTime)
    // val positions = result._2.map(x => Lenses._position.get(x))

    // putStrLn(positions.toString)

    val t = exec.Runner.foldStep(env,
                                 RNG.fromTime,
                                 swarm,
                                 iter,
                                 problemStream,
                                 (x: NonEmptyList[Particle[Mem[Double], Double]]) => RVar.point(x))

    putStrLn(t.runLast.unsafePerformSync.toString)
  }
}
