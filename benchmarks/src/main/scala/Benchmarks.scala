package cilib
package benchmarks

import _root_.scala.Predef.{any2stringadd => _, _}

import scalaz.{Functor,Foldable,Foldable1,Applicative,Monoid,NonEmptyList,Id,OneAnd}
import scalaz.syntax.apply._
import scalaz.syntax.foldable1._
import scalaz.std.list._
import scalaz.std.option._
import scalaz.syntax.std.option._

import spire.math._
import spire.algebra.{Monoid => _, _}
import spire.implicits._

object Benchmarks {

  type Sized1[A] = Id.Id[A]
  type Sized2[A] = (A, A)
  type Sized3[A] = (A, A, A)
  type Sized4[A] = (A, A, A, A)
  type Sized5[A] = (A, A, A, A, A)
  type Sized6[A] = (A, A, A, A, A, A)

  type Sized1And[F[_], A] = OneAnd[F, A]
  final case class Sized2And[F[_], A](a: A, b: A, rest: F[A])

  def toSized1[F[_]: Foldable, A](x: F[A]): Option[Sized1[A]] = x.index(0)

  def toSized2[F[_]: Foldable, A](x: F[A]): Option[Sized2[A]] =
    (x.index(0) |@| x.index(1)) { (_, _) }

  def toSized3[F[_]: Foldable, A](x: F[A]): Option[Sized3[A]] =
    (x.index(0) |@| x.index(1) |@| x.index(2)) { (_, _, _) }

  def toSized4[F[_]: Foldable, A](x: F[A]): Option[Sized4[A]] =
    (x.index(0) |@| x.index(1) |@| x.index(2) |@| x.index(3)) { (_, _, _, _) }

  def toSized5[F[_]: Foldable, A](x: F[A]): Option[Sized5[A]] =
    (x.index(0) |@| x.index(1) |@| x.index(2) |@| x.index(3) |@| x.index(4)) { (_, _, _, _, _) }

  def toSized6[F[_]: Foldable, A](x: F[A]): Option[Sized6[A]] =
    (x.index(0) |@| x.index(1) |@| x.index(2) |@| x.index(3) |@| x.index(4) |@| x.index(5)) {
      (_, _, _, _, _, _)
    }

  // Functions
  def absoluteValue[F[_]: Foldable1, A: Signed: Monoid](x: F[A]) =
    x.foldMap(abs(_))

  def ackley[F[_]: Foldable1, A: Field : IsReal : NRoot : Trig : Monoid](x: F[A]) = {
    val n = x.length
    val sumcos = x.foldMap(x => cos(2 * pi * x))
    val sumsqr = x.foldMap(_ ** 2)

    -20 * exp(-0.2 * sqrt(sumsqr / n)) - exp(sumcos / n) + 20 + e
  }

  def adjiman[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    cos(x1) * sin(x2) - (x1) / (x2 ** 2 + 1)
  }

  def alpine1[F[_]: Foldable1, A: Field : Signed : Trig : Monoid](x: F[A]) =
    x.foldMap(xi => abs((xi * sin(xi)) + (0.1 * xi)))

  def arithmeticMean[F[_]: Foldable1, A: NRoot : Monoid](x: F[A])(implicit A: Field[A]) = {
    val n = x.length
    val avg = x.fold / n
    val rootProd = x.foldLeft(A.one)(_ * _) ** (1.0 / n)

    (avg - rootProd) ** 2
  }

  def bartelsConn[A: Ring : Signed : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    abs(x1 ** 2 + x2 ** 2 + x1 * x2) + abs(sin(x1)) + abs(cos(x2))
  }

  def beale[A: Field : IsReal](x: Sized2[A]) = {
    val (x1, x2) = x
    (1.5 - x1 + x1 * x2) ** 2 +
    (2.25 - x1 + x1 * (x2 ** 2)) ** 2 +
    (2.625 - x1 + x1 * (x2 ** 3)) ** 2
  }

  def biggsEXP2[A: Field : Trig : Monoid](x: Sized2[A]) = {
    val (x1, x2) = x
    (1 to 10).toList.foldMap { i =>
      val ti = 0.1 * i
      val yi = exp(-ti) - 5.0 * exp(-10 * ti)
      (exp(-ti * x1) - 5.0 * exp(-ti * x2) - yi) ** 2
    }
  }

  def biggsEXP3[A: Field : Trig : Monoid](x: Sized3[A]) = {
    val (x1, x2, x3) = x
    (1 to 10).toList.foldMap { i =>
      val ti = 0.1 * i
      val yi = exp(-ti) - 5.0 * exp(-10 * ti)
      (exp(-ti * x1) - x3 * exp(-ti * x2) - yi) ** 2
    }
  }

  def biggsEXP4[A: Field : Trig : Monoid](x: Sized4[A]) = {
    val (x1, x2, x3, x4) = x
    (1 to 10).toList.foldMap { i =>
      val ti = 0.1 * i
      val yi = exp(-ti) - 5.0 * exp(-10 * ti)
      (x3 * exp(-ti * x1) - x4 * exp(-ti * x2) - yi) ** 2
    }
  }

  def biggsEXP5[A: Field : Trig : Monoid](x: Sized5[A]) = {
    val (x1, x2, x3, x4, x5) = x
    (1 to 11).toList.foldMap { i =>
      val ti = 0.1 * i
      val yi = exp(-ti) - 5.0 * exp(-10 * ti) + 3.0 * exp(-4.0 * ti)
      (x3 * exp(-ti * x1) - x4 * exp(-ti * x2) + 3.0 * exp(-ti * x5) - yi) ** 2
    }
  }

  def biggsEXP6[A: Field : Trig : Monoid](x: Sized6[A]) = {
    val (x1, x2, x3, x4, x5, x6) = x
    (1 to 13).toList.foldMap { i =>
      val ti = 0.1 * i
      val yi = exp(-ti) - 5.0 * exp(-10 * ti) + 3.0 * exp(-4.0 * ti)
      (x3 * exp(-ti * x1) - x4 * exp(-ti * x2) + x6 * exp(-ti * x5) - yi) ** 2
    }
  }

  def bird[A: Ring : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    sin(x1) * exp((1 - cos(x2)) ** 2) +
    cos(x2) * exp((1 - sin(x1)) ** 2) + (x1 - x2) ** 2
  }

  def bohachevsky1[A: Field : IsReal : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    (x1 ** 2) + 2 * (x2 ** 2) - 0.3 *
    cos(3 * pi * x1) - 0.4 * cos(4 * pi * x2) + 0.7
  }

  def bohachevsky2[A: Field : IsReal : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    (x1 ** 2) + 2 * (x2 ** 2) - 0.3 *
    cos(3 * pi * x1) * cos(4 * pi * x2) + 0.3
  }

