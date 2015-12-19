package cilib
package example

import scalaz._
import scalaz.effect._

object MovingPeaksDebug extends SafeApp {
/*
  import QuantumPSO._

  val domain = Interval(closed(0.0), closed(100.0)) ^ 2

  def debugState: StateT[RVar, (List[Problems.PeakCone],List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]), Unit] =
    StateT {
      case (peaks, pop) =>
        RVar.point(((peaks, pop), println(peaks)))
    }

  def action = (mpb(1.0, 0.05).flatMap(_ => debugState)).replicateM(10)

  val peak = List(Problems.PeakCone(domain, 1.0)) // progressive parameters
*/
  override def runc: IO[Unit] = {
    IO(())
  }
}
