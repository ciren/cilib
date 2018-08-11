package cilib
package example

import cilib.exec._
import cilib.pso.PSO._
import cilib.pso._
import eu.timepit.refined.auto._
import scalaz.Scalaz._
import scalaz._
import scalaz.effect.IO.putStrLn
import scalaz.effect._
import spire.implicits._
import spire.math.Interval

object DynamicProblem extends SafeApp {

  // An example showcasing how to create dynamic environments.
  // The example uses an absolute value benchmark with an added bias factor
  // that increases over time. This causes the most optimum area to shift
  // from the upper bound of each dimension to the lower bound.

  override val runc: IO[Unit] = {
    val t = Runner.foldStep(
      env,
      RNG.init(12L),
      swarm,
      Runner.staticAlgorithm("gbestPSO", iter),
      problemStream,
      (x: NonEmptyList[Particle[Mem[Double], Double]], _: Eval[NonEmptyList, Double]) =>
        RVar.pure(x)
    )

    putStrLn(t.take(1000).runLast.unsafePerformSync.toString)
  }

  val bounds = Interval(0.0, 10.0) ^ 30
  val env =
    Environment(cmp = Comparison.dominance(Max), eval = Eval.unconstrained(fitnessFunction(0.0)))// Start with no bias
    // Define the guides for our PSO algorithms
  val cognitive = Guide.pbest[Mem[Double], Double]
  val social = Guide.gbest[Mem[Double]]
  val gbestPSO = gbest(0.729844, 1.496180, 1.496180, cognitive, social)
    // Define the initial swarm
    val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
    // Define iterators for our algorithm
    val iter = Iteration.sync(gbestPSO)
  // How frequently we want to update the evaluation method
  val freq = Env.frequency(10)
  // problem takes in the name of the problem, the initial state and a method to update the
  // evaluation process.
  val problemStream =
    Runner.problem[Double, Double]("spherical", Dist.stdUniform, dynamicUpdate)(freq, RNG.init(12L))

  // Our gbest PSO algorithm with bounds a checking mechanism (updatePBestBounds)
  def gbest[S](
      w: Double,
      c1: Double,
      c2: Double,
      cognitive: Guide[S, Double],
      social: Guide[S, Double]
  )(implicit M: HasMemory[S, Double], V: HasVelocity[S, Double]): NonEmptyList[
    Particle[S, Double]] => Particle[S, Double] => Step[Double, Particle[S, Double]] =
    collection =>
      x =>
        for {
          cog <- cognitive(collection, x)
          soc <- social(collection, x)
          v <- stdVelocity(x, soc, cog, w, c1, c2)
          p <- stdPosition(x, v)
          p2 <- evalParticle(p)
          p3 <- updateVelocity(p2, v)
          updated <- updatePBestBounds(p3)
        } yield updated

  // A method to update update the evaluation method that takes in some state,
  // in oue case a double.
  def dynamicUpdate(state: Double): RVar[(Double, Eval[NonEmptyList, Double])] =
    Dist.stdUniform.map(x => {
      val newState = x + state
      val eval = Eval.unconstrained(fitnessFunction(newState))
      (newState, eval)
    })

  // Our fitness function with a bias.
  def fitnessFunction(bias: Double)(l: NonEmptyList[Double]): Double =
    Math.abs(l.map(x => x - bias).suml)
}
