package cilib
package example

import cilib.exec._
import cilib.pso.Defaults._
import cilib.pso._
import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
import zio.console._
import zio.{ ExitCode, URIO }

object LBestPSO extends zio.App {
  val bounds: NonEmptyVector[Interval[Double]] = Interval(-5.12, 5.12) ^ 30
  val cmp = Comparison.quality(Min)
  val eval = Eval.unconstrained(ExampleHelper.spherical andThen Feasible)

  // LBest is a network topology where every Paricle 'x' has (n/2) neighbours
  // on each side. For example, a neighbourhood size of 3 means that there is
  // a single neighbour on both sides of the current particle.

  // Define a LBest PSO and run it for a single iteration
  val cognitive: Guide[Mem[Double], Double] = Guide.pbest
  val social: Guide[Mem[Double], Double]    = Guide.lbest[Mem[Double]](3)

  val lbestPSO: NonEmptyVector[Particle[Mem[Double], Double]] => (
    Particle[Mem[Double], Double] => Step[Particle[Mem[Double], Double]]
  ) =
    gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  val swarm: RVar[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)
  val iter: NonEmptyVector[Particle[Mem[Double], Double]] => Step[NonEmptyVector[Particle[Mem[Double], Double]]] =
    Iteration.sync(lbestPSO)

  def run(args: List[String]): URIO[Console with Console, ExitCode] =
    putStrLn(Runner.repeat(1000, iter, swarm).provide((cmp, eval)).runAll(RNG.fromTime).toString).exitCode

}
