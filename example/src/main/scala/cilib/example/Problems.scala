package cilib

import _root_.scala.Predef.{ any2stringadd => _, _ }
import scalaz.NonEmptyList
import scalaz.std.list._
import scalaz.syntax.traverse._
import scalaz.syntax.foldable._
import scalaz.syntax.foldable1._
import scalaz.syntax.apply._
import spire.math._
import spire.algebra._
import spire.implicits._

import scalaz.NonEmptyList

/**
  Examples of how to define "Problem" instances.

  There are two types of problems: dynamic and static. With a static
  problem, the problem space never changes, but in the dynamic environment,
  the problem may change at any time.

  There is a very strong link between the quantification of a problem solution
  and the type of fitness that is the result:
    - Valid(x) -> A valid fitness in the environment with `x` and the value
    - Penalty(x, y) -> A valid fitness, but the solution has had a penalty of `y` applied

  */
object Problems {
//  import scalaz.Foldable1

  def spherical[A](implicit N: Numeric[A]) =
    Unconstrained[A]((a: List[A]) => Valid(a.foldLeft(0.0)((a,c) => a * N.toDouble(c))))


/* ////
  case class PeakCone(domain: NonEmptyList[Interval[Double]], s: Double, height: Double, width: Double, location: Position[Double], shift: Position[Double]) {

    def apply(x: List[Double])(implicit M: Module[List[Double],Double]): Double = {
      val sum = (location.pos.list zip x).map(a => (a._1 - a._2) * (a._1 - a._2)).foldLeft(0.0)(_ + _)
      (height - width) * math.sqrt(sum)
//      (height - width) * math.sqrt((location.pos.list - x).foldMap1(a => a*a))
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

  def initPeaks(n: Int, domain: NonEmptyList[Interval[Double]], minWidth: Double = 1.0, maxWidth: Double = 12.0, minHeight: Double = 30.0, maxHeight: Double = 70.0): RVar[List[PeakCone]] = {
    import scalaz._//syntax.applicative._
    import Scalaz._
    // TODO: Change the parameters for the min and max height and width
    domain.traverseU(x => Dist.uniform(x.lower.value, x.upper.value)).flatMap(x => {
      val height = Dist.stdUniform.map(x => (maxHeight - minHeight) * x + minHeight)
      val width  = Dist.stdUniform.map(x => (maxWidth - minWidth) * x + minWidth)
      (height |@| width) { (h,w) => PeakCone(h, w, x) }
    }).replicateM(n)
    //.map(_.toNel.getOrElse(sys.error("")))
  }

  case class PeakCone(height: Double, width: Double, location: NonEmptyList[Double]) {
    def eval(x: List[Double]) = {
      val c = math.sqrt((x.zip(location.list)).map(a => (a._1 - a._2) * (a._1 - a._2)).foldLeft(0.0)(_ + _))
      println("c: " + c)
      height - width * c
    }
  }

  def peakEval(peaks: /*NonEmpty*/List[PeakCone]): Eval[Double] =
    Unconstrained((a: List[Double]) => {
      //      import scalaz.std.anyVal._
      println("Peaks:" + peaks)
      val x = peaks.map(_.eval(a))
      println("x: " + x)
      val r = Valid(if (x.max == Double.NaN) 0.0 else x.max)
      println("r: " + r)
      r
    })

}
