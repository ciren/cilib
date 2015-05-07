package cilib
package benchmarks

import _root_.scala.Predef._

import Benchmarks._

import scalaz.{NonEmptyList,OneAnd}
import scalaz.std.anyVal._
import scalaz.std.list._
import scalaz.syntax.traverse1._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen

import spire.math._
import spire.implicits._

object BenchmarksTest extends Properties("Benchmarks") {

  val zero = NonEmptyList.nels(0.0, 0.0, 0.0)
  def accurate(v: Double, d: Double, e: Double) = abs(v - d) <= e

  def epsilon(precision: Double) = 1.0 / (10.0 ** precision)
  val epsilon = 1e-15

  implicit class DoubleEpsilonOps(d: Double) {
    def ~(v: Double, e: Double) = accurate(d, v, e)
    def ~(v: Double) = accurate(d, v, epsilon)
  }

  def gen1(l: Double = Double.MinValue, u: Double = Double.MaxValue) = (Gen.choose(l, u))

  def gen2(l: Double = Double.MinValue, u: Double = Double.MaxValue) =
    for {
      a <- Gen.choose(l, u)
      b <- Gen.choose(l, u)
    } yield (a, b)

  def gen3(l: Double = Double.MinValue, u: Double = Double.MaxValue) =
    for {
      a <- Gen.choose(l, u)
      b <- Gen.choose(l, u)
      c <- Gen.choose(l, u)
    } yield (a, b, c)

  def gen4(l: Double = Double.MinValue, u: Double = Double.MaxValue) =
    for {
      a <- Gen.choose(l, u)
      b <- Gen.choose(l, u)
      c <- Gen.choose(l, u)
      d <- Gen.choose(l, u)
    } yield (a, b, c, d)

  def gen5(l: Double = Double.MinValue, u: Double = Double.MaxValue) =
    for {
      a <- Gen.choose(l, u)
      b <- Gen.choose(l, u)
      c <- Gen.choose(l, u)
      d <- Gen.choose(l, u)
      e <- Gen.choose(l, u)
    } yield (a, b, c, d, e)

  def gen6(l: Double = Double.MinValue, u: Double = Double.MaxValue) =
    for {
      a <- Gen.choose(l, u)
      b <- Gen.choose(l, u)
      c <- Gen.choose(l, u)
      d <- Gen.choose(l, u)
      e <- Gen.choose(l, u)
      f <- Gen.choose(l, u)
    } yield (a, b, c, d, e, f)

  def gen1And(l: Double = Double.MinValue, u: Double = Double.MaxValue) =
    for {
      a <- Gen.choose(l, u)
      b <- Gen.containerOf[List, Double](Gen.choose(l, u))
    } yield OneAnd(a, b)

  def gen2And(l: Double = Double.MinValue, u: Double = Double.MaxValue) =
    for {
      a <- Gen.choose(l, u)
      b <- Gen.choose(l, u)
      c <- Gen.containerOf[List, Double](Gen.choose(l, u))
    } yield Sized2And(a, b, c)

  def genNEL(l: Double = Double.MinValue, u: Double = Double.MaxValue) =
    gen1And(l, u).map(x => NonEmptyList.nel(x.head, x.tail))

  def genConst(v: Double = Double.MinValue) = genNEL(v, v)

  property("absoluteValue") = forAll(genNEL(-100.0, 100.0)) { g =>
    val abs = absoluteValue(g)
    abs === absoluteValue(g.map(_ * -1)) &&
    abs >= 0.0 &&
    abs >= g.foldMap()
  } && {
    absoluteValue(zero) === 0.0 &&
    absoluteValue(NonEmptyList.nels(1.0, 2.0, 3.0)) === 6.0 &&
    absoluteValue(NonEmptyList.nels(-1.0, -2.0, -3.0)) === 6.0
  }

  property("ackley") = forAll(genNEL(-32.768, 32.768)) { g =>
    ackley(g) >= 0.0
  } && ackley(zero) < epsilon

