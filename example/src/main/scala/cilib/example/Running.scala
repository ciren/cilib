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
    val cognitive: PSO.Guide[PSO.Particle[Mem[Double],Double]] = c => x => x
    val social: PSO.Guide[PSO.Particle[Mem[Double],Double]] = c => x => x

    val gbest = PSO.gbest[Mem[Double]](0.8, 1.4, 1.4, cognitive, social)
    val a = PSO.Instruction.pointR(PSO.createCollection(20, 20).map(_.map(PSO.createParticle(x => (Mem(x,x),x)))))
    val b: PSO.Instruction[List,Double,List[PSO.Particle[Mem[Double],Double]]] = a flatMap (l => PSO.syncUpdate(l, gbest(l))) // This needs to be that the syncUpdate accepts the a?

    val minimized = b.run(Min)
    val y = minimized run sum
    val z = y.run(RNG.fromTime).run

    println(z)
  }
}
