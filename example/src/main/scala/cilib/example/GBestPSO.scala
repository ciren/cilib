package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
import zio.stream.UStream
import zio.{ ExitCode, URIO }

object GBestPSO extends zio.App {
  val bounds: NonEmptyVector[Interval[Double]] = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                          = cilib.Comparison.dominance(Min)

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive: Guide[Mem[Double], Double] = Guide.pbest[Mem[Double], Double]
  val social: Guide[Mem[Double], Double]    = Guide.gbest[Mem[Double]]
  val gbestPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  )                                         = gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  // RVar
  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]]                                                  =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter
    : Kleisli[Step, NonEmptyVector[Particle[Mem[Double], Double]], NonEmptyVector[Particle[Mem[Double], Double]]] =
    Kleisli(Iteration.sync(gbestPSO))

  val problemStream: UStream[Problem] = Runner.staticProblem(
    "spherical",
    Eval.unconstrained((x: NonEmptyVector[Double]) => Feasible(ExampleHelper.spherical(x)))
  )

  // Our IO[Unit] that runs the algorithm, at the end of the world
  def run(args: List[String]): URIO[Any with zio.console.Console, ExitCode] = {
    val t = Runner.foldStep(
      cmp,
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
