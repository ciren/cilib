package cilib

import zio.prelude.{ Assertion => _, _ }
import zio.random.Random
import zio.test.{ Gen, _ }

object RVarTests extends DefaultRunnableSpec {

  val rng = RNG.fromTime

  val interval: Interval                           = Interval(-10.0, 10.0)
  def boundary(dim: Int): NonEmptyVector[Interval] = interval ^ dim

  def nelGen(dim: Int): Gen[Random, NonEmptyVector[Double]] =
    for {
      head <- Gen.double(-10, 10)
      tail <- Gen.listOfN(dim - 1)(Gen.double(-10, 10))
    } yield NonEmptyVector.fromIterable(head, tail)

  def positionGen: Gen[Random, Position[Double]] = nelGen(10).map(Position(_, boundary(10)))

  def spec: ZSpec[Environment, Failure] = suite("RVar")(
    testM("shuffle") {
      check(nelGen(10)) { case xs =>
        val shuffled = RVar.shuffle(xs).run(RNG.fromTime)._2

        assert(shuffled.length)(Assertion.equalTo(xs.length)) &&
        assert(shuffled.toChunk.sorted)(Assertion.equalTo(xs.toChunk.sorted))
      }
    },
    testM("sampling") {
      check(Gen.int(1, 10), Gen.int(1, 20)) { case (sampleSize, listSize) =>
        val elements = NonEmptyVector.fromIterableOption((1 to listSize).toList).get

        val selected: List[Int] =
          RVar.sample(positiveInt(sampleSize), elements).runResult(rng).getOrElse(List.empty)

        if (elements.length < sampleSize) assert(selected)(Assertion.isEmpty)
        else
          assert(selected.length)(Assertion.isLessThanEqualTo(sampleSize)) &&
          assert(selected.forall(s => elements.contains(s)))(Assertion.isTrue)
      }
    }
  )
}
