package cilib
package example

import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._

object GBestPSO extends zio.App {
  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(
      cmp = cilib.Comparison.dominance(Min),
      eval = Eval.unconstrained((x: NonEmptyVector[Double]) => Feasible(ExampleHelper.spherical(x)))
    )

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[Double], Double]
  val social    = Guide.gbest[Mem[Double]]
  val gbestPSO  = gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  // RVar
  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter = Kleisli(Iteration.sync(gbestPSO))

  val problemStream = Runner.staticProblem("spherical", env.eval)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  def run(args: List[String]) = {
    val t = Runner.foldStep(
      env,
      RNG.fromTime,
      swarm,
      Runner.staticAlgorithm("gbestPSO", iter),
      problemStream,
      (x: NonEmptyVector[Particle[Mem[Double], Double]], _: Eval[NonEmptyVector]) => RVar.pure(x)
    )

    t.take(1000)
      .runLast
      .fold(eh => println(eh.toString), ah => println(ah.toString))
      .exitCode
  }
}
