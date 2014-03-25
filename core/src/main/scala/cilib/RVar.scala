package cilib

import scalaz._
import syntax.state._
import syntax.applicative._
import syntax.std.option._
import scalaz.Free._

final class RVar[+A](val state: StateT[Trampoline, RNG, A]) {
  import Trampoline._

  def map[B](f: A => B): RVar[B] =
    new RVar(state map f)

  def flatMap[B](f: A => RVar[B]): RVar[B] =
    new RVar(state flatMap (f(_).state))

  def ap[X](f: RVar[A => X]): RVar[X] =
    for {
      ff <- f
      aa <- this
    } yield ff(aa)

  def zip[X](other: RVar[X]): RVar[(A, X)] =
    zipWith(other, a => (a, _: X))

  def zipWith[B, C](zb: RVar[B], f: A => B => C): RVar[C] =
    zb.ap(map(f))

  def replicateM(n: Int) =
    new RVar(state replicateM n)

  def run(r: RNG) =
    state.run(r)
}

object RVar {
  import Trampoline._

  def apply[A](s: StateT[Trampoline, RNG, A]) =
    new RVar[A](s)

  def point[A](a: A) =
    new RVar[A](StateT.stateT[Trampoline, RNG, A](a))

  implicit val monad: Monad[RVar] =
    new Monad[RVar] {
      def bind[A, B](a: RVar[A])(f: A => RVar[B]) =
        a flatMap f
      def point[A](a: => A) =
        RVar.point(a)
    }

  def next[A](implicit e: Generator[A]): RVar[A] =
    implicitly[Generator[A]].gen

  def ints(n: Int) =
    next[Int](Generator.intGen) replicateM n

  def doubles(n: Int) =
    next[Double](Generator.doubleGen) replicateM n

  def choose[A](xs: NonEmptyList[A]) =
    Dist.uniformInt(0, xs.size - 1) map { xs.list.apply(_) }

  // implementation of Oleg Kiselgov's perfect shuffle:
  // http://okmij.org/ftp/Haskell/perfect-shuffle.txt
  def shuffle[A](xs: List[A]): RVar[List[A]] = {
    sealed trait BinTree[A]
    case class Node[A](c: Int, left: BinTree[A], right: BinTree[A]) extends BinTree[A]
    case class Leaf[A](element: A) extends BinTree[A]

    def fix[A, B](f: (A => B) => (A => B)): A => B = f(fix(f))(_)

    def buildTree(zs: List[A]) = {
      def join(left: BinTree[A], right: BinTree[A]) = (left, right) match {
        case (Leaf(_), Leaf(_)) => Node(2, left, right)
        case (Node(ct, _, _), Leaf(_)) => Node(ct + 1, left, right)
        case (Leaf(_), Node(ct, _, _)) => Node(ct + 1, left, right)
        case (Node(ctl, _, _), Node(ctr, _, _)) => Node(ctl + ctr, left, right)
      }

      def inner(l: List[BinTree[A]]): List[BinTree[A]] = l match {
        case Nil => Nil
        case e :: Nil => e :: Nil
        case e1 :: e2 :: es => join(e1, e2) :: inner(es)
      }

      fix[List[BinTree[A]], List[BinTree[A]]](f => a => if (a.length == 1) a else f(inner(a)))(zs.map(Leaf(_)))
    }

    def extractTree(n: Int, t: BinTree[A]): (A, BinTree[A]) = (n, t) match {
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

    def shuffleTree(l: BinTree[A], rs: List[Int]): List[A] = (l, rs) match {
      case (Leaf(e), Nil) => e :: Nil
      case (tree, r :: rs) =>
        val (b, rest) = extractTree(r, tree)
        b :: shuffleTree(rest, rs)
      case (_, _) => sys.error("[shuffle] called with lists of different lengths")
    }

    import scalaz.std.list._
    import scalaz.syntax.traverse._

    val length = xs.length - 1
    val randoms = (0 until length).foldLeft(List[RVar[Int]]())((a, c) => Dist.uniformInt(0, length - c + 1) :: a).reverse.sequence // TODO / FIX: Remove the need to reverse!

    xs match {
      case Nil => RVar(StateT.stateT(xs))
      case x => randoms map { r => shuffleTree(buildTree(x).head, r) } // head is safe because build tree will always result in [node]
    }
  }

  def sample[A](n: Int, xs: List[A]) =
    choices(n, xs)

  def choices[A](n: Int, xs: List[A]): OptionT[RVar, List[A]] =
    OptionT {
      if (xs.length < n) RVar.point(None)
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
        } eval xs).map(Some(_))
      }
    }

}

