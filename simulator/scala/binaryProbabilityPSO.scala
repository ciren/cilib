import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def pbpso = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

// PBPSO uses a function decorator to convert a real-valued vector to a binary vector.
// The domain range e.g. (-1:1) is a parameter of the PBPSO algorithm.
def onemax = new problem.FunctionOptimisationProblem() {
  setDomain("R(-1:1)^90")
  setObjective(new problem.objective.Maximise())
  setFunction {
    new functions.continuous.decorators.PBPSOFunctionDecorator() {
      setFunction(new functions.discrete.Onemax())
    }
  }
}

def order3bipolar = new problem.FunctionOptimisationProblem() {
  setDomain("R(-1:1)^90")
  setObjective(new problem.objective.Maximise())
  setFunction {
    new functions.continuous.decorators.PBPSOFunctionDecorator() {
      setFunction(new functions.discrete.Order3Bipolar())
    }
  }
}

def order3deceptive = new problem.FunctionOptimisationProblem() {
  setDomain("R(-1:1)^90")
  setObjective(new problem.objective.Maximise())
  setFunction {
    new functions.continuous.decorators.PBPSOFunctionDecorator() {
      setFunction(new functions.discrete.Order3Deceptive())
    }
  }
}

def order5deceptive = new problem.FunctionOptimisationProblem() {
  setDomain("R(-1:1)^90")
  setObjective(new problem.objective.Maximise())
  setFunction {
    new functions.continuous.decorators.PBPSOFunctionDecorator() {
      setFunction(new functions.discrete.Order5Deceptive())
    }
  }
}

def royalroad = new problem.FunctionOptimisationProblem() {
  setDomain("R(-1:1)^240")
  setObjective(new problem.objective.Maximise())
  setFunction {
    new functions.continuous.decorators.PBPSOFunctionDecorator() {
      setFunction(new functions.discrete.RoyalRoad())
    }
  }
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

//simulations
runner(simulation(pbpso, onemax), fitness, "data/pbpso-onemax.txt", 10, 10)
runner(simulation(pbpso, order3bipolar), fitness, "data/pbpso-order3bipolar.txt", 10, 10)
runner(simulation(pbpso, order3deceptive), fitness, "data/pbpso-order3deceptive.txt", 10, 10)
runner(simulation(pbpso, order5deceptive), fitness, "data/pbpso-order5deceptive.txt", 10, 10)
runner(simulation(pbpso, royalroad), fitness, "data/pbpso-royalroad.txt", 10, 10)