  property("adjiman") = forAll(gen2(-5.0, 5.0)) { g =>
    adjiman(g) >= -5.02181
  } && adjiman((2.0, 0.10578)) === -2.0218067833370204

  property("alpine1") = forAll(genNEL(-10.0, 10.0)) { g =>
    alpine1(g) >= 0.0
  } && alpine1(zero) === 0.0

  property("arithmeticMean") = forAll(genNEL(0.0, 1.0)) { g =>
    arithmeticMean(g) >= 0.0
  } && arithmeticMean(zero) === 0.0

  property("bartelsConn") = forAll(gen2(-50.0, 50.0)) { g =>
    bartelsConn(g) >= 1.0
  } && bartelsConn((0.0, 0.0)) === 1.0

  property("beale") = forAll(gen2(-4.5, 4.5)) { g =>
    beale(g) >= 0.0
  } && beale((3.0, 0.5)) === 0.0

  property("bohachevsky") = forAll(gen2(-100.0, 100.0)) { g =>
    bohachevsky1(g) >= 0.0 &&
    bohachevsky2(g) >= 0.0 &&
    bohachevsky3(g) >= 0.0
  } && {
    val zero2 = (0.0, 0.0)
    bohachevsky1(zero2) === 0.0 &&
    bohachevsky2(zero2) === 0.0 &&
    bohachevsky3(zero2) === 0.0
  }

  property("booth") = forAll(gen2(-10.0, 10.0)) { g =>
    booth(g) >= 0.0
  } && {
    booth((1.0, 3.0)) === 0.0
  }

  val genBraninRCOS = for {
    x1 <- Gen.choose(-5.0, 10.0)
    x2 <- Gen.choose(0.0, 15.0)
  } yield (x1, x2)

  property("braninRCOS1") = forAll(genBraninRCOS) { g =>
    braninRCOS1(g) >= 0.3978874 - epsilon
  } && accurate(braninRCOS1((-pi, 12.275)), 0.3978874, epsilon(5))

  property("brent") = forAll(genNEL(-10.0, 10.0)) { g =>
    brent(g) >= 0.0
  } && accurate(brent(NonEmptyList.nels(-10.0, -10.0, -10.0)), 0.0, epsilon)

  property("brown") = forAll(gen2And(-1.0, 1.0)) { g =>
    brown(g) >= 0.0
  } && brown(Sized2And(0.0, 0.0, List(0.0))) === 0.0

  val genBukin = for {
    a <- Gen.choose(-15.0, -5.0)
    b <- Gen.choose(-3.0, 3.0)
  } yield (a, b)

  property("bukin") = forAll(genBukin) { g =>
    bukin2(g) >= 0.0 &&
    bukin2Adapted(g) >= 0.0 &&
    bukin4(g) >= 0.0 &&
    bukin6(g) >= 0.0
  } && {
    val g = (-10.0, 0.0)
    bukin2(g)            === 0.0 &&
    bukin2Adapted(g)     === 0.0 &&
    bukin4(g)            === 0.0 &&
    bukin6((-10.0, 1.0)) === 0.0
  }

  property("centralTwoPeakTrap") = forAll(gen1(0.0, 20.0)) { g =>
    centralTwoPeakTrap(g) >= -200.0
  } && centralTwoPeakTrap((20.0)) === -200.0

  property("chichinadze") = forAll(gen2(-30.0, 30.0)) { g =>
    chichinadze(g) >= -43.3159
  } && accurate(chichinadze((5.90133, 0.5)), -43.3159, epsilon(4))

  property("chungReynolds") = forAll(genNEL(-100.0, 100.0)) { g =>
    chungReynolds(g) >= 0.0
  } && chungReynolds(zero) === 0.0

  property("cigar") = forAll(gen2And(-100.0, 100.0)) { g =>
    cigar(10e6)(g) >= 0.0
  } && cigar(10e6)(Sized2And(0.0, 0.0, List(0.0))) === 0.0

