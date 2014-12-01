package cilib

import cilib.Functions._
import cilib.FunctionWrappers._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import spire.implicits._

object FunctionsTest extends Properties("Functions") {

  val x = List(0.0, 0.0, 0.0)
  val y = List(1.0, 2.0, 3.0)
  val epsilon = 1e-15

  property("absoluteValue") = forAll { (g: List[Double]) =>
    val abs = absoluteValue(g)
    abs == absoluteValue(g.map(_ * -1)) &&
    abs.forall(_ >= 0.0) &&
    abs.forall(_ >= g.sum)
  } &&
    absoluteValue(x) == Some(0.0) &&
    absoluteValue(y) == Some(6.0) &&
    absoluteValue(y.map(_ * -1)) == Some(6.0)

  val genAckley = Gen.containerOf[List, Double](Gen.choose(-32.768, 32.768))

  property("ackley") = forAll(genAckley) { g =>
    ackley(g).forall(_ >= 0.0)
  } && ackley(x).forall(_ < epsilon)

  property("alpine") = forAll { (g: List[Double]) =>
    alpine(g).forall(_ >= 0.0)
  } && alpine(x) == Some(0.0)

  val genBeale = Gen.containerOfN[List, Double](2, Gen.choose(-4.5, 4.5))

  property("beale") = forAll(genBeale) { g =>
    beale(g).forall(_ >= 0.0)
  } && beale(List(3, 0.5)) == Some(0.0)

  property("bentCigar") = forAll { (g: List[Double]) =>
    g match {
      case List() => bentCigar(g) == None
      case _      => bentCigar(g).forall(_ >= 0.0)
    }
  } && bentCigar(x) == Some(0.0)

  val genBohavchevsky = Gen.containerOfN[List, Double](2, Gen.choose(-100, 100))

  property("bohachevsky") = forAll(genBohavchevsky) { g =>
    bohachevsky1(g).forall(_ >= 0.0) &&
    bohachevsky2(g).forall(_ >= 0.0) &&
    bohachevsky3(g).forall(_ >= 0.0)
  } && {
    bohachevsky1(List(0.0, 0.0)) == Some(0.0) &&
    bohachevsky2(List(0.0, 0.0)) == Some(0.0) &&
    bohachevsky3(List(0.0, 0.0)) == Some(0.0) &&
    bohachevsky1(x) == None &&
    bohachevsky2(x) == None &&
    bohachevsky3(x) == None
  }

  val genBooth = Gen.containerOfN[List, Double](2, Gen.choose(-10.0, 10.0))

  property("booth") = forAll(genBooth) { g =>
    booth(g).forall(_ >= 0.0)
  } && {
    booth(List(1, 3)) == Some(0.0)
    booth(x) == None
  }

  val genBukin = Gen.containerOfN[List, List[Double]](1, for {
    a <- Gen.choose(-15.0, -5.0)
    b <- Gen.choose(-3.0, 3.0)
  } yield List(a, b))

  property("bukin") = forAll(genBukin) { g =>
    (bukin4(g.flatten).forall(_ >= 0.0)) && (bukin6(g.flatten)forall(_ >= 0.0))
  } && {
    bukin4(List(-10, 0)) == Some(0.0) &&
    bukin6(List(-10, 1)) == Some(0.0) &&
    bukin4(x)            == None &&
    bukin6(x)            == None
  }

  val genDamavandi = Gen.containerOfN[List, Double](2, Gen.choose(0.0, 14.0))

  property("damavandi") = forAll(genDamavandi) { g =>
    damavandi(g).forall(_ >= 0.0)
  }

  val genDejongF4 = Gen.containerOf[List, Double](Gen.choose(-1.28, 1.28))

  property("dejongF4") = forAll(genDejongF4) { g =>
    dejongF4(g).forall(_ >= 0.0)
  } && forAll(Gen.containerOf[List, Double](Gen.const(0.0))) { g =>
    dejongF4(g) == Some(0.0)
  }

  val genEasom = Gen.containerOfN[List, Double](2, Gen.choose(-100.0, 100.0))

  property("easom") = forAll(genEasom) { g =>
    easom(g).forall(_ >= -1.0)
  } && easom(List(Math.PI, Math.PI)) == Some(-1.0)

  val genEggHolder =
    Gen.containerOfN[List, Double](2, Gen.choose(-512.0, 512.0))

  property("eggHolder") = forAll(genEggHolder) { g =>
    eggHolder(g).forall(_ >= -959.6407)
  } && eggHolder(List(512.0, 404.2319)) == Some(-959.6406627106155)

  val genGoldsteinPrice = Gen.containerOfN[List, Double](2, Gen.choose(-2.0, 2.0))

  property("goldsteinPrice") = forAll(genGoldsteinPrice) { g =>
    goldsteinPrice(g).forall(_ >= 3.0)
  } && goldsteinPrice(List(0.0, -1.0)) == Some(3.0)

  property("griewank") = forAll { (g: List[Double]) =>
    griewank[Double](g).forall(_ >= 0.0)
  } && griewank[Double](x) == Some(0.0)

  val genHimmelblau = Gen.containerOfN[List, Double](2, Arbitrary.arbitrary[Double])

  property("himmelblau") = forAll(genHimmelblau) { g =>
    himmelblau(g).forall(_ >= 0.0)
  } && himmelblau(x) == None

