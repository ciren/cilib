import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def pspg = new pso.PSO() {
  addStoppingCondition {
    new stoppingcondition.MeasuredStoppingCondition() {
      setMeasurement(new measurement.single.FitnessEvaluations())
      setTarget(100000)
    }
  }

  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(25)
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ConstrictionVelocityProvider() {
              setKappa(controlparameter.ConstantControlParameter.of(1.0))
              setSocialAcceleration(controlparameter.ConstantControlParameter.of(2.05))
              setCognitiveAcceleration(controlparameter.ConstantControlParameter.of(2.05))
            }
          }
        }
      }
    }
  }

  setIterationStrategy {
    new pso.iterationstrategies.PSPGIterationStrategy() {
      setIterationStrategy(new pso.iterationstrategies.SynchronousIterationStrategy())

      setCrossoverStrategy {
        new pso.crossover.ParticleCrossoverStrategy() {
          setCrossoverStrategy {
            new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
              setNumberOfParents(controlparameter.ConstantControlParameter.of(3))
              setNumberOfOffspring(controlparameter.ConstantControlParameter.of(3))
              setParentProvider(new entity.operators.crossover.parentprovider.BestParentProvider())
              setSigma1(controlparameter.ConstantControlParameter.of(0.1))
              setSigma2(controlparameter.ConstantControlParameter.of(0.1))
            }
          }
        }
      }

      setIterationProbability(controlparameter.ConstantControlParameter.of(0.05))
    }
  }
}

//problems
def spherical = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Spherical())
  setDomain("R(-5.12:5.12)^30")
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

//simulations
runner(simulation(pspg, spherical), fitness, "data/pspg_spherical.txt", 10, 10)

