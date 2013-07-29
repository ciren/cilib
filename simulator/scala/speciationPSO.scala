import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def speciationPSO = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setNeighbourhood {
    new entity.topologies.SpeciationNeighbourhood[pso.particle.Particle]() {
      setRadius(controlparameter.ConstantControlParameter.of(0.1))
    }
  }
}

def enhancedSpeciationPSO = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setNeighbourhood {
    new entity.topologies.SpeciationNeighbourhood[pso.particle.Particle]() {
      setRadius(controlparameter.ConstantControlParameter.of(0.1))
    }
  }
  setIterationStrategy(new niching.iterationstrategies.EnhancedSpeciation())
}

//problems
def f1 = new problem.FunctionOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction1())
  setObjective(new problem.objective.Maximise())
}

def f2 = new problem.FunctionOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction2())
  setObjective(new problem.objective.Maximise())
}

def f3 = new problem.FunctionOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction3())
  setObjective(new problem.objective.Maximise())
}

def f4 = new problem.FunctionOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction4())
  setObjective(new problem.objective.Maximise())
}

def f5 = new problem.FunctionOptimisationProblem() {
  setDomain("R(-5:5)^2")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction5())
  setObjective(new problem.objective.Maximise())
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.multiple.MultipleSolutions())
  addMeasurement(new measurement.multiple.MultipleFitness())
}

//simulations
runner(simulation(speciationPSO, f1), fitness, "data/speciationPSO-f1.txt", 10, 10)
runner(simulation(speciationPSO, f2), fitness, "data/speciationPSO-f2.txt", 10, 10)
runner(simulation(speciationPSO, f3), fitness, "data/speciationPSO-f3.txt", 10, 10)
runner(simulation(speciationPSO, f4), fitness, "data/speciationPSO-f4.txt", 10, 10)
runner(simulation(speciationPSO, f5), fitness, "data/speciationPSO-f5.txt", 10, 10)
runner(simulation(enhancedSpeciationPSO, f1), fitness, "data/enhancedSpeciationPSO-f1.txt", 10, 10)
runner(simulation(enhancedSpeciationPSO, f2), fitness, "data/enhancedSpeciationPSO-f2.txt", 10, 10)
runner(simulation(enhancedSpeciationPSO, f3), fitness, "data/enhancedSpeciationPSO-f3.txt", 10, 10)
runner(simulation(enhancedSpeciationPSO, f4), fitness, "data/enhancedSpeciationPSO-f4.txt", 10, 10)
runner(simulation(enhancedSpeciationPSO, f5), fitness, "data/enhancedSpeciationPSO-f5.txt", 10, 10)

