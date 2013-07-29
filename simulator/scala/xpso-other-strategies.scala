import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def xpso_lovbjerg_hybrid = new pso.PSO() { // Lovbjerg Hybrid XPSO
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ClampingVelocityProvider() {
              setVMax(controlparameter.ConstantControlParameter.of(100))
              setDelegate {
                new pso.velocityprovider.StandardVelocityProvider(
                  new controlparameter.LinearlyVaryingControlParameter(0.7, 0.4),
                  controlparameter.ConstantControlParameter.of(2.0),
                  controlparameter.ConstantControlParameter.of(2.0)
                )
              }
            }
          }
        }
      }
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.HybridCrossoverOperation() {
          setParticleCrossover {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy(new entity.operators.crossover.real.ArithmeticCrossoverStrategy())
              setPbestProvider(new pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.LovbjergOffspringVelocityProvider())
            }
          }
          setParentReplacementStrategy(new pso.crossover.parentupdate.AlwaysReplaceParentReplacementStrategy())
          setSelector(new util.selection.recipes.RandomSelector())
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.2))
        }
      }
    }
  }
}

def xpso_gliw_glac_arithmetic = new pso.PSO() { // Global Local Best PSO with GLbestIW and GLbestAC with Arithmetic Xover
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ClampingVelocityProvider() {
              setVMax(controlparameter.ConstantControlParameter.of(100))
              setDelegate(new pso.velocityprovider.GlobalLocalBestVelocityProvider())
              // Do not set acceleration and inertia to use the default GLbestIW and GLbestAC
            }
          }
        }
      }
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.HybridCrossoverOperation() {
          setParticleCrossover {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ArithmeticCrossoverStrategy() {
                  setLambda(new controlparameter.RandomControlParameter())
                }
              }
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
            }
          }
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.8))
        }
      }
    }
  }
}

def xpso_gliw_fac_arithmetic = new pso.PSO() { // Global Local Best PSO with GLbestIW and Fixed AC with Arithmetic Xover
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ClampingVelocityProvider() {
              setVMax(controlparameter.ConstantControlParameter.of(100))
              setDelegate {
                new pso.velocityprovider.GlobalLocalBestVelocityProvider() {
                  setAcceleration(controlparameter.ConstantControlParameter.of(2.0))
                }
              }
            }
          }
        }
      }
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.HybridCrossoverOperation() {
          setParticleCrossover {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ArithmeticCrossoverStrategy() {
                  setLambda(new controlparameter.RandomControlParameter())
                }
              }
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
            }
          }
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.8))
        }
      }
    }
  }
}

def xpso_gliw_glac_averageConvex = new pso.PSO() { // Global Local Best PSO with GLbestIW and GLbestAC with Average Convex Xover
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ClampingVelocityProvider() {
              setVMax(controlparameter.ConstantControlParameter.of(100))
              setDelegate(new pso.velocityprovider.GlobalLocalBestVelocityProvider())
              // Do not set acceleration and inertia to use the default GLbestIW and GLbestAC
            }
          }
        }
      }
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.HybridCrossoverOperation() {
          setParticleCrossover {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ArithmeticCrossoverStrategy() {
                  setLambda(controlparameter.ConstantControlParameter.of(0.5))
                }
              }
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
            }
          }
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.8))
        }
      }
    }
  }
}

def xpso_gliw_fac_averageConvex = new pso.PSO() { // Global Local Best PSO with GLbestIW and Fixed AC with Average Convex Xover
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ClampingVelocityProvider() {
              setVMax(controlparameter.ConstantControlParameter.of(100))
              setDelegate {
                new pso.velocityprovider.GlobalLocalBestVelocityProvider() {
                  setAcceleration(controlparameter.ConstantControlParameter.of(2.0))
                }
              }
            }
          }
        }
      }
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.HybridCrossoverOperation() {
          setParticleCrossover {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.ArithmeticCrossoverStrategy() {
                  setLambda(controlparameter.ConstantControlParameter.of(0.5))
                }
              }
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
            }
          }
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.8))
        }
      }
    }
  }
}

def xpso_gliw_glac_rootProbability = new pso.PSO() { // Global Local Best PSO with GLbestIW and GLbestAC with Root Probability Xover
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ClampingVelocityProvider() {
              setVMax(controlparameter.ConstantControlParameter.of(100))
              setDelegate(new pso.velocityprovider.GlobalLocalBestVelocityProvider())
              // Do not set acceleration and inertia to use the default GLbestIW and GLbestAC
            }
          }
        }
      }
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.HybridCrossoverOperation() {
          setParticleCrossover {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.RootProbabilityCrossoverStrategy() {
                  setLambda(controlparameter.ConstantControlParameter.of(0.5))
                }
              }
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
            }
          }
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.8))
        }
      }
    }
  }
}

