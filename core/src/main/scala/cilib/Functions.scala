package cilib

import cilib.Interval._

import spire.math._
import spire.algebra._
import spire.implicits._

object Functions {

  def absoluteValue[T: Field : Signed](x: List[T]) = Some(x.map(abs(_)).qsum)

  def ackley[T: Field : IsReal : NRoot : Trig](x: List[T]) = {
    val domain = Interval(Closed(-32.768), Closed(32.768))
    if (x in domain) {
      val n = x.length
      val sumcos = x.map(xi => cos(2.0 * pi * xi)).qsum

      spherical(x).map(sum =>
          -20 * exp(-0.2 * sqrt(sum / n)) - exp(sumcos / n) + 20 + exp(1))
    } else None
  }

  def alpine[T: Field : Signed : Trig](x: List[T]) =
    Some(x.map(xi => abs((xi * sin(xi)) + (0.1 * xi))).qsum)

  def beale[T: Field : IsReal](x: List[T]) = {
    val domain = Interval(Closed(-4.5), Closed(4.5))
    x match {
      case List(x1, x2) =>
        if (x in domain)
          Some((1.5 - x1 + x1 * x2) ** 2 +
               (2.25 - x1 + x1 * (x2 ** 2)) ** 2 +
               (2.625 - x1 + x1 * (x2 ** 3)) ** 2)
        else None
      case _ => None
    }
  }

  def bentCigar[T: Field](x: List[T]) =
    if (x.length >= 2)
      Some(x.head ** 2 + 10 ** 6 * x.tail.map(_ ** 2).qsum)
    else None

  def bird[T: Field : Trig](x: List[T]) = x match {
    case List(x1, x2) => Some(sin(x1) * exp((1 - cos(x2)) ** 2) +
                              cos(x2) * exp((1 - sin(x1)) ** 2) +
                              (x1 - x2) ** 2)
    case _            => None
  }

  val domain100 = Interval(Closed(-100.0), Closed(100.0))

  def bohachevsky1[T: Field : IsReal : Trig](x: List[T]) = {
    x match {
      case List(x1, x2) =>
        if (x in domain100)
          Some((x1 ** 2) + 2 * (x2 ** 2) - 0.3 *
              cos(3 * pi * x1) - 0.4 * cos(4 * pi * x2) + 0.7)
        else None
      case _ => None
    }
  }

  def bohachevsky2[T: Field : IsReal : Trig](x: List[T]) = {
    x match {
      case List(x1, x2) =>
        if (x in domain100)
          Some((x1 ** 2) + 2 * (x2 ** 2) - 0.3 *
              cos(3 * pi * x1) * cos(4 * pi * x2) + 0.3)
        else None
      case _ => None
    }
  }

  def bohachevsky3[T: Field : IsReal : Trig](x: List[T]) = {
    x match {
      case List(x1, x2) =>
        if (x in domain100)
          Some((x1 ** 2) + 2 * (x2 ** 2) - 0.3 *
              cos(3 * pi * x1 + 4 * pi * x2) + 0.3)
        else None
      case _ => None
    }
  }

  def booth[T: Field : IsReal](x: List[T]) = {
    val domain = Interval(Closed(-10.0), Closed(10.0))
    x match {
      case List(x1, x2) =>
        if (x in domain)
          Some((x1 + 2 * x2 - 7) ** 2 + (2 * x1 + x2 - 5) ** 2)
        else None
      case _ => None
    }
  }

  def bukin4[T: Field : IsReal : Signed](x: List[T]) = {
    val domain1 = Interval(Closed(-15.0), Closed(-5.0))
    val domain2 = Interval(Closed(-3.0),  Closed(3.0))
    x match {
      case List(x1, x2) =>
        if ((x1 in domain1) && (x2 in domain2))
          Some(100 * (x2 ** 2) + 0.01 * abs(x1 + 10))
        else None
      case _ => None
    }
  }

  def bukin6[T: Field : IsReal : NRoot : Signed](x: List[T]) = {
    val domain1 = Interval(Closed(-15.0), Closed(-5.0))
    val domain2 = Interval(Closed(-3.0),  Closed(3.0))
    x match {
      case List(x1, x2) =>
        if ((x1 in domain1) && (x2 in domain2))
          Some(100.0 * sqrt(abs(x2 - 0.01 * (x1 ** 2))) + 0.01 * abs(x1 + 10.0))
        else None
    case _ => None
    }
  }

