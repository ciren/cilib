package cilib

import scalaz._
import scalaz.std.list._
import scalaz.std.option._
import scalaz.syntax.std.list._
import scalaz.syntax.traverse._
import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

import spire.implicits._

object PSOTests extends Properties("QPSO") {

  def genInterval: Gen[spire.math.Interval[Double]] =
    for {
      lower <- Gen.choose(-10.0, 0.0)
      upper <- Gen.choose(0.0, 10.0)
    } yield spire.math.Interval(lower, upper)

  implicit def arbInterval = Arbitrary { genInterval }
  implicit def arbPosition = Arbitrary { genPosition }

  def genPosition: Gen[Position[Double]] =
    for {
      bounds <- Gen.listOfN(2, arbitrary[spire.math.Interval[Double]])
      pos <- Gen.const(bounds.traverse(b => Gen.choose(b.lowerValue, b.upperValue).sample))
    } yield Position(pos.flatMap(_.toNel).getOrElse(sys.error("Error generating NonEmptyList[Double]")),
                     bounds.toNel.getOrElse(sys.error("Error generating NonEmptyList[Interval[Double]]")))

  property("Uniform sampled cloud <= R") = forAll {
    (center: Position[Double], x: Position[Double], seed: Long) => {
      val p = Entity(Mem(x, x.zeroed), x)
      val env = Environment(
        cmp = Comparison.dominance(Min),
        eval = Eval.unconstrained((x: NonEmptyList[Double]) => Feasible(0.0)))

      val (_, result) =
        cilib.pso.PSO.quantum(p.pos, RVar.pure(10.0), (a,b) => Dist.uniform(spire.math.Interval(a,b)))
          .run(env).run(RNG.init(seed))

      result match {
        case -\/(_) =>
          false

        case \/-(value) =>
          val vectorLength = math.sqrt(value.pos.foldLeft(0.0)((a,c) => a + c*c))

          vectorLength <= 10.0
      }
    }
  }
}
