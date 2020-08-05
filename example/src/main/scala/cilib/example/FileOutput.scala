package cilib
package example
import java.io.File

import eu.timepit.refined.auto._
import scalaz._, Scalaz._
import spire.implicits._
import spire.math.Interval
import zio._
import zio.blocking.Blocking
import zio.console._
import zio.stream._

import cilib.exec.Runner._
import cilib.exec._
import cilib.io._
import cilib.pso.Defaults._
import cilib.pso._

object FileOutput extends zio.App {

  // An example showing how to compare multiple algorithms across multiple
  // benchmarks and save the results to a a file (either csv or parquet).

  val bounds = Interval(-5.12, 5.12) ^ 30
  val rng    = RNG.init(12L)

  // Define the benchmarks. These functions are hardcoded, but it would be better to consider
  // using https://github.com/ciren/benchmarks which is a far more extensive and
  // complete set of benchmark functions and suites.
  val absolute = Environment(
    cmp = Comparison.dominance(Max),
    eval = Eval.unconstrained(ExampleHelper.absoluteValue andThen Feasible)
  )

  val ackley = Environment(
    cmp = Comparison.dominance(Max),
    eval = Eval.unconstrained(ExampleHelper.ackley andThen Feasible)
  )

  val quadric = Environment(
    cmp = Comparison.dominance(Max),
    eval = Eval.unconstrained(ExampleHelper.quadric andThen Feasible)
  )

  val spherical = Environment(
    cmp = Comparison.dominance(Max),
    eval = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)
  )

  // Define the problem streams
  val absoluteStream  = Runner.staticProblem("absolute", absolute.eval)
  val ackleyStream    = Runner.staticProblem("ackley", ackley.eval)
  val sphericalStream = Runner.staticProblem("spherical", spherical.eval)
  val quadricStream   = Runner.staticProblem("quadric", quadric.eval)

  // Define the guides for our PSO algorithms
  val cognitive  = Guide.pbest[Mem[Double], Double]
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
    measure[NonEmptyList[Entity[Mem[Double], Double]], Unit, Results] { collection =>
      val feasibleOptic = Lenses._singleFitness[Double].composePrism(Lenses._feasible)
      val fitnessValues =
        collection.map(x => feasibleOptic.headOption(x.pos).getOrElse(Double.PositiveInfinity))
      Results(fitnessValues.minimum1, fitnessValues.suml / fitnessValues.size)
    }

  // A simple RVar.pure function that is called when the the environment changes
  // In this example our environments do not change.
  val onChange = (x: NonEmptyList[Entity[Mem[Double], Double]], _: Eval[NonEmptyList, Double]) => RVar.pure(x)

  def simulation(env: Environment[Double], stream: Stream[Nothing, Problem[Double]]) =
    List(Runner.staticAlgorithm("GBestPSO", gbestIter), Runner.staticAlgorithm("LBestPSO", lbestIter))
      .map(alg => Runner.foldStep(env, rng, swarm, alg, stream, onChange))

  val simulations: List[Stream[Exception, Progress[NonEmptyList[Entity[Mem[Double], Double]]]]] =
    List.concat(
      simulation(absolute, absoluteStream),
      simulation(ackley, ackleyStream),
      simulation(quadric, quadricStream),
      simulation(spherical, sphericalStream)
    )

  sealed abstract class Choice {
    def filename =
      this match {
        case CSV     => "results.csv"
        case Parquet => "results.parquet"
        case Invalid => throw new Exception("Invalid choice")
      }
  }
  final case object CSV     extends Choice
  final case object Parquet extends Choice
  final case object Invalid extends Choice

  def writeResults(choice: Choice): ZIO[Blocking, Throwable, Unit] = {
    val measured: List[ZStream[Any, Exception, Measurement[Results]]] =
      simulations.map(_.take(1000).map(performanceMeasure))

    ZStream
      .mergeAll(4)(measured: _*)
      .run(choice match {
        case CSV     => csvSink(new File(choice.filename))
        case Parquet => parquetSink(new File(choice.filename))
        case _       => ZSink.fail(new Exception("Unsupported value. Please select a valid choice."))
      })
  }

  def run(args: List[String]) =
    myAppLogic.exitCode

  val myAppLogic =
    for {
      _      <- putStrLn("Please enter the output format type: (1) for Parquet or (2) for CSV")
      result <- getStrLn
      choice <- ZIO.succeed(\/.attempt(result.toInt)(_.getMessage) match {
                 case \/-(1) => Parquet
                 case \/-(2) => CSV
                 case _      => Invalid
               })
      _ <- writeResults(choice)
      _ <- putStrLn("Complete.")
    } yield ()

}
