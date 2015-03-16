package cilib
package example

object QunatumPSO {
  import scalaz.std.list._
  import PSO._

  import monocle._
  import spire.algebra._
  import spire.implicits._

  import scalaz.Traverse

  case class QuantumState(b: Position[List,Double], v: Position[List,Double], charge: Double)

  object QuantumState {
    implicit object QSMemory extends Memory[QuantumState,List,Double]
                             with Velocity[QuantumState,List,Double]
                             with Charge[QuantumState] {
      def _memory = Lens[QuantumState,Position[List,Double]](_.b)(b => a => a.copy(b = b))
      def _velocity = Lens[QuantumState, Position[List,Double]](_.v)(b => a => a.copy(v = b))
      def _charge = Lens[QuantumState,Double](_.charge)(b => a => a.copy(charge = b))
    }
  }

  def main(args: Array[String]): Unit = {
    def quantumPSO[S,F[_]:Traverse](
      w: Double,
      c1: Double,
      c2: Double,
      cognitive: Guide[S,F,Double],
      social: Guide[S,F,Double]
    )(
      implicit C: Charge[S], V: Velocity[S,F,Double], M: Memory[S,F,Double], mod: Module[F[Double],Double]
    ): List[Particle[S,F,Double]] => Particle[S,F,Double] => Instruction[F,Double,Particle[S,F,Double]] =
      collection => x => {
        for {
          cog     <- cognitive(collection, x)
          soc     <- social(collection, x)
          p       <- if (C._charge.get(x._1) < 0.01) for {
            v  <- stdVelocity(x, soc, cog, w, c1, c2)
            p  <- stdPosition(x, v)
            p2 <- evalParticle(p)
            p3 <- updateVelocity(p2, v)
          } yield p3
          else for {
            p <- quantum(collection, x, soc, 60.0)
            p2 <- replace(x, p)
            p3 <- evalParticle(p2)
          } yield p3
          updated <- updatePBest(p)
        } yield updated
      }

    val interval = Interval(closed(0.0),closed(100.0))^2

    val cognitive: Guide[QuantumState,List,Double] = Guide.pbest
    val social: Guide[QuantumState,List,Double] = Guide.gbest

    val swarm = Position.createCollection(PSO.createParticle(x => (QuantumState(x,x.map(_ => 0.0), 0.0), x)))(interval, 40)
    val pop = Instruction.pointR[List,Double,List[Particle[QuantumState,List,Double]]](swarm)

    // 20% of the swarm are charged particles
    val pop2 = pop.map(coll => coll.take(8).map(x => (x._1.copy(charge = 0.05), x._2)) ++ coll.drop(8))

    val initial: RVar[(Problems.PeakState, Eval[List,Double])] =
      Problems.initPeaks(15, interval, 30.0, 70.0, 1.0, 12.0)

    val r = Iteration.sync(quantumPSO[QuantumState,List](0.729844, 1.496180, 1.496180, cognitive, social))//.repeat(1000)

    // Need to track both the problem and the current collection
  /*  Range(0, 1000).foldLeft((prob, pop2))((a, c) => {
      val ((peakState, (rng, peaks)), pop3) = a
      val w = pop3.flatMap(r.run)
      val x = w.run((Max, ))
      val (rng2, y) = {
        val a = Problems.movingPeaks(RVar.point(peaks))
        val (r, p) = a.run(rng)
        (r, x.run(p))
      }
      val (rng3, z: (Problem[List,Double],List[Particle[QuantumState,Double]])) = y.run(rng2)
g
      if (c == 1000) {
        println(z.map(_.map(x => {
          x._1.v + ":" + x._2.pos.mkString(",") + "," + x._2.fit.getOrElse(0.0)
        }).mkString("\n")))
      }

      if (c % 100 == 0) {
        val (ps, r) = Problems.modifyPeaks(peaks).run(peakState)
        val (rng4, a) = r.run(rng3)

        println("Changing environment peaks: " + a)

        ((ps, r.run(rng3)), Instruction.point(z._2))
      }
      else
        ((peakState, (rng3, peaks)), Instruction.point(z._2))
    })*/
  }
}
