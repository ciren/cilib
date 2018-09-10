package cilib

import scalaz._
import Scalaz._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.{Gen, _}
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary._
import eu.timepit.refined._

object GenArchive {
  def unboundedArchive(insertPolicy: (Double, Double) => Boolean): Gen[Archive[Double]] = for {
    u <- Gen.posNum[Int] // ...
  } yield Archive.unbounded[Double](insertPolicy)

  def boundedArchive(insertPolicy: (Double, Double) => Boolean)(deletePolicy: (List[Double]) => Double): Gen[Archive[Double]] = for {
    x <- Gen.posNum[Int]
    b <- refineV[Positive](x) match {
      case Left(_) => Gen.fail
      case Right(value) => Gen.const(value)
    }
  } yield Archive.bounded(b, insertPolicy, deletePolicy)

  def unboundedNonEmptyArchive(insertPolicy: (Double, Double) => Boolean): Gen[Archive[Double]] = for {
    n <- Gen.chooseNum(1, 100)
    list <- Gen.listOfN(n, arbitrary[Double])
  } yield list match {
    case Nil => sys.error("this is impossible, we generate a number between 1 and 100")
    case x :: xs => Archive.unboundedNonEmpty(NonEmptyList.nel(x, xs.toIList), insertPolicy)
  }

  def boundedNonEmptyArchive(insertPolicy: (Double, Double) => Boolean)(deletePolicy: List[Double] => Double): Gen[Archive[Double]] = for {
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
      Archive.boundedNonEmpty(NonEmptyList.nel(x, xs.toIList), b, insertPolicy, deletePolicy)
    }
  }
}

object ArchiveTest extends Properties("Archive") {
  property("Archive Insert") =
    forAll { archive: Archive[Double] =>
      archive.bound match {
        case Unbounded() => {
          if (archive.insertCondition(1, 2) == true) {
            val x = archive.insert(7.7)
            (x.size == archive.size + 1)
          }
          else {
            if(archive.size == 0){
              val x = archive.insert(7.7)
              (x.size == archive.size + 1)
            }
            else {
              val x = archive.insert(7.7)
              (x.size == archive.size)
            }
          }
        }
        case Bounded(limit, _) => {
          if (archive.size < limit.toString().toInt) {
            if (archive.insertCondition(1, 2) == true) {
              val x = archive.insert(7.7)
              (x.size == archive.size + 1)
            }
            else {
              if(archive.size == 0){
                val x = archive.insert(7.7)
                (x.size == archive.size + 1)
              }
              else {
                val x = archive.insert(7.7)
                (x.size == archive.size)
              }
            }
          }
          else {
            val x = archive.insert(7.7)
            (x.size <= archive.size) && (x.size <= limit.toString().toInt) // (x.size == archive.size) && (x.size == limit.toString().toInt) fails when duplicate values are removed - due to filterNot in Archive insert function used.
          }
        }
      }
    }

  implicit def arbArchive: Arbitrary[Archive[Double]] =
    Arbitrary{
      Gen.frequency(
        (2, GenArchive.unboundedArchive((x: Double, y: Double) => true)),
        (2, GenArchive.boundedArchive((x: Double, y: Double) => true)((l: List[Double]) => l.head)),
        (4, GenArchive.unboundedNonEmptyArchive((x: Double, y: Double) => true)),
        (4, GenArchive.unboundedNonEmptyArchive((x: Double, y: Double) => false)),
        (4, GenArchive.boundedNonEmptyArchive((x: Double, y: Double) => true)((l: List[Double]) => l.head)),
        (4, GenArchive.boundedNonEmptyArchive((x: Double, y: Double) => false)((l: List[Double]) => l.head))
      )
    }
}