  def damavandi[T: Field : IsReal : Signed : Trig](x: List[T]) = {
    val domain = Interval(Closed(0.0), Closed(14.0))
    x match {
      case List(x1, x2) => if (x in domain) {
        val numer = sin(pi * (x1 - 2)) * sin(pi * (x2 - 2))
        val denum = pi ** 2 * (x1 - 2) * (x2 - 2)
        val factor1 = 1 - (abs(numer / denum) ** 5)
        val factor2 = 2 + (x1 - 7) ** 2 + 2 * (x2 - 7) ** 2
        Some(factor1 * factor2)
      } else None
      case _ => None
    }
  }

  def dejongF4[T: Field](x: List[T]) =
    Some(x.zipWithIndex.map { case (xi, i) => (i + 1) * (xi ** 4) }.qsum)

  def easom[T: Field : IsReal : Trig](x: List[T]) = {
    val domain = Interval(Closed(-100.0), Closed(100.0))
    x match {
      case List(x1, x2) =>
        if (x in domain)
          Some(-cos(x1) * cos(x2) * exp(-((x1 - pi) ** 2 + (x2 - pi) ** 2)))
        else None
      case _ => None
    }
  }

  def eggHolder[T : Field : IsReal : NRoot : Signed : Trig](x: List[T]) = {
    val domain = Interval(Closed(-512.0), Closed(512.0))
    x match {
      case List(x1, x2) => if (x in domain)
        Some(-(x2 + 47) * sin(sqrt(abs(x2 + (x1 / 2) + 47))) -
             x1 * sin(sqrt(abs(x1 - x2 - 47))))
        else None
      case _ => None
    }
  }

  def goldsteinPrice[T: Field : IsReal](x: List[T]) = {
    val domain = Interval(Closed(-2.0), Closed(2.0))
    x match {
      case List(x1, x2) => if (x in domain) {
          val term1 = 1 + ((x1 + x2 + 1) ** 2) * (19 - 14 * x1 + 3 * (x1 ** 2) -
                      14 * x2 + 6 * x1 * x2 + 3 * (x2 ** 2))
          val term2 = 30 + ((2 * x1 - 3 * x2) ** 2) * (18 - 32 * x1 + 12 *
                      (x1 ** 2) + 48 * x2 - 36 * x1 * x2 + 27 * (x2 ** 2))
          Some(term1 * term2)
      } else None
      case _ => None
    }
  }

  def griewank[T: Field : NRoot : Trig](x: List[T]) = {
    val prod = x.zipWithIndex.map { case (xi, i) =>
      cos(xi / sqrt(i + 1))
    }.qproduct

    spherical(x).map(sum => 1 + sum * (1.0 / 4000.0) - prod)
  }

  def himmelblau[T: Field](x: List[T]) = x match {
    case List(x1, x2) => Some((x1 ** 2 + x2 - 11) ** 2 + (x1 + x2 ** 2 - 7) ** 2)
    case _            => None
  }

  def katsuura[T: Field : IsReal : NRoot](x: List[T]) = {
    val domain = Interval(Closed(-100.0), Closed(100.0))
    if (x in domain) {
      val n = x.length

      val product = x.zipWithIndex.map { case (xi, i) =>
        val sum = (1 to 32).map { j =>
          val term = (2 ** j) * xi
          abs(term - round(term)) / (2 ** j)
        }.qsum
        (1 + i + sum) ** (10.0 / pow(n, 1.2))
      }.qproduct

      Some((10.0 / (n ** 2)) * product  - 1)
    } else None
  }

  def levy[T: Field : IsReal : Trig](x: List[T]) = {
    val domain = Interval(Closed(-10.0), Closed(10.0))

    def w(i: Int) = 1 + (x(i) - 1) / 4

    if (x in domain) {
      val n = x.length
      val term1 = (sin(3 * pi * w(0))) ** 2

      val term2 = (0 until n - 1).map { i =>
        (w(i) - 1) ** 2 * (1 + sin(3 * pi * w(i) + 1) ** 2)
      }.qsum

      val term3 = (w(n - 1) - 1) ** 2 * (1 + sin(2 * pi * w(n - 1)) ** 2)

      Some(term1 + term2 + term3)
    } else None
  }

