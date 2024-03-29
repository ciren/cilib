package cilib

import zio.prelude._

object Problems {

  // Attempt to implment the moving peaks in such a way that it does not suck

  def defaultPeaks(n: Int)(domain: NonEmptyVector[Interval]): RVar[NonEmptyVector[PeakCone]] =
    initPeaks(
      n,
      domain,
      minWidth = 1.0,
      maxWidth = 12.0,
      minHeight = 30.0,
      maxHeight = 70.0
    )

  def initPeaks(
    n: Int,
    domain: NonEmptyVector[Interval],
    minWidth: Double,
    maxWidth: Double,
    minHeight: Double,
    maxHeight: Double
  ): RVar[NonEmptyVector[PeakCone]] =
    // TODO: Change the parameters for the min and max height and width
    domain
      .forEach(x => Dist.uniform(x))
      .flatMap { x =>
        val height = Dist.stdUniform.map(x => (maxHeight - minHeight) * x + minHeight)
        val width  = Dist.stdUniform.map(x => (maxWidth - minWidth) * x + minWidth)
        height.zipWith(width)((h, w) =>
          PeakCone(h, w, x, x.map(_ => 1.0), domain, minWidth, maxWidth, minHeight, maxHeight)
        )
      }
      .replicateM(n)
      .map(x => NonEmptyVector.fromIterableOption(x).getOrElse(sys.error("List cannot be empty")))

  sealed trait PeakMovement
  // final case object Linear extends PeakMovement
  // final case object Circular extends PeakMovement
  case object Random extends PeakMovement

  /*
   The provided function `f` applies the _kind_ of peak update
   */
  def modifyPeaks(
    peaks: NonEmptyVector[PeakCone],
    movement: PeakMovement,
    changeSeverity: Double = 10.0,
    hSeverity: Double = 7.0,
    wSeverity: Double = 1.0,
    lambda: Double = 1.0 // Random environemnt when lambda is 0.0
  ): RVar[NonEmptyVector[PeakCone]] =
    movement match {
      case Random =>
        peaks.forEach { p =>
          val r: RVar[NonEmptyVector[Double]]       = p.shift.forEach(_ => Dist.stdNormal)
          val term1: RVar[NonEmptyVector[Double]]   = r.map(_.map(_ * (1.0 - lambda) * changeSeverity))
          val linComb: RVar[NonEmptyVector[Double]] =
            term1.map { t1 =>
              val term2: NonEmptyVector[Double] = p.shift.map(_ * lambda)

              t1.zip(term2).map { case (a, b) => a + b }
            }

          val length: RVar[Double] =
            r.map { _r =>
              _r.zip(p.shift).foldLeft(0.0) { case (z, (a, b)) => z + ((a + b) * (a + b)) }
            }

          val stepSize = length.map(l => changeSeverity / l)

          for {
            lin    <- linComb
            scalar <- stepSize
            s1     <- Dist.stdNormal
            s2     <- Dist.stdNormal
          } yield {
            val shift                                            = lin.map(_ * scalar)
            val (newPos, newShift): (List[Double], List[Double]) =
              shift
                .zip(p.location.zip(p.domain))
                .map { case (s, (p, d)) =>
                  val trial = p + s

                  if (d.contains(trial)) {
                    (trial, s)
                  } else if (d.lowerValue > trial) {
                    (2.0 * d.lowerValue - p - s, -1.0 * s)
                  } else {
                    (2.0 * d.upperValue - p - s, -1.0 * s)
                  }
                }
                .toList
                .unzip(x => x)

            val newHeight = {
              val change = s1 * hSeverity
              val temp   = p.height + change

              if (temp < p.minHeight) 2.0 * p.minHeight - p.height - change
              else if (temp > p.maxHeight) 2.0 * p.maxHeight - p.height - change
              else temp
            }

            val newWidth = {
              val change = s2 * wSeverity
              val temp   = p.width + change

              if (temp < p.minWidth) 2.0 * p.minWidth - p.width - change
              else if (temp > p.maxWidth) 2.0 * p.maxWidth - p.width - change
              else temp
            }

            p.copy(
              location = NonEmptyVector.fromIterableOption(newPos).getOrElse(sys.error("")),
              shift = NonEmptyVector.fromIterableOption(newShift).getOrElse(sys.error("")),
              height = newHeight,
              width = newWidth
            )
          }
        }
    }

  case class PeakCone(
    height: Double,
    width: Double,
    location: NonEmptyVector[Double],
    shift: NonEmptyVector[Double],
    domain: NonEmptyVector[Interval],
    minWidth: Double,
    maxWidth: Double,
    minHeight: Double,
    maxHeight: Double
  ) {
    def eval(x: NonEmptyVector[Double]): Double = {
      val sum = x.zip(location).map(a => (a._1 - a._2) * (a._1 - a._2)).foldLeft(0.0)(_ + _)
      val c   = math.sqrt(sum)

      val z = math.max(0, height - width * c)

      z
    }
  }

  def peakEval(peaks: NonEmptyVector[PeakCone]): Eval[NonEmptyVector] =
    Eval.unconstrained { (a: NonEmptyVector[Double]) =>
      val x = peaks.map(_.eval(a)).toChunk.max
      val r = if (x == Double.NaN) Infeasible(0.0) else Feasible(x)

      r
    }
  //  def printForPlot: Seq[(Double, Double, Double)] = {

//     val init: RVar[NonEmptyList[PeakCone]] = defaultPeaks(2, Interval(0.0, 100.0)^2)
//     val init2: RVar[NonEmptyList[PeakCone]] = defaultPeaks(5, Interval(0.0, 100.0)^2)
//     val (rng2, evaluator): (RNG, NonEmptyList[Double] => Objective[Double]) = init.flatMap(peakEval(_).eval).run(RNG.init(1234))
//     val (rng3, overlay) = init2.flatMap(peakEval(_).eval).run(rng2)

//     val lens: monocle.Optional[Objective[Double],Double] =
//       (Lenses._singleObjective[Double] composeLens Lenses._singleFit[Double]) composePrism Lenses._feasible

//     val objective: Seq[(Double, Double, Double)] = for {
//       x <- (0.0 until 100.0 by 1.0)
//       y <- (0.0 until 100.0 by 1.0)
//     } yield {
//       val input = NonEmptyList(x, y)
//       val overlayed = overlay.apply(input)
//       //val z = evaluator.apply(input)

//       val overlayedOut = lens.getOption(overlayed).getOrElse(0.0)// + 35.0
// //      val z2 = lens.getOption(z).getOrElse(0.0)

//       val d = if (overlayedOut > 0.0) 1.0 else 0.0 // - z2)

// //      if (d >= 35.0) println("d: " + d)

//       (x,y,d)
//     }
//     import java.nio.file.{Paths, Files}
//     import java.nio.charset.StandardCharsets

//     Files.write(Paths.get("file.txt"), objective.map(x => s"${x._1}\t${x._2}\t${x._3}").mkString("\n").getBytes(StandardCharsets.UTF_8))

//     objective
//   }
}
