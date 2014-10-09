package cilib

import scalaz._
import scalaz.syntax.equal._
import scalaz.std.list._
import PSO._

object Predef {

  // The function below needs the guides for the particle, for the standard PSO update
  // and will eventually live in the simulator
  def gbest[S:Memory:Velocity](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double],
    social: Guide[S,Double]
  ): List[Particle[S, Double]] => Particle[S,Double] => Instruction[Particle[S,Double]] =
    collection => x => for {
      cog     <- cognitive(collection, x)
      soc     <- social(collection, x)
      v       <- updateVelocity(x, soc, cog, w, c1, c2)
      p       <- updatePosition(x, v)
      p2      <- evalParticle(p)
      updated <- updatePBest(p2)
    } yield updated

  case class GCParams(p: Double, successes: Int, failures: Int, e_s: Double, e_f: Double)

  // This is only defined for the gbest topology because the "method" described in Edwin's
  // paper for alternate topologies does not make sense. I can only assume that there is
  // some additional research that needs to be done to correctly create an algorithm to
  // apply gcpso to other topology structures. Stating that you simply "copy" something
  // into something else is not elegant and does not have a solid reasoning
  // attached to it.
  def gcpso[S: Velocity: Memory](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double]
  ): List[Particle[S,Double]] => Particle[S,Double] => StateT[Instruction, GCParams, Particle[S,Double]] =
    collection => x => {
      import scalaz.StateT._
      val S = StateT.stateTMonadState[GCParams, Instruction]
      val hoist = StateT.StateMonadTrans[GCParams]
      for {
        s       <- S.get
        gbest   <- hoist.liftM(Guide.nbest(collection, x))
        cog     <- hoist.liftM(cognitive(collection, x))
        v       <- hoist.liftM(if (x._2 eq gbest) gcVelocity(x, s) else updateVelocity(x, gbest, cog, w, c1, c2)) // Yes, we do want reference equality
        p       <- hoist.liftM(updatePosition(x, v))
        p2      <- hoist.liftM(evalParticle(p))
        updated <- hoist.liftM(updatePBest(p2))
      } yield updated
    }

  def gcVelocity[S](entity: Particle[S,Double], s: GCParams): Instruction[Pos[Double]] = ???

  // def updateVelocity[S](entity: (S,Position[List,Double]), social: RVar[Position[List,Double]], cognitive: RVar[Position[List, Double]], w: Double, c1: Double, c2: Double)(implicit V: Velocity[S]): Instruction[Position[List,Double]] = {

}
