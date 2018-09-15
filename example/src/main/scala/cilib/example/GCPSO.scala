package cilib
package example

import cilib.pso._
import cilib.pso.Defaults._
import cilib.exec._

import eu.timepit.refined.auto._

import scalaz.NonEmptyList
import scalaz.effect._
import scalaz.effect.IO.putStrLn
import spire.implicits._
import spire.math.Interval

import scalaz._

object GCPSO extends SafeApp {

  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(cmp = Comparison.dominance(Min),
                eval = Eval.unconstrained((xs: NonEmptyList[Double]) =>
                  Feasible(cilib.benchmarks.Benchmarks.spherical(xs))))

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[Double], Double]
  val social = Guide.gbest[Mem[Double]]
  val gcPSO: NonEmptyList[Particle[Mem[Double], Double]] => Particle[Mem[Double], Double] => StepS[
    Double,
    PSO.GCParams,
    Particle[Mem[Double], Double]] =
    gcpso(0.729844, 1.496180, 1.496180, cognitive)

  val iter: Kleisli[StepS[Double, PSO.GCParams, ?],
                    NonEmptyList[Particle[Mem[Double], Double]],
                    NonEmptyList[Particle[Mem[Double], Double]]] =
    Iteration.syncS(gcPSO)

  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  override val runc: IO[Unit] = {
    val algParams = PSO.defaultGCParams

    val result =
      Runner
        .repeat(1000, iter, swarm)
        .run(algParams)
        .run(env)
        .run(RNG.fromTime)

    putStrLn(result.toString)
  }

}