  property("colville") = forAll(gen4(-10.0, 10.0)) { g =>
    colville(g) >= 0.0
  } && {
    colville((1.0, 1.0, 1.0, 1.0)) === 0.0 &&
    colville((0.0, 0.0, 0.0, 0.0)) === 42.0
  }

  property("cosineMixture") = forAll(genNEL(-1.0, 1.0)) { g =>
    cosineMixture(g) >= -0.1 * g.length
  } && accurate(cosineMixture(zero), -0.1 * zero.length, epsilon(5))

  property("cross") = forAll(genNEL(-10.0, 10.0)) { g =>
    crossInTray(g) >= -2.11 &&
    crossLegTable(g) >= -1.0 &&
    crossCrowned(g) >= -0.0001
  } && {
    accurate(crossInTray(NonEmptyList.nels(1.349406685353340,1.349406608602084)), -2.06261218, epsilon(6)) &&
    crossLegTable(zero) === -1.0 &&
    crossCrowned(zero) === 0.0001
  }

  property("csendes") = forAll(genNEL(-1.0, 1.0)) { g =>
    val fit = csendes(g)
    if (g.any(_ == 0.0))
      fit === None
    else
     fit.forall(_ >= 0.0)
  } && csendes(zero) === None

  property("cube") = forAll(gen2(-10.0, 10.0)) { g =>
    cube(g) >= 0.0
  } && {
    cube((1.0, 1.0)) === 0.0 &&
    cube((-1.0, 1.0)) === 404.0
  }

  property("damavandi") = forAll(gen2(0.0, 14.0)) { g =>
    damavandi(g) >= 0.0
  }

  property("deb") = forAll(genNEL(0.0, 1.0)) { g =>
    deb1(g) >= -1.0 &&
    deb3(g) >= -1.0
  } && deb1(zero) === 0.0

  property("decanomial") = forAll(gen2(-10.0, 10.0)) { g =>
    decanomial(g) >= 0.0
  } && decanomial((2.0, -3.0)) === 0.0

  property("deckkersAarts") = forAll(gen2(-20.0, 20.0)) { g =>
    deckkersAarts(g) >= -24777.0
  } && {
    accurate(deckkersAarts((0.0, 15.0)), -24771.0, epsilon(0)) &&
    accurate(deckkersAarts((0.0, -15.0)), -24771.0, epsilon(0))
  }

  property("deVilliersGlasser1") = forAll(gen4(1.0, 100.0)) { g =>
    deVilliersGlasser1(g) >= 0.0
  }

  property("deVilliersGlasser2") = forAll(gen5(1.0, 6.0)) { g =>
    deVilliersGlasser2(g) >= 0.0
  }

  property("differentPowers") = forAll(gen2And(-100.0, 100.0)) { g =>
    differentPowers(g) >= 0.0
  } && differentPowers(Sized2And(0.0, 0.0, List(0.0))) === 0.0

  property("discus") = forAll(gen1And(-100.0, 100.0)) { g =>
    discus(g) >= 0.0
  } && {
    discus(OneAnd(0.0, zero))      === 0.0 &&
    discus(OneAnd(1.0, Nil))       === 1e6 &&
    discus(OneAnd(1.0, List(1.0))) === 1e6 + 1.0
  }

  property("dixonPrice") = forAll(gen2And(-10.0, 10.0)) { g =>
    dixonPrice(g) >= 0.0
  } && accurate(dixonPrice(Sized2And(1.0, 1.0 / sqrt(2), Nil)), 0.0, epsilon)

  property("dropWave") = forAll(genNEL(-5.12, 5.12)) { g =>
    dropWave(g) >= -1.0
  } && dropWave(zero) === -1.0

  property("easom") = forAll(gen2(-100.0, 100.0)) { g =>
    easom(g) >= -1.0
  } && easom((Math.PI, Math.PI)) === -1.0