trait Generator[A] {
  def gen: RVar[A]
}

object Generator {

  private def nextBits(bits: Int): RVar[Int] =
    RVar(StateT((rng: RNG) => Trampoline.delay(rng.next(bits))))

  implicit object doubleGen extends Generator[Double] {
    def gen =
      (nextBits(26) |@| nextBits(27)) { (a, b) => ((a.toLong << 27) + b) / (1L << 53).toDouble }
  }

  implicit object intGen extends Generator[Int] {
    def gen =
      nextBits(32)
  }

  implicit object BooleanGen extends Generator[Boolean] {
    def gen =
      nextBits(1) map (_ == 1)
  }
}

object Dist {

  import RVar._

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

  // TODO: This should be an implementation of Doornik's improved ziggurat method
  def gaussian(mean: Double, variance: Double) = RVar(StateT { rng =>

    def uniformPair = stdUniform zip stdUniform

    def update(x: Double, y: Double, i: Int) = { (rng: RNG) =>
      if (i < 127) {
        val y0 = ytab(i)
        val y1 = ytab(i + 1)
        val (rng1, uni) = stdUniform.run(rng).run
        (rng1, (x, y1 + (y0 - y1) * uni))
      } else {
        val (rng1, (a, b)) = uniformPair.run(rng).run
        val x1 = PARAM_R - math.log(1.0 - a) / PARAM_R
        val y1 = math.exp(-PARAM_R * (x - 0.5 * PARAM_R)) * b
        (rng1, (x1, y1))
      }
    }

    import Free.Trampoline
    import Trampoline._

    def inner(y: Double): StateT[Trampoline, RNG, Double] = StateT { rng =>
      val (rng1, u) = next[Int].run(rng).run
      val i = u & 0x0000007F
      val sign = u & 0x00000080
      val j = u >> 8
      val x = j * wtab(i)

      if (j >= ktab(i)) {
        val (rng2, (x1, y1)) = update(x, y, i)(rng1)

        if (y1 < math.exp(-0.5 * x1 * x1)) {
          done((rng2, if (sign != 0) x1 else -x1))
        } else suspend(inner(y1).run(rng2))
      } else done((rng1, if (sign != 0) x else -x))
    }

    inner(0).run(rng)
  })

  def cauchy(l: Double, s: Double) =
    stdUniform map { x => l + s * math.tan(math.Pi * (x - 0.5)) }

  def gamma(k: Double, theta: Double) = {
    import Scalaz._

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

    for {
      a <- gammaInt
      b <- gammaFrac
    } yield (a + b) * theta
  }

  def exponential(l: Option[Double @@ Positive]) =
    l.cata(x => stdUniform map { math.log(_) / x }, RVar.point(0.0))

  def laplace(b0: Double, b1: Double) =
    stdUniform map { x =>
      val rr = x - 0.5
      b0 - b1 * (math.log(1 - 2 * rr.abs)) * rr.signum
    }

  def lognormal(mean: Double, dev: Double) =
    stdNormal map (x => math.exp(mean + dev * x))

  import Scalaz._
  def dirichlet(alphas: List[Double]) =
    alphas.traverse(gamma(_, 1)).map(ys => {
      val sum = ys.sum
      ys.map(_ / sum)
    })

  // Constants related to the gaussian number generation
  private[this] val PARAM_R = 3.44428647676 // position of right-most step