  def bohachevsky3[A: Field : IsReal : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    (x1 ** 2) + 2 * (x2 ** 2) - 0.3 *
    cos(3 * pi * x1 + 4 * pi * x2) + 0.3
  }

  def booth[A: IsReal : Ring](x: Sized2[A]) = {
    val (x1, x2) = x
    (x1 + 2 * x2 - 7) ** 2 + (2 * x1 + x2 - 5) ** 2
  }

  def boxBettsQuadraticSum[A: Field : Trig : Monoid](k: Int)(x: Sized3[A]) = {
    val (x1, x2, x3) = x
    (1 to k).toList.foldMap { i =>
      val co = -0.1 * i
      val t1 = exp(co * x1)
      val t2 = exp(co * x2)
      val t3 = (exp(co) - exp(-i.toDouble)) * x3
      (t1 - t2 - t3) ** 2
    }
  }

  def brad[A: Field : Monoid](x: Sized3[A]) = {
    val (x1, x2, x3) = x
    val y = List(
      0.14, 0.18, 0.22, 0.25, 0.29,
      0.32, 0.35, 0.39, 0.37, 0.58,
      0.73, 0.96, 1.34, 2.10, 4.39
    )

    (1 to 15).toList.foldMap { ui =>
      val vi = 16 - ui
      val wi = spire.math.min(ui, vi)
      ((y(ui - 1) - x1 - ui) / (vi * x2 + wi * x3)) ** 2
    }
  }

  def braninRCOS1[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (x2 - (5.1 / (4.0 * (pi ** 2))) * (x1 ** 2) +
      (5.0 / pi) * x1 - 6.0) ** 2
    val t2 = 10.0 * (1.0 - 1 / (8.0 * pi)) * cos(x1)
    t1 + t2 + 10.0
  }

  def braninRCOS2[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (-1.275 * (x1 ** 2)/(pi ** 2) + (5.0 * x1) / pi + x2 - 6) ** 2
    val t2 = (10.0 - 5.0 / (4.0 * pi)) * cos(x1) * cos(x2)
    val t3 = log((x1 ** 2) + (x2 ** 2) + 1.0) + 10.0
    t1 + t2 + t3
  }

  def brent[F[_]: Foldable1, A: Ring : Trig : Monoid](x: F[A]) =
    x.foldMap(xi => (xi + 10) ** 2) + exp(-x.foldMap(_ ** 2))

  def brown[F[_]: Foldable, A: Ring : Monoid : NRoot](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case List(xi, xi1) => (xi ** 2).fpow((xi1 ** 2) + 1) + (xi1 ** 2).fpow((xi ** 2) + 1)
    }

  def bukin2[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    100 * (x2 - 0.01 * (x1 ** 2) + 1) ** 2 + 0.01 * ((x1 + 10) ** 2)
  }

  def bukin2Adapted[A: Field](x: Sized2[A]) =
    bukin2(x) ** 2

  def bukin4[A: Field : Signed](x: Sized2[A]) = {
    val (x1, x2) = x
    100 * (x2 ** 2) + 0.01 * abs(x1 + 10)
  }

  def bukin6[A: Field : NRoot : Signed](x: Sized2[A]) = {
    val (x1, x2) = x
    100.0 * sqrt(abs(x2 - 0.01 * (x1 ** 2))) + 0.01 * abs(x1 + 10.0)
  }

  def centralTwoPeakTrap[A: Field : Order](x1: Sized1[A]) =
    if      (x1 < 0)   0.0 * x1
    else if (x1 <= 10) (-160.0 / 10.0) * x1
    else if (x1 <= 15) (-160.0 / 5.0) * (15.0 - x1)
    else if (x1 <= 20) (-200.0 / 5.0) * (x1 - 15.0)
    else               (0.0 * x1) - 200.0

  def chichinadze[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (x1 ** 2) - (12.0 * x1) + 11.0
    val t2 = 10.0 * cos(pi * (x1 / 2.0)) + 8.0 * sin(5.0 * pi * x1)
    val t3 = ((1.0 / 5.0) ** 0.5) * exp(-0.5 * ((x2 - 0.5) ** 2))

    t1 + t2 - t3
  }

  def chungReynolds[F[_]: Foldable1, A: Ring : Monoid](x: F[A]) = x.foldMap(_ ** 2) ** 2

  def cigar[F[_]: Foldable, A: Field : Monoid](condition: Double = 10e6)(x: Sized2And[F, A]) =
    x.a ** 2 + x.b ** 2 * condition + x.rest.foldMap(_ ** 2) * condition

  def colville[A: Field](x: Sized4[A]) = {
    val (x1, x2, x3, x4) = x
    val t1 = 100.0 * ((x1 - (x2 ** 2)) ** 2) + ((1.0 - x1) ** 2)
    val t2 = 90.0 * ((x4 - x3) ** 2) + ((1.0 - x3) ** 2)
    val t3 = 10.1 * (((x2 - 1.0) ** 2) + ((x4 - 1.0) ** 2))
    val t4 = 19.8 * (x2 - 1.0) * (x4 - 1.0)

    t1 + t2 + t3 + t4
  }

  def cosineMixture[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) =
    -0.1 * x.foldMap(xi => cos(5 * pi * xi)) + x.foldMap(_ ** 2)

  def crossInTray[F[_]: Foldable1, A: NRoot : Signed : Trig : Monoid](x: F[A])(implicit A: Field[A]) = {
    val t1 = x.foldLeft(A.one)((a,c) => a * sin(c))
    val t2 = exp(abs(100.0 - (sqrt(x.foldMap(_ ** 2)) / pi)))

    -0.0001 * ((abs(t1 * t2) + 1.0) ** 0.1)
  }

  def crossLegTable[F[_]: Foldable1, A: Field : NRoot : Signed : Trig : Monoid](x: F[A]) =
    -1.0 / (crossInTray(x) / -0.0001)

  def crossCrowned[F[_]: Foldable1, A: Field : NRoot : Signed : Trig : Monoid](x: F[A]) =
    -crossInTray(x)

  def csendes[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) =
    x.foldMapM { xi =>
      if (xi != 0.0)
        ((xi ** 6) * (2 + sin(1.0 / xi))).some
      else
        none
    }

  def cube[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    100 * ((x2 - (x1 ** 3)) ** 2) + ((1.0 - x1) ** 2)
  }

  def damavandi[A: Field : IsReal : Signed : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    if ((x1 != 2.0) && (x2 != 2.0)) {
      val numer = sin(pi * (x1 - 2)) * sin(pi * (x2 - 2))
      val denom = (pi ** 2) * (x1 - 2) * (x2 - 2)
      val factor1 = 1 - (abs(numer / denom) ** 5)
      val factor2 = 2 + ((x1 - 7) ** 2) + 2 * ((x2 - 7) ** 2)
      (factor1 * factor2).some
    } else none
  }

