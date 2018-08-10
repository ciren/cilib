package cilib
package example
import java.io.File

import cilib.benchmarks.Benchmarks
import cilib.exec.Runner._
import cilib.exec._
import cilib.io._
import cilib.pso.Defaults._
import cilib.pso._
import eu.timepit.refined.auto._
import scalaz.Scalaz._
import scalaz._
import scalaz.concurrent.Task
import scalaz.effect.IO.putStrLn
import scalaz.effect._
import scalaz.stream._
import spire.implicits._
import spire.math.Interval

object FileOutput extends SafeApp {

  // An example showing how to compare multiple algorithms across multiple
  // benchmarks and save the results to a a file (either csv or parquet).

  val bounds = Interval(-5.12, 5.12) ^ 30
  val rng = RNG.init(12L)

  // Define the benchmarks
  val absolute = Environment(
    cmp = Comparison.dominance(Max),
    eval = Eval.unconstrained(Benchmarks.absoluteValue[NonEmptyList, Double])
  )

  val ackley = Environment(
    cmp = Comparison.dominance(Max),
    eval = Eval.unconstrained(Benchmarks.ackley[NonEmptyList, Double])
  )
  val quadric = Environment(
    cmp = Comparison.dominance(Max),
    eval = Eval.unconstrained(Benchmarks.quadric[NonEmptyList, Double])
  )

  val spherical = Environment(
    cmp = Comparison.dominance(Max),
    eval = Eval.unconstrained(Benchmarks.spherical[NonEmptyList, Double])
  )

  // Define the problem streams
  val absoluteStream = Runner.staticProblem("absolute", absolute.eval)
  val ackleyStream = Runner.staticProblem("ackley", ackley.eval)
  val sphericalStream = Runner.staticProblem("spherical", spherical.eval)
  val quadricStream = Runner.staticProblem("quadric", quadric.eval)

  // Define the guides for our PSO algorithms
  val cognitive = Guide.pbest[Mem[Double], Double]
  val gbestGuide = Guide.gbest[Mem[Double]]
  val lbestGuide = Guide.lbest[Mem[Double]](3)

  // Define our algorithms
  val gbestPSO = gbest(0.729844, 1.496180, 1.496180, cognitive, gbestGuide)
  val lbestPSO = gbest(0.729844, 1.496180, 1.496180, cognitive, lbestGuide)

  // Define iterators for the algorithms
  val gbestIter = Iteration.sync(gbestPSO)
  val lbestIter = Iteration.sync(lbestPSO)

  // Define the initial swarm
  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)

  // A class to hold the results that we want to save at the end of each iteration
  final case class Results(min: Double, average: Double)

  // A performance measure that we apply to the collection at the end of each iteration.
  def performanceMeasure =
    measure[NonEmptyList[Entity[Mem[Double], Double]], Unit, Results](collection => {
      val feasibleOptic = Lenses._singleFitness[Double].composePrism(Lenses._feasible)
      val fitnessValues = collection.map(x => feasibleOptic.headOption(x.pos).getOrElse(Double.PositiveInfinity))
      Results(fitnessValues.minimum1, fitnessValues.suml / fitnessValues.size)
    })

  // A simple RVar.pure function that is called when the the environment changes
  // In this example our environments do not change.
  val onChange = (x: NonEmptyList[Entity[Mem[Double], Double]], _: Eval[NonEmptyList, Double]) =>
    RVar.pure(x)

  def simulation(env: Environment[Double], stream: Process[Task, Problem[Double]]) =
      List(
            Runner.staticAlgorithm("GBestPSO", gbestIter),
            Runner.staticAlgorithm("LBestPSO", lbestIter))
            .map(alg => Runner.foldStep(env,
                rng,
                swarm,
                alg,
                stream,
                onChange))


  val simulations = List.concat(
    simulation(absolute, absoluteStream),
    simulation(ackley, ackleyStream),
    simulation(quadric, quadricStream),
    simulation(spherical, sphericalStream)
  )

  // Our method to execute the simulations, where each simulation lasts 1000 iterations,
  // across 4 cores and save the results to a csv file.
  // To save the results to a parquet file change the line
  // .to(csvHeaderSink[Results](new File("Results.csv")))
  // to
  // .to(parquetSink[Results](new File("Results.parquet")))
  def writeResultsToCSV(): Unit = {
    val measured: Process[Task, Process[Task, Measurement[Results]]] =
      Process.emitAll(simulations.map(_.take(1000).pipe(performanceMeasure)))

    merge
      .mergeN(4)(measured)
      .to(csvHeaderSink[Results](new File("Results.csv")))
      .run
      .unsafePerformSync
  }

  override val runc: IO[Unit] =
    for {
      _ <- putStrLn("Executing " + simulations.size + " simulations.")
      _ <- IO(writeResultsToCSV())
      _ <- putStrLn("Complete.")
    } yield ()

}
