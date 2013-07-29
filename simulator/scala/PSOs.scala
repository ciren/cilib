import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def async = new pso.PSO() { //asynchronous pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setIterationStrategy(new pso.iterationstrategies.ASynchronousIterationStrategy())
}

def bbpso = new pso.PSO() { //barebones pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.LinearPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.BareBonesVelocityProvider() {
              setExploitProbability(controlparameter.ConstantControlParameter.of(0))
            }
          }
        }
      }
    }
  }
}

def bbpso_exploit = new pso.PSO() { //modified barebones pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPositionProvider(new pso.positionprovider.LinearPositionProvider())
          setVelocityProvider {
            new pso.velocityprovider.BareBonesVelocityProvider() {
              setExploitProbability(controlparameter.ConstantControlParameter.of(0.5))
            }
          }
        }
      }
    }
  }
}

def constriction = new pso.PSO() { //pso with constriction coefficient
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
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
}

def fips = new pso.PSO() { // fully informed pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider(new pso.velocityprovider.FIPSVelocityProvider())
        }
      }
    }
  }
}

def gbest = new pso.PSO() { //gbest pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

def lbest = new pso.PSO() { //lbest pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setNeighbourhood(new entity.topologies.LBestNeighbourhood())
}

def vonNeumann = new pso.PSO() { // pso with von neumann topology
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setNeighbourhood(new entity.topologies.VonNeumannNeighbourhood())
}

def tviw = new pso.PSO() { // time varying inertia weight
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              new controlparameter.LinearlyVaryingControlParameter(0.9, 0.4),
              controlparameter.ConstantControlParameter.of(1.496180),
              controlparameter.ConstantControlParameter.of(1.496180)
            )
          }
        }
      }
    }
  }
}

def linearVelocity = new pso.PSO() { // one r1 and r2 for all dimensions
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setNeighbourhoodBestUpdateStrategy(new pso.positionprovider.IterationNeighbourhoodBestUpdateStrategy())
          setVelocityProvider(new pso.velocityprovider.LinearVelocityProvider())
        }
      }
    }
  }
}

def gcpso = new pso.PSO() { // guaranteed convergence pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.GCVelocityProvider() {
              setRho(controlparameter.ConstantControlParameter.of(0.1)) // very problem dependant
              setRhoExpandCoefficient(controlparameter.ConstantControlParameter.of(2.0))
              setRhoContractCoefficient(controlparameter.ConstantControlParameter.of(0.5))
            }
          }
        }
      }
    }
  }
}

def vmax = new pso.PSO() { // pso with velocity clamping
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.ClampingVelocityProvider() {
              setVMax(controlparameter.ConstantControlParameter.of(0.1))
              setDelegate(new pso.velocityprovider.StandardVelocityProvider())
            }
          }
        }
      }
    }
  }
}

def noisy = new pso.PSO() { // pso with velocity/position noise
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.NoisyVelocityProvider() {
              setDelegate(new pso.velocityprovider.StandardVelocityProvider())
            }
          }
          setPositionProvider {
            new pso.positionprovider.NoisyPositionProvider() {
              setDelegate(new pso.positionprovider.StandardPositionProvider())
            }
          }
        }
      }
    }
  }
}

def cognitive = new pso.PSO() { // cognitive only pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              controlparameter.ConstantControlParameter.of(0.72),
              controlparameter.ConstantControlParameter.of(0.0),
              controlparameter.ConstantControlParameter.of(2.0)
            )
          }
        }
      }
    }
  }
}

def social = new pso.PSO() { // social only pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              controlparameter.ConstantControlParameter.of(0.72),
              controlparameter.ConstantControlParameter.of(2.0),
              controlparameter.ConstantControlParameter.of(0.0)
            )
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
runner(simulation(async, spherical), fitness, "data/asyncPSO_spherical.txt", 10, 10)
runner(simulation(bbpso, spherical), fitness, "data/barebonesPSO_spherical.txt", 10, 10)
runner(simulation(bbpso_exploit, spherical), fitness, "data/modifiedBarebonesPSO_spherical.txt", 10, 10)
runner(simulation(constriction, spherical), fitness, "data/constrictionPSO_spherical.txt", 10, 10)
runner(simulation(fips, spherical), fitness, "data/FIPS_spherical.txt", 10, 10)
runner(simulation(gbest, spherical), fitness, "data/gbestPSO_spherical.txt", 10, 10)
runner(simulation(lbest, spherical), fitness, "data/lbestPSO_spherical.txt", 10, 10)
runner(simulation(vonNeumann, spherical), fitness, "data/vonNeumannPSO_spherical.txt", 10, 10)
runner(simulation(tviw, spherical), fitness, "data/TVIW-PSO_spherical.txt", 10, 10)
runner(simulation(linearVelocity, spherical), fitness, "data/linearPSO_spherical.txt", 10, 10)
runner(simulation(vmax, spherical), fitness, "data/vmaxPSO_spherical.txt", 10, 10)
runner(simulation(gcpso, spherical), fitness, "data/gcPSO_spherical.txt", 10, 10)
runner(simulation(noisy, spherical), fitness, "data/noisyPSO_spherical.txt", 10, 10)
runner(simulation(cognitive, spherical), fitness, "data/cognitivePSO_spherical.txt", 10, 10)
runner(simulation(social, spherical), fitness, "data/socialPSO_spherical.txt", 10, 10)

