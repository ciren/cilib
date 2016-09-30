package cilib

import _root_.scala.Predef.{ any2stringadd => _, _ }
import scalaz._, Scalaz._
import spire.math._
import spire.algebra._
import spire.implicits._

object Problems {

  import Eval._

  def spherical =
    unconstrained(cilib.benchmarks.Benchmarks.spherical[NonEmptyList, Double])

/*
////
  case class PeakCone(domain: NonEmptyList[Interval[Double]], s: Double, height: Double, width: Double, location: Position[Double], shift: Position[Double]) {

    def apply(x: List[Double])(implicit M: Module[List[Double],Double]): Double = {
      val sum = (location.pos.list zip x).map(a => (a._1 - a._2) * (a._1 - a._2)).foldLeft(0.0)(_ + _)
      (height - width) * math.sqrt(sum)
//      (height - width) * math.sqrt((location.pos.list - x).foldMap1(a => a*a))

  * Some of the more common static benchmark problems
  import scalaz.Foldable

  def spherical[F[_]:Foldable:SolutionRep,A](implicit N: Numeric[A]) =
    new Unconstrained[F,A]((a: F[A]) => Valid(a.foldMap(x => N.toDouble(N.times(x, x)))))

  // Not sure where to put these yet....

  * G13 Problems. Runarrson

  // This needs to be something that is "sized"
  *val g1 = Problem.violations(
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
  )

  case class Peak(pos: List[Double], width: Double, height: Double, movementDirection: List[Double], shiftVector: List[Double])
  case class PeakState(peaks: List[Peak], interval: NonEmptyList[Interval[Double]],
    frequency: Int = 10,
    widthSeverity: Double = 0.01, heightSeverity: Double = 7.0,
    shiftSeverity: Double = 1.0, lambda: Double = 0.75,
    minHeight: Double = 30.0, maxHeight: Double = 70.0, minWidth: Double = 1.0, maxWidth: Double = 12.0)

  import scalaz._

  def initPeaks[F[_]:SolutionRep:Foldable](n: Int, interval: NonEmptyList[Interval[Double]],
    minHeight: Double, maxHeight: Double, minWidth: Double, maxWidth: Double
  ): RVar[(PeakState, Eval[F,Double])] = {
    val t = List.fill(interval.size)(1.0)
    val peaks = (1 to n).toList.traverse(_ => {
      val position = interval.list.traverse(x => Dist.uniform(x))
      val height = Dist.uniform(Interval(minHeight, maxHeight))
      val width = Dist.uniform(Interval(minWidth, maxWidth))

      (position |@| width |@| height) { Peak(_, _, _, t, t) }
    })

    peaks.map(p => (PeakState(p, interval, 10, 0.01, 7.0, 1.0, 0.75, minHeight, maxHeight, minWidth, maxWidth), movingPeaksEval(p)))
  }

  def modifyPeaks[F[_]:SolutionRep:Foldable]: StateT[RVar, PeakState, Eval[F,Double]] =
    StateT {
      ps => {
        val newPeaks = ps.peaks.traverse(peak => {
          val heightOffset: RVar[Double] = Dist.stdNormal.map(x => {
            val offset = x * ps.heightSeverity
            if (peak.height + offset > ps.maxHeight || peak.height - offset < ps.minHeight)
              peak.height - offset
            else
              peak.height + offset
          })
          val widthOffset  = Dist.stdNormal.map(x => {
            val offset = x * ps.widthSeverity
            if (peak.width + offset > ps.maxWidth || peak.width - offset < ps.minWidth)
              peak.width - offset
            else
              peak.width + offset
          })

          val shift = peak.pos + ((peak.shiftVector, peak.movementDirection).zipped map { _ * _ })
          val newDirection = (shift, peak.movementDirection, ps.interval.list).zipped.map { case (a,b,c) => if (a > c.upperValue || a < c.lowerValue) b * -1.0 else b }
          val newShift = (shift, peak.shiftVector, ps.interval.list).zipped.map { case (a,b,c) => if (a > c.upperValue || a < c.lowerValue) b * -1.0 else b }
          val newPos = peak.pos + newShift

          (widthOffset |@| heightOffset) { Peak(newPos, _, _, newDirection, newShift) }
        })

        newPeaks.map(np => (ps.copy(peaks = np), movingPeaksEval(np)))
      }
>>>>>>> series/2.0.x
    }

    def update(heightSeverity: Double, widthSeverity: Double): RVar[PeakCone] = {
      val newS = newShift(shift, s, 1.0, domain)
      val newLoc = newS.map(_ + location)
      val h = severityUpdate(height, heightSeverity)
      val w = severityUpdate(width, widthSeverity)

      (newLoc |@| h |@| w |@| newS) { (l, h, w, shift) => PeakCone(domain, s, h, w, l, shift) }
    }
  }

  def initPeak(domain: NonEmptyList[Interval[Double]], s: Double, minHeight: Double, maxHeight: Double, minWidth: Double, maxWidth: Double): RVar[PeakCone] = {
    val height = Dist.uniform(minHeight, maxHeight)
    val width  = Dist.uniform(minWidth, maxWidth)
    val loc    = Position.createPosition(domain)

    (height |@| width |@| loc) { (h, w, l) => PeakCone(domain, s, h, w, l, l.map(_ => 1.0)) }
  }

  def defaultPeak(domain: NonEmptyList[Interval[Double]], s: Double) =
    initPeak(domain, s, 7.0, 30.0, 1.0, 12.0)

  def severityUpdate(old: Double, severity: Double): RVar[Double] =
    Dist.stdNormal.map(s => old + severity*s)

  def newShift(old: Position[Double], s: Double, lambda: Double, domain: NonEmptyList[Interval[Double]])(implicit S: NormedVectorSpace[Position[Double],Double]): RVar[Position[Double]] = {
    val p_r = Position.createPosition(domain)
    p_r.map(p => {
      val d = old + p
      val t = ((1.0 - lambda) *: p) + (lambda *: old)
      (s / d.norm) *: t
    })
  }

  def peakEval(peaks: List[PeakCone]): Eval[Double] =
    Unconstrained[Double]((a: NonEmptyList[Double]) => {
      import scalaz.syntax.foldable._
      import scalaz.std.anyVal._
      val r = peaks.map(x => {
        //val h = (a - x.location.pos).foldLeft(0.0)((acc,y) => acc + y*y)
        val h = x(a.list)
        val w = 1 + (h * x.width)
        x.height / w
      })
      Valid(r.maximum.getOrElse(-1.0))
 })*/



