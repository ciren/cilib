import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def xoverPBestUpdate(zv: Boolean = false) = new pso.pbestupdate.DistinctPersonalBestUpdateStrategy() {
  setPositionProvider {
    new pso.pbestupdate.CrossoverDistinctPositionProvider() {
      setCrossoverStrategy(new entity.operators.crossover.real.ParentCentricCrossoverStrategy())
    }
  }
  if (zv) setVelocityProvider(new pso.crossover.velocityprovider.ZeroOffspringVelocityProvider())
}

def mutationPBestUpdate(zv: Boolean = false) = new pso.pbestupdate.DistinctPersonalBestUpdateStrategy() {
  setPositionProvider {
    new pso.pbestupdate.MutatedDistinctPositionProvider() {
      setDistribution {
        new math.random.GaussianDistribution() {
          setDeviation(new controlparameter.LinearlyVaryingControlParameter(1,0))
        }
      }
    }
  }
  if (zv) setVelocityProvider(new pso.crossover.velocityprovider.ZeroOffspringVelocityProvider())
}

def bounded(d: pso.pbestupdate.PersonalBestUpdateStrategy) = {
  val b = new pso.pbestupdate.BoundedPersonalBestUpdateStrategy()
  b.setDelegate(d)
  b
}

def xpso_pcx_guide_repeating(com: String, pbu: pso.pbestupdate.PersonalBestUpdateStrategy) = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPersonalBestUpdateStrategy(pbu)
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

def xpso_pcx_guide_boltzmann(com: String, pbu: pso.pbestupdate.PersonalBestUpdateStrategy) = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPersonalBestUpdateStrategy(pbu)
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

def xpso_discrete_pbestupdate = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPersonalBestUpdateStrategy {
            new pso.pbestupdate.CrossoverPersonalBestUpdateStrategy() {
              setCrossoverStrategy(new entity.operators.crossover.UniformCrossoverStrategy())
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

// regular variants (xover, bounded xover, mutation, bounded mutation)
runner(simulation(xpso_pcx_guide_repeating("pbest", xoverPBestUpdate()), spherical), fitness, "data/xpso_pcx_guide_repeating_xover_pbest-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_boltzmann("pbest", xoverPBestUpdate()), spherical), fitness, "data/xpso_pcx_guide_boltzmann_xover_pbest-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_repeating("pbest", bounded(xoverPBestUpdate())), spherical), fitness, "data/xpso_pcx_guide_repeating_bounded_xover_pbest-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_boltzmann("pbest", bounded(xoverPBestUpdate())), spherical), fitness, "data/xpso_pcx_guide_boltzmann_bounded_xover_pbest-spherical.txt", 5, 5)

runner(simulation(xpso_pcx_guide_repeating("pbest", mutationPBestUpdate()), spherical), fitness, "data/xpso_pcx_guide_repeating_mutation_pbest-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_boltzmann("pbest", mutationPBestUpdate()), spherical), fitness, "data/xpso_pcx_guide_boltzmann_mutation_pbest-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_repeating("pbest", bounded(mutationPBestUpdate())), spherical), fitness, "data/xpso_pcx_guide_repeating_bounded_mutation_pbest-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_boltzmann("pbest", bounded(mutationPBestUpdate())), spherical), fitness, "data/xpso_pcx_guide_boltzmann_bounded_mutation_pbest-spherical.txt", 5, 5)

// zero velocity variants
runner(simulation(xpso_pcx_guide_repeating("pbest", xoverPBestUpdate(true)), spherical), fitness, "data/xpso_pcx_guide_repeating_xover_pbest_zero_velocity-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_boltzmann("pbest", xoverPBestUpdate(true)), spherical), fitness, "data/xpso_pcx_guide_boltzmann_xover_pbest_zero_velocity-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_repeating("pbest", bounded(xoverPBestUpdate(true))), spherical), fitness, "data/xpso_pcx_guide_repeating_bounded_xover_pbest_zero_velocity-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_boltzmann("pbest", bounded(xoverPBestUpdate(true))), spherical), fitness, "data/xpso_pcx_guide_boltzmann_bounded_xover_pbest_zero_velocity-spherical.txt", 5, 5)

runner(simulation(xpso_pcx_guide_repeating("pbest", mutationPBestUpdate(true)), spherical), fitness, "data/xpso_pcx_guide_repeating_mutation_pbest-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_boltzmann("pbest", mutationPBestUpdate(true)), spherical), fitness, "data/xpso_pcx_guide_boltzmann_mutation_pbest-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_repeating("pbest", bounded(mutationPBestUpdate(true))), spherical), fitness, "data/xpso_pcx_guide_repeating_bounded_mutation_pbest_zero_velocity-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_guide_boltzmann("pbest", bounded(mutationPBestUpdate(true))), spherical), fitness, "data/xpso_pcx_guide_boltzmann_bounded_mutation_pbest_zero_velocity-spherical.txt", 5, 5)

runner(simulation(xpso_discrete_pbestupdate, spherical), fitness, "data/xpso_discrete_pbestupdate-spherical.txt", 5, 5)