  def deb1[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) =
    -(1.0 / x.length) * x.foldMap(xi => sin(5 * pi * xi) ** 6)

  def deb3[F[_]: Functor : Foldable1, A: Field : NRoot : Trig : Monoid](x: F[A]) =
    deb1(x.map(xi => (xi ** 0.75) - 0.05))

  def decanomial[A: Field : Signed : Monoid](x: Sized2[A]) = {
    val (x1, x2) = x
    val coX1 = List(1, -20, 180, -960, 3360, -8064,
                    13340, -15360, 11520, -5120, 2624)
    val coX2 = List(1, 12, 54, 108, 81)

    def one(l: List[Int], xi: A) =
      abs(l.zipWithIndex.foldMap {
        case (ci, i) => ci * (xi ** (l.length - 1 - i))
      })

    0.001 * ((one(coX2, x2) + one(coX1, x1)) ** 2)
  }

  def deckkersAarts[A: Field](x: Sized2[A]) = {
    val (x1, x2) = (x._1 ** 2, x._2 ** 2)
    val t1 = (10 ** 5) * x1 + x2
    val t2 = (x1 + x2) ** 2
    val t3 = (1.0 / (10 ** 5)) * ((x1 + x2) ** 4)

    t1 - t2 + t3
  }

  def deVilliersGlasser1[A: Field : NRoot : Trig : Monoid](x: Sized4[A]) = {
    val (x1, x2, x3, x4) = x
    (1 to 24).toList.foldMap { i =>
      val ti = 0.1 * (i - 1)
      val yi = 60.137 * (1.371 ** ti) * sin(3.112 * ti + 1.761)
      val t1 = x1 * (x2 ** ti)
      val t2 = sin(x3 * ti + x4)
      val t3 = yi
      (t1 * t2 - t3) ** 2
    }
  }

  def deVilliersGlasser2[A: Field : NRoot : Trig : Monoid](x: Sized5[A]) = {
    val (x1, x2, x3, x4, x5) = x
    (1 to 16).toList.foldMap { i =>
      val ti = 0.1 * (i - 1)
      val yi = 53.81 * (1.27 ** ti) * tanh(3.012 * ti + sin(2.13 * ti)) *
        cos(exp(0.507) * ti)
      val t1 = x1 * (x2 ** ti)
      val t2 = tanh(x3 * ti + sin(x4 * ti))
      val t3 = cos(ti * exp(x5))
      val t4 = yi

      (t1 * t2 * t3 - t4) ** 2
    }
  }

  def differentPowers[F[_]: Foldable, A: NRoot : Ring : Signed : Monoid](x: Sized2And[F, A]) = {
    val n = x.rest.length + 2
    val inner = (x.a :: x.b :: x.rest.toList).zipWithIndex.foldMap {
      case (xi, i) => abs(xi) ** (2 + ((4 * i) / (n - 1)))
    }
    sqrt(inner)
  }

  def discus[F[_]: Foldable, A: Ring : Monoid](x : Sized1And[F, A]) =
    (10 ** 6) * (x.head ** 2) + x.tail.foldMap(_ ** 2)

  def dixonPrice[F[_]: Foldable, A: Ring : Monoid](x: Sized2And[F, A]) = {
    def t(l: (Seq[A], Int)) = l match {
      case (Seq(xi, xi1), i) => (i + 2) * (((2 * (xi1 ** 2)) - xi) ** 2)
    }

    val t1 = ((x.a - 1) ** 2)
    val t2 = (x.a :: x.b :: x.rest.toList).sliding(2).toList.zipWithIndex.foldMap(t)
    t1 + t2
  }

  def dolan[A: Field : Signed : Trig](x: Sized5[A]) = {
    val (x1, x2, x3, x4, x5) = x
    val t1 = (x1 + 1.7 * x2) * sin(x1)
    val t2 = -1.5 * x3 - 0.1 * x4 * cos(x4 + x5 - x1)
    val t3 = 0.2 * (x5 ** 2) - x2 -1.0
    abs(t1 + t2 + t3)
  }

  def dropWave[F[_]: Foldable1, A: Field : NRoot : Trig : Monoid](x: F[A]) = {
    val sumsqr = x.foldMap(_ ** 2)
    -(1 + cos(12 * sqrt(sumsqr))) / (2 + 0.5 * sumsqr)
  }

  def easom[A: Field : IsReal : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    -cos(x1) * cos(x2) * exp(-((x1 - pi) ** 2 + (x2 - pi) ** 2))
  }

  def eggCrate[F[_]: Foldable1, A: Ring : Trig : Monoid](x: F[A]) =
    x.foldMap(_ ** 2) + 24 * x.foldMap(sin(_) ** 2)

  def eggHolder[F[_]: Foldable, A: Field : NRoot : Signed : Trig : Monoid](x: Sized2And[F, A]) = {
    def g(l: Seq[A]) = l match {
      case Seq(x1, x2) =>
        -(x2 + 47) * sin(sqrt(abs(x2 + (x1 / 2) + 47))) -
        x1 * sin(sqrt(abs(x1 - x2 - 47)))
    }
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap(g)
  }

  def elliptic[F[_]: Foldable, A: Field : Monoid](x: Sized2And[F, A]) = {
    val n = x.rest.length + 2
      (x.a :: x.b :: x.rest.toList).zipWithIndex.foldMap {
        case (xi, i) => (10e6 ** (i / (n - 1.0))) * (xi ** 2)
      }
  }

  def elAttarVidyasagarDutta[A: Ring](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = ((x1 ** 2) + x2 - 10) ** 2
    val t2 = (x1 + (x2 ** 2) - 7) ** 2
    val t3 = ((x1 ** 2) + (x2 ** 3) - 1) ** 2
    t1 + t2 + t3
  }

  def exponential1[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) =
    -exp(-0.5 * x.foldMap(_ ** 2))

  def exponential2[A: Field : Trig : Monoid](x: Sized2[A]) = {
    val (x1, x2) = x
    (0 to 9).toList.foldMap { i =>
      val t1 = 1.0 * exp(-i * x1 / 10.0)
      val t2 = 5.0 * exp(-i * x2 / 10.0)
      val t3 = 1.0 * exp(-i / 10.0)
      val t4 = 5.0 * exp(-i.toDouble)
      (t1 - t2 - t3 + t4) ** 2
    }
  }

  def freudensteinRoth[A: Ring](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (x1 - 13 + ((5 - x2) * x2 - 2) * x2) ** 2
    val t2 = (x1 - 29 + ((x2 + 1) * x2 -14) * x2) ** 2
    t1 + t2
  }

