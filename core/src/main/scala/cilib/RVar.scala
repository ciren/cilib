package cilib

import _root_.scala.Predef.{ any2stringadd => _, _ }
import eu.timepit.refined.api._
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import scalaz._, Scalaz._
import spire.implicits._
import spire.math._

import _root_.zio.prelude._

/**
RVar is essentially a newtype wrapper of the a State monad with the
state type fixed to RNG.

The wrapper is used to prevent access to the internal state monad,
thereby preventing the use of state modification functions (i.e.:
functions such as MonadState[RVar].modify and MonadState[RVar].puts)
 */
// sealed abstract class RVar[A](private val state: StateT[RNG, Trampoline, A]) { self: RVar[A] =>
//   def run(rng: RNG)  = state.run(rng).run
//   def eval(rng: RNG) = state.eval(rng).run
//   def exec(rng: RNG) = state.exec(rng).run

//   def map[B](f: A => B): RVar[B] =
//     new RVar[B](self.state.map(f)) {}

//   def flatMap[B](f: A => RVar[B]): RVar[B] =
//     new RVar[B](self.state.flatMap(f(_).state)) {}
// }

// sealed abstract class RVarInstances1 {
//   implicit val rvarCatsFunctor: cats.Functor[RVar] =
//     new cats.Functor[RVar] {
//       def map[A, B](fa: RVar[A])(f: A => B): RVar[B] =
//         fa.map(f)
//     }
// }

sealed abstract class RVarInstances0 /* extends RVarInstances1*/ {
  implicit val rvarMonad: scalaz.Monad[RVar] =
    new scalaz.Monad[RVar] {
      def bind[A, B](a: RVar[A])(f: A => RVar[B]) =
        a.flatMap(f)

      def point[A](a: => A) =
        RVar.pure(a)
    }
}

sealed abstract class RVarInstances extends RVarInstances0 {
  // implicit val rvarBindRec: BindRec[RVar] =
  //   new BindRec[RVar] {
  //     def bind[A, B](fa: RVar[A])(f: A => RVar[B]): RVar[B] =
  //       fa.flatMap(f)

  //     def map[A, B](fa: RVar[A])(f: A => B): RVar[B] =
  //       fa.map(f)

  //     def tailrecM[A, B](a: A)(f: A => RVar[A \/ B]): RVar[B] =
  //       f(a).flatMap {
  //         case -\/(a0) => tailrecM(a0)(f)
  //         case \/-(b)  => RVar.pure(b)
  //       }
  //   }
}


object RVar extends RVarInstances {

  def apply[A](f: RNG => (RNG, A)): RVar[A] =
    zio.prelude.fx.ZPure.modify(f)

  def pure[A](a: => A): RVar[A] =
    zio.prelude.fx.ZPure.succeed(a)

  def next[A](implicit e: Generator[A]): RVar[A] =
    e.gen

  def ints(n: Int): RVar[List[Int]] =
    next[Int](Generator.IntGen).replicateM(n).map(_.toList)

  def doubles(n: Int): RVar[List[Double]] =
    next[Double](Generator.DoubleGen).replicateM(n).map(_.toList)

  def choose[A](xs: scalaz.NonEmptyList[A]): RVar[A] =
    Dist
      .uniformInt(Interval(0, xs.size - 1))
      .map { i =>
        import monocle.Monocle._

        xs.list.toList.applyOptional(index(i)).getOption.getOrElse(xs.head)
      }

