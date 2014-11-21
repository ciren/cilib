package cilib
package example

object QunatumPSO {

  def main(args: Array[String]): Unit = {
    //import scalaz._
    //import scalaz.syntax.bind._
    import scalaz.syntax.equal._
    import scalaz.std.anyVal._
    import PSO._

    def quantumPSO[S:Memory:Velocity:Charge](
      w: Double,
      c1: Double,
      c2: Double,
      cognitive: Guide[S,Double],
      social: Guide[S,Double]
    )(implicit C: Charge[S]): List[Particle[S,Double]] => Particle[S,Double] => Instruction[Particle[S,Double]] =
      collection => x => for {
        cog     <- cognitive(collection, x)
        soc     <- social(collection, x)
        p       <- if (C._charge.get(x._1) =/= 0.0) for {
          v  <- stdVelocity(x, soc, cog, w, c1, c2)
          p  <- stdPosition(x, v)
          p2 <- evalParticle(p)
          p3 <- updateVelocity(p2, v)
        } yield p3
        else for {
          p <- quantum(collection, x, soc, 2.0)
          p2 <- replace(x, p)
          p3 <- evalParticle(p2)
        } yield p3
        updated <- updatePBest(p)
      } yield updated

  }
}
