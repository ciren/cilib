package cilib
package pso

import _root_.scala.Predef.{any2stringadd => _}
import PSO._
import scalaz._
import spire.implicits._

object Defaults {

  // def pso[S,A:spire.math.Numeric](
  //   velocity: (Entity[S,A]) => Step[A,Position[A]],
  //   position: (Entity[S,A], Position[A]) => Step[A,Entity[S,A]]
  // ): List[Entity[S,A]] => Entity[S,A] => Step[A,Entity[S,A]] =
  //   collection => x => for {
  //     v <- velocity(x)
  //     p <- position(x, v)
  //     p2      <- evalParticle(p)
  //     p3      <- updateVelocity(p2, v)
  //     updated <- updatePBest(p3)
  //   } yield updated

  def gbest[S](
      w: Double,
      c1: Double,
      c2: Double,
      cognitive: Guide[S, Double],
      social: Guide[S, Double]
  )(implicit M: HasMemory[S, Double], V: HasVelocity[S, Double]): NonEmptyList[
    Particle[S, Double]] => Particle[S, Double] => Step[Double, Particle[S, Double]] =
    collection =>
      x =>
        for {
          cog <- cognitive(collection, x)
          soc <- social(collection, x)
          v <- stdVelocity(x, soc, cog, w, c1, c2)
          p <- stdPosition(x, v)
          p2 <- evalParticle(p)
          p3 <- updateVelocity(p2, v)
          updated <- updatePBest(p3)
        } yield updated

  def cognitive[S](
      w: Double,
      c1: Double,
      cognitive: Guide[S, Double]
  )(implicit M: HasMemory[S, Double], V: HasVelocity[S, Double]): NonEmptyList[
    Particle[S, Double]] => Particle[S, Double] => Step[Double, Particle[S, Double]] =
    collection =>
      x => {
        for {
          cog <- cognitive(collection, x)
          v <- singleComponentVelocity(x, cog, w, c1)
          p <- stdPosition(x, v)
          p2 <- evalParticle(p)
          p3 <- updateVelocity(p2, v)
          updated <- updatePBest(p3)
        } yield updated
    }

  def social[S](
      w: Double,
      c1: Double,
      social: Guide[S, Double]
  )(implicit M: HasMemory[S, Double], V: HasVelocity[S, Double]): NonEmptyList[
    Particle[S, Double]] => Particle[S, Double] => Step[Double, Particle[S, Double]] =
    collection =>
      x => {
        for {
          soc <- social(collection, x)
          v <- singleComponentVelocity(x, soc, w, c1)
          p <- stdPosition(x, v)
          p2 <- evalParticle(p)
          p3 <- updateVelocity(p2, v)
          updated <- updatePBest(p3)
        } yield updated
    }

  // This is only defined for the gbest topology because the "method" described in Edwin's
  // paper for alternate topologies _does not_ make sense. I can only assume that there is
  // some additional research that needs to be done to correctly create an algorithm to
  // apply gcpso to other topology structures. Stating that you simply "copy" something
  // into something else is not elegant and does not have a solid reasoning
  // attached to it.
  def gcpso[S](w: Double, c1: Double, c2: Double, cognitive: Guide[S, Double])(
      implicit M: HasMemory[S, Double],
      V: HasVelocity[S, Double],
      S: MonadState[StepS[Double, GCParams, ?], GCParams])
    : NonEmptyList[Particle[S, Double]] => Particle[S, Double] => StepS[Double,
                                                                        GCParams,
                                                                        Particle[S, Double]] =
    collection =>
      x => {
        val g = Guide.gbest[S]
        for {
          gbest <- StepS.pointS(g(collection, x))
          cog <- StepS.pointS(cognitive(collection, x))
          isBest <- StepS.pointS(Step.pure[Double, Boolean](x.pos eq gbest)) // Yes, we do want reference equality
          s <- S.get
          v <- StepS.pointS(
            if (isBest) gcVelocity(x, gbest, w, s)
            else stdVelocity(x, gbest, cog, w, c1, c2))
          p <- StepS.pointS(stdPosition(x, v))
          p2 <- StepS.pointS(evalParticle(p))
          p3 <- StepS.pointS(updateVelocity(p2, v))
          updated <- StepS.pointS(updatePBest(p3))
          failure <- StepS.pointS(
            Step.withCompare[Double, Boolean](
              Comparison.compare(x.pos, updated.pos).andThen(_ eq x.pos)))
          _ <- S.modify(params =>
            if (isBest) {
              params.copy(
                p =
                  if (params.successes > params.e_s) 2.0 * params.p
                  else if (params.failures > params.e_f) 0.5 * params.p
                  else params.p,
                failures = if (failure) params.failures + 1 else 0,
                successes = if (!failure) params.successes + 1 else 0
              )
            } else params)
        } yield updated
    }

  def charged[S: HasCharge](
      w: Double,
      c1: Double,
      c2: Double,
      cognitive: Guide[S, Double],
      social: Guide[S, Double],
      distance: (Position[Double], Position[Double]) => Double,
      rp: Double,
      rc: Double
  )(implicit M: HasMemory[S, Double], V: HasVelocity[S, Double]): NonEmptyList[
    Particle[S, Double]] => Particle[S, Double] => Step[Double, Particle[S, Double]] =
    collection =>
      x =>
        for {
          cog <- cognitive(collection, x)
          soc <- social(collection, x)
          accel <- acceleration(collection, x, distance, rp, rc)
          v <- stdVelocity(x, soc, cog, w, c1, c2)
          p <- stdPosition(x, v + accel)
          p2 <- evalParticle(p)
          p3 <- updateVelocity(p2, v)
          updated <- updatePBest(p3)
        } yield updated

  def nmpc[S](
      guide: Guide[S, Double]
  )(implicit M: HasMemory[S, Double])
    : NonEmptyList[Particle[S, Double]] => Particle[S, Double] => Step[Double,
                                                                       Particle[S, Double]] =
    collection =>
      x =>
        for {
          p <- evalParticle(x)
          p1 <- updatePBestBounds(p)
          co <- guide(collection, p1)
          p2 <- replace(p1, co)
          p3 <- evalParticle(p2)
          isBetter <- better(p1, p3)
        } yield if (isBetter) p1 else p3

  def crossoverPSO[S](
      guide: Guide[S, Double]
  )(implicit M: HasMemory[S, Double])
    : NonEmptyList[Particle[S, Double]] => Particle[S, Double] => Step[Double,
                                                                       Particle[S, Double]] =
    collection =>
      x =>
        for {
          p <- evalParticle(x)
          p1 <- updatePBestBounds(p)
          g <- guide(collection, p1)
          updated <- replace(p1, g)
        } yield updated

  /*import scalaz.syntax.applicative._

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
      Step.liftR(for {
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
 */
}
