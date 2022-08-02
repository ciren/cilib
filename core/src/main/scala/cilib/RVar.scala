package cilib

import _root_.scala.Predef.{ any2stringadd => _, _ }
import _root_.zio.prelude._
import zio.prelude.newtypes.Natural

/**
 * RVar represents the application of randomness as a type.
 */
object RVar {

  /** Use the given [[cilib.RNG]] to generate a value as well as the modified [[cilib.RNG]] */
  def apply0[A](f: RNG => (A, RNG)): RVar[A] =
    zio.prelude.fx.ZPure.modify(state => f(state))

  /** Argument flipped version of [[cilib.RVar#apply0]] */
  def apply[A](f: RNG => (RNG, A)): RVar[A] =
    zio.prelude.fx.ZPure.modify(state => f(state).swap)

  /** Lift a value into a [[cilib.RVar]], without applying any randomness */
  def pure[A](a: => A): RVar[A] =
    zio.prelude.fx.ZPure.modify(state => (a, state))

  /** Generate the next `A` value in the random stream */
  def next[A](implicit e: Generator.Generator[A]): RVar[A] =
    e.gen

  /** Generate the next `n` random `scala.Int` values */
  def ints(n: Int): RVar[List[Int]] =
    next[Int](Generator.IntGen).replicateM(n).map(_.toList)

  /** Generate the next `n` random `scala.Double` values */
  def doubles(n: Int): RVar[List[Double]] =
    next[Double](Generator.DoubleGen).replicateM(n).map(_.toList)

  /** Randomly select a value from [[cilib.NonEmptyVector]] */
  def choose[A](xs: NonEmptyVector[A]): RVar[A] =
    Dist
      .uniformInt(0, xs.size - 1)
      .map(i => xs.toChunk.lift(i).getOrElse(xs.head))

  /**
   * Shuffle the [[cilib.NonEmptyVector]] elements randomly.
   *
   * Implementation of [[http://okmij.org/ftp/Haskell/perfect-shuffle.txt Oleg Kiselgov's perfect shuffle]]
   */
  def shuffle[A](xs: NonEmptyVector[A]): RVar[NonEmptyVector[A]] = {
    sealed trait BinTree
    final case class Node(c: Int, left: BinTree, right: BinTree) extends BinTree
    final case class Leaf(element: A)                            extends BinTree

    def buildTree(zs: NonEmptyVector[A]): BinTree =
      growLevel(zs.map(Leaf(_): BinTree).toChunk.toList)

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
      ForEach[List].forEach(list)(x => Dist.uniformInt(0, x))
    }

    def extractTree(target: Int, tree: BinTree, next: BinTree => List[A]): List[A] =
      (target, tree, next) match {
        case (0, Node(_, Leaf(e), r), k)                 => e :: k(r)
        case (1, Node(2, l @ Leaf(_), Leaf(r)), k)       => r :: k(l)
        case (n, Node(c, l @ Leaf(_), r), k)             =>
          extractTree(n - 1, r, new_r => k(Node(c - 1, l, new_r)))
        case (n, Node(n1, l, Leaf(e)), k) if n + 1 == n1 => e :: k(l)
        case (n, Node(c, l @ Node(c1, _, _), r), k)      =>
          if (n < c1) extractTree(n, l, new_l => k(Node(c - 1, new_l, r)))
          else extractTree(n - c1, r, new_r => k(Node(c - 1, l, new_r)))
        case _                                           =>
          sys.error("???")
      }

    def local(t: BinTree, rs: List[Int]): List[A] =
      (t, rs) match {
        case (Leaf(e), Nil)        => List(e)
        case (tree, ri :: rothers) => extractTree(ri, tree, (t: BinTree) => local(t, rothers))
        case _                     => sys.error("impossible")
      }

    rseq(xs.length).map(r =>
      NonEmptyVector
        .fromIterableOption(local(buildTree(xs), r))
        .getOrElse(sys.error("Impossible - NonEmptyVector is guaranteed to be non-empty"))
    )
  }

  /** Alias for [[cilib.RVar#choices]] */
  def sample[F[+_]: ForEach, A](n: Natural, xs: F[A]): RVar[Option[List[A]]] =
    choices(n, xs)

  /** Select `n` distince elements from the given container type */
  def choices[F[+_], A](n: Natural, xs: F[A])(implicit F: ForEach[F]): RVar[Option[List[A]]] =
    if (F.size(xs) < n) RVar.pure(None)
    else {
      val length                   = F.size(xs)
      val backsaw: RVar[List[Int]] = {
        val l = length - 1 to length - Natural.unwrap(n) by -1
        ForEach[List].forEach(l.toList)(x => Dist.uniformInt(0, x))
      }

      backsaw.map { l =>
        def dropIndex(target: Int, l: List[A]): List[A] = {
          def innerDropIndex(count: Int, current: List[A]): List[A] =
            current match {
              case Nil     => Nil
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
            case _             => List.empty[A]
          }

        Some(go(l, F.toList(xs)))
      }
    }
}

object Generator {

  sealed trait Generator[A] {
    def gen: RVar[A]
  }

  /** Return the next `bits` as an Int */
  private def nextBits(bits: Int): RVar[Int] =
    RVar(_.next(bits))

  /**
   * Generate a random `Double`.
   *
   * The algorihm used is the same as that used within the Java SDK
   * [[https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/util/Random.html#nextDouble() `Random#nextDouble()`]]
   */
  implicit object DoubleGen extends Generator[Double] {
    def gen: RVar[Double] =
      zio.prelude.fx.ZPure.mapN(nextBits(26), nextBits(27)) { (a, b) =>
        ((a.toLong << 27) + b) / (1L << 53).toDouble
      }
  }

  /** Generate a 32-bit `Int` */
  implicit object IntGen extends Generator[Int] {
    def gen: RVar[Int] = nextBits(32)
  }

  /** Generate a 64-bit `Long` */
  implicit object LongGen extends Generator[Long] {
    def gen: RVar[Long] =
      for {
        upper <- nextBits(32)
        lower <- nextBits(32)
      } yield (upper.toLong << 32) + lower
  }

  /** Generate a random `Boolean` from a single random bit */
  implicit object BooleanGen extends Generator[Boolean] {
    def gen: RVar[Boolean] = nextBits(1).map(_ == 1)
  }
}
