package cilib
package example

import scalaz._
import Scalaz._

object Running {

  def main(args: Array[String]): Unit = {
    val sum = Problem.static((a: List[Double]) => Valid(a.sum))

    // Define a normal GBest PSO and run it for a single iteration
    val cognitive: PSO.Guide[PSO.Particle[Mem[Double],Double]] = c => x => RVar.point(x)
    val social: PSO.Guide[PSO.Particle[Mem[Double],Double]] = c => x => RVar.point(x)

    val gbest = PSO.gbest[Mem[Double]](0.8, 1.4, 1.4, cognitive, social)
    val a = Instruction.pointR(
      PSO.createCollection(20, 20).map(_.map(PSO.createParticle(x => (Mem(x,x),x))))
    )
    val b2 = Scheme.sync(gbest)
    val w = a flatMap (l => b2.run(l))

    val m = w.run(Min)
    val y = m run sum
    val z = y.run(RNG.fromTime)

    println(z)

    // Run the above algorithm 1000 times, without any parameter changes
    val r = b2.repeat(1000)
    val w2 = a flatMap (r run _)
    val m2 = w2 run Min
    val y2 = m2 run sum
    val z2 = y2.run(RNG.fromTime)

    println(z2)

    // Now run the a PSO algorithm but the cognitive and social coefficients are adjusted from 2.0 to 1.4 over 1000 iterations
    import scalaz._
    import Scalaz._
    def linearDecrease(n: Int, d: Int) = n / d.toDouble


    // val functions = (1 to 1000).map(x =>
    //   (l: List[PSO.Particle[Mem[Double],Double]]) =>
    //   PSO.syncUpdate(l, PSO.gbest(0.8, linearDecrease(x, 1000), linearDecrease(x, 1000), cognitive, social))
    // )

    //functions.foldLeft()
  }
}
