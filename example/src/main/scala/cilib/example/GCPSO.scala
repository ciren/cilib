package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import zio.prelude.newtypes.Natural
import zio.{ ZEnvironment, ZIO }

object GCPSO extends zio.ZIOAppDefault {

  val swarmSize: Natural               = positiveInt(20)
  val bounds: NonEmptyVector[Interval] = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                  = Comparison.dominance(Opt.Min)
  val eval: Eval[NonEmptyVector]       = Eval.unconstrained(ExampleHelper.spherical andThen Fit.feasible)

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive: Guide[Mem[Double], Double] = Guide.pbest[Mem[Double], Double]
  val social: Guide[Mem[Double], Double]    = Guide.gbest[Mem[Double]]
  val gcPSO: NonEmptyVector[Particle[Mem[Double], Double]] => Particle[Mem[Double], Double] => StepS[
    PSO.GCParams,
    Particle[Mem[Double], Double]
  ] =
    gcpso(0.729844, 1.496180, 1.496180, cognitive)

  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => StepS[PSO.GCParams, NonEmptyVector[
    Particle[Mem[Double], Double]
  ]] = Iteration.syncS(gcPSO)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, swarmSize)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  def run: ZIO[Environment & zio.ZIOAppArgs & zio.Scope, Any, Any] = {
    val algParams = PSO.defaultGCParams
    val env       = ZEnvironment((cmp, eval))

    Runner
      .repeat(1000, iter, swarm)
      .provideEnvironment(env)
      .toZIOWith((RNG.fromTime, algParams))
      .fold(
        failure = ex => println(ex.toString()),
        success = swarm => print(swarm.toString())
      )
  }

}