  val genKatsuura = Gen.containerOf[List, Double](Gen.choose(-100.0, 100.0))

  property("katsuura") = forAll(genKatsuura) { g =>
    g match {
      case List() => katsuura(g) == None
      case _ => katsuura(g).forall(_ >= 0.0)
    }
  }

  val genLevy = Gen.containerOf[List, Double](Gen.choose(-10.0, 10.0))

  property("levy") = forAll(genLevy) { g =>
    levy(g).forall(_ >= 0.0)
  } && forAll(Gen.containerOf[List, Double](Gen.const(1.0))) { g =>
    g match {
      case List() => levy(g) == None
      case _ => levy(g).forall(_ <= epsilon)
    }
  }

  val genMatyas = Gen.containerOfN[List, Double](2, Gen.choose(-10.0, 10.0))

  property("matyas") = forAll(genMatyas) { g =>
    matyas(g).forall(_ >= 0.0)
  } && matyas(List(0.0, 0.0)) == Some(0.0)

  property("maximum") = forAll { (x: List[Double]) =>
    x match {
      case List() => maximum(x) == None
      case _      => {
        maximum(x) == Some(x.max) &&
        maximum(x).forall(xi => x.exists(_ == xi))
      }
    }
  }

  val genMichalewicz = Gen.containerOf[List, Double](Gen.choose(0.0, Math.PI))

  property("michalewicz") = forAll(genMichalewicz) { g =>
    michalewicz(g).forall(_ >= -g.length)
  } && michalewicz(List(2.20, 1.57)).forall(_ == -1.801140718473825)

  property("minimum") = forAll { (x: List[Double]) =>
    x match {
      case List() => minimum(x) == None
      case _      => {
        minimum(x) == Some(x.min) &&
        minimum(x).forall(xi => x.exists(_ == xi))
      }
    }
  }

  val genRastrigin = Gen.containerOf[List, Double](Gen.choose(-5.12, 5.12))

  property("rastrigin") = forAll(genRastrigin) { g =>
    rastrigin(x)forall(_ >= 0.0)
  } && rastrigin(x) == Some(0.0)

  property("rosenbrock") = forAll { (g: List[Double]) =>
    rosenbrock(g).forall(_ >= 0.0)
  } && forAll(Gen.containerOf[List, Double](Gen.const(1.0))) { g =>
    rosenbrock(g) == Some(0.0)
  }

  val genSalomon = Gen.containerOf[List, Double](Gen.choose(-4.0, 4.0))

  property("salomon") = forAll(genSalomon) { g =>
    salomon(g).forall(_ >= 0.0)
  } && forAll(Gen.containerOf[List, Double](Gen.const(0.0))) { g =>
    salomon(g) == Some(0.0)
  }

  val genSchaffer2 = Gen.containerOfN[List, Double](2, Arbitrary.arbitrary[Double])

  property("schaffer2") = forAll(genSchaffer2) { g =>
    schaffer2(g).forall(_ >= 0.0)
  } && schaffer2(List(0.0, 0.0)) == Some(0.0)

  property("spherical") = forAll { (g: List[Double]) =>
    spherical(g) == spherical(g.map(_ * -1)) &&
    spherical(g).forall(_ >= 0.0)
  }

  property("step") = forAll { (g: List[Double]) =>
    step[Double](g).forall(_ >= 0.0)
  } && {
    val y = List(1.3, 2.5, 3.7)

    step[Double](x) == Some(0.75) &&
    step[Double](y) == Some(2.25 + 6.25 + 12.25)
  }

  val genSchwefel = Gen.containerOf[List, Double](Gen.choose(-500.0, 500))

  property("schwefel") = forAll(genSchwefel) { g =>
    schwefel(g).forall(_ >= 0)
  }

  val genShubert = Gen.containerOfN[List, Double](2, Gen.choose(-5.12, 5.12))

  property("shubert") = forAll(genShubert) { g =>
    shubert(g).forall(_ >= -186.7309)
  }

  val genThreeHumpCamelback =
    Gen.containerOfN[List, Double](2, Gen.choose(-5, 5))

  property("threeHumpCamelback") = forAll(genThreeHumpCamelback) { g =>
    threeHumpCamelback(g).forall(_ >= 0.0)
  } && threeHumpCamelback(List(0.0, 0.0)) == Some(0.0)

  val genVincent = Gen.containerOf[List, Double](Gen.choose(0.25, 10.0))

  property("vincent") = forAll(genVincent) { g =>
    vincent(g).forall(_ >= -g.length)
    vincent(g).forall(_ <= g.length)
  }

  val genZakharov = Gen.containerOf[List, Double](Gen.choose(-5.00, 10.0))

  property("zakharov") = forAll { (g: List[Double]) =>
    zakharov(g).forall(_ >= 0.0)
  } && zakharov(x) == Some(0.0)

}

object FunctionWrappersTest extends Properties("FunctionWrappers") {

  val x = List(0.0, 0.0, 0.0)
  val y = List(1.0, 2.0, 3.0)

  val shift = shifted[Double]((spherical _), 10.0, 10.0)

  property("shifted") = forAll { (g: List[Double]) =>
    shift(g).forall(sh => spherical(g).forall(sp => sh >= sp - 10.0))
  } && {
    shift(x) == Some(100 * 3 - 10.0) &&
    shift(y) == Some(81.0 + 64.0 + 49.0 - 10.0)
  }

}