  def gear[A: Field : IsReal](x: Sized4[A]) = {
    val (x1, x2, x3, x4) = (
      floor(x._1), floor(x._2), floor(x._3), floor(x._4)
    )
    val t1 = 1.0 / 6.931
    val numer = x1 * x2
    val denom = x3 * x4
    (t1 - (numer / denom)) ** 2
  }

  def giunta[A: Field : Trig : Monoid](x: Sized2[A]) =
    0.6 + List(x._1, x._2).foldMap { xi =>
      val factor = (16.0 / 15.0) * xi - 1
      val t1 = sin(factor)
      val t2 = t1 ** 2
      val t3 = (1.0 / 50.0) * sin(4 * factor)
      t1 + t2 + t3
    }

  def goldsteinPrice1[A: Ring](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = 1 + ((x1 + x2 + 1) ** 2) * (19 - 14 * x1 + 3 * (x1 ** 2) -
      14 * x2 + 6 * x1 * x2 + 3 * (x2 ** 2))
    val t2 = 30 + ((2 * x1 - 3 * x2) ** 2) * (18 - 32 * x1 + 12 *
      (x1 ** 2) + 48 * x2 - 36 * x1 * x2 + 27 * (x2 ** 2))
    t1 * t2
  }

  def goldsteinPrice2[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = exp(0.5 * (((x1 ** 2) + (x2 ** 2) - 25) ** 2))
    val t2 = sin(4 * x1 - 3 * x2) ** 4
    val t3 = 0.5 * ((2 * x1 + x2 - 10) ** 2)
    t1 + t2 + t3
  }

  def griewank[F[_]: Foldable1, A: Field : NRoot : Trig : Monoid](x: F[A]) = {
    val prod = x.toList.zipWithIndex.map { case (xi, i) =>
      cos(xi / sqrt(i + 1.0))
    }.qproduct

    1 + x.foldMap(_ ** 2) * (1.0 / 4000.0) - prod
  }

  def hansen[A: Ring : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (0 to 4).map { i =>
      (i + 1) * cos(i * x1 + i + 1)
    }.qsum
    val t2 = (0 to 4).map { j =>
      (j + 1) * cos((j + 2) * x2 + j + 1)
    }.qsum

    t1 * t2
  }

  def hartman3[A: Field : Trig](x: Sized3[A]) = {
    val y = List(x._1, x._2, x._3)

    val a = List(
      List(3.0, 10.0, 30.0),
      List(0.1, 10.0, 35.0),
      List(3.0, 10.0, 30.0),
      List(0.1, 10.0, 35.0)
    )

    val c = List(1.0, 1.2, 3.0, 3.2)

    val p = List(
      List(0.6890, 0.1170, 0.2673),
      List(0.4699, 0.4387, 0.7470),
      List(0.1091, 0.8732, 0.5547),
      List(0.0381, 0.5743, 0.8828)
    )

    val ts = for {
      i <- 0 until 4
      power = for (j <- 0 until 3) yield a(i)(j) * ((y(j) - p(i)(j)) ** 2)
    } yield c(i) * exp(-power.qsum)

    -ts.qsum
  }

  def hartman6[A: Field : Trig](x: Sized6[A]) = {
    val y = List(x._1, x._2, x._3, x._4, x._5, x._6)

    val a = List(
      List(10.0, 3.00, 17.0, 3.50, 1.70, 8.00),
      List(0.05, 10.0, 17.0, 0.10, 8.00, 14.0),
      List(3.00, 3.50, 1.70, 10.0, 17.0, 8.00),
      List(17.0, 8.00, 0.05, 10.0, 0.10, 14.0)
    )

    val c = List(1.0, 1.2, 3.0, 3.2)

    val p = List(
      List(0.1312, 0.1696, 0.5569, 0.0124, 0.8283, 0.5886),
      List(0.2329, 0.4135, 0.8307, 0.3736, 0.1004, 0.9991),
      List(0.2348, 0.1451, 0.3522, 0.2883, 0.3047, 0.6650),
      List(0.4047, 0.8828, 0.8732, 0.5743, 0.1091, 0.0381)
    )

    val ts = for { i <- 0 until 4
      power = for (j <- 0 until 6) yield a(i)(j) * ((y(j) - p(i)(j)) ** 2)
    } yield c(i) * exp(-power.qsum)

    -ts.qsum
  }

  def helicalValley[A: Field : NRoot : Order : Trig](x: Sized3[A]) = {
    val (x1, x2, x3) = x

    val theta = {
      val t  = if (x1 >= (x1 * 0)) 0.0 else 0.5
      atan((x2 / x1) + t) / (2 * pi)
    }

    val t1 = (x2 - 10.0 * theta) ** 2
    val t2 = sqrt((x1 ** 2) + (x2 ** 2)) - 1.0
    val t3 = x3 ** 2

    100.0 * (t1 + t2) + t3
  }

  def himmelblau[A: Ring](x: Sized2[A]) = {
    val (x1, x2) = x
    (x1 ** 2 + x2 - 11) ** 2 + (x1 + x2 ** 2 - 7) ** 2
  }

  def hosaki[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = 1 - 8 * x1 + 7 * (x1 ** 2)
    val t2 = (7.0 / 3.0) * (x1 ** 3)
    val t3 = (1.0 / 4.0) * (x1 ** 4)
    val t4 = (x2 ** 2) * exp(-x2)
    (t1 - t2 + t3) * t4
  }

  def hyperEllipsoid[F[_]: Foldable1, A: Ring](x: F[A]) =
    x.toList.zipWithIndex.map { case (xi, i) => i * (xi ** 2) }.qsum

  def hyperEllipsoidRotated[F[_]: Foldable1, A: Ring : Monoid](x: F[A]) = {
    val y = x.toList
    val values = (1 to x.length).toList.map(y take _)
    values.foldMap(_.foldMap(xi => xi ** 2))
  }

  def jennrichSampson[A: Ring : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val ts = for { i <- 1 to 10
      t1 = 2 + 2 * i
      t2 = exp(i * x1) + exp(i * x2)
    } yield (t1 - t2) ** 2

    ts.qsum
  }

