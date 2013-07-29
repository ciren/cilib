import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def sequentialVBPSO = new niching.NichingAlgorithm() {
  addStoppingCondition {
    new stoppingcondition.MeasuredStoppingCondition() {
      setTarget(0)
      setPredicate(new stoppingcondition.Minimum())
      setMeasurement(new measurement.single.SwarmSize())
    }
  }

  setNicheCreator {
    new niching.creation.VectorBasedNicheCreationStrategy() {
      setSwarmBehavior {
        new pso.particle.ParticleBehavior() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              controlparameter.ConstantControlParameter.of(0.8),
              controlparameter.ConstantControlParameter.of(1.0),
              controlparameter.ConstantControlParameter.of(1.0)
            )
          }
        }
      }
      setSwarmType {
        new pso.PSO() {
          addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() { setTarget(500) })
        }
      }
    }
  }

  setNicheDetector(new niching.creation.NPerIterationNicheDetection() { setN(1) })
  setSubSwarmIterator {
    new niching.iterators.FirstSwarmIterator() {
      setIterator(new niching.iterators.CompleteNicheIteration())
    }
  }

  setIterationStrategy(new niching.iterationstrategies.VectorBasedPSO())
}

def parallelVBPSO = new niching.NichingAlgorithm() {
  addStoppingCondition {
    new stoppingcondition.MeasuredStoppingCondition() {
      setTarget(10)
    }
  }

  setNicheCreator {
    new niching.creation.VectorBasedNicheCreationStrategy() {
      setSwarmBehavior {
        new pso.particle.ParticleBehavior() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              controlparameter.ConstantControlParameter.of(0.8),
              controlparameter.ConstantControlParameter.of(1.0),
              controlparameter.ConstantControlParameter.of(1.0)
            )
          }
        }
      }
      setSwarmType {
        new pso.PSO() {
          addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
        }
      }
    }
  }

  setNicheDetector(new niching.creation.AlwaysTrueNicheDetection())
  setSubSwarmIterator {
    new niching.iterators.MergingSubswarmIterator() {
      setIterator(new niching.iterators.SingleNicheIteration())
      setGranularity(controlparameter.ConstantControlParameter.of(0.01))
      setIterations(controlparameter.ConstantControlParameter.of(10))
    }
  }

  setIterationStrategy(new niching.iterationstrategies.VectorBasedPSO())
}

def enhancedVBPSO = new niching.NichingAlgorithm() {
  addStoppingCondition {
    new stoppingcondition.MeasuredStoppingCondition() {
      setTarget(10)
    }
  }

  setNicheCreator {
    new niching.creation.VectorBasedNicheCreationStrategy() {
      setSwarmBehavior {
        new pso.particle.ParticleBehavior() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              controlparameter.ConstantControlParameter.of(0.8),
              controlparameter.ConstantControlParameter.of(1.0),
              controlparameter.ConstantControlParameter.of(1.0)
            )
          }
          setPositionProvider {
            new pso.positionprovider.VectorBasedPositionProvider() {
              setGranularity(controlparameter.ConstantControlParameter.of(0.01))
            }
          }
        }
      }
      setSwarmType {
        new pso.PSO() {
          addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
        }
      }
    }
  }

  setNicheDetector(new niching.creation.AlwaysTrueNicheDetection())
  setSubSwarmIterator {
    new niching.iterators.MergingSubswarmIterator() {
      setIterator(new niching.iterators.SingleNicheIteration())
      setGranularity(controlparameter.ConstantControlParameter.of(0.01))
      setIterations(controlparameter.ConstantControlParameter.of(10))
    }
  }

  setIterationStrategy(new niching.iterationstrategies.VectorBasedPSO())
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

def solution = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.FitnessEvaluations())
  addMeasurement {
    val m = new measurement.multiple.CompositeMeasurement()
    m.addMeasurement(new measurement.single.SwarmSize())
    m.addMeasurement(new measurement.single.Solution())
    m
  }
  addMeasurement(new measurement.single.SwarmSize())
}

runner(simulation(sequentialVBPSO, f1), solution, "data/sequentialVBPSO-f1.txt", 1, 10)
runner(simulation(sequentialVBPSO, f2), solution, "data/sequentialVBPSO-f2.txt", 1, 10)
runner(simulation(sequentialVBPSO, f3), solution, "data/sequentialVBPSO-f3.txt", 1, 10)
runner(simulation(sequentialVBPSO, f4), solution, "data/sequentialVBPSO-f4.txt", 1, 10)
runner(simulation(sequentialVBPSO, f5), solution, "data/sequentialVBPSO-f5.txt", 1, 10)
runner(simulation(parallelVBPSO, f1), solution, "data/parallelVBPSO-f1.txt", 1, 10)
runner(simulation(parallelVBPSO, f2), solution, "data/parallelVBPSO-f2.txt", 1, 10)
runner(simulation(parallelVBPSO, f3), solution, "data/parallelVBPSO-f3.txt", 1, 10)
runner(simulation(parallelVBPSO, f4), solution, "data/parallelVBPSO-f4.txt", 1, 10)
runner(simulation(parallelVBPSO, f5), solution, "data/parallelVBPSO-f5.txt", 1, 10)
runner(simulation(enhancedVBPSO, f1), solution, "data/enhancedVBPSO-f1.txt", 1, 10)
runner(simulation(enhancedVBPSO, f2), solution, "data/enhancedVBPSO-f2.txt", 1, 10)
runner(simulation(enhancedVBPSO, f3), solution, "data/enhancedVBPSO-f3.txt", 1, 10)
runner(simulation(enhancedVBPSO, f4), solution, "data/enhancedVBPSO-f4.txt", 1, 10)
runner(simulation(enhancedVBPSO, f5), solution, "data/enhancedVBPSO-f5.txt", 1, 10)

