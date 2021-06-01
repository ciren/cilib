package cilib

import eu.timepit.refined._
import eu.timepit.refined.numeric.Positive
import spire.implicits._
import spire.math.Interval
import zio.prelude._
import zio.test._

object RVarTests extends DefaultRunnableSpec {

  val rng = RNG.fromTime

  val interval           = Interval(-10.0, 10.0)
  def boundary(dim: Int) = interval ^ dim

  def nelGen(dim: Int) =
    for {
      head <- Gen.double(-10, 10)
      tail <- Gen.listOfN(dim - 1)(Gen.double(-10, 10))
    } yield NonEmptyList.fromIterable(head, tail)

  def positionGen = nelGen(10).map(Position(_, boundary(10)))

  def spec: ZSpec[Environment, Failure] = suite("RVar")(
    testM("shuffle") {
      check(nelGen(10)) {
        case xs =>
          val shuffled = RVar.shuffle(xs).run(RNG.fromTime)._2

          assert(shuffled.length)(Assertion.equalTo(xs.length)) &&
          assert(shuffled.sorted)(Assertion.equalTo(xs.sorted))
      }
    },
    testM("sampling") {
      check(Gen.int(1, 10), Gen.int(1, 20)) {
        case (sampleSize, listSize) =>
          refineV[Positive](sampleSize) match {
            case Left(error) => sys.error(s"Cannot refine: $error")
            case Right(value) =>
              val elements = NonEmptyList.fromIterableOption((1 to listSize).toList).get

              val selected: List[Int] =
                RVar.sample(value, elements).runResult(rng).getOrElse(List.empty)

              if (elements.length < sampleSize) assert(selected)(Assertion.isEmpty)
              else
                assert(selected.length)(Assertion.isLessThanEqualTo(sampleSize)) &&
                assert(selected.forall(s => elements.contains(s)))(Assertion.isTrue)
          }
      }
    }
  )
}
