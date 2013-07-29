import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._
import scala.collection.JavaConversions._

def dhpsoBehaviors = List(
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.StandardVelocityProvider(
        controlparameter.ConstantControlParameter.of(0.729),
        new controlparameter.LinearlyVaryingControlParameter(2.5, 0.5),
        new controlparameter.LinearlyVaryingControlParameter(0.5, 2.5)
      )
    }
  },
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.StandardVelocityProvider(
        controlparameter.ConstantControlParameter.of(0.729),
        controlparameter.ConstantControlParameter.of(2.5),
        controlparameter.ConstantControlParameter.of(0.0)
      )
    }
  },
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.StandardVelocityProvider(
        controlparameter.ConstantControlParameter.of(0.729),
        controlparameter.ConstantControlParameter.of(0.0),
        controlparameter.ConstantControlParameter.of(2.5)
      )
    }
  },
  new pso.particle.ParticleBehavior() {
    setPositionProvider(new pso.positionprovider.LinearPositionProvider())
    setVelocityProvider {
      new pso.velocityprovider.BareBonesVelocityProvider() {
        setExploitProbability(controlparameter.ConstantControlParameter.of(0))
      }
    }
  },
  new pso.particle.ParticleBehavior() {
    setPositionProvider(new pso.positionprovider.LinearPositionProvider())
    setVelocityProvider {
      new pso.velocityprovider.BareBonesVelocityProvider() {
        setExploitProbability(controlparameter.ConstantControlParameter.of(0.5))
      }
    }
  }
)

def dhpso = new pso.PSO() {
  val behaviors = dhpsoBehaviors
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy() {
      setEntityNumber(30)
      setEntityType(new pso.particle.StandardParticle())
      setBehaviorPool(behaviors)
    }
  }
  setIterationStrategy {
    new pso.hpso.HPSOIterationStrategy() {
      setBehaviorPool(behaviors)
      setWindowSize(controlparameter.ConstantControlParameter.of(1000))
      setSelectionRecipe(new util.selection.recipes.RandomSelector())
    }
  }
}

def fkpsoBehaviors = List(
  new pso.particle.ParticleBehavior(),
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.StandardVelocityProvider(
        controlparameter.ConstantControlParameter.of(0.729),
        new controlparameter.LinearlyVaryingControlParameter(2.5, 0.0),
        new controlparameter.LinearlyVaryingControlParameter(0.0, 2.5)
      )
    }
  },
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.StandardVelocityProvider(
        new controlparameter.LinearlyVaryingControlParameter(0.9, 0.4),
        controlparameter.ConstantControlParameter.of(1.496),
        controlparameter.ConstantControlParameter.of(1.496)
      )
    }
  },
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.StandardVelocityProvider(
        controlparameter.ConstantControlParameter.of(0.729),
        controlparameter.ConstantControlParameter.of(2.5),
        controlparameter.ConstantControlParameter.of(0.0)
      )
    }
  },
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.StandardVelocityProvider(
        controlparameter.ConstantControlParameter.of(0.729),
        controlparameter.ConstantControlParameter.of(0.0),
        controlparameter.ConstantControlParameter.of(2.5)
      )
    }
  },
  new pso.particle.ParticleBehavior() {
    setPositionProvider(new pso.positionprovider.LinearPositionProvider())
    setVelocityProvider {
      new pso.velocityprovider.BareBonesVelocityProvider() {
        setExploitProbability(controlparameter.ConstantControlParameter.of(0))
      }
    }
  },
  new pso.particle.ParticleBehavior() {
    setPositionProvider(new pso.positionprovider.LinearPositionProvider())
    setVelocityProvider {
      new pso.velocityprovider.BareBonesVelocityProvider() {
        setExploitProbability(controlparameter.ConstantControlParameter.of(0.5))
      }
    }
  },
  new pso.particle.ParticleBehavior() {
    setPositionProvider(new pso.dynamic.QuantumPositionProvider())
    setVelocityProvider(new pso.dynamic.QuantumVelocityProvider())
  }
)

def fkpso = new pso.PSO() {
  val behaviors = fkpsoBehaviors
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy() {
      setDelegate {
        new algorithm.initialisation.ChargedPopulationInitialisationStrategy() {
          setEntityNumber(30)
          setEntityType(new pso.dynamic.ChargedParticle())
          setChargedRatio(controlparameter.ConstantControlParameter.of(1))
        }
      }
      setBehaviorPool(behaviors)
    }
  }
  setIterationStrategy {
    new pso.hpso.HPSOIterationStrategy() {
      setBehaviorPool(behaviors)
      setWindowSize(controlparameter.ConstantControlParameter.of(10))
      setSelectionRecipe {
        new util.selection.recipes.TournamentSelector[pso.particle.ParticleBehavior]() {
          setTournamentSize(controlparameter.ConstantControlParameter.of(0.5))
        }
      }
    }
  }
}

def dpppsoBehaviors = List(
  new pso.particle.ParticleBehavior(),
  new pso.particle.ParticleBehavior() {
    setVelocityProvider(new pso.velocityprovider.FIPSVelocityProvider())
  }
)

def dpppso = new pso.PSO() {
  val behaviors = dpppsoBehaviors
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy() {
      setEntityNumber(30)
      setEntityType(new pso.dynamic.ChargedParticle())
      setBehaviorPool(behaviors)
    }
  }
  setIterationStrategy {
    new pso.hpso.DifferenceProportionalProbabilityIterationStrategy() {
      setBehaviorPool(behaviors)
      setRigidCountPerBehavior(controlparameter.ConstantControlParameter.of(10))
      setBeta(controlparameter.ConstantControlParameter.of(0.5))
    }
  }
}

