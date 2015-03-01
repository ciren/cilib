package cilib

import _root_.scala.Predef.{ any2stringadd => _, _ }
import scalaz.syntax.traverse._
import scalaz.syntax.apply._
import scalaz.syntax.id._
import scalaz.std.list._
import spire.math._
import spire.implicits._

/**
  Examples of how to define "Problem" instances.

  There are two types of problems: dynamic and static. With a static
  problem, the problem space never changes, but in the dynamic environment,
  the problem may change at any time.

  There is a very strong link between the quantification of a problem solution
  and the type of fitness that is the result:
  * Valid(x) - A valid fitness in the environment with `x` and the value
  * Penalty(x, y) - A valid fitness, but the solution has had a penalty of `y` applied

  */
object Problems {

  /* Some of the more common static benchmark problems */
  import scalaz.Foldable

  def spherical[F[_]:Foldable:SolutionRep,A](implicit N: Numeric[A]) =
    new Eval[F,A] {
      def eval(a: F[A]) = (Valid(a.foldMap(x => N.toDouble(N.times(x, x)))), List.empty)
    }

  // Not sure where to put these yet....

  /* G13 Problems. Runarrson */

  // This needs to be something that is "sized"
  /*val g1 = Problem.violations(
    Problem.static((a: List[Double]) => {
      val x = a.take(4).sum * 5.0
      val y = a.take(4).map(x => x*x).sum * 5.0
      val z = a.drop(4).sum
      Valid(x - y - z)
    }),
    List(
      (a: List[Double]) => Violation.bool( 2*a(0) + 2*a(1) + a(9) + a(10) - 10 <= 0),
      (a: List[Double]) => Violation.bool( 2*a(0) + 2*a(2) + a(9) + a(11) - 10 <= 0),
      (a: List[Double]) => Violation.bool( 2*a(0) + 2*a(2) + a(10) + a(11) - 10 <= 0),
      (a: List[Double]) => Violation.bool(-8*a(0) + a(9) <= 0),
      (a: List[Double]) => Violation.bool(-8*a(1) + a(10) <= 0),
      (a: List[Double]) => Violation.bool(-8*a(2) + a(11) <= 0),
      (a: List[Double]) => Violation.bool(-2*a(3) - a(4) + a(9) <= 0),
      (a: List[Double]) => Violation.bool(-2*a(5) - a(6) + a(10) <= 0),
      (a: List[Double]) => Violation.bool(-2*a(7) - a(8) + a(11) <= 0)
    )
  )*/


  //  def g24(x1: ) = Problem.

