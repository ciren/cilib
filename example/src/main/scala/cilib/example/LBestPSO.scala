package cilib
package example

import cilib.Defaults._

import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.std.list._
import spire.implicits._
import spire.math.Interval

import cilib.syntax.algorithm._

object LBestPSO extends SafeApp {

  val sum = Problems.spherical[List,Double]

  // LBest is a network topology where every Paricle 'x' has (n/2) neighbours
  // on each side. For example, a neighbourhood size of 3 means that there is
  // a single neighbour on both sides of the current particle.

  // Define a LBest PSO and run it for a single iteration
  val cognitive: Guide[Mem[List,Double],List,Double] = Guide.pbest
  val social: Guide[Mem[List,Double],List,Double] = Guide.lbest[Mem[List,Double],List](3)

  val lbestPSO: List[Particle[Mem[List,Double],List,Double]] => Particle[Mem[List,Double],List,Double] => Step[List,Double,Result[Particle[Mem[List,Double],List,Double]]] =
    gbest(0.729844, 1.496180, 1.496180, cognitive, social).map(One(_))

  val swarm = Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(Interval(-5.12,5.12)^30, 20)
  val syncLBest = Iteration.sync(lbestPSO)

  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, syncLBest, swarm).run(Min)(sum).run(RNG.fromTime).toString)
}