  property("eggCrate") = forAll(genNEL(-5.0, 5.0)) { g =>
    eggCrate(g) >= 0.0
  } && eggCrate(zero) === 0.0

  property("eggHolder") = forAll(gen2And(-512.0, 512.0)) { g =>
    eggHolder(g) >= -959.64 * (g.rest.length + 2)
  } && accurate(eggHolder(Sized2And(512.0, 404.2319, Nil)), -959.64, epsilon(3))

  property("elliptic") = forAll(gen2And(-100.0, 100.0)) { g =>
    elliptic(g) >= 0.0
  } && elliptic(Sized2And(0.0, 0.0, List(0.0))) === 0.0

  property("elAttarVidyasagarDutta") = forAll(gen2(-100.0, 100.0)) { g =>
    elAttarVidyasagarDutta(g) >= 1.712780354
  } && accurate(elAttarVidyasagarDutta((3.40918683, -2.17143304)), 1.712780354, epsilon(9))

  property("exponential1") = forAll(genNEL(-1.0, 1.0)) { g =>
    exponential1(g) >= -1.0
  } && exponential1(zero) === -1.0

  property("exponential2") = forAll(gen2(0.0, 20.0)) { g =>
    exponential2(g) >= 0.0
  } && accurate(exponential2((1.0, 10.0)), 0.0, epsilon)

  property("freudensteinRoth") = forAll(gen2(-10.0, 10.0)) { g =>
    freudensteinRoth(g) >= 0.0
  } && freudensteinRoth((5.0, 4.0)) === 0.0

  property("gear") = forAll(gen4(12.0, 60.0)) { g =>
    gear(g) >= 2.7 * 10e-12
  } && accurate(gear((16.0, 19.0, 43.0, 49.0)), 2.7 * 10e-12, epsilon(10))

  property("giunta") = forAll(gen2(-1.0, 1.0)) { g =>
    giunta(g) >= 0.06447042053690566
  } && accurate(giunta((0.45834282, 0.45834282)), 0.06447042053690566, epsilon(3))

  property("goldsteinPrice1") = forAll(gen2(-2.0, 2.0)) { g =>
    goldsteinPrice1(g) >= 3.0
  } && {
    accurate(goldsteinPrice1((1.2, 0.8)), 840.0, epsilon(12)) &&
    accurate(goldsteinPrice1((1.8, 0.2)), 84.0, epsilon(12)) &&
    goldsteinPrice1((-0.6, -0.4)) === 30.0 &&
    goldsteinPrice1((0.0, -1.0))  === 3.0
  }

  property("goldsteinPrice2") = forAll(gen2(-5.0, 5.0)) { g =>
    goldsteinPrice2(g) >= 1.0
  } && {
    goldsteinPrice2((3.0, 4.0)) === 1.0
  }

  property("griewank") = forAll(genNEL(-600, 600)) { g =>
    griewank(g) >= 0.0
  } && griewank(zero) === 0.0

  property("hansen") = forAll(gen2(-10.0, 10.0)) { g =>
    hansen(g) >= -176.54
  } && {
    accurate(hansen((-7.58993, -7.708314)),  -176.54, epsilon(2)) &&
    accurate(hansen((-7.58993, -1.425128)),  -176.54, epsilon(2)) &&
    accurate(hansen((-7.58993, 4.858057)),   -176.54, epsilon(2)) &&
    accurate(hansen((-1.306708, -7.708314)), -176.54, epsilon(2)) &&
    accurate(hansen((-1.306708, 4.858057)),  -176.54, epsilon(2)) &&
    accurate(hansen((4.976478, 4.858057)),   -176.54, epsilon(2)) &&
    accurate(hansen((4.976478, -1.425128)),  -176.54, epsilon(2)) &&
    accurate(hansen((4.976478, -7.708314)),  -176.54, epsilon(2))
  }

