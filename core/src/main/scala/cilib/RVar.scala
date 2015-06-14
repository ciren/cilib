package cilib

import _root_.scala.Predef.{any2stringadd => _, _}
import scalaz._
import scalaz.syntax.applicative._
import scalaz.syntax.traverse._
import scalaz.Free._

sealed abstract class RVar[A] {
  def trampolined(s: RNG): Trampoline[(RNG, A)]

  def run(initial: RNG): (RNG,A) = trampolined(initial).run
  def exec(s: RNG) = run(s)._1
  def eval(s: RNG) = run(s)._2

  def map[B](f: A => B): RVar[B] =
    RVar.trampolined(rng => trampolined(rng).map(a => (a._1, f(a._2))))

  def flatMap[B](f: A => RVar[B]): RVar[B] =
    RVar.trampolined(rng => trampolined(rng).flatMap((a: (RNG,A)) => f(a._2).trampolined(a._1)))
}

object RVar {
  def apply[A](f: RNG => (RNG,A)) = new RVar[A] {
    def trampolined(s: RNG) = Trampoline.delay(f(s))
  }

  def trampolined[A](f: RNG => Trampoline[(RNG,A)]) = new RVar[A] {
    def trampolined(s: RNG) = Trampoline.suspend(f(s))
  }

  def point[A](a: => A): RVar[A] =
    apply(r => (r,a))

  implicit val monad: Monad[RVar] =
    new Monad[RVar] {
      def bind[A, B](a: RVar[A])(f: A => RVar[B]) =
        a flatMap f
      def point[A](a: => A) =
        RVar.point(a)
    }

  def next[A](implicit e: Generator[A]): RVar[A] =
    e.gen

  def ints(n: Int) =
    next[Int](Generator.IntGen) replicateM n

  def doubles(n: Int) =
    next[Double](Generator.DoubleGen) replicateM n

  def choose[A](xs: NonEmptyList[A]) =
    Dist.uniformInt(0, xs.size - 1) map { xs.list.apply(_) }

  // implementation of Oleg Kiselgov's perfect shuffle:
  // http://okmij.org/ftp/Haskell/perfect-shuffle.txt
  def shuffle[A](xs: List[A]): RVar[List[A]] = {
    sealed trait BinTree
    case class Node(c: Int, left: BinTree, right: BinTree) extends BinTree
    case class Leaf(element: A) extends BinTree

    def fix[AA, B](f: (AA => B) => (AA => B)): AA => B = f(fix(f))(_)

    def buildTree(zs: List[A]) = {
      def join(left: BinTree, right: BinTree) = (left, right) match {
        case (Leaf(_), Leaf(_)) => Node(2, left, right)
        case (Node(ct, _, _), Leaf(_)) => Node(ct + 1, left, right)
        case (Leaf(_), Node(ct, _, _)) => Node(ct + 1, left, right)
        case (Node(ctl, _, _), Node(ctr, _, _)) => Node(ctl + ctr, left, right)
      }

      def inner(l: List[BinTree]): List[BinTree] = l match {
        case Nil => Nil
        case e :: Nil => e :: Nil
        case e1 :: e2 :: es => join(e1, e2) :: inner(es)
      }

      fix[List[BinTree], List[BinTree]](f => a => if (a.length == 1) a else f(inner(a)))(zs.map(Leaf(_)))
    }

    def extractTree(n: Int, t: BinTree): (A, BinTree) = (n, t) match {
      case (0, Node(_, Leaf(e), r)) => (e, r)
      case (1, Node(2, Leaf(l), Leaf(r))) => (r, Leaf(l))
      case (n, Node(c, Leaf(l), r)) =>
        val (e, r2) = extractTree(n - 1, r)
        (e, Node(c - 1, Leaf(l), r2))
      case (n, Node(c, l, Leaf(e))) if n + 1 == c => (e, l)
      case (n, Node(c, l @ Node(c1, _, _), r)) =>
        if (n < c1) {
          val (e, l2) = extractTree(n, l)
          (e, Node(c - 1, l2, r))
        } else {
          val (e, r2) = extractTree(n - c1, r)
          (e, Node(c - 1, l, r2))
        }
      case (_, _) => sys.error("[extractTree] impossible")
    }

    def shuffleTree(l: BinTree, rs: List[Int]): List[A] = (l, rs) match {
      case (Leaf(e), Nil) => e :: Nil
      case (tree, r :: rs) =>
        val (b, rest) = extractTree(r, tree)
        b :: shuffleTree(rest, rs)
      case (_, _) => sys.error("[shuffle] called with lists of different lengths")
    }

    import scalaz.std.list._

    val length = xs.length - 1
    val randoms = (0 until length).foldLeft(List.empty[RVar[Int]])((a, c) => Dist.uniformInt(0, length - c) :: a).reverse.sequence // TODO / FIX: Remove the need to reverse!

    xs match {
      case Nil => RVar.point(xs)
      case x => randoms map { r => shuffleTree(buildTree(x).head, r) } // head is safe because build tree will always result in [node]
    }
  }

  def sample[A](n: Int, xs: List[A]) =
    choices(n, xs)

  def choices[A](n: Int, xs: List[A]): MaybeT[RVar, List[A]] =
    MaybeT {
      if (xs.length < n) RVar.point(Maybe.empty)
      else {
        import scalaz.syntax.foldable._
        import scalaz.std.list._
        type M[B] = StateT[RVar, List[A], B]

        ((0 until xs.size).toList.reverse.take(n).foldLeftM[M, List[A]](List.empty) {
          case (s, a) => StateT[RVar, List[A], List[A]] {
            currentList => Dist.uniformInt(0, a).map(r => {
              val selected = currentList(r)
              (currentList diff List(selected), selected :: s)
            })
          }
        } eval xs).map(Maybe.just(_))
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
      (nextBits(26) |@| nextBits(27)) { (a,b) =>
        ((a.toLong << 27) + b) / (1L << 53).toDouble
      }
  }

  implicit object IntGen extends Generator[Int] {
    def gen = nextBits(32)
  }

  implicit object BooleanGen extends Generator[Boolean] {
    def gen = nextBits(1) map (_ == 1)
  }
}


object Dist {
  import RVar._
  import scalaz.std.AllInstances._