def alpsoBehaviors = List(
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.ClampingVelocityProvider() {
        setVMax(new controlparameter.DomainProportionalControlParameter() { setProportion(0.5)})
        setDelegate {
          new pso.velocityprovider.StandardVelocityProvider(
            new controlparameter.LinearlyVaryingControlParameter(0.9, 0.4),
            controlparameter.ConstantControlParameter.of(2.0),
            controlparameter.ConstantControlParameter.of(0.0)
          )
        }
      }
    }
  },
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.ClampingVelocityProvider() {
        setVMax(new controlparameter.DomainProportionalControlParameter() { setProportion(0.5)})
        setDelegate {
          new pso.velocityprovider.StandardVelocityProvider(
            new controlparameter.LinearlyVaryingControlParameter(0.9, 0.4),
            controlparameter.ConstantControlParameter.of(0.0),
            controlparameter.ConstantControlParameter.of(2.0)
          )
        }
      }
    }
  },
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.ClampingVelocityProvider() {
        setVMax(new controlparameter.DomainProportionalControlParameter() { setProportion(0.5)})
        setDelegate {
          new pso.velocityprovider.StandardVelocityProvider(
            new controlparameter.LinearlyVaryingControlParameter(0.9, 0.4),
            controlparameter.ConstantControlParameter.of(2.0),
            controlparameter.ConstantControlParameter.of(0.0)
          )
        }
      }
    }
    setGlobalGuideProvider(new pso.guideprovider.RandomGuideProvider())
  },
  new pso.particle.ParticleBehavior() {
    setVelocityProvider {
      new pso.velocityprovider.ClampingVelocityProvider() {
        setVMax(new controlparameter.DomainProportionalControlParameter() { setProportion(0.5)})
        setDelegate(new pso.velocityprovider.RandomNearbyVelocityProvider())
      }
    }
  }
)

def alpso = new pso.PSO() {
  val behaviors = alpsoBehaviors
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() { setTarget(250) } )
  setInitialisationStrategy {
    new algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy() {
      setEntityNumber(10)
      setEntityType(new pso.particle.StandardParticle())
      setBehaviorPool(behaviors)
    }
  }
  setIterationStrategy {
    new pso.hpso.AdaptiveLearningIterationStrategy() {
      setBehaviorPool(behaviors)
      setQ(controlparameter.ConstantControlParameter.of(10))
      setMinRatio(controlparameter.ConstantControlParameter.of(0.01))
    }
  }
}

def phpsoConst = new pso.PSO() {
  val behaviors = fkpsoBehaviors
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy() {
      setDelegate {
        new algorithm.initialisation.ChargedPopulationInitialisationStrategy() {
          setEntityNumber(30)
          setEntityType(new pso.dynamic.ChargedParticle())
          setChargedRatio(controlparameter.ConstantControlParameter.of(1))
        }
      }
      setBehaviorPool(behaviors)
    }
  }
  setIterationStrategy {
    new pso.hpso.PheromoneIterationStrategy() {
      setBehaviorPool(behaviors)
      setPheromoneUpdateStrategy {
        new pso.hpso.pheromoneupdate.ConstantPheromoneUpdateStrategy() {
          setBetter(controlparameter.ConstantControlParameter.of(1.0))
          setSame(controlparameter.ConstantControlParameter.of(0.5))
          setWorse(controlparameter.ConstantControlParameter.of(0.0))
        }
      }
      setDetectionStrategy {
        new pso.hpso.detectionstrategies.PersonalBestStagnationDetectionStrategy() {
          setWindowSize(controlparameter.ConstantControlParameter.of(10))
        }
      }
    }
  }
}

def phpsoLin = new pso.PSO() {
  val behaviors = fkpsoBehaviors
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy() {
      setDelegate {
        new algorithm.initialisation.ChargedPopulationInitialisationStrategy() {
          setEntityNumber(30)
          setEntityType(new pso.dynamic.ChargedParticle())
          setChargedRatio(controlparameter.ConstantControlParameter.of(1))
        }
      }
      setBehaviorPool(behaviors)
    }
  }
  setIterationStrategy {
    new pso.hpso.PheromoneIterationStrategy() {
      setBehaviorPool(behaviors)
      setPheromoneUpdateStrategy {
        new pso.hpso.pheromoneupdate.LinearPheromoneUpdateStrategy() {
          setGradient(controlparameter.ConstantControlParameter.of(0.1))
        }
      }
      setDetectionStrategy {
        new pso.hpso.detectionstrategies.PersonalBestStagnationDetectionStrategy() {
          setWindowSize(controlparameter.ConstantControlParameter.of(10))
        }
      }
    }
  }
}

//problems
def rastrigin = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Rastrigin())
  setDomain("R(-5.12:5.12)^30")
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
  addMeasurement(new measurement.single.FitnessEvaluations())
  addMeasurement(new measurement.multiple.AdaptiveHPSOBehaviorProfileMeasurement())
}

runner(simulation(dhpso, rastrigin), fitness, "data/dhpso_rastrigin.txt", 10, 10)
runner(simulation(fkpso, rastrigin), fitness, "data/fkpso_rastrigin.txt", 10, 10)
runner(simulation(dpppso, rastrigin), fitness, "data/dpppso_rastrigin.txt", 10, 10)
runner(simulation(alpso, rastrigin), fitness, "data/alpso_rastrigin.txt", 10, 10)
runner(simulation(phpsoConst, rastrigin), fitness, "data/phpsoConst_rastrigin.txt", 10, 10)
runner(simulation(phpsoLin, rastrigin), fitness, "data/phpsoLin_rastrigin.txt", 10, 10)