def xpso_gliw_fac_rootProbability = new pso.PSO() { // Global Local Best PSO with GLbestIW and Fixed AC with Root Probability Xover
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ClampingVelocityProvider() {
              setVMax(controlparameter.ConstantControlParameter.of(100))
              setDelegate {
                new pso.velocityprovider.GlobalLocalBestVelocityProvider() {
                  setAcceleration(controlparameter.ConstantControlParameter.of(2.0))
                }
              }
            }
          }
        }
      }
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.HybridCrossoverOperation() {
          setParticleCrossover {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy {
                new entity.operators.crossover.real.RootProbabilityCrossoverStrategy() {
                  setLambda(controlparameter.ConstantControlParameter.of(0.5))
                }
              }
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
              setVelocityProvider(new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider())
            }
          }
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.8))
        }
      }
    }
  }
}

def xpso_nmpco = new pso.PSO() { // Novel Multi Parent Crossover Operator PSO
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(100)
      setEntityType(new pso.particle.StandardParticle())
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.GBestMutationIterationStrategy() {
      setDelegate {
        new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
          setCrossoverOperation {
            new pso.crossover.operations.MultiParentCrossoverOperation() {
              setCrossover {
                new pso.crossover.ParticleCrossoverStrategy() {
                  setCrossoverStrategy(new entity.operators.crossover.real.MultiParentCrossoverStrategy())
                }
              }
              setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.8))
              setParentReplacementStrategy(new pso.crossover.parentupdate.ElitistParentReplacementStrategy())
              setSelector(new util.selection.recipes.RandomSelector())
              setRandom(new math.random.UniformDistribution())
            }
          }
        }
      }
      setVMax(controlparameter.ConstantControlParameter.of(1.0))
      setDistribution(new math.random.CauchyDistribution())
    }
  }
}

def xpso_nmpco_without_cauchy = new pso.PSO() { // Novel Multi Parent Crossover Operator PSO without Cauchy Mutation
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(100)
      setEntityType(new pso.particle.StandardParticle())
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.MultiParentCrossoverOperation() {
          setCrossover {
            new pso.crossover.ParticleCrossoverStrategy() {
              setCrossoverStrategy(new entity.operators.crossover.real.MultiParentCrossoverStrategy())
            }
          }
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.8))
          setParentReplacementStrategy(new pso.crossover.parentupdate.ElitistParentReplacementStrategy())
          setSelector(new util.selection.recipes.RandomSelector())
          setRandom(new math.random.UniformDistribution())
        }
      }
    }
  }
}

def xpso_hea = new pso.PSO() { // Hybrid Evolutionary Algorithm PSO
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              controlparameter.ConstantControlParameter.of(0.6),
              controlparameter.ConstantControlParameter.of(1.0),
              controlparameter.ConstantControlParameter.of(1.0)
            )
          }
        }
      }
    }
  }
  setIterationStrategy {
    new pso.iterationstrategies.HybridEAIterationStrategy() {
      setCrossoverStrategy {
        new entity.operators.CrossoverOperator() {
          setCrossoverStrategy {
            new entity.operators.crossover.real.BlendCrossoverStrategy() {
              setAlpha(controlparameter.ConstantControlParameter.of(1.6))
            }
          }
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.1))
          setSelectionStrategy {
            new util.selection.recipes.RouletteWheelSelector() {
              setWeighing {
                new util.selection.weighting.EntityWeighting() {
                  setEntityFitness(new util.selection.weighting.CurrentFitness())
                }
              }
            }
          }
        }
      }
      setSelector(new util.selection.recipes.ElitistSelector())
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
runner(simulation(xpso_lovbjerg_hybrid, spherical), fitness, "data/xpso_lovbjerg_hybrid_spherical.txt", 10, 10)
runner(simulation(xpso_gliw_glac_arithmetic, spherical), fitness, "data/xpso_gliw_glac_arithmetic_spherical.txt", 10, 10)
runner(simulation(xpso_gliw_fac_arithmetic, spherical), fitness, "data/xpso_gliw_fac_arithmetic_spherical.txt", 10, 10)
runner(simulation(xpso_gliw_glac_averageConvex, spherical), fitness, "data/xpso_gliw_glac_averageConvex_spherical.txt", 10, 10)
runner(simulation(xpso_gliw_fac_averageConvex, spherical), fitness, "data/xpso_gliw_fac_averageConvex_spherical.txt", 10, 10)
runner(simulation(xpso_gliw_glac_rootProbability, spherical), fitness, "data/xpso_gliw_glac_rootProbability_spherical.txt", 10, 10)
runner(simulation(xpso_gliw_fac_rootProbability, spherical), fitness, "data/xpso_gliw_fac_rootProbability_spherical.txt", 10, 10)
runner(simulation(xpso_nmpco, spherical), fitness, "data/xpso_nmpco_spherical.txt", 10, 10)
runner(simulation(xpso_nmpco_without_cauchy, spherical), fitness, "data/xpso_nmpco_without_cauchy_spherical.txt", 10, 10)
runner(simulation(xpso_hea, spherical), fitness, "data/xpso_hea_spherical.txt", 10, 10)