  val stdUniform = next[Double]
  val stdNormal = gaussian(0.0, 1.0)
  val stdCauchy = cauchy(0.0, 1.0)
  val stdExponential = exponential(positive(1.0))
  val stdGamma = gamma(2, 2.0)
  val stdLaplace = laplace(0.0, 1.0)
  val stdLognormal = lognormal(0.0, 1.0)

  /** Generate a discrete uniform value in [from, to]. Note that the upper bound is *inclusive* */
  def uniformInt(from: Int, to: Int) =
    next[Int].map(x => {
      val (ll, hh) = if (to < from) (to, from) else (from, to)
      val diff = hh.toLong - ll.toLong
      if (diff == 0) ll
      else (ll.toLong + (math.abs(x.toLong) % (diff + 1))).toInt
    })

  def uniform(a: Double, b: Double) =
    stdUniform map { x => a + x * (b - a) }

  def cauchy(l: Double, s: Double) =
    stdUniform map { x => l + s * math.tan(math.Pi * (x - 0.5)) }

  def gamma(k: Double, theta: Double) = {
    implicit def doubleInstance: Monoid[Double] = new Monoid[Double] {
      def zero = 0.0
      def append(f1: Double, f2: => Double) = f1 + f2
    }

    val n = k.toInt
    val gammaInt = (stdUniform replicateM n).map(_.foldMap(x => -math.log(x)))
    val gammaFrac = {
      val delta = k - n

      def inner: RVar[Double] =
        for {
          u1 <- stdUniform
          u2 <- stdUniform
          u3 <- stdUniform
          (zeta, eta) = {
            val v0 = math.E / (math.E + delta)
            if (u1 <= v0) {
              val zeta = math.pow(u2, 1.0 / delta)
              val eta = u3 * math.pow(zeta, delta - 1)
              (zeta, eta)
            } else {
              val zeta = 1 - math.log(u2)
              val eta = u3 * math.exp(-zeta)
              (zeta, eta)
            }
          }
          r <- if (eta > math.pow(zeta, delta - 1) * math.exp(-zeta)) inner else RVar.point(zeta)
        } yield r

      inner
    }

    (gammaInt |@| gammaFrac) { (a,b) => (a + b) * theta }
  }

  def exponential(l: Maybe[Double @@ Tags.Positive]) =
    l.map(Tag.unwrap(_)).cata(x => stdUniform map { math.log(_) / x }, RVar.point(0.0))

  def laplace(b0: Double, b1: Double) =
    stdUniform map { x =>
      val rr = x - 0.5
      b0 - b1 * (math.log(1 - 2 * rr.abs)) * rr.signum
    }

  def lognormal(mean: Double, dev: Double) =
    stdNormal map (x => math.exp(mean + dev * x))

  def dirichlet(alphas: List[Double]) =
    alphas.traverse(gamma(_, 1)).map(ys => {
      val sum = ys.sum
      ys.map(_ / sum)
    })

  import scalaz.syntax.monad._

  private def DRandNormalTail(min: Double, ineg: Boolean): RVar[Double] = {
    def sample =
      (stdUniform.map(x => math.log(x) / min) |@| stdUniform.map(math.log(_))) { Tuple2.apply }

    sample.iterateUntil(v => -2.0 * v._2 >= v._1 * v._1).map(x => if (ineg) x._1 - min else min - x._1)
  }

  private val ZIGNOR_C = 128                 // Number of blocks
  private val ZIGNOR_R = 3.442619855899      // Start of the right tail
  private val ZIGNOR_V = 9.91256303526217e-3 // (R * phi(R) + Pr(X>=3)) * sqrt(2/pi)
  private val (blocks, ratios) = {
    import Scalaz._

    val f = math.exp(-0.5 * ZIGNOR_R * ZIGNOR_R)
    val blocks =
      (ZIGNOR_V / f) #::
      ZIGNOR_R #::
      unfold((126, ZIGNOR_V/f, ZIGNOR_R)) { (a: (Int,Double, Double)) => {
        val f = math.exp(-0.5 * a._3 * a._3)
        val v = math.sqrt(-2.0 * math.log(ZIGNOR_V / a._2 + f))
        if (a._1 == 0) none else (v, (a._1-1, a._3, v)).some
      }} :+ 0.0

    (blocks.toList, blocks.apzip(_.tail).map(a => a._1 / a._2).toList)
  }

  def gaussian(mean: Double, dev: Double): RVar[Double] =
    for {
      u <- stdUniform.map(2.0 * _ - 1)
      i <- next[Int].map(a => ((a & 0xffffffffl) % 127).toInt)
      r <- if (math.abs(u) < ratios(i)) RVar.point(u * blocks(i))
           else if (i == 0) DRandNormalTail(ZIGNOR_R, u < 0)
           else {
             val x = u * blocks(i)
             val f0 = math.exp(-0.5 * (blocks(i) * blocks(i) - x * x))
             val f1 = math.exp(-0.5 * (blocks(i+1) * blocks(i+1) -x * x))
             stdUniform.map(a => f1 + a * (f0 - f1) < 1.0).ifM(
               ifTrue = RVar.point(x),
               ifFalse = gaussian(mean, dev)
             )
           }
    } yield mean + dev * r
}
