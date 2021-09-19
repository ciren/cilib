package cilib
package example
import cilib.exec.Runner._
import cilib.exec.{ Progress, _ }
import cilib.io._
import cilib.pso.Defaults._
import cilib.pso._
import cilib.{ Entity, Mem, NonEmptyVector }
import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
import zio._
import zio.blocking.Blocking
import zio.console._
import zio.prelude.{ Comparison => _, _ }
import zio.stream._

import java.io.File

object FileOutput extends zio.App {

  // An example showing how to compare multiple algorithms across multiple
  // benchmarks and save the results to a a file (either csv or parquet).

  val bounds: NonEmptyVector[Interval[Double]] = Interval(-5.12, 5.12) ^ 30
  val rng: RNG                                 = RNG.init(12L)
  val cmp: Comparison                          = Comparison.dominance(Max)

  // Define the benchmarks. These functions are hardcoded but it would
  // be better to consider using https://github.com/ciren/benchmarks
  // which is a far more extensive and complete set of benchmark
  // functions and suites.
  // The problems are repesented as streams:
  val absoluteStream: UStream[Problem] =
    Runner.staticProblem("absolute", Eval.unconstrained(ExampleHelper.absoluteValue andThen Feasible))
  val ackleyStream: UStream[Problem] =
    Runner.staticProblem("ackley", Eval.unconstrained(ExampleHelper.ackley andThen Feasible))
  val sphericalStream: UStream[Problem] =
    Runner.staticProblem("spherical", Eval.unconstrained(ExampleHelper.spherical andThen Feasible))
  val quadricStream: UStream[Problem] =
    Runner.staticProblem("quadric", Eval.unconstrained(ExampleHelper.quadric andThen Feasible))

  // Define the guides for our PSO algorithms
  val cognitive: Guide[Mem[Double], Double]  = Guide.pbest[Mem[Double], Double]
  val gbestGuide: Guide[Mem[Double], Double] = Guide.gbest[Mem[Double]]
  val lbestGuide: Guide[Mem[Double], Double] = Guide.lbest[Mem[Double]](3)

  // Define our algorithms
  val gbestPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  ) = gbest(0.729844, 1.496180, 1.496180, cognitive, gbestGuide)
  val lbestPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  ) = gbest(0.729844, 1.496180, 1.496180, cognitive, lbestGuide)

  // Define iterators for the algorithms
  val gbestIter
    : Kleisli[Step, NonEmptyVector[Particle[Mem[Double], Double]], NonEmptyVector[Particle[Mem[Double], Double]]] =
    Kleisli(Iteration.sync(gbestPSO))
  val lbestIter
    : Kleisli[Step, NonEmptyVector[Particle[Mem[Double], Double]], NonEmptyVector[Particle[Mem[Double], Double]]] =
    Kleisli(Iteration.sync(lbestPSO))

  // Define the initial swarm
  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)

  // A class to hold the results that we want to save at the end of each iteration
  final case class Results(min: Double, average: Double)

  // A performance measure that we apply to the collection at the end of each iteration.
  def performanceMeasure: Progress[NonEmptyVector[Entity[Mem[Double], Double]]] => Measurement[Results] =
    measure[NonEmptyVector[Entity[Mem[Double], Double]], Results] { collection =>
      val fitnessValues = collection.map(x =>
        x.pos.objective
          .flatMap(_.fitness match {
            case Left(f) =>
              f match {
                case Feasible(v) => Some(v)
                case _           => None
              }
            case _ => None
          })
          .getOrElse(Double.PositiveInfinity)
      )

      //val feasibleOptic = Lenses._singleFitness[Double].composePrism(Lenses._feasible)
      // val fitnessValues =
      //   collection.map(x => feasibleOptic.headOption(x.pos).getOrElse(Double.PositiveInfinity))

      Results(fitnessValues.toChunk.min, fitnessValues.toChunk.reduceLeft(_ + _) / fitnessValues.size)
    }

  // A simple RVar.pure function that is called when the the environment changes
  // In this example our environments do not change.
  val onChange: (NonEmptyVector[Entity[Mem[Double], Double]], Eval[NonEmptyVector]) => RVar[
    NonEmptyVector[Entity[Mem[Double], Double]]
  ] = (x: NonEmptyVector[Entity[Mem[Double], Double]], _: Eval[NonEmptyVector]) => RVar.pure(x)

  def simulation(
    env: cilib.Comparison,
    stream: Stream[Nothing, Problem]
  ): List[zio.stream.Stream[Exception, Progress[NonEmptyVector[Entity[Mem[Double], Double]]]]] =
    List(Runner.staticAlgorithm("GBestPSO", gbestIter), Runner.staticAlgorithm("LBestPSO", lbestIter))
      .map(alg => Runner.foldStep(env, rng, swarm, alg, stream, onChange))

  val simulations: List[Stream[Exception, Progress[NonEmptyVector[Entity[Mem[Double], Double]]]]] =
    List.concat(
      simulation(cmp, absoluteStream),
      simulation(cmp, ackleyStream),
      simulation(cmp, quadricStream),
      simulation(cmp, sphericalStream)
    )

  sealed abstract class Choice {
    def filename: String =
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

  val myAppLogic: ZIO[Blocking with Console, Throwable, Unit] =
    for {
      _      <- putStrLn("Please enter the output format type: (1) for Parquet or (2) for CSV")
      result <- getStrLn
      choice <- ZIO.fromTry(scala.util.Try(result.toInt))
      _ <- writeResults(choice match {
            case 1 => Parquet
            case 2 => CSV
            case _ => Invalid
          })
      _ <- putStrLn("Complete.")
    } yield ()

}
