import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def tviw = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())

  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(40)
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              new controlparameter.LinearlyVaryingControlParameter(0.9, 0.4),
              controlparameter.ConstantControlParameter.of(1.496180),
              controlparameter.ConstantControlParameter.of(1.496180)
            )
          }
        }
      }
    }
  }
  setNeighbourhood(new entity.topologies.VonNeumannNeighbourhood())
}

def de = new ec.EC() {
  setIterationStrategy(new ec.iterationstrategies.DifferentialEvolutionIterationStrategy())

  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(50)
      setEntityType(new ec.Individual())
    }
  }

  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

def ga = new ec.EC() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

//problems
def spherical = new functions.continuous.decorators.AngleModulation() {
  setDomain("R(-1.0:1.0)^4")
  setPrecision(2)
  setProblem {
    new problem.FunctionOptimisationProblem() {
      setDomain("R(-5.12:5.12)^30")
      setFunction(new functions.continuous.unconstrained.Spherical())
    }
  }
}

def rosenbrock = new functions.continuous.decorators.AngleModulation() {
  setDomain("R(-1.0:1.0)^4")
  setPrecision(3)
  setProblem {
    new problem.FunctionOptimisationProblem() {
      setDomain("R(-2.048:2.048)^30")
      setFunction(new functions.continuous.unconstrained.Rosenbrock())
    }
  }
}

def griewank = new functions.continuous.decorators.AngleModulation() {
  setDomain("R(-300.0:300.0)^30")
  setPrecision(3)
  setProblem {
    new problem.FunctionOptimisationProblem() {
      setDomain("R(-1.0:1.0)^4")
      setFunction(new functions.continuous.unconstrained.Griewank())
    }
  }
}

def ackley = new functions.continuous.decorators.AngleModulation() {
  setDomain("R(-1.0:1.0)^4")
  setPrecision(2)
  setProblem {
    new problem.FunctionOptimisationProblem() {
      setDomain("R(-30.0:30.0)^30")
      setFunction(new functions.continuous.unconstrained.Ackley())
    }
  }
}

def rastrigin = new functions.continuous.decorators.AngleModulation() {
  setDomain("R(-1.0:1.0)^4")
  setPrecision(2)
  setProblem {
    new problem.FunctionOptimisationProblem() {
      setDomain("R(-5.12:5.12)^30")
      setFunction(new functions.continuous.unconstrained.Rastrigin())
    }
  }
}

def rastrigin_multiple_generators = new functions.continuous.decorators.AngleModulation() {
  setNumberOfGenerators(3)
  setPrecision(2)
  setProblem {
    new problem.FunctionOptimisationProblem() {
      setDomain("R(-5.12:5.12)^30")
      setFunction(new functions.continuous.unconstrained.Rastrigin())
    }
  }
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
  addMeasurement(new measurement.single.Solution())
}

//simulations
runner(simulation(de, spherical), fitness, "data/amde-spherical.txt", 10, 10)
runner(simulation(de, griewank), fitness, "data/amde_griewank.txt", 10, 10)
runner(simulation(de, ackley), fitness, "data/amde_ackley.txt", 10, 10)
runner(simulation(de, rosenbrock), fitness, "data/amde_rosenbrock.txt", 10, 10)
runner(simulation(de, rastrigin), fitness, "data/amde_rastrigin.txt", 10, 10)
runner(simulation(de, rastrigin_multiple_generators), fitness, "data/amde_rastrigin_multiple_generators.txt", 10, 10)

runner(simulation(ga, spherical), fitness, "data/amga-spherical.txt", 10, 10)
runner(simulation(ga, griewank), fitness, "data/amga_griewank.txt", 10, 10)
runner(simulation(ga, ackley), fitness, "data/amga_ackley.txt", 10, 10)
runner(simulation(ga, rosenbrock), fitness, "data/amga_rosenbrock.txt", 10, 10)
runner(simulation(ga, rastrigin_multiple_generators), fitness, "data/amga_rastrigin_multiple_generators.txt", 10, 10)

runner(simulation(tviw, spherical), fitness, "data/ampso-spherical.txt", 10, 10)
runner(simulation(tviw, griewank), fitness, "data/ampso_griewank.txt", 10, 10)
runner(simulation(tviw, ackley), fitness, "data/ampso_ackley.txt", 10, 10)
runner(simulation(tviw, rosenbrock), fitness, "data/ampso_rosenbrock.txt", 10, 10)
runner(simulation(tviw, rastrigin), fitness, "data/ampso_rastrigin.txt", 10, 10)
runner(simulation(tviw, rastrigin_multiple_generators), fitness, "data/ampso_rastrigin_multiple_generators.txt", 10, 10)
