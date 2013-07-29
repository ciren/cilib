import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def xpso_pcx_velocity = new pso.PSO() { //Crossover velocity provider PCX
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.LinearPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.CrossoverVelocityProvider() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setSigma1(controlparameter.ConstantControlParameter.of(1.0))
                  setSigma2(controlparameter.ConstantControlParameter.of(1.0))
                }
              }
            }
          }
        }
      }
    }
  }
}

def xpso_pcx_velocity_xover_pbest = new pso.PSO() { //Crossover velocity provider PCX with Crossover pBest update
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.LinearPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.CrossoverVelocityProvider() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setSigma1(controlparameter.ConstantControlParameter.of(1.0))
                  setSigma2(controlparameter.ConstantControlParameter.of(1.0))
                }
              }
            }
          }
          setPersonalBestUpdateStrategy {
            new pso.pbestupdate.DistinctPersonalBestUpdateStrategy() {
              setPositionProvider {
                new pso.pbestupdate.CrossoverDistinctPositionProvider() {
                  setCrossoverStrategy(new entity.operators.crossover.real.ParentCentricCrossoverStrategy())
                }
              }
            }
          }
        }
      }
    }
  }
}

def xpso_pcx_velocity_bounded_xover_pbest = new pso.PSO() { //Crossover velocity provider PCX with Bounded Crossover pBest update
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.LinearPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.CrossoverVelocityProvider() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setSigma1(controlparameter.ConstantControlParameter.of(1.0))
                  setSigma2(controlparameter.ConstantControlParameter.of(1.0))
                }
              }
            }
          }
          setPersonalBestUpdateStrategy {
            new pso.pbestupdate.BoundedPersonalBestUpdateStrategy() {
              setDelegate {
                new pso.pbestupdate.DistinctPersonalBestUpdateStrategy() {
                  setPositionProvider {
                    new pso.pbestupdate.CrossoverDistinctPositionProvider() {
                      setCrossoverStrategy(new entity.operators.crossover.real.ParentCentricCrossoverStrategy())
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

def xpso_pcx_velocity_alternative = new pso.PSO() { //Deb's alternative velocity update
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.LinearPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.DistinctCrossoverVelocityProvider() {
              setMainCrossover {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setSigma1(controlparameter.ConstantControlParameter.of(0.17))
                  setSigma2(controlparameter.ConstantControlParameter.of(0.17))
                  setParentProvider(new entity.operators.crossover.parentprovider.BestParentProvider())
                }
              }
              setAlternateCrossover {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setSigma1(controlparameter.ConstantControlParameter.of(0.17))
                  setSigma2(controlparameter.ConstantControlParameter.of(0.0))
                  setParentProvider(new entity.operators.crossover.parentprovider.BestParentProvider())
                }
              }
            }
          }
        }
      }
    }
  }
}

def xpso_pcx_velocity_mutation_pbest = new pso.PSO() { //Crossover velocity provider PCX with Mutation pBest update
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.LinearPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.CrossoverVelocityProvider() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setSigma1(controlparameter.ConstantControlParameter.of(1.0))
                  setSigma2(controlparameter.ConstantControlParameter.of(1.0))
                }
              }
            }
          }
          setPersonalBestUpdateStrategy {
            new pso.pbestupdate.DistinctPersonalBestUpdateStrategy() {
              setPositionProvider {
                new pso.pbestupdate.MutatedDistinctPositionProvider() {
                  setDistribution {
                    new math.random.GaussianDistribution() {
                      setDeviation(new controlparameter.LinearlyVaryingControlParameter(20,0))
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

def xpso_pcx_velocity_bounded_mutation_pbest = new pso.PSO() { //Crossover velocity provider PCX with Bounded Mutation pBest update
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.LinearPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.CrossoverVelocityProvider() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                  setSigma1(controlparameter.ConstantControlParameter.of(1.0))
                  setSigma2(controlparameter.ConstantControlParameter.of(1.0))
                }
              }
            }
          }
          setPersonalBestUpdateStrategy {
            new pso.pbestupdate.BoundedPersonalBestUpdateStrategy() {
              setDelegate {
                new pso.pbestupdate.DistinctPersonalBestUpdateStrategy() {
                  setPositionProvider {
                    new pso.pbestupdate.MutatedDistinctPositionProvider() {
                      setDistribution {
                        new math.random.GaussianDistribution() {
                          setDeviation(new controlparameter.LinearlyVaryingControlParameter(20,0))
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
runner(simulation(xpso_pcx_velocity, spherical), fitness, "data/xpso_pcx_velocity_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_velocity_xover_pbest, spherical), fitness, "data/xpso_pcx_velocity_xover_pbest_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_velocity_bounded_xover_pbest, spherical), fitness, "data/xpso_pcx_velocity_bounded_xover_pbest_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_velocity_alternative, spherical), fitness, "data/xpso_pcx_velocity_alternative_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_velocity_mutation_pbest, spherical), fitness, "data/xpso_pcx_velocity_mutation_pbest_spherical.txt", 10, 10)
runner(simulation(xpso_pcx_velocity_bounded_mutation_pbest, spherical), fitness, "data/xpso_pcx_velocity_bounded_mutation_pbest_spherical.txt", 10, 10)