  def judge[A: Field : Monoid](x: Sized2[A]) = {
    val (x1, x2) = x
    val A = List(
      4.284, 4.149, 3.877, 0.533, 2.211,
      2.389, 2.145, 3.231, 1.998, 1.379,
      2.106, 1.428, 1.011, 2.179, 2.858,
      1.388, 1.651, 1.593, 1.046, 2.152
    )

    val B = List(
      0.286, 0.973, 0.384, 0.276, 0.973,
      0.543, 0.957, 0.948, 0.543, 0.797,
      0.936, 0.889, 0.006, 0.828, 0.399,
      0.617, 0.939, 0.784, 0.072, 0.889
    )

    val C = List(
      0.645, 0.585, 0.310, 0.058, 0.455,
      0.779, 0.259, 0.202, 0.028, 0.099,
      0.142, 0.296, 0.175, 0.180, 0.842,
      0.039, 0.103, 0.620, 0.158, 0.704
    )

    val mappedB = B.map(_ * x2)
    val mappedC = C.map(_ * (x2 ** 2))

    val t1 = mappedB.zip(mappedC).map { case (ai, bi) => ai + bi }
    val t2 = t1.map(_ + x1)

    t2.zip(A).foldMap {
      case (t2, ai) => (t2 - ai) ** 2
    }
  }

  def katsuura[F[_]: Foldable1, A: Field : IsReal : NRoot](x: F[A]) = {
    val n = x.length

    val ts = x.toList.zipWithIndex.map { case (xi, i) =>
      val t1 = i + 1
      val d = 32
      val t2 = for (k <- 1 to d) yield floor((2 ** k) * xi) * (1.0 / (2 ** k))
      1 + t1 * t2.qsum
    }

    ts.qproduct
  }

  def keane[A: Field : NRoot : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val numer = (sin(x1 - x2) ** 2) * (sin(x1 + x2) ** 2)
    val denom = sqrt((x1 ** 2) + (x2 ** 2))
    numer / denom
  }

  def kowalik[A: Field : Monoid](x: Sized4[A]) = {
    val (x1, x2, x3, x4) = x
    val b = List(
      4.0, 2.0, 1.0, 0.5, 0.25, 1.0 / 6.0, 0.125,
      0.1, 1.0 / 12.0, 1.0 / 14.0, 0.0625
    )

    val a = List(
      0.1957, 0.1947, 0.1735, 0.1600, 0.0844, 0.0627,
      0.0456, 0.0342, 0.0323, 0.0235, 0.0246
    )

    val ts = a.zip(b).map { case (ai, bi) =>
      val numer = x1 * ((bi ** 2) + bi * x2)
      val denom = (bi ** 2) + bi * x3 + x4
      ai - (numer / denom)
    }
    ts.foldMap(_ ** 2)
  }

  def leon[A: Ring](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = 100 * ((x2 - (x1 ** 2)) ** 2)
    val t2 = (1 - x1) ** 2

    t1 + t2
  }

  def levy3[F[_]: Foldable, A: Field : Trig](x: Sized2And[F, A]) = {
    def y(xi: A): A = 1 + (xi - 1) / 4.0

    val t1 = sin(pi * y(x.a)) ** 2
    val t2 = (x.a :: x.b :: x.rest.toList).sliding(2).toList.map {
      case List(xi, xi1) =>
        ((y(xi) - 1) ** 2) * (1 + 10 * ((pi * y(xi1) ** 2)))
    }.qsum
    val t3 = (y(x.rest.toList.lastOption.getOrElse(x.b)) - 1) ** 2

    t1 + t2 + t3
  }

  def levy5[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (for (i <- 1 to 5) yield i * cos((i - 1) * x1 + i)).qsum
    val t2 = (for (j <- 1 to 5) yield j * cos((j + 1) * x2 + j)).qsum
    val t3 = (x1 + 1.42513) ** 2
    val t4 = (x2 + 0.80032) ** 2
    t1 * t2 + t3 + t4
  }

  def levy13[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = ((x1 - 1) ** 2) * ((sin(3 * pi * x2) ** 2) + 1)
    val t2 = ((x2 - 1) ** 2) * ((sin(2 * pi * x2) ** 2) + 1)
    val t3 = sin(3 * pi * x1) ** 2
    t1 + t2 + t3
  }

  def levyMontalvo2[F[_]: Foldable, A: Field : Trig](x: Sized2And[F, A]) = {
    val t1 = sin(3 * pi * x.a) ** 2

    def t(a: Seq[A]) = a match {
      case Seq(xi, xi1) =>
        ((xi - 1) ** 2) * ((sin(3 * pi * xi1) ** 2) + 1 )
    }

    val t2 = (x.a :: x.b :: x.rest.toList).sliding(2).toList.map(t).qsum
    val last = x.rest.toList.lastOption.getOrElse(x.b)
    val t3 = ((last - 1) ** 2) * ((sin(2 * pi * last) ** 2) + 1)

    0.1 * (t1 + t2 + t3)
  }

  def matyas[A: Field : IsReal](x: Sized2[A]) = {
    val (x1, x2) = x
    0.26 * (x1 ** 2 + x2 ** 2) - 0.48 * x1 * x2
  }

  def maximum[F[_]: Foldable1, A: scalaz.Order](x: F[A]) =
    x.maximum1

  def mcCormick[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = sin(x1 + x2) + ((x1 - x2) ** 2)
    val t2 = -1.5 * x1 + 2.5 * x2 + 1
    t1 + t2
  }

  def michalewicz[F[_]: Foldable1, A: Field : IsReal : NRoot : Trig](m: Double = 10.0)(x: F[A]) =
    -x.toList.zipWithIndex.map { case (xi, i) =>
      sin(xi) * (sin(((i + 1) * (xi ** 2)) / pi) ** (2 * m))
    }.qsum

  def mieleCantrell[A: Field : Trig](x: Sized4[A]) = {
    val (x1, x2, x3, x4) = x
    val t1 = (exp(-x1) - x2) ** 4
    val t2 = 100 * ((x2 - x3) ** 6)
    val t3 = tan(x3 - x4) ** 4
    val t4 = x1 ** 8
    t1 + t2 + t3 + t4
  }

  def minimum[F[_]: Foldable1, A: scalaz.Order](x: F[A]) =
    x.minimum1

  def mishra1[F[_]: Foldable1, A: Ring : NRoot](x: F[A]) = {
    val sum = x.toList.init.qsum
    val n = x.length
    (1 + n - sum).fpow(n - sum)
  }

  def mishra2[F[_]: Foldable, A: Field : NRoot](x: Sized2And[F, A]) = {
    def t(a: Seq[A]) = a match {
      case Seq(xi, xi1) => 0.5 * (xi + xi1)
    }

    val sum = (x.a :: x.b :: x.rest.toList).sliding(2).toList.map(t).qsum
    val n = x.rest.length + 2

    (1 + n - sum).fpow(n - sum)
  }

  def mishra5[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = sin((cos(x1) + cos(x2)) ** 2) ** 2
    val t2 = cos((sin(x1) + sin(x2)) ** 2) ** 2
    val t3 = 0.01 * (x1 + x2)
    ((t1 + t2 + x1) ** 2) + t3
  }

