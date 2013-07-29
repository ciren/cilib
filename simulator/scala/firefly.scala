import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def firefly = new ff.FFA() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setIterationStrategy(new ff.iterationstrategies.StandardFireflyIterationStrategy())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new ff.firefly.StandardFirefly() {
          setPositionUpdateStrategy {
            new ff.positionupdatestrategies.StandardFireflyPositionUpdateStrategy() {
              // alpha determines the randomness of the fireflies
              setAlpha(new controlparameter.LinearlyVaryingControlParameter(0.2, 0.0))
              // betaMin controls the minimum attraction of neighbouring fireflies
              setBetaMin(controlparameter.ConstantControlParameter.of(0.2))
              /* gamma determines the light absorbtion: as gamma -> 0, the algorithm
                 behaves more like the standard PSO, as gamma -> infinity it behaves
                 like a random search algorithm */
              setGamma(controlparameter.ConstantControlParameter.of(1.0))
            }
          }
        }
      }
    }
  }
}

//problems
def spherical = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Spherical())
  setDomain("R(-5.12:5.12)^30")
}

def yang1 = new problem.FunctionOptimisationProblem() {
  setFunction {
    new functions.continuous.unconstrained.Yang1() {
      setM(controlparameter.ConstantControlParameter.of(5))
      setBeta(controlparameter.ConstantControlParameter.of(15))
    }
  }
  setDomain("R(-20:20)^30")
}

def yang2 = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Yang2())
  setDomain("R(-6.28318:6.28318)^30")
}

def yang3 = new problem.FunctionOptimisationProblem() {
  setFunction {
    new functions.continuous.unconstrained.Yang3() {
      setAlpha(controlparameter.ConstantControlParameter.of(1))
      setBeta(controlparameter.ConstantControlParameter.of(1))
    }
  }
  setDomain("R(0:10)^2")
}

def yang4 = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Yang4())
  setDomain("R(-5:5)^30")
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

runner(simulation(firefly, spherical), fitness, "data/firefly-spherical.txt", 10, 10)
runner(simulation(firefly, yang1), fitness, "data/firefly-yang1.txt", 10, 10)
runner(simulation(firefly, yang2), fitness, "data/firefly-yang2.txt", 10, 10)
runner(simulation(firefly, yang3), fitness, "data/firefly-yang3.txt", 10, 10)
runner(simulation(firefly, yang4), fitness, "data/firefly-yang4.txt", 10, 10)

