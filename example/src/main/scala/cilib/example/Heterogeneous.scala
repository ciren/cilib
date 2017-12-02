package cilib
package example

import pso._
import pso.Heterogeneous._
import pso.PSO.{defaultGCParams,GCParams}

import syntax.algorithm._
import syntax.step._

import scalaz.{ Lens => _, _ }
import scalaz.std.stream._
import scalaz.effect._
import scalaz.effect.IO.putStrLn

import scalaz.syntax.foldable._

import spire.math.Interval
import spire.implicits._

object HPSO extends SafeApp {

  type BehaviourPool = Pool[Behaviour[PState, Double, Params]]
  type Entity = User[Particle[PState, Double], Behaviour[PState, Double, Params]]

  case class PState(mem: Mem[Double], stag: Int)
  object PState {
    implicit val myParticleMemLens = monocle.Lens[PState, Mem[Double]](_.mem)(b => a => a.copy(mem = b))

    implicit object PStateMemory extends HasMemory[PState,Double] {
      def _memory = PState.myParticleMemLens ^|-> HasMemory.memMemory._memory
    }

    implicit object PStateVelocity extends HasVelocity[PState,Double] {
      def _velocity = PState.myParticleMemLens ^|-> HasVelocity.memVelocity._velocity
    }

    implicit object PStatePBestStagnation extends HasPBestStagnation[PState] {
      def _pbestStagnation = monocle.Lens[PState, Int](_.stag)(b => a => a.copy(stag = b))
    }
  }

  val cognitive = Guide.pbest[PState, Double]
  val social = Guide.gbest[PState]

  case class Params(gc1: GCParams, gc2: GCParams)
  object Params {
    def gc1 = scalaz.Lens.lensu[Params, GCParams]((a,b) => a.copy(gc1=b), _.gc1)
    def gc2 = scalaz.Lens.lensu[Params, GCParams]((a,b) => a.copy(gc2=b), _.gc2)
  }

  // used to create pools with changing parameters
  def createBehaviours(n: Double, i: Int): List[Behaviour[PState,Double,Params]] = List(
    Defaults.gbest(linear(0.9, 0.4, n)(i), 1.496, 1.496, cognitive, social).flatMap(updateStagnation[PState,Double]).liftStepS[Params],
    Defaults.cognitive(linear(0.9, 0.4, n)(i), 1.496, cognitive).flatMap(updateStagnation[PState,Double]).liftStepS[Params],
    Defaults.social(linear(0.9, 0.4, n)(i), 1.496, social).flatMap(updateStagnation[PState,Double]).liftStepS[Params],
    Defaults.gcpso(linear(0.9, 0.4, n)(i), 1.496, 1.496, social).flatMap(x => updateStagnation[PState,Double](x).liftStepS).zoom(Params.gc1),
    Defaults.gcpso(linear(0.9, 0.4, n)(i), 1.496, 1.496, social).flatMap(x => updateStagnation[PState,Double](x).liftStepS).zoom(Params.gc2)
  )

  def linear(i: Double, f: Double, t: Double) = (x: Int) => (f - i) * (x / t) + i

  def behaviourProfile(p: BehaviourPool, xs: List[Entity]): List[Int] = {
    def one(acc: List[Int], p: BehaviourPool, xs: List[Entity]): List[Int] = {
      if (xs.isEmpty)
        acc
      else {
        val b = xs.head.item
        val ind = p.indexOf(b)
        one(acc.zipWithIndex.map { x =>
          if (x._2 == ind)
            (x._1 + 1, x._2)
          else
            x
        }.unzip[Int, Int](x => (x._1, x._2))._1, p, xs.tail)
      }
    }
    one(p.map(_ => 0), p, xs)
  }

  val particleBuilder = PSO.createParticle {
    (x: Position[Double]) => Entity(PState(Mem(x,x.map(_ / 10.0)), 0), x)
  } _

  val bounds = Interval(-5.12,5.12)^2

  val population =
    StepS.pointR[Double,BehaviourPool,List[Particle[PState,Double]]](Position.createCollection(particleBuilder)(bounds, 10))
      //.liftStepS[Double,BehaviourPool]
      .flatMap(assignRandom).zoom(StepS.lensIso.get(scalaz.Lens.firstLens[BehaviourPool,Params]))

  //val iteration = Iteration.syncS(dHPSO[PState,List,Double, Params](10))
  val iteration = Iteration.syncS(fkPSO[PState, Double, Params](2, 2))

  type SI[A] = StepS[Double, (BehaviourPool, Params), A]
  val algorithm = (l: List[Entity]) => {
    import StepS._
    implicit val SP = StateT.stateTMonadState[BehaviourPool, Step[Double, ?]]
    val poolLens = scalaz.Lens.firstLens[BehaviourPool, Params]

    Range.inclusive(1, 1000).toStream.map((_, iteration.run))
      .foldLeftM[SI, List[Entity]](l) {
      (a, c) => for {
        newPopulation <- c._2(a)
        oldPool       <- StepS(SP.get.zoom(poolLens))
        newPool       =  Pool.mkFromOldPool(
          oldPool.map(_.lastK(10)),
          createBehaviours(1000, c._1): _*
        )
        _             <- StepS(SP.put(newPool).zoom(poolLens))
      } yield Pool.updateUserBehaviours(oldPool, newPool)(newPopulation.toList)
    }
  }

  val spherical = Eval.unconstrained(cilib.benchmarks.Benchmarks.spherical[NonEmptyList, Double]).eval

  val finalResult = population
    .flatMap(algorithm)
    .run((Pool.mkPoolListScore(Pool.mkZeroPool(createBehaviours(1000, 0): _*)), Params(defaultGCParams, defaultGCParams)))
    .run(Comparison.quality(Min))(spherical)
    .run(RNG.fromTime)


  override val runc: IO[Unit] =
    putStrLn(behaviourProfile(finalResult._2._1._1, finalResult._2._2).toString)

}