  def mishra8[A: Field : Signed](x: Sized2[A]) = {
    val (x1, x2) = x
    val coX1 = List(1, -20, 180, -960, 3360, -8064,
      13340, -15360, 11520, -5120, 2624)
    val coX2 = List(1, 12, 54, 108, 81)

    val t1 = abs(coX1.zipWithIndex.map {
      case (ci, i) => ci * (x1 ** (coX1.length - 1 - i))
    }.qsum)

    val t2 = abs(coX2.zipWithIndex.map {
      case (ci, i) => ci * (x2 ** (coX2.length - 1 - i))
    }.qsum)

    0.001 * ((t1 * t2) ** 2)
  }

  def mishra11[F[_]: Foldable1, A: NRoot : Signed : Monoid](x: F[A])(implicit A: Field[A]) = {
    val n = x.length
    val t1 = (1.0 / n) * x.foldMap(abs(_))
    val t2 = x.foldLeft(A.one)(_ * abs(_)) ** (1.0 / n)
    (t1 - t2) ** 2
  }

  def multiModal[F[_]: Foldable1, A: Signed : Monoid](x: F[A])(implicit A: Field[A]) =
    x.foldLeft(A.one)(_ * abs(_)) * x.foldMap(abs(_))

  def parsopoulus[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    (cos(x1) ** 2) + (sin(x2) ** 2)
  }

  def pathological[F[_]: Foldable, A: Field : NRoot : Trig : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(xi, xi1) =>
        val numer = sin(sqrt(100.0 * (xi ** 2) + (xi1 ** 2))) ** 2 - 0.5
        val denom = 1.0 + 0.001 * (((xi ** 2) - 2.0 * xi * xi1 + (xi1 ** 2)) ** 2)
        0.5 + numer / denom
    }

  def penalty1[F[_]: Foldable, A: Field : Order : Trig : Monoid](x: Sized2And[F, A]) = {
    def u(xi: A, a: Int, k: Int, m: Int) =
      if (xi > a) k * ((xi - a) ** m)
      else if (xi < -a) k * ((-xi - a) ** m)
      else 0.0 * xi

    def yi(xi: A) = 1 + ((xi + 1.0) / 4.0)

    val xs = x.a :: x.b :: x.rest.toList
    val term1 = 10 * (sin(pi * yi(xs.head)) ** 2)
    val term2 = xs.sliding(2).toList.foldMap {
      case Seq(xi, xi1) =>
        val t1 = (yi(xi) - 1.0) ** 2
        val t2 = 1.0 + 10.0 * (sin(pi * yi(xi1)) ** 2)
        t1 * t2
    }
    val term3 = (yi(xs.last) - 1.0) ** 2
    val term4 = xs.foldMap(xi => u(xi, 10, 100, 4))

    (pi / 30.0) * (term1 + term2 + term3) + term4
  }

  def penalty2[F[_]: Foldable, A: Field : Order : Trig : Monoid](x: Sized2And[F, A]) = {
    def u(xi: A, a: Int, k: Int, m: Int) =
      if (xi > a) k * ((xi - a) ** m)
      else if (xi < -a) k * ((-xi - a) ** m)
      else 0.0 * xi

    val xs = (x.a :: x.b :: x.rest.toList)
    val term1 = sin(3.0 * pi * xs.head) ** 2
    val term2 = xs.sliding(2).toList.foldMap {
      case Seq(xi, xi1) =>
        val t1 = (xi - 1.0) ** 2
        val t2 = 1.0 + (sin(3.0 * pi * xi1) ** 2)
        t1 * t2
    }

    val term3 = ((xs.last - 1.0) ** 2) * (1.0 + sin(2.0 * pi * xs.last) ** 2)
    val term4 = xs.foldMap(xi => u(xi, 5, 100, 4))

    0.1 * (term1 + term2 + term3) + term4
  }

  def penHolder[A: Field : NRoot : Signed : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = abs(1 - (sqrt((x1 ** 2) + (x2 ** 2)) / pi))
    val t2 = cos(x1) * cos(x2)
    val expon = abs(exp(t1) * t2) ** -1
    -exp(-expon)
  }

  def periodic[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) = {
    val t1 = x.foldMap(sin(_) ** 2)
    val t2 = 0.1 * exp(-x.foldMap(_ ** 2))
    1 + t1 - t2
  }

  def powell[A: Field](x: Sized4[A]) = {
    val (x1, x2, x3, x4) = x
    val t1 = (x3 + 10 * x1) ** 2
    val t2 = 5.0 * ((x2 - x4) ** 2)
    val t3 = (x1 - 2 * x2) ** 4
    val t4 = 10.0 * ((x3 - x4) ** 4)
    t1 + t2 + t3 + t4
  }

  def powellSum[F[_]: Foldable1, A: Ring : Signed : Monoid](x: F[A]) =
    x.toList.zipWithIndex.foldMap {
      case (xi, i) => abs(xi) ** (i + 1)
    }

  def powerSum[A: Ring : Monoid](x: Sized4[A]) = {
    val b = List(8, 18, 44, 114)
    val (x1, x2, x3, x4) = x
    val xs = List(x1, x2, x3, x4)

    b.zipWithIndex.foldMap {
      case (bk, i) =>
        val k = i + 1
        val t = xs.foldMap(xi => xi ** k)
        (t - bk) ** 2
    }
  }

  def price1[F[_]: Foldable1, A: Ring : Signed : Monoid](x: F[A]) =
    x.foldMap(xi => (abs(xi) - 5) ** 2)

  def price2[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) =
    1.0 + x.foldMap(xi => sin(xi) ** 2) - 0.1 * exp(-x.foldMap(_ ** 2))

  def qing[F[_]: Foldable1, A: Field : Monoid](x: F[A]) =
    x.toList.zipWithIndex.foldMap {
      case (xi, i) => ((xi ** 2) - (i + 1.0)) ** 2
    }

  def quadratic[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = -3803.84 - 138.08 * x1
    val t2 = -232.92 * x2 + 128.08 * (x1 ** 2)
    val t3 = 203.64 * (x2 ** 2) + 182.25 * x1 * x2
    t1 + t2 + t3
  }

  def quadric[F[_]: Foldable1, A: Ring : Monoid](x: F[A]) =
    (1 to x.length).toList.foldMap { i =>
      (x.toList take i).foldMap(xi => xi) ** 2
    }

  def quintic[F[_]: Foldable1, A: Field : Signed : Monoid](x: F[A]) =
    abs(x.foldMap { xi =>
      (xi ** 5) - 3 * (xi ** 4) + 4 * (xi ** 3) + 2 * (xi ** 2) - 10.0 * xi - 4
    })

