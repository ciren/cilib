import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def de = new ec.EC() {
  setIterationStrategy(new ec.iterationstrategies.DifferentialEvolutionIterationStrategy())

  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(50)
      setEntityType(new ec.Individual())
    }
  }

  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() {
    setTarget(5000)
  })
}

def ga = new ec.EC() {
  setIterationStrategy(new ec.iterationstrategies.GeneticAlgorithmIterationStrategy())

  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(50)
      setEntityType(new ec.Individual())
    }
  }

  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() {
    setTarget(5000)
  })
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
  addMeasurement(new measurement.single.Solution())
}

//simulations
runner(simulation(ga, spherical), fitness, "data/ga_spherical.txt", 10, 10)
runner(simulation(ga, griewank), fitness, "data/ga_griewank.txt", 10, 10)
runner(simulation(ga, rastrigin), fitness, "data/ga_rastrigin.txt", 10, 10)
runner(simulation(ga, rosenbrock), fitness, "data/ga_rosenbrock.txt", 10, 10)

runner(simulation(de, spherical), fitness, "data/de_spherical.txt", 10, 10)
runner(simulation(de, griewank), fitness, "data/de_griewank.txt", 10, 10)
runner(simulation(de, rastrigin), fitness, "data/de_rastrigin.txt", 10, 10)
runner(simulation(de, rosenbrock), fitness, "data/de_rosenbrock.txt", 10, 10)

