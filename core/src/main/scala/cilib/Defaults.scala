package cilib

import _root_.scala.Predef.{any2stringadd => _}
import scalaz._
import PSO._
import spire.algebra._
import spire.implicits._
import spire.syntax.module._

object Defaults {

  // The function below needs the guides for the particle, for the standard PSO update
  // and will eventually live in the simulator
  def gbest[S,F[_]:Traverse](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,F,Double],
    social: Guide[S,F,Double]
  )(implicit M: Memory[S,F,Double], V: Velocity[S,F,Double], MO: Module[F[Double],Double]): List[Particle[S,F,Double]] => Particle[S,F,Double] => Step[F,Double,Particle[S,F,Double]] =
    collection => x => for {
      cog     <- cognitive(collection, x)
      soc     <- social(collection, x)
      v       <- stdVelocity(x, soc, cog, w, c1, c2)
      p       <- stdPosition(x, v)
      p2      <- evalParticle(p)
      p3      <- updateVelocity(p2, v)
      updated <- updatePBest(p3)
    } yield updated

  def cognitive[S,F[_]:Traverse](
    w: Double,
    c1: Double,
    cognitive: Guide[S,F,Double]
  )(implicit M: Memory[S,F,Double], V: Velocity[S,F,Double], MO: Module[F[Double],Double]): List[Particle[S,F,Double]] => Particle[S,F,Double] => Step[F,Double,Particle[S,F,Double]] =
    collection => x => {
      for {
        cog     <- cognitive(collection, x)
        v       <- singleComponentVelocity(x, cog, w, c1)
        p       <- stdPosition(x, v)
        p2      <- evalParticle(p)
        p3      <- updateVelocity(p2, v)
        updated <- updatePBest(p3)
      } yield updated
    }

  def social[S,F[_]:Traverse](
    w: Double,
    c1: Double,
    social: Guide[S,F,Double]
  )(implicit M: Memory[S,F,Double], V: Velocity[S,F,Double], MO: Module[F[Double],Double]): List[Particle[S,F,Double]] => Particle[S,F,Double] => Step[F,Double,Particle[S,F,Double]] =
    collection => x => {
      for {
        soc     <- social(collection, x)
        v       <- singleComponentVelocity(x, soc, w, c1)
        p       <- stdPosition(x, v)
        p2      <- evalParticle(p)
        p3      <- updateVelocity(p2, v)
        updated <- updatePBest(p3)
      } yield updated
    }

  // This is only defined for the gbest topology because the "method" described in Edwin's
  // paper for alternate topologies _does not_ make sense. I can only assume that there is
  // some additional research that needs to be done to correctly create an algorithm to
  // apply gcpso to other topology structures. Stating that you simply "copy" something
  // into something else is not elegant and does not have a solid reasoning
  // attached to it.
  def gcpso[S,F[_]:Traverse](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,F,Double])(
    implicit M:Memory[S,F,Double], V:Velocity[S,F,Double],mod: Module[F[Double],Double]
  ): List[Particle[S,F,Double]] => Particle[S,F,Double] => StateT[Step[F,Double,?], GCParams, Particle[S,F,Double]] =
    collection => x => {
      val S = StateT.stateTMonadState[GCParams, Step[F,Double,?]]
      val hoist = StateT.StateMonadTrans[GCParams]
      val g = Guide.gbest[S,F]
      for {
        gbest   <- hoist.liftMU(g(collection, x))
        cog     <- hoist.liftMU(cognitive(collection, x))
        isBest  <- hoist.liftMU(Step.point[F,Double,Boolean](x.pos eq gbest))
        s       <- S.get
        v       <- hoist.liftMU(if (isBest) gcVelocity(x, gbest, w, s) else stdVelocity(x, gbest, cog, w, c1, c2)) // Yes, we do want reference equality
        p       <- hoist.liftMU(stdPosition(x, v))
        p2      <- hoist.liftMU(evalParticle(p))
        p3      <- hoist.liftMU(updateVelocity(p2, v))
        updated <- hoist.liftMU(updatePBest(p3))
        failure <- hoist.liftMU(Step.liftK[F,Double,Boolean](Fitness.compare(x.pos, updated.pos) map (_ eq x.pos)))
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

  def charged[S:Charge,F[_]:Traverse](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,F,Double],
    social: Guide[S,F,Double],
    distance: (Position[F,Double], Position[F,Double]) => Double,
    rp: Double,
    rc: Double
  )(implicit M:Memory[S,F,Double], V:Velocity[S,F,Double], MO: Module[F[Double],Double]): List[Particle[S,F,Double]] => Particle[S,F,Double] => Step[F,Double,Particle[S,F,Double]] =
    collection => x => for {
      cog     <- cognitive(collection, x)
      soc     <- social(collection, x)
      accel   <- acceleration(collection, x, distance, rp, rc)
      v       <- stdVelocity(x, soc, cog, w, c1, c2)
      p       <- stdPosition(x, v + accel)
      p2      <- evalParticle(p)
      p3      <- updateVelocity(p2, v)
      updated <- updatePBest(p3)
    } yield updated


}
