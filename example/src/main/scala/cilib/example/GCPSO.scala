package cilib
package example

import eu.timepit.refined.auto._
import scalaz.NonEmptyList
import scalaz._
import spire.implicits._
import spire.math.Interval
import zio.console._

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._

object GCPSO extends zio.App {

  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(
      cmp = Comparison.dominance(Min),
      eval = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)
    )

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[Double], Double]
  val social    = Guide.gbest[Mem[Double]]
  val gcPSO: NonEmptyList[Particle[Mem[Double], Double]] => Particle[Mem[Double], Double] => StepS[
    Double,
    PSO.GCParams,
    Particle[Mem[Double], Double]
  ] =
    gcpso(0.729844, 1.496180, 1.496180, cognitive)

  val iter: Kleisli[StepS[Double, PSO.GCParams, ?], NonEmptyList[Particle[Mem[Double], Double]], NonEmptyList[
    Particle[Mem[Double], Double]
  ]] =
    Iteration.syncS(gcPSO)

  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  def run(args: List[String]) = {
    val algParams = PSO.defaultGCParams

    val result =
      Runner
        .repeat(1000, iter, swarm)
        .run(algParams)
        .run(env)
        .run(RNG.fromTime)

    putStrLn(result.toString).exitCode
  }

}