  def matyas[T: Field : IsReal](x: List[T]) = {
    val domain = Interval(Closed(-10.0), Closed(10.0))
    x match {
      case List(x1, x2) =>
        if (x in domain) Some(0.26 * (x1 ** 2 + x2 ** 2) - 0.48 * x1 * x2)
        else None
      case _ => None
    }
  }

  def maximum[T: Ordering](x: List[T]) = {
    x match {
      case List() => None
      case _      => Some(x.max)
    }
  }

  def minimum[T: Ordering](x: List[T]) = {
    x match {
      case List() => None
      case _      => Some(x.min)
    }
  }

  def michalewicz[T: Field : IsReal : NRoot : Trig](x: List[T]) = {
    val domain = Interval(Closed(0.0), Closed(pi))

    if (x in domain) {
      val m = 10.0
      Some(-x.zipWithIndex.map { case (xi, i) =>
        sin(xi) * (sin(((i + 1) * (xi ** 2)) / pi) ** (2 * m))
      }.qsum)
    } else None
  }

  def nastyBenchmark[T: Field](x: List[T]) =
    Some(x.zipWithIndex.map { case(xi, i) => (xi - (i + 1)) ** 2 })

  def rastrigin[T: Field : IsReal : Trig](x: List[T]) = {
    val domain = Interval(Closed(-5.12), Closed(5.12))
    if (x in domain)
      Some(10 * x.size + x.map(xi => xi ** 2 - 10 * cos(2 * pi * xi)).qsum)
    else None
  }

  def rosenbrock[T: Field](x: List[T]) =
    Some((0 until x.length - 1).map(i => {
      val term1 = 100 * (x(i + 1) - x(1) ** 2) ** 2
      val term2 = (x(1) - 1) ** 2
      term1 + term2
    }).qsum)

  def salomon[T: Field : NRoot : Trig](x: List[T]) =
    spherical(x).map(sum => -cos(2 * pi * sqrt(sum)) + (0.1 * sqrt(sum)) + 1)

  def schaffer2[T: Field : NRoot](x: List[T]) = x match {
    case List(_, _) => spherical(x).map(sum =>
      (sum ** 0.25) * (50 * (sum ** 0.1) + 1))
    case _          => None
  }

  def spherical[T: Field](x: List[T]) = Some(x.map(_ ** 2).qsum)

  def step[T: Field : IsReal](x: List[T]) =
    Some(x.map(xi => (floor(xi) + 0.5) ** 2).qsum)

  def schwefel[T: Field : IsReal : NRoot : Signed : Trig](x: List[T]) = {
    val domain = Interval(Closed(-500.0), Closed(500.0))

    if (x in domain)
      Some(418.9829 * x.length - x.map(xi => xi * sin(sqrt(abs(xi)))).qsum)
    else None
  }

  def shubert[T: Field : IsReal : Trig](x: List[T]) = {
    val domain = Interval(Closed(-10.0), Closed(10.0))
    if ((x in domain) && (x.length == 2))
      Some(x.map(xi => (1 to 5).map(j => j * cos((j + 1) * xi + j)).qsum).qproduct)
    else None
  }

  def threeHumpCamelback[T: Field : IsReal](x: List[T]) = {
    val domain = Interval(Closed(-5.0), Closed(5.0))
    x match {
      case List(x1, x2) =>
        if (x in domain)
          Some(2 * x1 ** 2 - 1.05 * x1 ** 4 + ((x1 ** 6) / 6) +
               x1 * x2 + x2 ** 2)
        else None
      case _ => None
    }
  }

  def vincent[T: Field : IsReal : Trig](x: List[T]) = {
    val domain = Interval(Closed(0.25), Closed(10.0))
    if (x in domain) Some(-x.map(xi => sin(10.0 * log(xi))).qsum)
    else None
  }

  def zakharov[T: Field : IsReal](x: List[T]) = {
    val domain = Interval(Closed(-5.0), Closed(10.0))
    if (x in domain) {
      val term = x.zipWithIndex.map { case (xi, i) => 0.5 * i * xi }.qsum
      spherical(x).map(sum => sum + term ** 2 + term ** 4)
    } else None
  }

}

object FunctionWrappers {

  def shifted[T: Ring](f: (List[T]) => Option[T], horizontal: T, vertical: T) =
    (x: List[T]) => f(x.map(_ - horizontal)).map(_ - vertical)

}
