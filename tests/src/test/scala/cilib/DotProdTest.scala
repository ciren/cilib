package cilib

import CIlibProperties._
import org.scalacheck._

import scalaz.{IList,NonEmptyList}
import scalaz.std.list._

import spire.implicits._
import spire.math.Interval

object DotProdTest extends Spec("DotProd") {

  val interval = Interval(-10.0, 10.0)
  def boundary(dim: Int) = interval ^ dim

  val dimGen = Gen.posNum[Int] suchThat (_ >= 1)
  def nelGen(dim: Int): Gen[NonEmptyList[Int]] = for {
    head <- Gen.choose(-10, 10)
    tail <- Gen.containerOfN[List,Int](dim - 1, Gen.choose(-10, 10))
  } yield NonEmptyList.nel(head, IList.fromFoldable(tail))

  def positionGen(dim: Int) = nelGen(dim).map(Position(_, boundary(dim)))

  // def laws[F[_]](implicit af: Arbitrary[F[Int]]) = new Properties("dot product") {
  //   property("commutativity") = dotprod.commutativity[F, Int]
  // }

  implicit val arbPosition = Arbitrary { dimGen.flatMap(dim => positionGen(dim)) }

  def laws = new Properties("dot_product") {
    property("commutativity") = dotprod.commutativity[Position, Int]
    property("distributive") = dotprod.distributive[Position, Int]
    property("bilinear") = dotprod.bilinear[Position, Int]
    property("scalarMultiplication") = dotprod.scalarMultiplication[Position,Int]
  }

  checkAll(laws)
}
