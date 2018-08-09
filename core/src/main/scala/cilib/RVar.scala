package cilib

import _root_.scala.Predef.{any2stringadd => _, _}
import scalaz._
import Scalaz._
import scalaz.Free._

import eu.timepit.refined.auto._
import eu.timepit.refined.api._
import eu.timepit.refined.numeric.Positive

import spire.math._
import spire.implicits._

sealed abstract class RVar[A] {
  def trampolined(s: RNG): Trampoline[(RNG, A)]

  def run(initial: RNG): (RNG, A) = trampolined(initial).run
  def exec(s: RNG): RNG = run(s)._1
  def eval(s: RNG): A = run(s)._2

  def map[B](f: A => B): RVar[B] =
    RVar.trampolined(rng => trampolined(rng).map(a => (a._1, f(a._2))))

  def flatMap[B](f: A => RVar[B]): RVar[B] =
    RVar.trampolined(
      rng =>
        trampolined(rng)
          .flatMap((a: (RNG, A)) => f(a._2).trampolined(a._1)))
}

sealed abstract class RVarInstances0 {
  implicit val rvarMonad: Monad[RVar] =
    new Monad[RVar] {
      def bind[A, B](a: RVar[A])(f: A => RVar[B]) =
        a.flatMap(f)

      def point[A](a: => A) =
        RVar.pure(a)
    }
}

sealed abstract class RVarInstances extends RVarInstances0 {
  implicit val rvarBindRec: BindRec[RVar] =
    new BindRec[RVar] {
      def bind[A, B](fa: RVar[A])(f: A => RVar[B]): RVar[B] =
        fa.flatMap(f)

      def map[A, B](fa: RVar[A])(f: A => B): RVar[B] =
        fa.map(f)

      def tailrecM[A, B](f: A => RVar[A \/ B])(a: A): RVar[B] =
        f(a).flatMap {
          case -\/(a0) => tailrecM(f)(a0)
          case \/-(b)  => RVar.pure(b)
        }
    }
}

object RVar extends RVarInstances {

  def apply[A](f: RNG => (RNG, A)): RVar[A] =
    new RVar[A] {
      def trampolined(s: RNG) = Trampoline.delay(f(s))
    }

  def trampolined[A](f: RNG => Trampoline[(RNG, A)]): RVar[A] =
    new RVar[A] {
      def trampolined(s: RNG) = Trampoline.suspend(f(s))
    }

  @deprecated("This method has been deprecated, use pure instead, it is technically better",
              "2.0.2")
  def point[A](a: => A): RVar[A] =
    pure(a)

  def pure[A](a: => A): RVar[A] =
    apply(r => (r, a))

  def next[A](implicit e: Generator[A]): RVar[A] =
    e.gen

  def ints(n: Int): RVar[List[Int]] =
    next[Int](Generator.IntGen).replicateM(n)

  def doubles(n: Int): RVar[List[Double]] =
    next[Double](Generator.DoubleGen).replicateM(n)

  def choose[A](xs: NonEmptyList[A]): RVar[A] =
    Dist
      .uniformInt(Interval(0, xs.size - 1))
      .map(i => {
        import monocle._
        import Monocle._

        xs.list.applyOptional(index(i)).getOption.getOrElse(xs.head)
      })

  // implementation of Oleg Kiselgov's perfect shuffle:
  // http://okmij.org/ftp/Haskell/perfect-shuffle.txt
  def shuffle[A](xs: NonEmptyList[A]): RVar[NonEmptyList[A]] = {
    sealed trait BinTree
    final case class Node(c: Int, left: BinTree, right: BinTree) extends BinTree
    final case class Leaf(element: A) extends BinTree

    def buildTree(zs: NonEmptyList[A]): BinTree =
      growLevel(zs.toList.map(Leaf(_): BinTree))

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

    def rseq(n: Int): RVar[List[Int]] =
      (n - 1 to 1 by -1).toList
        .traverse(x => Dist.uniformInt(Interval(0, x)))

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
      }