  // Attempt to implment the moving peaks in such a way that it does not suck

  def initPeaks(n: Int, domain: NonEmptyList[spire.math.Interval[Double]], minWidth: Double = 1.0, maxWidth: Double = 12.0, minHeight: Double = 30.0, maxHeight: Double = 70.0): RVar[NonEmptyList[PeakCone]] = {
    import scalaz._//syntax.applicative._
    import Scalaz._
    // TODO: Change the parameters for the min and max height and width
    domain.traverseU(x => Dist.uniform(x)).flatMap(x => {
      val height = Dist.stdUniform.map(x => (maxHeight - minHeight) * x + minHeight)
      val width  = Dist.stdUniform.map(x => (maxWidth - minWidth) * x + minWidth)
      (height |@| width) { (h,w) => PeakCone(h, w, x) }
    }).replicateM(n).map(_.toNel.getOrElse(sys.error("List cannot be empty")))
    //.map(_.toNel.getOrElse(sys.error("")))
  }

  case class PeakCone(height: Double, width: Double, location: NonEmptyList[Double]) {
    def eval(x: NonEmptyList[Double]) = {
      // println("position: " + x)
      // println("location: " + location)

      val c = math.sqrt((x zip location).map(a => (a._1 - a._2) * (a._1 - a._2)).foldLeft(0.0)(_ + _))
//      println("c: " + c)
      height - width * c
    }
  }

  def peakEval(peaks: NonEmptyList[PeakCone]): Eval[Double] =
    Eval.unconstrained((a: NonEmptyList[Double]) => {
  //    println("Peaks:" + peaks)
      val x = peaks.map(_.eval(a)).list.toList.max // FIXME
      //println("x: " + x)
      val r = if (x == Double.NaN) 0.0 else x
      //println("r: " + r)
      r
    })

}
