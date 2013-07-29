import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def bInertiaPSO = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())

  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider {
            new pso.positionprovider.binary.BinaryInertiaPositionProvider() {
              //delta value influences convergence speed
              setDelta(new controlparameter.LinearlyVaryingControlParameter(0.25, 0.0))
            }
          }
        }
      }
    }
  }
}

def bQuantumPSO = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())

  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.binary.BinaryQuantumPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.binary.BinaryQuantumVelocityProvider() {
              setAlpha(controlparameter.ConstantControlParameter.of(0.3))
              setBeta(controlparameter.ConstantControlParameter.of(0.7))
              setSelfAcceleration(controlparameter.ConstantControlParameter.of(0.2))
              setSocialAcceleration(controlparameter.ConstantControlParameter.of(0.2))
              setCognitiveAcceleration(controlparameter.ConstantControlParameter.of(0.6))
            }
          }
        }
      }
    }
  }
}

def bStaticProbPSO = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() {
    setTarget(100)
  })

  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(100)
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider(new pso.velocityprovider.binary.BinaryRandomVelocityProvider())
          setPositionProvider {
            new pso.positionprovider.binary.BinaryStaticProbPositionProvider() {
              setA(new controlparameter.LinearlyVaryingControlParameter(0.5, 0.33))
            }
          }
        }
      }
    }
  }
}

//problems
def order3deceptive = new problem.FunctionOptimisationProblem() {
  setDomain("B^60")
  setFunction(new functions.discrete.Order3Deceptive())
  setObjective(new problem.objective.Maximise())
}

def order3bipolar = new problem.FunctionOptimisationProblem() {
  setDomain("B^60")
  setFunction(new functions.discrete.Order3Bipolar())
  setObjective(new problem.objective.Maximise())
}

def onemax = new problem.FunctionOptimisationProblem() {
  setDomain("B^60")
  setFunction(new functions.discrete.Onemax())
  setObjective(new problem.objective.Maximise())
}

def order5deceptive = new problem.FunctionOptimisationProblem() {
  setDomain("B^60")
  setFunction(new functions.discrete.Order5Deceptive())
  setObjective(new problem.objective.Maximise())
}

def royalroad = new problem.FunctionOptimisationProblem() {
  setDomain("B^240")
  setFunction(new functions.discrete.RoyalRoad())
  setObjective(new problem.objective.Maximise())
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

//simulations
runner(simulation(bInertiaPSO, order3deceptive), fitness, "data/binaryInertiaPSO-order3deceptive.txt", 10, 10)
runner(simulation(bQuantumPSO, order3deceptive), fitness, "data/binaryQuantumPSO-order3deceptive.txt", 10, 10)
runner(simulation(bStaticProbPSO, order3deceptive), fitness, "data/bStaticProbPSO-order3deceptive.txt", 10, 10)
runner(simulation(bInertiaPSO, order3bipolar), fitness, "data/binaryInertiaPSO-order3bipolar.txt", 10, 10)
runner(simulation(bQuantumPSO, order3bipolar), fitness, "data/binaryQuantumPSO-order3bipolar.txt", 10, 10)
runner(simulation(bStaticProbPSO, order3bipolar), fitness, "data/bStaticProbPSO-order3bipolar.txt", 10, 10)
runner(simulation(bInertiaPSO, order5deceptive), fitness, "data/binaryInertiaPSO-order5deceptive.txt", 10, 10)
runner(simulation(bQuantumPSO, order5deceptive), fitness, "data/binaryQuantumPSO-order5deceptive.txt", 10, 10)
runner(simulation(bStaticProbPSO, order5deceptive), fitness, "data/bStaticProbPSO-order5deceptive.txt", 10, 10)
runner(simulation(bInertiaPSO, onemax), fitness, "data/binaryInertiaPSO-onemax.txt", 10, 10)
runner(simulation(bQuantumPSO, onemax), fitness, "data/binaryQuantumPSO-onemax.txt", 10, 10)
runner(simulation(bStaticProbPSO, onemax), fitness, "data/bStaticProbPSO-onemax.txt", 10, 10)
runner(simulation(bInertiaPSO, royalroad), fitness, "data/binaryInertiaPSO-royalroad.txt", 10, 10)
runner(simulation(bQuantumPSO, royalroad), fitness, "data/binaryQuantumPSO-royalroad.txt", 10, 10)
runner(simulation(bStaticProbPSO, royalroad), fitness, "data/bStaticProbPSO-royalroad.txt", 10, 10)

