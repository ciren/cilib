package cilib

import zio.prelude.{ Assertion => _, _ }
import zio.test._
import zio.{ Scope, ZEnvironment }

object PSOTests extends ZIOSpecDefault {

  val interval: Interval                           = Interval(-10.0, 10.0)
  def boundary(dim: Int): NonEmptyVector[Interval] = interval ^ dim

  def nelGen(dim: Int): Gen[Any, NonEmptyVector[Double]] =
    for {
      head <- Gen.double(-10, 10)
      tail <- Gen.listOfN(dim - 1)(Gen.double(-10, 10))
    } yield NonEmptyVector.fromIterable(head, tail)

  def positionGen: Gen[Any, Position[Double]] = nelGen(10).map(Position(_, boundary(10)))

  def spec: Spec[Environment with TestEnvironment with Scope, Any] = suite("QPSO")(
    test("Uniform sampled cloud <= R") {
      check(positionGen, Gen.long) { case (x, seed) =>
        val p    = Entity(Mem(x, x.zeroed), x)
        val cmp  = cilib.Comparison.dominance(Min)
        val eval = Eval.unconstrained((_: NonEmptyVector[Double]) => Feasible(0.0))

        val environment = ZEnvironment((cmp, eval))

        val (_, result) =
          cilib.pso.PSO
            .quantum(p.pos, RVar.pure(10.0), (a, b) => Dist.uniform(Interval(a, b)))
            .provideEnvironment(environment)
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