  def rastrigin[F[_]: Foldable1, A: Field : IsReal : Trig : Monoid](x: F[A]) =
    10 * x.length + x.foldMap(xi => xi ** 2 - 10 * cos(2 * pi * xi))

  def rosenbrock[F[_]: Foldable, A: Ring : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(xi, xi1) => 100 * ((xi1 - (xi ** 2)) ** 2) + ((xi - 1) ** 2)
    }

  def rotatedEllipse1[F[_]: Foldable, A: Field : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(x1, x2) =>
        (7 * (x1 ** 2)) - (6 * sqrt(3.0) * x1 * x2) + (13 * (x2 ** 2))
    }

  def rotatedEllipse2[F[_]: Foldable, A: Ring : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(x1, x2) => (x1 ** 2) - (x1 * x2) + (x2 ** 2)
    }

  def salomon[F[_]: Foldable1,  A: Field : NRoot : Trig : Monoid](x: F[A]) = {
    val ss = sqrt(spherical(x))
    -cos(2 * pi * ss) + (0.1 * ss) + 1
  }

  def schaffer1[F[_]: Foldable, A: Field : Trig : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(xi, xi1) =>
        val t1 = (xi ** 2) + (xi1 ** 2)
        val t2 = (xi ** 2) + (xi1 ** 2)
        val t3 = (sin((t1) ** 2) ** 2) - 0.5
        val t4 = (1 + 0.001 * t2) ** 2
        0.5 + (t3 / t4)
    }

  def schaffer2[F[_]: Foldable, A: Field : Trig : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(xi, xi1) =>
        val t1 = (xi ** 2) - (xi1 ** 2)
        val t2 = (xi ** 2) + (xi1 ** 2)
        val t3 = (sin((t1) ** 2) ** 2) - 0.5
        val t4 = (1 + 0.001 * t2) ** 2
        0.5 + (t3 / t4)
    }

  def schaffer3[F[_]: Foldable, A: Field : Signed : Trig : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(xi, xi1) =>
        val t1 = cos(abs((xi ** 2) - (xi1 ** 2)))
        val t2 = (xi ** 2) + (xi1 ** 2)
        val t3 = (sin((t1) ** 2) ** 2) - 0.5
        val t4 = (1 + 0.001 * t2) ** 2
        0.5 + (t3 / t4)
    }

  def schaffer4[F[_]: Foldable, A: Field : Trig : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(xi, xi1) =>
        val t1 = sin((xi ** 2) - (xi1 ** 2))
        val t2 = (xi ** 2) + (xi1 ** 2)
        val t3 = (cos((t1) ** 2) ** 2) - 0.5
        val t4 = (1 + 0.001 * t2) ** 2
        0.5 + (t3 / t4)
    }

  def schwefel1[F[_]: Foldable1, A: Field : NRoot : Monoid](x: F[A]) =
    x.foldMap(_ ** 2) ** sqrt(pi)

  def schwefel12[F[_]: Foldable1, A: Ring : Monoid](x: F[A]) =
    x.toList.zipWithIndex.foldMap {
      case (xi, i) => x.toList.take(i + 1).foldMap(xi => xi) ** 2
    }

  def schwefel221[F[_]: Foldable, A: Order : Signed](x: Sized1And[F, A]) =
    x.foldLeft(abs(x.head)) { (xi, xi1) => spire.math.max(abs(xi), abs(xi1)) }

  def schwefel222[F[_]: Foldable1 : Functor, A: Signed : Monoid](x: F[A])(implicit A: Ring[A]) =
    x.foldMap(abs(_)) + x.map(abs(_)).foldLeft(A.one)(_ * _)

  def schwefel223[F[_]: Foldable1, A: Ring : Monoid](x: F[A]) =
    x.foldMap(_ ** 10)

  def schwefel225[F[_]: Foldable, A: Field : Monoid](x: Sized1And[F, A]) =
    x.foldMap(xi => ((xi - 1.0) ** 2) + ((x.head - (xi ** 2)) ** 2))

  def schwefel226[F[_]: Foldable1, A: Field : NRoot : Signed : Trig : Monoid](x: F[A]) =
    418.9829 * x.length - x.foldMap(xi => xi * sin(sqrt(abs(xi))))

  def schwefel236[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    -x1 * x2 * (72.0 - 2.0 * x1 - 2.0 * x2)
  }

  def schwefel24[F[_]: Foldable, A: Field : Monoid](x: Sized1And[F, A]) =
    x.foldMap(xi => ((x.head - 1) ** 2) + ((x.head - xi) ** 2))

  def schwefel26[A: Order : Ring : Signed](x: Sized2[A]) = {
    val (x1, x2) = x
    spire.math.max(abs(x1 + 2 * x2 - 7), abs(2 * x1 + x2 - 5))
  }

  def shubert[F[_]: Foldable, A: Field : Trig : Monoid](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (1 to 5).toList.foldMap(j => j * cos((j + 1) * x1 + j))
    val t2 = (1 to 5).toList.foldMap(j => j * cos((j + 1) * x2 + j))
    t1 * t2
  }

  def sixHumpCamelback[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    val tX1 = 4 * (x1 ** 2) - 2.1 * (x1 ** 4) + (1.0 / 3.0) * (x1 ** 6)
    val tX2 = x1 * x2 - 4 * (x2 ** 2) + 4 * (x2 ** 4)
    tX1 + tX2
  }

  def spherical[F[_]: Foldable1, A: Ring : Monoid](x: F[A]) =
    x.foldMap(_ ** 2)

  def step1[F[_]: Foldable1, A: IsReal : Ring : Signed : Monoid](x: F[A]) =
    x.foldMap(xi => floor(abs(xi)))

  def step2[F[_]: Foldable1, A: Field : IsReal : Monoid](x: F[A]) =
    x.foldMap(xi => (floor(xi) + 0.5) ** 2)

  def step3[F[_]: Foldable1, A: IsReal : Ring : Monoid](x: F[A]) =
    x.foldMap(xi => floor(xi ** 2))

  def stretchedVSineWave[F[_]: Foldable, A: Field : NRoot : Trig : Monoid](x: Sized2And[F, A]) =
    (x.a :: x.b :: x.rest.toList).sliding(2).toList.foldMap {
      case Seq(xi, xi1) =>
        val t1 = ((xi1 ** 2) + (xi ** 2)) ** 0.25
        val t2 = sin(50.0 * (((xi1 ** 2) + (xi ** 2) ** 0.1))) ** 2 + 0.1
        t1 * t2
    }

  def styblinksiTang[F[_]: Foldable1, A: Field : Monoid](x: F[A]) =
    0.5 * x.foldMap(xi => (xi ** 4) - 16 * (xi ** 2) + 5 * xi)