  /* tabulated values for the height of the Ziggurat levels */
  private[this] val ytab = Array(1, 0.963598623011, 0.936280813353, 0.913041104253,
    0.892278506696, 0.873239356919, 0.855496407634, 0.838778928349,
    0.822902083699, 0.807732738234, 0.793171045519, 0.779139726505,
    0.765577436082, 0.752434456248, 0.739669787677, 0.727249120285,
    0.715143377413, 0.703327646455, 0.691780377035, 0.68048276891,
    0.669418297233, 0.65857233912, 0.647931876189, 0.637485254896,
    0.62722199145, 0.617132611532, 0.607208517467, 0.597441877296,
    0.587825531465, 0.578352913803, 0.569017984198, 0.559815170911,
    0.550739320877, 0.541785656682, 0.532949739145, 0.524227434628,
    0.515614886373, 0.507108489253, 0.498704867478, 0.490400854812,
    0.482193476986, 0.47407993601, 0.466057596125, 0.458123971214,
    0.450276713467, 0.442513603171, 0.434832539473, 0.427231532022,
    0.419708693379, 0.41226223212, 0.404890446548, 0.397591718955,
    0.390364510382, 0.383207355816, 0.376118859788, 0.369097692334,
    0.362142585282, 0.355252328834, 0.348425768415, 0.341661801776,
    0.334959376311, 0.328317486588, 0.321735172063, 0.31521151497,
    0.308745638367, 0.302336704338, 0.29598391232, 0.289686497571,
    0.283443729739, 0.27725491156, 0.271119377649, 0.265036493387,
    0.259005653912, 0.253026283183, 0.247097833139, 0.241219782932,
    0.235391638239, 0.229612930649, 0.223883217122, 0.218202079518,
    0.212569124201, 0.206983981709, 0.201446306496, 0.195955776745,
    0.190512094256, 0.185114984406, 0.179764196185, 0.174459502324,
    0.169200699492, 0.1639876086, 0.158820075195, 0.153697969964,
    0.148621189348, 0.143589656295, 0.138603321143, 0.133662162669,
    0.128766189309, 0.123915440582, 0.119109988745, 0.114349940703,
    0.10963544023, 0.104966670533, 0.100343857232, 0.0957672718266,
    0.0912372357329, 0.0867541250127, 0.082318375932, 0.0779304915295,
    0.0735910494266, 0.0693007111742, 0.065060233529, 0.0608704821745,
    0.056732448584, 0.05264727098, 0.0486162607163, 0.0446409359769,
    0.0407230655415, 0.0368647267386, 0.0330683839378, 0.0293369977411,
    0.0256741818288, 0.0220844372634, 0.0185735200577, 0.0151490552854,
    0.0118216532614, 0.00860719483079, 0.00553245272614, 0.00265435214565)

  /* tabulated values for 2^24 times x[i]/x[i+1],
     * used to accept for U*x[i+1]<=x[i] without any floating point operations */
  private[this] val ktab = Array(0, 12590644, 14272653, 14988939,
    15384584, 15635009, 15807561, 15933577,
    16029594, 16105155, 16166147, 16216399,
    16258508, 16294295, 16325078, 16351831,
    16375291, 16396026, 16414479, 16431002,
    16445880, 16459343, 16471578, 16482744,
    16492970, 16502368, 16511031, 16519039,
    16526459, 16533352, 16539769, 16545755,
    16551348, 16556584, 16561493, 16566101,
    16570433, 16574511, 16578353, 16581977,
    16585398, 16588629, 16591685, 16594575,
    16597311, 16599901, 16602354, 16604679,
    16606881, 16608968, 16610945, 16612818,
    16614592, 16616272, 16617861, 16619363,
    16620782, 16622121, 16623383, 16624570,
    16625685, 16626730, 16627708, 16628619,
    16629465, 16630248, 16630969, 16631628,
    16632228, 16632768, 16633248, 16633671,
    16634034, 16634340, 16634586, 16634774,
    16634903, 16634972, 16634980, 16634926,
    16634810, 16634628, 16634381, 16634066,
    16633680, 16633222, 16632688, 16632075,
    16631380, 16630598, 16629726, 16628757,
    16627686, 16626507, 16625212, 16623794,
    16622243, 16620548, 16618698, 16616679,
    16614476, 16612071, 16609444, 16606571,
    16603425, 16599973, 16596178, 16591995,
    16587369, 16582237, 16576520, 16570120,
    16562917, 16554758, 16545450, 16534739,
    16522287, 16507638, 16490152, 16468907,
    16442518, 16408804, 16364095, 16301683,
    16207738, 16047994, 15704248, 15472926)

