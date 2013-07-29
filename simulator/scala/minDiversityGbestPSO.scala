import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def gbest = new pso.PSO() {
  addStoppingCondition {
    new stoppingcondition.MaintainedStoppingCondition() {
      setCondition {
        new stoppingcondition.MeasuredStoppingCondition() {
          setMeasurement {
            new measurement.single.diversity.AverageEntropyDiversityMeasure() {
              setIntervals(10)
            }
          }
          setPredicate(new stoppingcondition.Minimum())
          setTarget(0.001)
        }
      }
      setConsecutiveIterations(10)
    }
  }
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

//problems
def spherical = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Spherical())
  setDomain("R(-5.12:5.12)^30")
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement {
    new measurement.single.diversity.AverageEntropyDiversityMeasure() {
      setIntervals(10)
    }
  }
  addMeasurement(new measurement.single.Fitness())
}

//simulations
runner(simulation(gbest, spherical), fitness, "data/gbest_spherical_minDiversity.txt", 10, 10)

