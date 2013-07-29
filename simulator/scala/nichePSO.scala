import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def nichePSO = new niching.NichingAlgorithm() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())

  setMainSwarm {
    new pso.PSO() { // cognitive only pso
      setInitialisationStrategy {
        new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
          setEntityNumber(30)
          setEntityType {
            new pso.particle.StandardParticle() {
              setVelocityProvider {
                new pso.velocityprovider.StandardVelocityProvider(
                  controlparameter.ConstantControlParameter.of(0.72),
                  controlparameter.ConstantControlParameter.of(0.0),
                  controlparameter.ConstantControlParameter.of(1.2)
                )
              }
            }
          }
        }
      }
      setIterationStrategy {
        new pso.iterationstrategies.SynchronousIterationStrategy() {
          setBoundaryConstraint(new problem.boundaryconstraint.ClampingBoundaryConstraint())
        }
      }
    }
  }

  setNicheCreator {
    new niching.creation.ClosestNeighbourNicheCreationStrategy() {
      setSwarmType {
        new pso.PSO() {
          setIterationStrategy {
            new pso.iterationstrategies.SynchronousIterationStrategy() {
              setBoundaryConstraint(new problem.boundaryconstraint.ClampingBoundaryConstraint())
            }
          }
        }
      }
      setSwarmBehavior {
        new pso.particle.ParticleBehavior() {
          setVelocityProvider {
            new pso.velocityprovider.GCVelocityProvider() {
              setRho(controlparameter.ConstantControlParameter.of(0.001)) // very problem dependant
              setRhoExpandCoefficient(controlparameter.ConstantControlParameter.of(2.0))
              setRhoContractCoefficient(controlparameter.ConstantControlParameter.of(0.5))
              setDelegate {
                new pso.velocityprovider.ClampingVelocityProvider() {
                  setVMax(controlparameter.ConstantControlParameter.of(1.0))
                  setDelegate {
                    new pso.velocityprovider.StandardVelocityProvider(
                      new controlparameter.LinearlyVaryingControlParameter(0.7, 0.2),
                      controlparameter.ConstantControlParameter.of(1.2),
                      controlparameter.ConstantControlParameter.of(1.2)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  setNicheDetector {
    new niching.creation.MaintainedFitnessNicheDetection() {
      setThreshold(controlparameter.ConstantControlParameter.of(1e-4))
      setStationaryCounter(controlparameter.ConstantControlParameter.of(3))
    }
  }

  setMergeDetector {
    new niching.merging.detection.RadiusOverlapMergeDetection() {
      setThreshold(controlparameter.ConstantControlParameter.of(1e-12))
    }
  }

  setAbsorptionDetector {
    new niching.merging.detection.RadiusOverlapMergeDetection() {
      setThreshold(controlparameter.ConstantControlParameter.of(1e-12))
    }
  }

  setMainSwarmMerger(new niching.merging.SingleSwarmMergeStrategy())
  setMainSwarmAbsorber(new niching.merging.SingleSwarmMergeStrategy())
  setMainSwarmCreationMerger(new niching.merging.SingleSwarmMergeStrategy())

  setSubSwarmMerger(new niching.merging.StandardMergeStrategy())
  setSubSwarmAbsorber(new niching.merging.StandardMergeStrategy())

  setSubSwarmIterator {
    new niching.iterators.AllSwarmsIterator() {
      setIterator(new niching.iterators.SingleNicheIteration())
    }
  }

  setMainSwarmIterator(new niching.iterators.SingleNicheIteration())
  setIterationStrategy(new niching.iterationstrategies.NichePSO())
}

def scatterMerge = {
  val n = nichePSO
  n.setMainSwarmMerger(new niching.merging.ScatterMergeStrategy())
  n.setSubSwarmMerger(new niching.merging.ScatterMergeStrategy())
  n
}

def directionalMerge = {
  val n = nichePSO
  n.setMergeDetector {
    new niching.merging.detection.CompositeMergeDetection() {
      addDetector {
        new niching.merging.detection.RadiusOverlapMergeDetection() {
          setThreshold(controlparameter.ConstantControlParameter.of(1e-12))
        }
      }
      addDetector(new niching.merging.detection.VectorBasedMergeDetection())
    }
  }
  n
}

def directionalAbsorption = {
  val n = nichePSO
  n.setMergeDetector {
    new niching.merging.detection.CompositeMergeDetection() {
      addDetector {
        new niching.merging.detection.RadiusOverlapMergeDetection() {
          setThreshold(controlparameter.ConstantControlParameter.of(1e-12))
        }
      }
      addDetector(new niching.merging.detection.VectorBasedMergeDetection())
    }
  }
  n
}

def diversityAbsorption = {
  val n = nichePSO
  n.setAbsorptionDetector {
    new niching.merging.detection.CompositeMergeDetection() {
      addDetector {
        new niching.merging.detection.RadiusOverlapMergeDetection() {
          setThreshold(controlparameter.ConstantControlParameter.of(1e-12))
        }
      }
      addDetector {
        new niching.merging.detection.DiversityBasedMergeDetection() {
          setThreshold(controlparameter.ConstantControlParameter.of(0.1))
        }
      }
    }
  }
  n
}

def func(f: functions.ContinuousFunction, d: String) = new problem.FunctionOptimisationProblem() {
  setObjective(new problem.objective.Maximise())
  setDomain(d)
  setFunction(f)
}

def f1 = func(new functions.continuous.unconstrained.MultimodalFunction1(), "R(0:1)")
def f2 = func(new functions.continuous.unconstrained.MultimodalFunction2(), "R(0:1)")
def f3 = func(new functions.continuous.unconstrained.MultimodalFunction3(), "R(0:1)")
def f4 = func(new functions.continuous.unconstrained.MultimodalFunction4(), "R(0:1)")
def f5 = func(new functions.continuous.unconstrained.MultimodalFunction5(), "R(-5:5)^2")

def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.SwarmSize())
  addMeasurement {
    val m = new measurement.multiple.CompositeMeasurement()
    m.addMeasurement(new measurement.single.SwarmSize())
    m.addMeasurement(new measurement.single.Solution())
    m
  }
}

runner(simulation(nichePSO, f1), fitness, "data/nichePSO-f1.txt", 1, 10)
runner(simulation(nichePSO, f2), fitness, "data/nichePSO-f2.txt", 1, 10)
runner(simulation(nichePSO, f3), fitness, "data/nichePSO-f3.txt", 1, 10)
runner(simulation(nichePSO, f4), fitness, "data/nichePSO-f4.txt", 1, 10)
runner(simulation(nichePSO, f5), fitness, "data/nichePSO-f5.txt", 1, 10)
runner(simulation(scatterMerge, f1), fitness, "data/nichePSO-scatter-merge-f1.txt", 1, 10)
runner(simulation(scatterMerge, f2), fitness, "data/nichePSO-scatter-merge-f2.txt", 1, 10)
runner(simulation(scatterMerge, f3), fitness, "data/nichePSO-scatter-merge-f3.txt", 1, 10)
runner(simulation(scatterMerge, f4), fitness, "data/nichePSO-scatter-merge-f4.txt", 1, 10)
runner(simulation(scatterMerge, f5), fitness, "data/nichePSO-scatter-merge-f5.txt", 1, 10)
runner(simulation(directionalMerge, f1), fitness, "data/nichePSO-directional-merge-f1.txt", 1, 10)
runner(simulation(directionalMerge, f2), fitness, "data/nichePSO-directional-merge-f2.txt", 1, 10)
runner(simulation(directionalMerge, f3), fitness, "data/nichePSO-directional-merge-f3.txt", 1, 10)
runner(simulation(directionalMerge, f4), fitness, "data/nichePSO-directional-merge-f4.txt", 1, 10)
runner(simulation(directionalMerge, f5), fitness, "data/nichePSO-directional-merge-f5.txt", 1, 10)
runner(simulation(directionalAbsorption, f1), fitness, "data/nichePSO-directional-absorption-f1.txt", 1, 10)
runner(simulation(directionalAbsorption, f2), fitness, "data/nichePSO-directional-absorption-f2.txt", 1, 10)
runner(simulation(directionalAbsorption, f3), fitness, "data/nichePSO-directional-absorption-f3.txt", 1, 10)
runner(simulation(directionalAbsorption, f4), fitness, "data/nichePSO-directional-absorption-f4.txt", 1, 10)
runner(simulation(directionalAbsorption, f5), fitness, "data/nichePSO-directional-absorption-f5.txt", 1, 10)
runner(simulation(diversityAbsorption, f1), fitness, "data/nichePSO-diversity-absorption-f1.txt", 1, 10)
runner(simulation(diversityAbsorption, f2), fitness, "data/nichePSO-diversity-absorption-f2.txt", 1, 10)
runner(simulation(diversityAbsorption, f3), fitness, "data/nichePSO-diversity-absorption-f3.txt", 1, 10)
runner(simulation(diversityAbsorption, f4), fitness, "data/nichePSO-diversity-absorption-f4.txt", 1, 10)
runner(simulation(diversityAbsorption, f5), fitness, "data/nichePSO-diversity-absorption-f5.txt", 1, 10)

