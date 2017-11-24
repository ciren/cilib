package cilib

import scalaz._
import Scalaz._

import spire.implicits._
import spire.math.{sqrt,Interval}

import org.scalacheck._
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.Prop._

import cilib.algebra._
import cilib.syntax.dotprod._

object PositionTests extends Properties("Position") {

  type Pos = Position[Int]

  val interval = Interval(-10.0, 10.0)
  def boundary(dim: Int) = interval ^ dim

  val dimGen = Gen.posNum[Int] suchThat (_ >= 1)

  def nelGen(dim: Int): Gen[NonEmptyList[Int]] = for {
    head <- Gen.choose(-10, 10)
    tail <- Gen.containerOfN[List,Int](dim - 1, Gen.choose(-10, 10))
  } yield NonEmptyList.nel(head, IList.fromFoldable(tail))

  def positionGen(dim: Int) = nelGen(dim).map(Position(_, boundary(dim)))

  val positionsGen = for {
    n      <- Gen.posNum[Int] suchThat (_ >= 1)
    dim    <- dimGen
    bounds  = boundary(dim)
    head   <- nelGen(dim)
    tail   <- Gen.containerOfN[List,NonEmptyList[Int]](n -1, nelGen(dim))
  } yield NonEmptyList.nel(Position(head, bounds), IList.fromFoldable(tail.map(Position(_, bounds))))

  val positionTuple = for {
    dim    <- dimGen
    bounds  = boundary(dim)
    a      <- nelGen(dim)
    b      <- nelGen(dim)
    c      <- nelGen(dim)
  } yield (Position(a, bounds), Position(b, bounds), Position(c, bounds))

  val one   = Position(NonEmptyList(1.0, 1.0, 1.0), boundary(3))
  val two   = Position(NonEmptyList(2.0, 2.0, 2.0), boundary(3))
  val zero  = Position(NonEmptyList(0.0, 0.0, 0.0), boundary(3))

  implicit val arbPosition       = Arbitrary { dimGen.flatMap(dim => positionGen(dim)) }
  implicit val arbPositions      = Arbitrary { positionsGen }
  implicit val arbPositionTuple  = Arbitrary { positionTuple }
  implicit val arbPositionDouble = Arbitrary { Gen.oneOf(zero, one, two) }

  property("addition") = forAll { (ps: (Pos,Pos,Pos)) =>
    val (a, b, c) = ps
    a + b         === b + a       &&
    (a + b) + c   === a + (b + c) &&
    a + a.zeroed  === a           &&
    a + (-a)      === a.zeroed
  }

  property("subtraction") = forAll { (a: Pos) =>
    a - a.zeroed === a        &&
    a - a        === a.zeroed &&
    a - (-a)     === a + a
  }

  property("scalar multiplication") = forAll { (a: Pos, b: Pos, n: Int, m: Int) =>
    a.boundary    === (n *: a).boundary   &&
    1 *: a        === a                   &&
    2 *: a        === a + a               &&
    (2.0 *: zero) === zero                &&
    0 *: a        === a.zeroed            &&
    -1 *: a       === -a                  &&
    (n + m) *: a  === (n *: a) + (m *: a) &&
    n *: (a + b)  === (n *: a) + (n *: b)
  }

  property("negation") = forAll { (a: Pos) =>
    (a.foldLeft1(_+_) != 0) ==>
      (-a =/= a)                    &&
      -a          === a.map(_ * -1) &&
      a + (-a)    === a.zeroed
  }

  property("is zero") = forAll { (a: Pos) =>
    a.zeroed.foldLeft1(_+_) === 0     &&
    zero.foldLeft1(_+_).toInt === 0 &&
    one.foldLeft1(_+_).toInt =/= 0
  }

  property("dot product") = forAll { (ps: (Pos,Pos,Pos)) =>
    val (a, b, c) = ps
    a ∙ b               === b ∙ a                 &&
    a ∙ (b + c)         === (a ∙ b) + (a ∙ c)     &&
    a ∙ ((2 *: b) + c)  === 2 * (a ∙ b) + (a ∙ c) &&
    (2 *: a) ∙ (3 *: b) === 2 * 3 * (a ∙ b)
  }

  property("magnitude") = forAll { (a: Pos) =>
    a.magnitude    >= 0          &&
    zero.magnitude === 0.0       &&
    one.magnitude  === sqrt(3.0)
  }

  property("normalize") = forAll { (a: Position[Double]) =>
    a.foldLeft1(_+_) =/= 0.0 ==>
      (a.normalize.magnitude === 1.0) &&
      a.normalize.forall(_ <= 1.0) &&
      zero.normalize.magnitude === 0.0
  }

  property("mean") = {
    val ps   = NonEmptyList(zero, one, two)
    val mean = Algebra.meanVector(ps)
    mean     === one                         &&
    ps.all(_.boundary   === mean.boundary)   &&
    ps.all(_.pos.length === mean.pos.length)
  }

  property("orthonormalize") = {
    val ps = NonEmptyList(zero, one, two)
    val orth = Algebra.orthonormalize(ps)
    orth.all(_.boundary   === one.boundary) &&
    orth.all(_.pos.length === one.pos.length)
  }

}
