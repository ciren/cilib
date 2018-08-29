package cilib

import scalaz._
import Scalaz._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.{Gen, _}
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary._
import eu.timepit.refined._

object GenArchive {

  def unboundedArchive: Gen[Archive[Double]] = for {
    u <- Archive.unbounded[Double]
  } yield u

  def boundedArchive: Gen[Archive[Double]] = for {
    x <- Gen.posNum[Int]
    b <- refineV[Positive](x) match {
      case Left(_) => Gen.fail
      case Right(value) => Gen.const(value)
    }
  } yield Archive.bounded(b)

  def unboundedNonEmptyArchive: Gen[Archive[Double]] = for {
    n <- Gen.chooseNum(1, 100)
    list <- Gen.listOfN(n, arbitrary[Double])
  } yield list match {
    case Nil => sys.error("this is impossible, we generate a number between 1 and 100")
    case x :: xs => Archive.unboundedNonEmpty(NonEmptyList.nel(x, x::xs.toIList))
  }

  def boundedNonEmptyArchive: Gen[Archive[Double]] = for {
  n <- Gen.chooseNum(1, 100)
  list <- Gen.listOfN(n, arbitrary[Double])
  x <- Gen.posNum[Int]
  b <- refineV[Positive](x) match {
      case Left(_) => Gen.fail
      case Right(value) => Gen.const(value)
    }
  } yield list match {
    case Nil => sys.error("this is impossible, we generate a number between 1 and 100")
    case x :: xs => {
      Archive.boundedNonEmpty(NonEmptyList.nel(x, (x::xs).toIList), b)
    }
  }
}

object ArchiveTest extends Properties("Archive") {
  property("Archive Insert") =
    forAll { archive: Archive[Double] =>
      archive.bound match {
        case Unbounded() => {
          val x = archive.insert(7.7)
          (x.size == archive.size + 1) && x.contains(7.7)
        }
        case Bounded(limit) => {
          if(archive.size < limit.toString().toInt){
            val x = archive.insert(7.7)
            (x.size == archive.size + 1) && x.contains(7.7)
          }
          else{
            val x = archive.insert(7.7)
            (x.size == archive.size) && !x.contains(7.7)
          }
        }
      }
    }
  property("Archive Delete") =
    forAll { archive: Archive[Double] =>
      archive.size match {
        case 0 =>  {
          val x = archive.delete(9)
          (x.size == 0) && (archive.size == 0)
        }
        case _ => {
          val rm = archive.head.get
          val x = archive.delete(rm)
          (x.size <= archive.size - 1) && !x.contains(rm)
        }
      }
    }
  property("Archive Values") =
    forAll { archive: Archive[Double] =>
      archive.size match {
        case 0 => archive.values == None
        case _ => archive.values.get.size == archive.toList.size
      }
    }
  property("Archive Replace") =
    forAll { archive: Archive[Double] =>
      archive.size match {
        case 0 => archive.replace(1,1000).size == archive.size
        case _ => archive.replace(archive.toList.head, 1000).values.get.size == archive.toList.size
      }
    }
  
  implicit def arbArchive: Arbitrary[Archive[Double]] =
    Arbitrary{
      Gen.frequency((1, GenArchive.unboundedArchive), (2, GenArchive.boundedArchive), (4, GenArchive.boundedNonEmptyArchive), (4, GenArchive.unboundedNonEmptyArchive))
    }
}