  // implementation of Oleg Kiselgov's perfect shuffle:
  // http://okmij.org/ftp/Haskell/perfect-shuffle.txt
  def shuffle[A](xs: scalaz.NonEmptyList[A]): RVar[scalaz.NonEmptyList[A]] = {
    sealed trait BinTree
    final case class Node(c: Int, left: BinTree, right: BinTree) extends BinTree
    final case class Leaf(element: A)                            extends BinTree

    def buildTree(zs: scalaz.NonEmptyList[A]): BinTree =
      growLevel(zs.list.toList.map(Leaf(_): BinTree))

    def growLevel(zs: List[BinTree]): BinTree =
      zs match {
        case x :: Nil => x
        case l        => growLevel(inner(l))
      }

    def inner(zs: List[BinTree]): List[BinTree] = {
      @annotation.tailrec
      def go(acc: List[BinTree], rs: List[BinTree]): List[BinTree] =
        rs match {
          case Nil              => acc
          case x :: Nil         => acc :+ x
          case e1 :: e2 :: rest => go(acc :+ join(e1, e2), rest)
        }

      go(List.empty, zs)
    }

    def join(e1: BinTree, e2: BinTree): BinTree =
      (e1, e2) match {
        case (l @ Leaf(_), r @ Leaf(_))                 => Node(2, l, r)
        case (l @ Node(ct, _, _), r @ Leaf(_))          => Node(ct + 1, l, r)
        case (l @ Leaf(_), r @ Node(ct, _, _))          => Node(ct + 1, l, r)
        case (l @ Node(ctl, _, _), r @ Node(ctr, _, _)) => Node(ctl + ctr, l, r)
      }

    def rseq(n: Int): RVar[List[Int]] = {
      val list = (n - 1 to 1 by -1).toList
      ForEach[List].forEach(list)(x => Dist.uniformInt(Interval(0, x)))
    }

    def extractTree(target: Int, tree: BinTree, next: BinTree => List[A]): List[A] =
      (target, tree, next) match {
        case (0, Node(_, Leaf(e), r), k)           => e :: k(r)
        case (1, Node(2, l @ Leaf(_), Leaf(r)), k) => r :: k(l)
        case (n, Node(c, l @ Leaf(_), r), k) =>
          extractTree(n - 1, r, new_r => k(Node(c - 1, l, new_r)))
        case (n, Node(n1, l, Leaf(e)), k) if n + 1 == n1 => e :: k(l)
        case (n, Node(c, l @ Node(c1, _, _), r), k) =>
          if (n < c1) extractTree(n, l, new_l => k(Node(c - 1, new_l, r)))
          else extractTree(n - c1, r, new_r => k(Node(c - 1, l, new_r)))
        case _ =>
          sys.error("???")
      }

    def local(t: BinTree, rs: List[Int]): List[A] =
      (t, rs) match {
        case (Leaf(e), Nil)        => List(e)
        case (tree, ri :: rothers) => extractTree(ri, tree, (t: BinTree) => local(t, rothers))
        case _                     => sys.error("impossible")
      }

    rseq(xs.list.length).map(r =>
      local(buildTree(xs), r).toNel
        .getOrElse(sys.error("Impossible - NonEmptyList is guaranteed to be non-empty"))
    )
  }

  def sample[F[_]: Foldable, A](n: Int Refined Positive, xs: F[A]): RVar[Option[List[A]]] =
    choices(n, xs)

  def choices[F[_], A](n: Int Refined Positive, xs: F[A])(implicit F: Foldable[F]): RVar[Option[List[A]]] =
    if (xs.length < n) RVar.pure(None)
    else {
      val length = F.length(xs)
      val backsaw: RVar[List[Int]] = {
        val l = length - 1 to length - n by -1
        ForEach[List].forEach(l.toList)(x => Dist.uniformInt(Interval(0, x)))
      }

      backsaw.map { l =>
        def dropIndex(target: Int, l: List[A]): List[A] = {
          def innerDropIndex(count: Int, current: List[A]): List[A] =
            current match {
              case Nil => Nil
              case x :: xs =>
                if (count == target) xs else x :: innerDropIndex(count + 1, xs)
            }

          innerDropIndex(0, l)
        }

        def go(indexes: List[Int], current: List[A]): List[A] =
          indexes match {
            case index :: rest =>
              current.lift(index) match {
                case Some(element) => element :: go(rest, dropIndex(index, current))
                case None          => List.empty[A]
              }
            case _ => List.empty[A]
          }

        go(l, xs.toList).some
      }
    }

}

sealed trait Generator[A] {
  def gen: RVar[A]
}

object Generator {

  private def nextBits(bits: Int): RVar[Int] =
    RVar(_.next(bits))

  implicit object DoubleGen extends Generator[Double] {
    def gen =
      zio.prelude.fx.ZPure.mapN(nextBits(26), nextBits(27)) { (a, b) =>
        ((a.toLong << 27) + b) / (1L << 53).toDouble
      }
  }

  implicit object IntGen extends Generator[Int] {
    def gen = nextBits(32)
  }

  implicit object LongGen extends Generator[Long] {
    def gen =
      for {
        upper <- nextBits(32)
        lower <- nextBits(32)
      } yield (upper.toLong << 32) + lower
  }

  implicit object BooleanGen extends Generator[Boolean] {
    def gen = nextBits(1).map(_ == 1)
  }
}
