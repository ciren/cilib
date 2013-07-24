package net.cilib
package simulator

import scalaz._

object Simulation {
  import net.sourceforge.cilib.algorithm.AbstractAlgorithm
  import net.sourceforge.cilib.algorithm.AlgorithmListener
  import net.sourceforge.cilib.problem.Problem
  import net.sourceforge.cilib.simulator.MeasurementSuite

  final case class Simulation(alg: AbstractAlgorithm) {
    def run = {
      alg.performInitialisation
      alg.run
      alg
    }
  }

  def simulation(alg: => AbstractAlgorithm, prob: => Problem) = () =>
    Simulation({
        val a = alg
        val p = prob

        a.setOptimisationProblem(p)
        a
      })

  def runner(s: () => Simulation, m: => MeasurementSuite, file: String, samples: Int = 30, resolution: Int = 1) = {
    val descriptions = m.getDescriptions
    val combiner = new net.sourceforge.cilib.simulator.MeasurementCombiner(new java.io.File(file))

    class Listener(measurements: MeasurementSuite) extends AlgorithmListener {
      def algorithmFinished(e: net.sourceforge.cilib.algorithm.AlgorithmEvent): Unit = ()
      def algorithmStarted(e: net.sourceforge.cilib.algorithm.AlgorithmEvent): Unit = ()
      def getClone(): net.sourceforge.cilib.algorithm.AlgorithmListener = this
      def iterationCompleted(e: net.sourceforge.cilib.algorithm.AlgorithmEvent): Unit = {
        if (e.getSource.getIterations % resolution == 0) {
          measurements.measure(e.getSource)
        }
      }
    }

    import scala.collection.JavaConversions._
    import scala.collection.mutable.ListBuffer

    combiner.combine(descriptions, ListBuffer((1 to samples).par.map(_ => {
        val algorithm = s()
        val measurements = m
        algorithm.alg.addAlgorithmListener(new Listener(measurements))
        measurements.initialise
        algorithm.run
        measurements.close
        measurements.getFile
      }).toList: _*))
  }
}
