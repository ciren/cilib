package cilib

import scalaz._
import scalaz.std.list._

import scalaz.syntax.traverse._

import monocle.syntax._

import spire.implicits._

import syntax.step._

object Heterogeneous {

  sealed abstract class PoolItem[A] {

    import PoolItem._

    val item: A

    def score = this match {
      case PoolItemR(_, -\/(s)) => s
      case PoolItemR(_, \/-(s)) => s.foldLeft(0.0)(_ + _)
    }

    def reward(a: Double): PoolItem[A] = this match {
      case PoolItemR(i, -\/(s)) => PoolItemR(i, -\/(s + a))
      case PoolItemR(i, _ @ \/-(x :: xs)) => PoolItemR(i, \/-(a + x :: xs))
      case PoolItemR(i, \/-(Nil)) => PoolItemR(i, \/-(List(a)))
    }

    def split = this match {
      case PoolItemR(i, -\/(s)) => PoolItemR(i, \/-(List(s)))
      case _ => this
    }

    def lastK(k: Int) = this match {
      case PoolItemR(i, \/-(s)) => PoolItemR(i, \/-(0.0 :: s.take(k)))
      case _ => this //shouldn't do this, is there a cleaner way?
    }

    def change(a: A) = this match {
      case PoolItemR(i, s) => PoolItemR(a, s)
      case _ => this
    }
  }

  object PoolItem {

    private final case class PoolItemR[A](item: A, s: Double \/ List[Double]) extends PoolItem[A]

    def apply[A](item: A, score: Double): PoolItem[A] = PoolItemR(item, -\/(score))

  }

  object Pool {

    // Assign given score to each behaviour
    def mkPool[A](w: Double, xs: A*) = xs.map{ PoolItem(_, w) }.toList

    // Assign 1/|xs| as score
    def mkEvenPool[A](xs: A*) = mkPool(1.0 / xs.length, xs: _*)

    // Assign 0 as score
    def mkZeroPool[A](xs: A*) = mkPool(0.0, xs: _*)

    // Convert given pool scores from double to list
    def mkPoolListScore[A](pool: Pool[A]) = pool map { _.split }

    // Create new behaviours with same scores as old behaviours
    def mkFromOldPool[A](oldP: Pool[A], xs: A*) = (oldP zip xs.toList).map { case (x, y) =>
      x.change(y)
    }

    // Give entities updated behaviours
    def updateUserBehaviours[A, B](oldP: Pool[B], newP: Pool[B])(xs: List[User[A,B]]) = {
      val pool = oldP zip newP
      xs map { x =>
        pool.dropWhile(a => a._1.item != x.item.item)
          .headOption.map(y => User(x.user, y._2))
          .getOrElse(sys.error("Behaviour not in behaviour pool."))
      }
    }

  }

  case class User[A, B](user: A, item: PoolItem[B])

  type Pool[A] = List[PoolItem[A]]
  type Behaviour[S, A, B] = List[Entity[S, A]] => Entity[S, A] => StepS[A, B, Entity[S, A]]
  type SI[S, A, B] = StepS[A, S, B]
  type HEntity[S, A, B] = User[Entity[S, A], B]
  type HEntityB[S, A, B] = HEntity[S, A, Behaviour[S, A, B]]

  // Helper functions
  def updateStagnation[S, A](p: Entity[S,A])(implicit M: Memory[S,A], S: PBestStagnation[S]): Step[A, Entity[S,A]] = {
    val pbest = (p.state applyLens M._memory).get
    val stagnationL = p.state applyLens S._pbestStagnation
    val stagnation = stagnationL.get

    Step.liftK(Comparison.compare(p.pos, pbest).map(x =>
      Entity(stagnationL set (if (pbest eq p.pos) 0 else stagnation + 1), p.pos)
    ))
  }

  // Create population with behaviours
  def assignRandom[A, B, C]: List[A] => StepS[C,Pool[B],List[User[A, B]]] =
    xs => for {
      pool       <- StepS.get[C,Pool[B]]
      collection <- StepS.pointR(xs.traverse {
        x => RVar.shuffle(pool).map {
          _.headOption.map(User(x,_))
            .getOrElse(sys.error("Empty behaviour pool."))
        }
      })
    } yield collection

  // Behaviour changing schedules will prolly change to RVar[Boolean] or maybe even Instruction
  def pbestStagnated[S, A, B](threshold: Int)(implicit S: PBestStagnation[S]): HEntity[S,A,B] => Boolean =
    x => (x.user.state applyLens S._pbestStagnation).get % threshold === 0

  def resetStagnation[S, A, B](implicit S: PBestStagnation[S]): HEntity[S,A,B] => HEntity[S,A,B] =
    x => User(Entity((x.user.state applyLens S._pbestStagnation) set 0, x.user.pos), x.item)