  def sumSquares[F[_]: Foldable1, A: Ring : Monoid](x: F[A]) =
    x.toList.zipWithIndex.foldMap { case (xi, i) => (i + 1) * (xi ** 2) }

  def sumDifferentPowers[F[_]: Foldable1, A: Ring : Signed : Monoid](x: F[A]) =
    x.toList.zipWithIndex.foldMap { case (xi, i) => abs(xi) ** (i + 2) }

  def threeHumpCamelback[A: Field : IsReal](x: Sized2[A]) = {
    val (x1, x2) = x
    2 * (x1 ** 2) - 1.05 * (x1 ** 4) + ((x1 ** 6) / 6) + x1 * x2 + x2 ** 2
  }

  def trecanni[A: Ring](x: Sized2[A]) = {
    val (x1, x2) = x
    x1 ** 4 + 4 * (x1 ** 3) + 4 * (x1 ** 2) + (x2 ** 2)
  }

  def trigonometric1[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) =
    x.toList.zipWithIndex.foldMap {
      case (xi, i) =>
        (x.length - x.foldMap(cos(_)) + i * (1.0 - cos(xi) - sin(xi))) ** 2
    }

  def trigonometric2[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) =
    1.0 + x.foldMap { xi =>
      val co = (xi - 0.9) ** 2
      val t1 = 8.0 * (sin(7.0 * co) ** 2)
      val t2 = 6.0 * (sin(14.0 * co) ** 2)
      t1 + t2 + co
    }

  def ursem1[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    -sin(2.0 * x1 - 0.5 * pi) - 3.0 * cos(x2) - 0.5 * x1
  }

  def ursem3[A: Field : Trig : Signed](x: Sized2[A]) = {
    val (x1, x2) = x
    val co1 = -sin(2.2 * pi * x1 + 0.5 * pi)
    val co2 = -sin(2.2 * pi * x2 + 0.5 * pi)
    val t1 = co1 *  ((2.0 - abs(x1)) / (2.0)) * ((3.0 - abs(x1)) / (2.0))
    val t2 = co2 *  ((2.0 - abs(x2)) / (2.0)) * ((3.0 - abs(x2)) / (2.0))
    t1 + t2
  }

  def ursem4[A: Field : NRoot : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = -3.0 * sin(0.5 * pi * x1 + 0.5 * pi)
    val t2 = (2.0 - sqrt((x1 ** 2) + (x2 ** 2))) / 4.0
    t1 * t2
  }

  def ursemWaves[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = -0.9 * (x1 ** 2)
    val t2 = ((x2 ** 2) - 4.5 * (x2 ** 2)) * x1 * x2
    val t3 = 4.7 * cos(3.0 * x1 - (x2 ** 2) * (2.0 + x1))
    val t4 = sin(2.5 * pi * x1)
    t1 + t2 + t3 * t4
  }

  def venterSobiezcczanskiSobieski[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (x1 ** 2) - 100.0 * (cos(x1) ** 2)
    val t2 = -100.0 * cos((x1 ** 2) / 30.0) + (x2 ** 2)
    val t3 = -100.0 * (cos(x2) ** 2) - 100.0 * cos((x2 ** 2) / 30.0)
    t1 + t2 + t3
  }

  def vincent[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) =
    -x.foldMap(xi => sin(10.0 * log(xi)))

  def wayburnSeader1[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = ((x1 ** 6) + (x2 ** 4) - 17.0) ** 2
    val t2 = (2.0 * x1 + x2 - 4) ** 2
    t1 + t2
  }

  def wayburnSeader2[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = (1.613 - 4.0 * ((x1 - 0.3125) ** 2) - 4.0 * ((x2 - 1.625) ** 2)) ** 2
    val t2 = (x2 - 1.0) ** 2
    t1 + t2
  }

  def wayburnSeader3[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    val t1 = 2.0 * ((x1 ** 3) / 3.0) - 8.0 * (x1 ** 2) + 33.0 * x1 - x1 * x2 + 5
    val t2 = (((x1 - 4.0) ** 2) + ((x2 - 5.0) ** 2) - 4.0) ** 2
    t1 + t2
  }

  def weierstrass[F[_]: Foldable1, A: Field : Trig : Monoid](x: F[A]) = {
    val a = 0.5
    val b = 3.0
    val kmax = 20
    val constant = (0 to kmax).toList.map { k =>
      val t1 = a ** k.toDouble
      val t2 = cos(2 * pi * (b ** k.toDouble) * 0.5)
      t1 * t2
    }.sum

    val factor1 = x.foldMap { xi =>
      (0 to kmax).toList.foldMap { k =>
        val t1 = a ** k.toDouble
        val t2 = cos(2 * pi * (b ** k.toDouble) * (xi + 0.5))
        t1 * t2
      }
    }

    factor1 - x.length * constant
  }

  def wolfe[A: Field : NRoot](x: Sized3[A]) = {
    val (x1, x2, x3) = x
    (4.0 / 3.0) * (((x1 ** 2) + (x2 ** 2)) ** 0.75) + x3
  }

  def wood[A: Field](x: Sized4[A]) = {
    val (x1, x2, x3, x4) = x
    val t1 = 100 * (((x1 ** 2) - x2) ** 2)
    val t2 = (x1 - 1) ** 2 + (x3 - 1) ** 2
    val t3 = 90 * ((x3 ** 2 - x4) ** 2)
    val t4 = 10.1 * ((x2 - 1) ** 2)
    val t5 = (x4 - 1) ** 2 + 19.8 * (x2 - 1) * (x4 - 1)
    t1 + t2 + t3 + t4 + t4
  }

  def xinsheYang[F[_]: Foldable1, A: Field : Signed : Trig : Monoid](x: F[A]) = {
    val t1 = x.foldMap(abs(_))
    val t2 = exp(-x.foldMap(xi => sin(xi ** 2)))
    t1 * t2
  }

  def zakharov[F[_]: Foldable1, A: Field : IsReal : Monoid](x: F[A]) = {
    val t = x.toList.zipWithIndex.foldMap { case (xi, i) => 0.5 * i * xi }
    spherical(x) + (t ** 2) + (t ** 4)
  }

  def zettle[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    (x1 ** 2 + x2 ** 2 - 2 * x1) ** 2 + x1 / 4.0
  }

  def zirilli1[A: Field](x: Sized2[A]) = {
    val (x1, x2) = x
    0.25 * (x1 ** 4) - 0.5 * (x1 ** 2) + 0.1 * x1 + 0.5 * (x2 ** 2)
  }

  def zirilli2[A: Field : Trig](x: Sized2[A]) = {
    val (x1, x2) = x
    0.5 * (x1 ** 2) + 0.5 * (1.0 - cos(2.0 * x1)) + (x2 ** 2)
  }

}