  property("hartman3") = forAll(gen3(0.0, 1.0)) { g =>
    hartman3(g) >= -3.862782
  } && accurate(hartman3((0.1140, 0.556, 0.852)), -3.862782, epsilon(4))

  property("hartman6") = forAll(gen6(-5.0, 5.0)) { g =>
    hartman6(g) >= -3.32236
  } && accurate(hartman6((0.201690, 0.150011, 0.476874,
    0.275332, 0.311652, 0.657301)), -3.32236, epsilon(5))

  property("himmelblau") = forAll(gen2(-6.0, 6.0)) { g =>
    himmelblau(g) >= 0.0
  } && himmelblau((3.0, 2.0)) === 0.0

  property("hosaki") = forAll(gen2(0.0, 10.0)) { g =>
    hosaki(g) >= -2.3458
  } && accurate(hosaki((4.0, 2.0)), -2.3458, epsilon(4))

  property("hyperEllipsoid") = forAll(genNEL(-10.0, 10.0)) { g =>
    hyperEllipsoid(g.map(-_)) == hyperEllipsoid(g) &&
    hyperEllipsoid(g) >= 0.0
  } && hyperEllipsoid(zero) === 0.0

  property("hyperEllipsoidRotated") = forAll(genNEL(-65.536, 65.536)) { g =>
    hyperEllipsoidRotated (g) >= 0.0
  } && hyperEllipsoidRotated(zero) === 0.0

  property("jennrichSampson") = forAll(gen2(-1.0, 1.0)) { g =>
    jennrichSampson(g) >= 124.3612
  } && accurate(jennrichSampson((0.257825, 0.257825)), 124.3612, epsilon(3))

  property("judge") = forAll(gen2(-10.0, 10.0)) { g =>
    judge(g) >= 16.0817307
  } && accurate(judge((0.86479, 1.2357)), 16.0817307, epsilon(4))

  property("katsuura") = forAll(genNEL(0.0, 100.0)) { g =>
    katsuura(g) >= 1.0
  } && katsuura(zero) === 1.0

  property("kowalik") = forAll(gen4(-5.0, 5.0)) { g =>
    kowalik(g) >= 0.0003074861
  } && accurate(kowalik((0.192833, 0.190836, 0.123117, 0.135766)), 0.0003074861, epsilon(8))

  property("leon") = forAll(gen2(-1.2, 1.2)) { g =>
    leon(g) >= 0.0
  } && leon((1.0, 1.0)) === 0.0

  property("levy3") = forAll(gen2And(-10.0, 10.0)) { g =>
    levy3(g) >= 0.0
  } && forAll(gen2And(1.0, 1.0)) { g =>
    accurate(levy3(g), 0.0, epsilon)
  }

  property("levy5-13") = forAll(gen2(-10.0, 10.0)) { g =>
     levy5(g) >= -176.1375 &&
     levy13(g) >= 0.0
  } && {
     accurate(levy5((-1.3068, -1.4248)), -176.1375, epsilon(4)) &&
     accurate(levy13((1.0, 1.0)), 0.0, epsilon)
  }

  property("levyMontalvo2") = forAll(gen2And(-5.0, 5.0)) { g =>
    levyMontalvo2(g) >= 0.0
  } && forAll(gen2And(1.0, 1.0)) { g =>
    accurate(levyMontalvo2(g), 0.0, epsilon)
  }

  property("matyas") = forAll(gen2(-10.0, 10.0)) { g =>
    matyas(g) >= 0.0
  } && matyas((0.0, 0.0)) === 0.0

  property("maximum") = forAll(genNEL(-1000.0, 1000.0)) { g =>
    maximum(g) == g.maximum1 &&
    g.any(_ === maximum(g))
  }

  val genMcCormick = for {
    a <- Gen.choose(-1.5, 1.5)
    b <- Gen.choose(-3.0, 4.0)
  } yield (a, b)

  property("mcCormick") = forAll(genMcCormick) { g =>
    mcCormick(g) >= -1.9133
  } && accurate(mcCormick((-0.547, -1.547)), -1.9133, epsilon(4))

