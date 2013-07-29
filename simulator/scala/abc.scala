import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def abc = new boa.ABC() {
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(40)
      setEntityType(new boa.bee.WorkerBee())
    }
  }

  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setDancingSelectionStrategy(new util.selection.recipes.RouletteWheelSelector())

  setWorkerBeePercentage {
      new controlparameter.ConstantControlParameter() {
      setParameter(0.5)
    }
  }

  setForageLimit{
      new controlparameter.ConstantControlParameter() {
      setParameter(500)
    }
  }

  setExplorerBeeUpdateLimit {
      new controlparameter.ConstantControlParameter() {
      setParameter(1)
    }
  }

}

//problems
def spherical = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Spherical())
  setDomain("R(-5.12:5.12)^30")
}

def rosenbrock = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Rosenbrock())
  setDomain("R(-2.048:2.048)^30")
}

def rastrigin = new problem.FunctionOptimisationProblem(){
  setFunction(new functions.continuous.unconstrained.Rastrigin())
  setDomain("R(-5.12:5.12)^30")
}

def griewank = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Griewank())
  setDomain("R(-600:600)^30")
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

//simulations
runner(simulation(abc, spherical), fitness, "data/abc_spherical.txt", 10, 10)
runner(simulation(abc, griewank), fitness, "data/abc_griewank.txt", 10, 10)
runner(simulation(abc, rastrigin), fitness, "data/abc_rastrigin.txt", 10, 10)
runner(simulation(abc, rosenbrock), fitness, "data/abc_rosenbrock.txt", 10, 10)