  /*def movingPeaks(
    interval: List[Interval[Double]],
    frequency: Int = 10, peaks: Int = 5, widthSeverity: Double = 0.01, heightSeverity: Double = 7.0,
    shiftSeverity: Double = 1.0, lambda: Double = 0.75,
    minHeight: Double = 30.0, maxHeight: Double = 70.0, minWidth: Double = 1.0, maxWidth: Double = 12.0
  )(
    count: Int = 0,
    movementDirections: List[List[Double]] = List.fill(peaks)(List.fill(interval.size)(1.0)),
    shiftVectors: List[List[Double]] = List.fill(peaks)(List.fill(interval.size)(1.0))
  ): RVar[Problem[List,Double]] = {
    case class Peak(pos: List[Double], width: Double, height: Double)

    // Initialise the problem peak data
    val peak = (1 to peaks).toList.traverse(_ => {
      val position = interval.traverse(x => Dist.uniform(x.lower.value, x.upper.value))
      val height = Dist.uniform(minHeight, maxHeight)
      val width = Dist.uniform(minWidth, maxWidth)
      (position |@| width |@| height) { Peak(_, _, _) }
    })

    peak.map(p => new Problem[List,Double] {
      println("peaks: " + p.mkString("\n"))
      def eval(a: List[Double]) = {
        import scalaz.syntax.foldable._
        import scalaz.std.anyVal._
        import spire.implicits._
        val r = p.map(x => {
          val h = (a - x.pos).foldLeft(0.0)((acc,y) => acc + y*y)
          val w = 1 + (h * x.width)
          x.height / w
        })

        if (count == frequency) {
          import scalaz.syntax.applicative._
          val random: RVar[List[Double]] = Dist.uniform(-1.0, 1.0).replicateM(interval.size).map(x => shiftSeverity *: x.normalize)
          val newShift: RVar[List[List[Double]]] = shiftVectors.traverse(old => {
            val vector: RVar[List[Double]] = random.map((x: List[Double]) => {
              val a: List[Double] = (1.0 - lambda) *: x
              val b: List[Double] = lambda *: old
              a + b
            })
            vector.map(v => (1.0 / v.length.toDouble) *: (shiftSeverity *: v))
          })
    /*      p.map(np => {
            val heightOffset = Dist.heightSeverity
            val weightOffset
          })*/
        } else {
          (RVar.point(this), Valid(r.maximum.getOrElse(-1.0)), List.empty)
        }
      }
    })
   }*/

  case class Peak(pos: List[Double], width: Double, height: Double, movementDirection: List[Double], shiftVector: List[Double])
  case class PeakState(peaks: Int, interval: List[Interval[Double]],// movementDirection: List[List[Double]], shiftVectors: List[List[Double]],
    frequency: Int = 10,
    widthSeverity: Double = 0.01, heightSeverity: Double = 7.0,
    shiftSeverity: Double = 1.0, lambda: Double = 0.75,
    minHeight: Double = 30.0, maxHeight: Double = 70.0, minWidth: Double = 1.0, maxWidth: Double = 12.0)

  import scalaz._
  def initPeaks: State[PeakState, RVar[List[Peak]]] =
    State(s => {
      val t = List.fill(s.interval.size)(1.0)
      val peak = (1 to s.peaks).toList.traverse(_ => {
        val position = s.interval.traverse(x => Dist.uniform(x.lower.value, x.upper.value))
        val height = Dist.uniform(s.minHeight, s.maxHeight)
        val width = Dist.uniform(s.minWidth, s.maxWidth)
          (position |@| width |@| height) { Peak(_, _, _, t, t) }
      })
      (s, peak)
    })

  def modifyPeaks(l: List[Peak]): State[PeakState, RVar[List[Peak]]] =
    State(s => {
      (s, l.traverse(p => {
        val heightOffset = Dist.stdNormal.map(x => {
          val offset = x * s.heightSeverity
          if (p.height + offset > s.maxHeight || p.height - offset < s.minHeight)
            p.height - offset
          else
            p.height + offset
        })
        val widthOffset  = Dist.stdNormal.map(x => {
          val offset = x * s.widthSeverity
          if (p.width + offset > s.maxWidth || p.width - offset < s.minWidth)
            p.width - offset
          else
            p.width + offset
        })

        val shift = p.pos + ((p.shiftVector, p.movementDirection).zipped map { _ * _ })
        val newDirection = (shift, p.movementDirection, s.interval).zipped.map { case (a,b,c) => if (a > c.upper.value || a < c.lower.value) b * -1.0 else b }
        val newShift = (shift, p.shiftVector, s.interval).zipped.map { case (a,b,c) => if (a > c.upper.value || a < c.lower.value) b * -1.0 else b }
        val newPos = p.pos + newShift

        (widthOffset |@| heightOffset) { Peak(newPos, _, _, newDirection, newShift) }
      }))
    })

  def movingPeaks(peaks: RVar[List[Peak]]): RVar[Eval[List,Double]] = {
    peaks.map(p => //new Problem[List,Double] {
                   //      def eval(a: List[Double]) = {
      new Eval[List,Double] {
        def eval(a: List[Double]) = {
          import scalaz.syntax.foldable._
          import scalaz.std.anyVal._
          import spire.implicits._
          val r : List[Double] = p.map(x => {
            val h = (a - x.pos).foldLeft(0.0)((acc,y) => acc + y*y)
            val w = 1 + (h * x.width)
            x.height / w
          })

          (Valid(r.maximum.getOrElse(-1.0)), List.empty)
        }
      }
    )
  }

}