  property("michalewicz") = forAll(genNEL(0.0, Math.PI)) { g =>
    michalewicz(10.0)(g) >= -0.966 * g.length
  } && accurate(michalewicz(10.0)(NonEmptyList.nels(2.20, 1.57)), -1.8013, epsilon(3))

  property("mieleCantrell") = forAll(gen4(-1.0, 1.0)) { g =>
    mieleCantrell(g) >= 0.0
  } && mieleCantrell((0.0, 1.0, 1.0, 1.0)) === 0.0

  property("minimum") = forAll(genNEL(-1000.0, 1000.0)) { g =>
    minimum(g) == g.minimum1 &&
    g.any(_ === minimum(g))
  }

  property("mishra1") = forAll(genNEL(0.0, 1.0)) { g =>
    mishra1(g) >= 2.0
  } && forAll(genNEL(1.0, 1.0)) { g =>
    mishra1(g) === 2.0
  }

  property("mishra2") = forAll(gen2And(0.0, 1.0)) { g =>
    mishra2(g) >= 2.0
  } && forAll(gen2And(1.0, 1.0)) { g =>
    mishra2(g) === 2.0
  }

  property("mishra5-8") = forAll(gen2(-10.0, 10.0)) { g =>
    mishra5(g)  >= -0.119829 &&
    mishra8(g)  >= 0.0
  } && {
    accurate(mishra5((-1.98682, -10.0)), -0.119829, epsilon(5)) &&
    mishra8((2.0, -3.0)) === 0.0
  }

  property("mishra11") = forAll(genNEL(-10.0, 10.0)) { g =>
    mishra11(g) >= 0.0
  } && forAll(genNEL(0.0, 0.0)) { g =>
    mishra11(g) === 0.0
  }

  property("multiModal") = forAll(genNEL(-10.0, 10.0)) { g =>
    multiModal(g) >= 0.0
  } && forAll(genNEL(0.0, 0.0)) { g =>
    multiModal(g) === 0.0
  }

  property("parsopoulus") = forAll(gen2(-5.0, 5.0)) { g =>
    parsopoulus(g) >= 0.0
  } && {
    val x = List(-1.0, 1.0, -3.0, 3.0, -5.0, 5.0)
    val y = List(0.0, 0.0, -1.0, 1.0, -2.0, 2.0)
    val z = (x zip y).map {
      case (xi, yi) => (xi * (Math.PI / 2.0), yi * Math.PI)
    }
    z.forall(zi => parsopoulus(zi) ~ 0.0)
  }

  property("penalty") = forAll(gen2And(-50.0, 50.0)) { g =>
    penalty1(g) >= 0.0
    penalty2(g) >= 0.0
  } && forAll(gen2And(1.0, 1.0)) { g =>
    penalty2(g) ~ 0.0
  }

  property("penHolder") = forAll(gen2(-11.0, 11.0)) { g =>
    penHolder(g) >= -0.96354
  } && penHolder((9.646168, -9.646168)) ~ (-0.96354, epsilon(5))

  property("periodic") = forAll(genNEL(-10.0, 10.0)) { g =>
    periodic(g) >= 0.9
  } && forAll(genConst(0.0)) { g =>
    periodic(g) === 0.9
  }

  property("powell") = forAll(gen4(-4.0, 5.0)) { g =>
    powell(g) >= 0.0
  } && powell((0.0, 0.0, 0.0, 0.0)) === 0.0

