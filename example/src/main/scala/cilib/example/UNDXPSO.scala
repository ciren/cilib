package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
import zio.ZIO
import zio.console._

import java.io.IOException

object UNDXPSO extends zio.App {
  val bounds: NonEmptyVector[Interval[Double]] = Interval(-5.12, 5.12) ^ 30
  val cmp = Comparison.dominance(Min)
  val eval = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)


  val guide: Guide[Mem[Double], Double] = Guide.undx[Mem[Double]](1.0, 0.1)
  val undxPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  ) = crossoverPSO(guide)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(undxPSO)

  def run(args: List[String]) =
    program.exitCode

  val program: ZIO[Console, IOException, Unit] =
    putStrLn(Runner.repeat(1000, iter, swarm).provide((cmp, eval)).runAll(RNG.fromTime).toString)

}
