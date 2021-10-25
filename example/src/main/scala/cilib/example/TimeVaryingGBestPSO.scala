package cilib
package example

import cilib.exec.{ Kleisli, _ }
import cilib.pso.Defaults._
import cilib.pso.{ Particle, _ }
import cilib.{ Mem, NonEmptyVector, Step }
import spire.implicits._
import spire.math.Interval
import zio.URIO
import zio.stream.UStream
import zio.prelude.newtypes.Natural

object TimeVaryingGBestPSO extends zio.App {
  val swarmSize: Natural.subtype.Type with Natural.Tag = positiveInt(20)
  val bounds: NonEmptyVector[Interval[Double]]         = Interval(-5.12, 5.12) ^ 30
  val cmp: Comparison                                  = Comparison.dominance(Min)
  val eval: Eval[NonEmptyVector]                       = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)

  // To define one or more parameters for an algorithm, we need a few pieces:

  // 1. A data type to hold our values
  final case class GBestParams(inertia: Double, c1: Double, c2: Double)

  // 2. We need a default initial value of our data type
  val initial: GBestParams = GBestParams(0.729844, 1.496180, 1.496180)

  // 3. We need a function to create a new set of parameters, given the current set as input
  //    We need to define the maximum number of iterations as a stream effectively has no end
  def updateParams(params: GBestParams, iterations: Runner.IterationCount): TimeVaryingGBestPSO.GBestParams = {
    val i                            = Runner.IterationCount.unwrap(iterations)
    def linear(a: Double, b: Double) =
      a + (b - a) * (i.toDouble / 1000.0)

    params.copy(inertia = linear(params.inertia, 0.2))
  }

  // 4. Now we need a function to take the parameters and then create an algorithm
  def mkAlgorithm(
    params: GBestParams
  ): Kleisli[Step, NonEmptyVector[Particle[Mem[Double], Double]], NonEmptyVector[Particle[Mem[Double], Double]]] = {
    // Define a normal GBest PSO and run it for a single iteration
    val cognitive = Guide.pbest[Mem[Double], Double]
    val social    = Guide.gbest[Mem[Double]]

    val gbestPSO = gbest(params.inertia, params.c1, params.c2, cognitive, social)

    Kleisli(Iteration.sync(gbestPSO))
  }

  // RVar
  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, swarmSize)

  val problemStream: UStream[Problem] = Runner.staticProblem("spherical", eval)

  def run(args: List[String]) =
    runner.exitCode

  // Our IO[Unit] that runs the algorithm, at the end of the world
  val runner: URIO[Any, Unit] = {
    val t = Runner.foldStep(
      cmp,
      RNG.fromTime,
      swarm,
      Runner.algorithm( // We now create a stream of algorithms that are updated based on our custom functions
        "gbestPSO",
        initial,
        mkAlgorithm,
        updateParams
      ),
      problemStream,
      (x: NonEmptyVector[Particle[Mem[Double], Double]], _: Eval[NonEmptyVector]) => RVar.pure(x)
    )

    t.take(1000)
      .runLast
      .fold(
        eh => println(eh.toString),
        ah => println(ah.toString)
      )
  }
}
