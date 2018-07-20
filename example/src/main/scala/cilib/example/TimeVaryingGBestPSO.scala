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

object TimeVaryingGBestPSO extends SafeApp {
  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(cmp = Comparison.dominance(Min),
                eval =
                  Eval.unconstrained(cilib.benchmarks.Benchmarks.spherical[NonEmptyList, Double]))

  // To define one or more parameters for an algorithm, we need a few pieces:

  // 1. A data type to hold our values
  final case class GBestParams(inertia: Double, c1: Double, c2: Double)

  // 2. We need a default initial value of our data type
  val initial = GBestParams(0.729844, 1.496180, 1.496180)

  // 3. We need a function to create a new set of parameters, given the current set as input
  //    We need to define the maximum number of iterations as a stream effectively has no end
  def updateParams(params: GBestParams, iterations: Int @@ Runner.Iteration) = {
    val i = Tag.unwrap(iterations)
    def linear(a: Double, b: Double) =
      a + (b - a) * (i.toDouble / 1000.0)

    params.copy(inertia = linear(params.inertia, 0.2))
  }

  // 4. Now we need a function to take the parameters and then create an algorithm
  def mkAlgorithm(params: GBestParams) = {
    // Define a normal GBest PSO and run it for a single iteration
    val cognitive = Guide.pbest[Mem[Double], Double]
    val social = Guide.gbest[Mem[Double]]

    val gbestPSO = gbest(params.inertia, params.c1, params.c2, cognitive, social)

    Iteration.sync(gbestPSO)
  }

  // RVar
  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)

  val problemStream = Runner.staticProblem("spherical", env.eval)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  override val runc: IO[Unit] = {
    val t = Runner.foldStep(
      env,
      RNG.fromTime,
      swarm,
      Runner.algorithm( // We now create a stream of algorithms that are updated based on our custom functions
                       "gbestPSO",
                       initial,
                       mkAlgorithm,
                       updateParams),
      problemStream,
      (x: NonEmptyList[Particle[Mem[Double], Double]], _: Eval[NonEmptyList, Double]) =>
        RVar.pure(x)
    )

    putStrLn(t.take(1000).runLast.unsafePerformSync.toString)
  }
}