    def local(t: BinTree, rs: List[Int]): List[A] =
      (t, rs) match {
        case (Leaf(e), Nil)        => List(e)
        case (tree, ri :: rothers) => extractTree(ri, tree, (t: BinTree) => local(t, rothers))
        case _                     => sys.error("impossible")
      }

    rseq(xs.length).map(
      r =>
        local(buildTree(xs), r).toNel
          .getOrElse(sys.error("Impossible - NonEmptyList is guaranteed to be non-empty")))
  }

  def sample[F[_]: Foldable, A](n: Int Refined Positive, xs: F[A]): RVar[Option[List[A]]] =
    choices(n, xs)

  def choices[F[_], A](n: Int Refined Positive, xs: F[A])(
      implicit F: Foldable[F]): RVar[Option[List[A]]] =
    if (xs.length < n) RVar.pure(None)
    else {
      val length = F.length(xs)
      val backsaw: RVar[List[Int]] =
        (length - 1 to length - n by -1).toList
          .traverse(x => Dist.uniformInt(Interval(0, x)))

      backsaw
        .map(_.foldLeft(List.empty[A])((a, c) =>
          F.index(xs, c) match {
            case Some(i) => a :+ i
            case None    => sys.error("Shouldn't be possible")
        }))
        .map(_.some)
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
      (nextBits(26) |@| nextBits(27)) { (a, b) =>
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

object Dist {
  import RVar._
  import scalaz.std.AllInstances._

  val stdUniform: RVar[Double] = next[Double]
  val stdNormal: RVar[Double] = gaussian(0.0, 1.0)
  val stdCauchy: RVar[Double] = cauchy(0.0, 1.0)
  val stdExponential: RVar[Double] = exponential(1.0)
  val stdGamma: RVar[Double] = gamma(2, 2.0)
  val stdLaplace: RVar[Double] = laplace(0.0, 1.0)
  val stdLognormal: RVar[Double] = lognormal(0.0, 1.0)

  /** Generate a discrete uniform value in [from, to]. Note that the upper bound is *inclusive* */
  def uniformInt(i: Interval[Int]): RVar[Int] =
    next[Int].map(x => {
      val (from, to) = (i.lowerValue, i.upperValue)
      val (ll, hh) = if (to < from) (to, from) else (from, to)
      val diff = hh.toLong - ll.toLong
      if (diff == 0) ll
      else (ll.toLong + (math.abs(x.toLong) % (diff + 1))).toInt
    })

  def uniform(i: Interval[Double]): RVar[Double] =
    stdUniform.map { x =>
      i.lowerValue + x * (i.upperValue - i.lowerValue)
    }

  def cauchy(l: Double, s: Double): RVar[Double] =
    stdUniform.map { x =>
      l + s * math.tan(math.Pi * (x - 0.5))
    }

  def gamma(k: Double, theta: Double): RVar[Double] = {
    val n = k.toInt
    val gammaInt = stdUniform.replicateM(n).map(_.foldMap(x => -math.log(x)))
    val gammaFrac = {
      val delta = k - n

      val a: RVar[(Double, Double)] =
        (stdUniform |@| stdUniform |@| stdUniform) { (u1, u2, u3) =>
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

      a.flatMap(a0 =>
        BindRec[RVar].tailrecM((x: (Double, Double)) => {
          val (zeta, eta) = x

          if (eta > math.pow(zeta, delta - 1) * math.exp(-zeta)) a.map(_.left[Double])
          else RVar.pure(zeta.right[(Double, Double)])
        })(a0))

      // def inner: RVar[Double] =
      //   for {
      //     u1 <- stdUniform
      //     u2 <- stdUniform
      //     u3 <- stdUniform
      //     (zeta, eta) = {
      //       val v0 = math.E / (math.E + delta)
      //       if (u1 <= v0) {
      //         val zeta = math.pow(u2, 1.0 / delta)
      //         val eta = u3 * math.pow(zeta, delta - 1)
      //         (zeta, eta)
      //       } else {
      //         val zeta = 1 - math.log(u2)
      //         val eta = u3 * math.exp(-zeta)
      //         (zeta, eta)
      //       }
      //     }
      //     r <- if (eta > math.pow(zeta, delta - 1) * math.exp(-zeta)) inner else RVar.pure(zeta)
      //   } yield r
      //
      // inner
    }

    (gammaInt |@| gammaFrac) { (a, b) =>
      (a + b) * theta
    }
  }

  def exponential(l: Double): RVar[Double] =
    stdUniform.map { math.log(_) / l }

  def laplace(b0: Double, b1: Double): RVar[Double] =
    stdUniform.map { x =>
      val rr = x - 0.5
      b0 - b1 * (math.log(1 - 2 * rr.abs)) * rr.signum
    }

  def lognormal(mean: Double, dev: Double): RVar[Double] =
    stdNormal.map(x => math.exp(mean + dev * x))

  def dirichlet(alphas: List[Double]): RVar[List[Double]] =
    alphas
      .traverse(gamma(_, 1))
      .map(ys => {
        val sum = ys.sum
        ys.map(_ / sum)
      })

  def weibull(shape: Double, scale: Double): RVar[Double] =
    stdUniform.map { x =>
      scale * math.pow(-math.log(1 - x), 1 / shape)
    }

  import scalaz.syntax.monad._

  private def DRandNormalTail(min: Double, ineg: Boolean): RVar[Double] = {
    def sample =
      (stdUniform.map(x => math.log(x) / min) |@| stdUniform.map(math.log(_))) { Tuple2.apply }

    sample
      .iterateUntil(v => -2.0 * v._2 >= v._1 * v._1)
      .map(x => if (ineg) x._1 - min else min - x._1)
  }

  //private val ZIGNOR_C = 128                 // Number of blocks
  private val ZIGNOR_R = 3.442619855899 // Start of the right tail
  private val ZIGNOR_V = 9.91256303526217e-3 // (R * phi(R) + Pr(X>=3)) * sqrt(2/pi)
  private val (blocks, ratios) = {
    import Scalaz._

    val f = math.exp(-0.5 * ZIGNOR_R * ZIGNOR_R)
    val blocks =
      (ZIGNOR_V / f) #::
        ZIGNOR_R #::
        unfold((126, ZIGNOR_V / f, ZIGNOR_R)) { (a: (Int, Double, Double)) =>
        {
          val f = math.exp(-0.5 * a._3 * a._3)
          val v = math.sqrt(-2.0 * math.log(ZIGNOR_V / a._2 + f))
          if (a._1 == 0) none[(Double, (Int, Double, Double))] else (v, (a._1 - 1, a._3, v)).some
        }
      } :+ 0.0

    (blocks.toList, blocks.apzip(_.drop(1)).map(a => a._1 / a._2).toList)
  }

  def gaussian(mean: Double, dev: Double): RVar[Double] =
    for {
      u <- stdUniform.map(2.0 * _ - 1)
      i <- next[Int].map(a => ((a & 0xffffffffL) % 127).toInt)
      r <- if (math.abs(u) < ratios(i)) RVar.pure(u * blocks(i))
      else if (i == 0) DRandNormalTail(ZIGNOR_R, u < 0)
      else {
        val x = u * blocks(i)
        val f0 = math.exp(-0.5 * (blocks(i) * blocks(i) - x * x))
        val f1 = math.exp(-0.5 * (blocks(i + 1) * blocks(i + 1) - x * x))
        stdUniform
          .map(a => f1 + a * (f0 - f1) < 1.0)
          .ifM(
            ifTrue = RVar.pure(x),
            ifFalse = gaussian(mean, dev)
          )
      }
    } yield mean + dev * r

  private def invErf(x: Double) = {
    val a = 0.147
    val halfPi = 2.0 / (math.Pi * a)
    val xcomp = 1.0 - (x * x)

    val t1 = halfPi + (math.log(xcomp) / 2.0)
    val t2 = math.log(xcomp) / a

    math.signum(x) * math.sqrt(math.sqrt(t1 * t1 - t2) - t1)
  }

  private def invErfc(x: Double) = invErf(1.0 - x) // check this. invErfc(1 - x) == invErf(x)

  def levy(l: Double, s: Double): RVar[Double] =
    stdUniform.map { x =>
      l + s / (0.5 * invErfc(x) * invErfc(x))
    }
}
