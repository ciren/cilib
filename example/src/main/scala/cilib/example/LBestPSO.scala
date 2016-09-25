package cilib
package example

import scalaz.NonEmptyList
import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.std.list._
import spire.implicits._
import spire.math.Interval

import cilib.pso._
import cilib.pso.Defaults._
import cilib.syntax.algorithm._

object LBestPSO extends SafeApp {

  val sum = Problems.spherical

  // LBest is a network topology where every Paricle 'x' has (n/2) neighbours
  // on each side. For example, a neighbourhood size of 3 means that there is
  // a single neighbour on both sides of the current particle.

  // Define a LBest PSO and run it for a single iteration
  val cognitive: Guide[Mem[Double],Double] = Guide.pbest
  val social: Guide[Mem[Double],Double] = Guide.lbest[Mem[Double]](3)

  val lbestPSO =
    gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  val swarm = Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(Interval(-5.12,5.12)^30, 20)
  val iter = Iteration.sync(lbestPSO)

  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, iter, swarm).run(Comparison.quality(Min))(sum).run(RNG.fromTime).toString)

}
