import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def xpso_pcx_guide_repeating(com: String) = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setGlobalGuideProvider {
            new pso.guideprovider.CrossoverGuideProvider() {
              setComponent(com)
              setCrossoverSelection {
                new pso.crossover.operations.RepeatingCrossoverSelection() {
                  setCrossoverStrategy {
                    new pso.crossover.ParticleCrossoverStrategy() {
                      setCrossoverStrategy(new entity.operators.crossover.real.ParentCentricCrossoverStrategy())
                      setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
                      setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
                    }
                  }
                  setParticleProvider(new pso.crossover.particleprovider.GBestParticleProvider())
                  setRetries(controlparameter.ConstantControlParameter.of(10))
                }
              }
            }
          }
        }
      }
    }
  }
}

def xpso_pcx_guide_boltzmann(com: String) = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setGlobalGuideProvider {
            new pso.guideprovider.CrossoverGuideProvider() {
              setComponent(com)
              setCrossoverSelection {
                new pso.crossover.operations.BoltzmannCrossoverSelection() {
                  setCrossoverStrategy {
                    new pso.crossover.ParticleCrossoverStrategy() {
                      setCrossoverStrategy {
                        new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                          setNumberOfParents(controlparameter.ConstantControlParameter.of(3))
                        }
                      }
                      setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
                      setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
                    }
                  }
                  setParticleProvider(new pso.crossover.particleprovider.GBestParticleProvider())
                  setComparator {
                    new entity.comparator.BoltzmannComparator() {
                      setTempSchedule(new controlparameter.LinearlyVaryingControlParameter(0.5, 0))
                      setDistribution(new math.random.UniformDistribution())
                    }
                  }
                }
              }
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
runner(simulation(xpso_pcx_guide_repeating("pbest"), spherical), fitness, "data/xpso-pcx-guide-repeating-pbest_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_guide_repeating("position"), spherical), fitness, "data/xpso-pcx-guide-repeating-position_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_guide_boltzmann("pbest"), spherical), fitness, "data/xpso-pcx-guide-boltzmann-pbest_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_guide_boltzmann("position"), spherical), fitness, "data/xpso-pcx-guide-boltzmann-position_spherical.txt", 10, 10)

