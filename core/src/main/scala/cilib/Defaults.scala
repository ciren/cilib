package cilib

import _root_.scala.Predef.{any2stringadd => _}
import scalaz._
import PSO._
import spire.algebra._
import spire.implicits._

object Defaults {

  def gbest[S/*,F[_]:Traverse*/](
    //eval: Particle[S,Double] => Step[Double,Entity[S,Double]])(
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double],
    social: Guide[S,Double]
  )(implicit M: Memory[S,Double], V: Velocity[S,Double], MO: Module[Position[Double],Double]): List[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]] =
    collection => x => for {
      cog     <- cognitive(collection, x)
      soc     <- social(collection, x)
      v       <- stdVelocity(x, soc, cog, w, c1, c2)
      p       <- stdPosition(x, v)
      p2      <- evalParticle(p)
      //p2      <- eval(p)
      p3      <- updateVelocity(p2, v)
      updated <- updatePBest(p3)
    } yield updated

  // def stdGBest[S](implicit M: Memory[S,Double], V: Velocity[S,Double], MO: Module[Position[Double],Double]) =
  //   gbest[S](evalParticle) _

  def cognitive[S/*,F[_]:Traverse*/](
    w: Double,
    c1: Double,
    cognitive: Guide[S,Double]
  )(implicit M: Memory[S,Double], V: Velocity[S,Double], MO: Module[Position[Double],Double]): List[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]] =
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

  def social[S/*,F[_]:Traverse*/](
    w: Double,
    c1: Double,
    social: Guide[S,Double]
  )(implicit M: Memory[S,Double], V: Velocity[S,Double], MO: Module[Position[Double],Double]): List[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]] =
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
  def gcpso[S/*,F[_]:Traverse*/](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double])(
    implicit M:Memory[S,Double], V:Velocity[S,Double],mod: Module[Position[Double],Double]
  ): List[Particle[S,Double]] => Particle[S,Double] => StateT[Step[Double,?], GCParams, Particle[S,Double]] =
    collection => x => {
      val S = StateT.stateTMonadState[GCParams, Step[Double,?]]
      val hoist = StateT.StateMonadTrans[GCParams]
      val g = Guide.gbest[S]
      for {
        gbest   <- hoist.liftMU(g(collection, x))
        cog     <- hoist.liftMU(cognitive(collection, x))
        isBest  <- hoist.liftMU(Step.point[Double,Boolean](x.pos eq gbest))
        s       <- S.get
        v       <- hoist.liftMU(if (isBest) gcVelocity(x, gbest, w, s) else stdVelocity(x, gbest, cog, w, c1, c2)) // Yes, we do want reference equality
        p       <- hoist.liftMU(stdPosition(x, v))
        p2      <- hoist.liftMU(evalParticle(p))
        p3      <- hoist.liftMU(updateVelocity(p2, v))
        updated <- hoist.liftMU(updatePBest(p3))
        failure <- hoist.liftMU(Step.liftK[Double,Boolean](Fitness.compare(x.pos, updated.pos) map (_ eq x.pos)))
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

  def charged[S:Charge/*,F[_]:Traverse*/](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double],
    social: Guide[S,Double],
    distance: (Position[Double], Position[Double]) => Double,
    rp: Double,
    rc: Double
  )(implicit M:Memory[S,Double], V:Velocity[S,Double], MO: Module[Position[Double],Double]): List[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]] =
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


import scalaz.syntax.applicative._

  def quantumBehavedOriginal2004[S](
    social: Guide[S,Double],
    g: Double
  )(implicit M:Memory[S,Double], V:Velocity[S,Double], MO: Module[Position[Double],Double]
  ): List[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]] =
    collection => x => for {
      updated <- updatePBest(x)
      nbest   <- social(collection, x)
      y       <- quantumBehavedOriginal2004thing(x, nbest, g)
      
    } yield x.copy(pos = y)

  def quantumBehavedOriginal2004thing[S](
      entity: Particle[S,Double],
      nbest: Position[Double],
      g: Double
    )(implicit M:Memory[S,Double], MO: Module[Position[Double],Double], F:Field[Double]): Step[Double,Position[Double]] =
      Step.pointR(for {
        c1 <- Dist.stdUniform.replicateM(entity.pos.pos.size).map(Position.hack) // RVar[List[Double]]
        c2 <- Dist.stdUniform.replicateM(entity.pos.pos.size).map(Position.hack)
        (p_i: Position[Double]) = M._memory.get(entity.state).zip(c1).map(x => x._1 * x._2)
        p_g = nbest.zip(c2).map(x => x._1 * x._2)
        p_tot = p_i + p_g
        cs = c1 + c2
        (p: Position[Double]) = p_tot.zip(cs).map(x => x._1 / x._2)
        u <- Dist.stdUniform.replicateM(entity.pos.pos.size).map(Position.hack)
        (l: Position[Double]) = (entity.pos - p).map(math.abs(_))
        //L <- (1.0 / g) *: ((entity.pos - p).map(math.abs(_)))
        choice <- Dist.stdUniform.replicateM(entity.pos.pos.size).map(_.map(_ > 0.5)).map(Position.hack)
        lnu = u.map(x => math.log(1 / x))
        (l2: Position[Double]) = l.zip(lnu).map(x => x._1 * x._2)
        r = choice.zip(p.zip(l2)).map(b => if (b._1) b._2._1 - b._2._2 else b._2._1 + b._2._2) //((b, p, l) => if (b) p - l else p + l)
      } yield r)
      //newPart <- entity.copy(pos = r) // Positon lens

}