  property("powellSum") = forAll(genNEL(-1.0, 1.0)) { g =>
    powellSum(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    powellSum(g) === 0.0
  }

  property("powerSum") = forAll(gen4(0.0, 4.0)) { g =>
    powerSum(g) >= 0.0
  } && powerSum((1.0, 2.0, 2.0, 3.0)) === 0.0

  property("price1") = forAll(genNEL(-500.0, 500.0)) { g =>
    price1(g) >= 0.0
  } && price1(NonEmptyList(5.0, -5.0)) === 0.0

  property("price2") = forAll(genNEL(-10.0, 10.0)) { g =>
    price2(g) >= 0.9
  } && forAll(genConst(0.0)) { g =>
    price2(g) === 0.9
  }

  property("qing") = forAll(genNEL(-500.0, 500.0)) { g =>
    qing(g) >= 0.0
  } && qing(NonEmptyList(sqrt(1.0), sqrt(2.0), sqrt(3.0))) ~ 0.0

  property("quadratic") = forAll(gen2(-10.0, 10.0)) { g =>
    quadratic(g) >= -3873.7243
  } && {
    quadratic((0.19388, 0.48513)) ~ (-3873.7243, epsilon(3))
  }

  property("quadric") = forAll(genNEL(-100.0, 100.0)) { g =>
    quadric(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    quadric(g) === 0.0
    quadric(NonEmptyList(1.0, 2.0, 3.0)) === 1.0 + 9.0 + 36.0
  }

  property("quintic") = forAll(genNEL(-10.0, 10.0)) { g =>
    quintic(g) >= 0.0
  } && {
    quintic(NonEmptyList(-1.0, 2.0)) === 0.0 &&
    quintic(NonEmptyList(2.0, -1.0)) === 0.0
  }

  property("rastrigin") = forAll(genNEL(-5.12, 5.12)) { g =>
    rastrigin(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    rastrigin(g) === 0.0
  }

  property("rosenbrock") = forAll(gen2And(-30.0, 30.0)) { g =>
    rosenbrock(g) >= 0.0
  } && forAll(gen2And(1.0, 1.0)) { g =>
    rosenbrock(g) === 0.0
  }

  property("rotatedEllipse") = forAll(gen2And(-500.0, 500.0)) { g =>
    rotatedEllipse1(g) >= 0.0 &&
    rotatedEllipse2(g) >= 0.0
  } && forAll(gen2And(0.0, 0.0)) { g =>
    rotatedEllipse1(g) === 0.0 &&
    rotatedEllipse2(g) === 0.0
  }

  property("salomon") = forAll(genNEL(-100.0, 100.0)) { g =>
    salomon(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    salomon(g) === 0.0
  }

  property("schaffer") = forAll(gen2And(-100.0, 100.0)) { g =>
    schaffer1(g) >= 0.0 &&
    schaffer2(g) >= 0.0 &&
    schaffer3(g) >= 0.0 &&
    schaffer4(g) >= 0.0
  } && forAll(gen2And(0.0, 0.0)) { g =>
    schaffer1(g) === 0.0 &&
    schaffer2(g) === 0.0 &&
    schaffer3(Sized2And(0.0, 1.253115, List())) ~ (0.00156685, epsilon(6)) &&
    schaffer4(Sized2And(0.0, 1.253115, List())) ~ (0.29257900, epsilon(6))
  }

  property("schwefel1") = forAll(genNEL(-100.0, 100.0)) { g =>
    schwefel1(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    schwefel1(g) === 0.0
  }

  property("schwefel12") = forAll(genNEL(-500.0, 500.0)) { g =>
    schwefel12(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    schwefel12(g) === 0.0
  }

  property("schwefel221") = forAll(gen1And(-500.0, 500.0)) { g =>
    schwefel221(g) >= 0.0
  } && forAll(gen1And(0.0, 0.0)) { g =>
    schwefel221(g) === 0.0
  }

  property("schwefel222") = forAll(genNEL(-500.0, 500.0)) { g =>
    schwefel222(g) >= 0.0
  }  && forAll(genConst(0.0)) { g =>
    schwefel222(g) === 0.0
  }

  property("schwefel223") = forAll(genNEL(-10.0, 10.0)) { g =>
    schwefel223(g) >= 0.0
  }  && forAll(genConst(0.0)) { g =>
    schwefel223(g) === 0.0
  }

  property("schwefel226") = forAll(genNEL(-500.0, 500.0)) { g =>
    schwefel226(g) >= 0.0
  } && forAll(genConst(420.968746)) { g =>
    schwefel226(g) ~ (0.0, epsilon(2))
  }

  property("schwefel24") = forAll(gen1And(0.0, 10.0)) { g =>
    schwefel24(g) >= 0.0
  } && forAll(gen1And(1.0, 1.0)) { g =>
    schwefel24(g) === 0.0
  }

  property("schwefel26") = forAll(gen2(-100.0, 100.0)) { g =>
    schwefel26(g) >= 0.0
  } && schwefel26((1.0, 3.0)) === 0.0

  property("shubert") = forAll(gen2(-5.12, 5.12)) { g =>
    shubert(g) >= -186.7309
  }

  property("sixHumpCamelback") = forAll(gen2(-5.0, 5.0)) { g =>
    sixHumpCamelback(g) >= -1.0316285
  } && {
    sixHumpCamelback((-0.08983, 0.7126)) ~ (-1.0316285, epsilon(5)) &&
    sixHumpCamelback((0.08983, -0.7126)) ~ (-1.0316285, epsilon(5))
  }

  property("spherical") = forAll(genNEL(-100.0, 100.0)) { g =>
    spherical(g) === spherical(g.map(_ * -1)) &&
    spherical(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    spherical(g) === 0.0
  }

  property("step") = forAll(genNEL(-100.0, 100.0)) { g =>
    step1(g) >= 0.0 &&
    step2(g) >= 0.0 &&
    step3(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    step1(g) === 0.0 &&
    step2(g) === 0.25 * g.length &&
    step3(g) === 0.0
  }

  property("sumSquares") = forAll(genNEL(-10.0, 10.0)) { g =>
    sumSquares(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    sumSquares(g) === 0.0
  }

  property("sumDifferentPowers") = forAll(genNEL(-1.0, 1.0)) { g =>
    sumDifferentPowers(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    sumDifferentPowers(g) === 0.0
  }

  property("styblinksiTang") = forAll(genNEL(-5.0, 5.0)) { g =>
    styblinksiTang(g) >= -39.16616570377142 * g.length
  } && forAll(genConst(-2.90353401818596)) { g =>
    styblinksiTang(g) ~ (-39.16616570377142 * g.length, epsilon(10))
  }

  property("threeHumpCamelback") = forAll(gen2(-5.0, 5.0)) { g =>
    threeHumpCamelback(g) >= 0.0
  } && threeHumpCamelback((0.0, 0.0)) === 0.0

  property("trecanni") = forAll(gen2(-5.0, 5.0)) { g =>
    trecanni(g) >= 0.0
  } && {
    trecanni((0.0, 0.0))  === 0.0 &&
    trecanni((-2.0, 0.0)) === 0.0
  }

  property("vincent") = forAll(genNEL(0.25, 10.0)) { g =>
    vincent(g) >= -g.length + 0.0
  } && forAll(genConst(7.70628098)) { g =>
    vincent(g) ~ (-g.length + 0.0, epsilon(8))
  }

  property("wolfe") = forAll(gen3(0.0, 2.0)) { g =>
    wolfe(g) >= 0.0
  } && wolfe((0.0, 0.0, 0.0)) === 0.0

  property("wood") = forAll(gen4(-100.0, 100.0)) { g =>
    wood(g) >= 0.0
  } && wood((1.0, 1.0, 1.0, 1.0)) === 0.0

  property("zakharov") = forAll(genNEL(-5.00, 10.0)) { g =>
    zakharov(g) >= 0.0
  } && forAll(genConst(0.0)) { g =>
    zakharov(g) === 0.0
  }

  property("zettle") = forAll(gen2(-1.0, 5.0)) { g =>
    zettle(g) >= -0.0037912371501199
  } && zettle((-0.0299, 0.0)) === -0.0037912371501199

}