  /* tabulated values of 2^{-24}*x[i] */
  private[this] val wtab = Array(1.62318314817e-08, 2.16291505214e-08, 2.54246305087e-08, 2.84579525938e-08,
    3.10340022482e-08, 3.33011726243e-08, 3.53439060345e-08, 3.72152672658e-08,
    3.8950989572e-08, 4.05763964764e-08, 4.21101548915e-08, 4.35664624904e-08,
    4.49563968336e-08, 4.62887864029e-08, 4.75707945735e-08, 4.88083237257e-08,
    5.00063025384e-08, 5.11688950428e-08, 5.22996558616e-08, 5.34016475624e-08,
    5.44775307871e-08, 5.55296344581e-08, 5.65600111659e-08, 5.75704813695e-08,
    5.85626690412e-08, 5.95380306862e-08, 6.04978791776e-08, 6.14434034901e-08,
    6.23756851626e-08, 6.32957121259e-08, 6.42043903937e-08, 6.51025540077e-08,
    6.59909735447e-08, 6.68703634341e-08, 6.77413882848e-08, 6.8604668381e-08,
    6.94607844804e-08, 7.03102820203e-08, 7.11536748229e-08, 7.1991448372e-08,
    7.2824062723e-08, 7.36519550992e-08, 7.44755422158e-08, 7.52952223703e-08,
    7.61113773308e-08, 7.69243740467e-08, 7.77345662086e-08, 7.85422956743e-08,
    7.93478937793e-08, 8.01516825471e-08, 8.09539758128e-08, 8.17550802699e-08,
    8.25552964535e-08, 8.33549196661e-08, 8.41542408569e-08, 8.49535474601e-08,
    8.57531242006e-08, 8.65532538723e-08, 8.73542180955e-08, 8.8156298059e-08,
    8.89597752521e-08, 8.97649321908e-08, 9.05720531451e-08, 9.138142487e-08,
    9.21933373471e-08, 9.30080845407e-08, 9.38259651738e-08, 9.46472835298e-08,
    9.54723502847e-08, 9.63014833769e-08, 9.71350089201e-08, 9.79732621669e-08,
    9.88165885297e-08, 9.96653446693e-08, 1.00519899658e-07, 1.0138063623e-07,
    1.02247952126e-07, 1.03122261554e-07, 1.04003996769e-07, 1.04893609795e-07,
    1.05791574313e-07, 1.06698387725e-07, 1.07614573423e-07, 1.08540683296e-07,
    1.09477300508e-07, 1.1042504257e-07, 1.11384564771e-07, 1.12356564007e-07,
    1.13341783071e-07, 1.14341015475e-07, 1.15355110887e-07, 1.16384981291e-07,
    1.17431607977e-07, 1.18496049514e-07, 1.19579450872e-07, 1.20683053909e-07,
    1.21808209468e-07, 1.2295639141e-07, 1.24129212952e-07, 1.25328445797e-07,
    1.26556042658e-07, 1.27814163916e-07, 1.29105209375e-07, 1.30431856341e-07,
    1.31797105598e-07, 1.3320433736e-07, 1.34657379914e-07, 1.36160594606e-07,
    1.37718982103e-07, 1.39338316679e-07, 1.41025317971e-07, 1.42787873535e-07,
    1.44635331499e-07, 1.4657889173e-07, 1.48632138436e-07, 1.50811780719e-07,
    1.53138707402e-07, 1.55639532047e-07, 1.58348931426e-07, 1.61313325908e-07,
    1.64596952856e-07, 1.68292495203e-07, 1.72541128694e-07, 1.77574279496e-07,
    1.83813550477e-07, 1.92166040885e-07, 2.05295471952e-07, 2.22600839893e-07)

}
