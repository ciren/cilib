import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def xpso_pcx_iteration_repeating = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.RepeatingCrossoverSelection() {
          setCrossoverStrategy {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setNumberOfParents(controlparameter.ConstantControlParameter.of(10))
                }
              }
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
            }
          }
          setRetries(controlparameter.ConstantControlParameter.of(10))
        }
      }
    }
  }
}

def xpso_pcx_iteration_boltzmann = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.BoltzmannCrossoverSelection() {
          setCrossoverStrategy {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setNumberOfParents(controlparameter.ConstantControlParameter.of(10))
                }
              }
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
            }
          }
          setComparator {
            new entity.comparator.BoltzmannComparator() {
              setTempSchedule(new controlparameter.LinearlyVaryingControlParameter(100, 1))
              setDistribution(new math.random.UniformDistribution())
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

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

//simulations
runner(simulation(xpso_pcx_iteration_repeating, spherical), fitness, "data/xpso_pcx_iteration_repeating_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_iteration_boltzmann, spherical), fitness, "data/xpso_pcx_iteration_boltzmann_spherical.txt", 10, 10)

