package cilib
package example

import scalaz._
import Scalaz._
import scalaz.effect._
import scalaz.effect.IO._

import eu.timepit.refined.auto._

import spire.math._
import spire.implicits._

import cilib.de._
import cilib.pso._
import cilib.exec._

object Mixed extends SafeApp {

  val bounds = Interval(-5.12, 5.12) ^ 30
  val env =
    Environment(cmp = Comparison.dominance(Min),
                eval = Eval.unconstrained((xs: NonEmptyList[Double]) =>
                  Feasible(cilib.benchmarks.Benchmarks.spherical(xs))))

  // Define the DE
  val de = DE.de(0.5, 0.5, DE.randSelection[Mem[Double], Double], 1, DE.bin[Position, Double])

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[Double], Double]
  val social = Guide.gbest[Mem[Double]]
  val gbestPSO = cilib.pso.Defaults.gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  // The swarm / population is the maximal set of features needed for the state,
  // so in the case of DE and PSO, the state from the particle is needed to be
  // managed
  val swarm =
    Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(bounds, 20)

  val combinedAlg: NonEmptyList[Entity[Mem[Double], Double]] => Entity[Mem[Double], Double] => Step[
    Double,
    Entity[Mem[Double], Double]] =
    collection =>
      x =>
        for {
          a <- gbestPSO(collection)(x)
          b <- de(collection)(a)
          // The entity might have moved, so the current pbest is no longer valid
          c <- cilib.pso.PSO.updatePBest(b)
        } yield c

  val alg = Iteration.sync(combinedAlg)

  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, alg, swarm).run(env).run(RNG.fromTime).toString)
}