  // Select from behaviour pool
  def poolSelectRandom[A, B, C]: List[User[A, B]] => User[A, B] => StepS[C,Pool[B],User[A, B]] =
    xs => x => for {
      pool <- StepS.get[C,Pool[B]]
      user <- StepS.pointR(RVar.shuffle(pool).map {
        _.headOption.map(User(x.user, _)).getOrElse(sys.error("Empty behaviour pool."))
      })
    } yield user

  def poolSelectTournament[A, B, C](k: Int): List[User[A, B]] => User[A, B] => StepS[C,Pool[B],User[A, B]] =
    xs => x => for {
      pool <- StepS.get[C,Pool[B]]
      user <- StepS.pointR(RVar.shuffle(pool).map(_.take(k)).map {
        bs => {
          val tournament = bs.take(k)
          val head = tournament.headOption.getOrElse(sys.error("Empty behaviour pool."))

          bs.foldLeft(User(x.user, head)) {
            (a, b) => User(x.user, if (a.item.score > b.score) a.item else b)
          }
        }
      })
    } yield user

  // Use selected behaviour
  def useBehaviour[S, A, B]: List[HEntityB[S, A, B]] => HEntityB[S, A, B] => StepS[A, B, HEntityB[S, A, B]] =
    collection => x => x.item.item(collection.map(_.user))(x.user).map(User(_, x.item))

  // Update pool
  def incrementOne[S, A, B]: HEntityB[S, A, B] => HEntityB[S, A, B] => StepS[A,Pool[Behaviour[S,A,B]],Pool[Behaviour[S,A,B]]] =
    oldP => newP => for {
      pool <- StepS.get[A,Pool[Behaviour[S,A,B]]]
      newP <- StepS.liftK {
        Comparison.compare(oldP.user.pos, newP.user.pos).map { best =>
          if (best eq newP.user.pos)
            pool.map { b => if (b eq newP.item) b.reward(1.0) else b }
          else
            pool
        }
      }
    } yield newP

  def nullPoolUpdate[S, A, B]: HEntityB[S, A, B] => HEntityB[S, A, B] => StepS[A,Pool[Behaviour[S,A,B]],Pool[Behaviour[S,A,B]]] =
    oldP => newP => StepS.get

  // Algorithms
  def genericHPSO[S, A, B](
    schedule: HEntityB[S, A, B] => Boolean,
    selector: List[HEntityB[S, A, B]] => HEntityB[S, A, B] => StepS[A, Pool[Behaviour[S, A, B]], HEntityB[S, A, B]],
    updater: HEntityB[S, A, B] => HEntityB[S, A, B] => StepS[A, Pool[Behaviour[S, A, B]], Pool[Behaviour[S, A, B]]]
  ): List[HEntityB[S, A, B]] => HEntityB[S, A, B] => StepS[A, (Pool[Behaviour[S, A, B]], B), HEntityB[S, A, B]] =
    collection => x => {
      val S = StepS.stepSMonadState[A, (Pool[Behaviour[S,A,B]], B)]
      val pool = Lens.lensu[(Pool[Behaviour[S,A,B]], B), Pool[Behaviour[S,A,B]]]((a,b) => (b, a._2), _._1)
      val params = Lens.lensu[(Pool[Behaviour[S,A,B]], B), B]((a,b) => (a._1, b), _._2)

      for {
        p1   <- S.ifM(S.point(schedule(x)), selector(collection)(x).zoom(pool), S.point(x))
        p2   <- useBehaviour(collection)(p1).zoom(params)
        newP <- updater(x)(p2).zoom(pool)
        _    <- StateT.stateTMonadState[Pool[Behaviour[S,A,B]], Step[A, ?]].put(newP).zoom(pool)
      } yield p2
    }

  def dHPSO[S: PBestStagnation, A, B](stagThreshold: Int):
      List[HEntityB[S, A, B]] => HEntityB[S, A, B] => StepS[A, (Pool[Behaviour[S, A, B]], B), HEntityB[S, A, B]]
  = genericHPSO(
      pbestStagnated(stagThreshold),
      xs => x => poolSelectRandom(xs)(x).map(resetStagnation),
      nullPoolUpdate
    )

  def fkPSO[S: PBestStagnation, A, B](stagThreshold: Int, tournSize: Int):
      List[HEntityB[S, A, B]] => HEntityB[S, A, B] => StepS[A, (Pool[Behaviour[S, A, B]], B), HEntityB[S, A, B]]
  = genericHPSO(
    pbestStagnated(stagThreshold),
    xs => x => poolSelectTournament(tournSize)(xs)(x).map(resetStagnation),
    incrementOne
  )

}
