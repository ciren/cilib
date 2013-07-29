import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def chargedPSO = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ChargedPopulationInitialisationStrategy() {
      setEntityType(new pso.dynamic.ChargedParticle())
    }
  }
  setIterationStrategy {
    new pso.dynamic.DynamicIterationStrategy() {
      setDetectionStrategy {
        new pso.dynamic.detectionstrategies.PeriodicChangeDetectionStrategy() {
          setPeriod(100)
        }
      }
      setResponseStrategy(new pso.dynamic.responsestrategies.ParticleReevaluationResponseStrategy())
    }
  }
}

//measurements
def measurements = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.diversity.Diversity())
  addMeasurement(new measurement.single.ErrorMeasurement())
  addMeasurement(new measurement.single.CollectiveMeanOfMeasurement())
  addMeasurement(new measurement.single.dynamic.Error())
}

//problems
def movingPeaks = new problem.DynamicOptimisationProblem() {
  setDomain("R(0.0:100.0)^5")
  setFunction {
    new functions.continuous.dynamic.GeneralisedMovingPeaks() {
      setPeaks(1)
      setFrequency(100)
      setLambda(0.75)
      setShiftSeverity(5.0)
      setWidthSeverity(0.01)
      setHeightSeverity(7.0)
      setMinWidth(1.0)
      setMaxWidth(12.0)
      setMinHeight(30.0)
      setMaxHeight(70.0)
    }.asInstanceOf[functions.DynamicFunction[`type`.types.Type, _ <: Number]]
  }
}

//simulations
runner(simulation(chargedPSO, movingPeaks), measurements, "data/chargedPSO_MovingPeaks.txt", 10, 1)

