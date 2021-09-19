package cilib

import spire.implicits._
import spire.math.Interval
import zio.prelude._
import zio.random.Random
import zio.test.{ Gen, _ }

object PSOTests extends DefaultRunnableSpec {

  val interval: Interval[Double]                           = Interval(-10.0, 10.0)
  def boundary(dim: Int): NonEmptyVector[Interval[Double]] = interval ^ dim

  def nelGen(dim: Int): Gen[Random, NonEmptyVector[Double]] =
    for {
      head <- Gen.double(-10, 10)
      tail <- Gen.listOfN(dim - 1)(Gen.double(-10, 10))
    } yield NonEmptyVector.fromIterable(head, tail)

  def positionGen: Gen[Random, Position[Double]] = nelGen(10).map(Position(_, boundary(10)))

  def spec: ZSpec[Environment, Failure] = suite("QPSO")(
    testM("Uniform sampled cloud <= R") {
      check(positionGen, Gen.anyLong) {
        case (x, seed) =>
          val p = Entity(Mem(x, x.zeroed), x)
          val cmp = Comparison.dominance(Min)
          val eval = Eval.unconstrained((_: NonEmptyVector[Double]) => Feasible(0.0))

          val (_, result) =
            cilib.pso.PSO
              .quantum(p.pos, RVar.pure(10.0), (a, b) => Dist.uniform(spire.math.Interval(a, b)))
              .provide((cmp, eval))
              .runAll(RNG.init(seed))

          result match {
            case Left(_) =>
              sys.error("shouldn't happen")

            case Right((_, value)) =>
              val vectorLength = math.sqrt(value.pos.foldLeft(0.0)((a, c) => a + c * c))

              assert(vectorLength)(Assertion.isLessThanEqualTo(10.0))
          }
      }
    }
  )

}
