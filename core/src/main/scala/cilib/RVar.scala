package cilib

import _root_.scala.Predef.{any2stringadd => _, _}
import scalaz._
import Scalaz._
import scalaz.Free._

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

    def fix[AA, B](f: (AA => B) => (AA => B)): AA => B = f(fix(f))(_)

    def buildTree(zs: NonEmptyList[A]): NonEmptyList[BinTree] = {
      def join(left: BinTree, right: BinTree): BinTree =
        (left, right) match {
          case (Leaf(_), Leaf(_))                 => Node(2, left, right)
          case (Node(ct, _, _), Leaf(_))          => Node(ct + 1, left, right)
          case (Leaf(_), Node(ct, _, _))          => Node(ct + 1, left, right)
          case (Node(ctl, _, _), Node(ctr, _, _)) => Node(ctl + ctr, left, right)
        }

      def inner(l: NonEmptyList[BinTree]): NonEmptyList[BinTree] = {
        @annotation.tailrec
        def go(xs: List[BinTree], acc: List[BinTree]): List[BinTree] =
          xs match {
            case Nil            => acc
            case _ :: Nil       => acc ++ xs
            case e1 :: e2 :: es => go(es, join(e1, e2) :: acc)
          }

        go(l.toList, List.empty[BinTree]).toNel
          .getOrElse(sys.error("This is impossible as the input is non-empty"))
      }

      fix[NonEmptyList[BinTree], NonEmptyList[BinTree]](f =>
        a => if (a.length == 1) a else f(inner(a)))(zs.map(Leaf(_)))
    }

    def extractTree(n: Int, t: BinTree): (A, BinTree) =
      (n, t) match {
        case (0, Node(_, Leaf(e), r))       => (e, r)
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

    def shuffleTree(l: BinTree, rs: List[Int]): NonEmptyList[A] =
      (l, rs) match {
        case (Leaf(e), Nil) => NonEmptyList(e)
        case (tree, r :: rs) =>
          val (b, rest) = extractTree(r, tree)
          b <:: shuffleTree(rest, rs)
        case (_, _) => sys.error("[shuffle] called with lists of different lengths")
      }

    val length = xs.length - 1
    val randoms: RVar[List[Int]] =
      (0 until length)
        .foldLeft(List.empty[RVar[Int]])((a, c) => Dist.uniformInt(Interval(0, length - c)) :: a)
        .reverse // TODO / FIX: Remove the need to reverse!
        .sequence

    randoms.map { r =>
      shuffleTree(buildTree(xs).head, r)
    }
  }

  def sample[A](n: Int, xs: NonEmptyList[A]): OptionT[RVar, List[A]] =
    choices(n, xs)

  def choices[A](n: Int, xs: NonEmptyList[A]): OptionT[RVar, List[A]] =
    OptionT {
      if (xs.length < n) RVar.pure(None)
      else {
        import scalaz.syntax.foldable._
        import scalaz.std.list._
        type M[B] = StateT[RVar, List[A], B]

        (0 until xs.size).toList.reverse
          .take(n)
          .foldLeftM[M, List[A]](List.empty[A]) {
            case (s, a) =>
              StateT[RVar, List[A], List[A]] { currentList =>
                Dist
                  .uniformInt(Interval(0, a))
                  .map(r => {
                    val selected = currentList(r)
                    (currentList.diff(List(selected)), selected :: s)
                  })
              }
          }
          .eval(xs.toList)
          .map(Option(_))
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
