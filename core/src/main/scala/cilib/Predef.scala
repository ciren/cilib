package cilib

import _root_.scala.Predef.{any2stringadd => _, _}
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

  case class GCParams(p: Double = 1.0, successes: Int = 0, failures: Int = 0, e_s: Double = 15, e_f: Double = 5)

  // This is only defined for the gbest topology because the "method" described in Edwin's
  // paper for alternate topologies _does not_ make sense. I can only assume that there is
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
        isBest  <- hoist.liftM(Instruction.point(x._2 eq gbest))
        v       <- hoist.liftM(if (isBest) gcVelocity(x, gbest, w, s) else updateVelocity(x, gbest, cog, w, c1, c2)) // Yes, we do want reference equality
        p       <- hoist.liftM(updatePosition(x, v))
        p2      <- hoist.liftM(evalParticle(p))
        updated <- hoist.liftM(updatePBest(p2))
        failure  <- hoist.liftM(Instruction.liftK(Fitness.compare(x._2, updated._2) map (_ eq x._2)))
        _       <- S.modify(params =>
          if (isBest) {
            params.copy(
              p = if (params.successes > params.e_s) 2.0*params.p else if (params.failures > params.e_f) 0.5*params.p else params.p,
              failures = if (failure) params.failures + 1 else 0,
              successes = if (!failure) params.successes + 1 else 0
            )
          } else params)
      } yield updated
    }

  def gcVelocity[S](entity: Particle[S,Double], nbest: Position[List,Double], w: Double, s: GCParams)(implicit V: Velocity[S]): Instruction[Pos[Double]] =
    Instruction.pointR(
      nbest traverse (_ => Dist.stdUniform.map(x => s.p * (1 - 2*x))) map (a =>
        -1.0 *: entity._2 + nbest + w *: V.velocityLens.get(entity._1) + a
      )
    )
}
