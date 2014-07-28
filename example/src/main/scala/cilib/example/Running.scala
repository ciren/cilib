package cilib
package example

import scalaz._
import Scalaz._

object Running {

  def main(args: Array[String]): Unit = {
    val sum = Problem.static((a: List[Double]) => Valid(a.sum))

    /* TODO:
     2. iteration scheme for both sync and async

     */
    /*def gbest(p: PSO.Particle[Mem[Double],Double]) = for {
      v <- PSO.updateVelocity(p, p._2, p._2, 0.8, 1.4, 1.4)
      r <- PSO.updatePosition(p, v)
     } yield PSO.evalParticle(r) map PSO.updatePBest[Mem[Double]]*/

    val cognitive: PSO.Guide[PSO.Particle[Mem[Double],Double]] = c => x => x
    val social: PSO.Guide[PSO.Particle[Mem[Double],Double]] = c => x => x

    val gbest = PSO.gbest[Mem[Double]](0.8, 1.4, 1.4, cognitive, social)
    val a: PSO.Instruction[RVar, List[PSO.Particle[Mem[Double],Double]]] =
      PSO.createCollection(20, 20).map(_.map(PSO.createParticle(x => (Mem(x,x),x))))
    val b = a flatMap (l => PSO.syncUpdate(l, gbest(l))) // This needs to be that the syncUpdate accepts the a?

    val randomized = PSO.run(b).run(RNG.fromTime).run
    val y = randomized.map(_ run sum)
    val z = y.map(_ map(_ map (x => {
      //val local: Reader[Opt, List[PSO.Particle[Mem[Double],Double]]] = x.sequenceU
      x run Min
    })))

    println(z)
  }
}
