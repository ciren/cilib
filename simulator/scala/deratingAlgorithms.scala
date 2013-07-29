import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def snt_pso = new niching.SequentialNichingTechnique() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() {
    setTarget(10)
  })

  setAlgorithm(new pso.PSO() {
    addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition(){
      setTarget(500)
    })
  })
}

def snt_ga = new niching.SequentialNichingTechnique() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() {
    setTarget(10)
  })

  setAlgorithm(new ec.EC() {
    addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() {
      setTarget(500)
    })
  })
}

def deratingNichePSO = new niching.NichingAlgorithm() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() {
    setTarget(10)
  })
  setIterationStrategy(new niching.iterationstrategies.DeratingNichePSO())

  setMainSwarm(new pso.PSO() {
    addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition(){
      setTarget(500)
    })
    setIterationStrategy(new pso.iterationstrategies.SynchronousIterationStrategy() {
      setBoundaryConstraint(new problem.boundaryconstraint.ReinitialisationBoundary())
    })
  })

  setNicheCreator {
    new niching.creation.ClosestNeighbourNicheCreationStrategy() {
      setSwarmType {
        new pso.PSO() {
          setIterationStrategy {
            new pso.iterationstrategies.SynchronousIterationStrategy() {
              setBoundaryConstraint(new problem.boundaryconstraint.ReinitialisationBoundary())
            }
          }
        }
      }
    }
  }

  setNicheDetector {
    new niching.creation.MaintainedFitnessNicheDetection() {
      setThreshold(controlparameter.ConstantControlParameter.of(1e-12))
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

  setMainSwarmIterator(new niching.iterators.SingleNicheIteration())
  setSubSwarmIterator {
    new niching.iterators.AllSwarmsIterator() {
      setIterator(new niching.iterators.SingleNicheIteration())
    }
  }
}

//problems
def f1 = new problem.DeratingOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction1())
  setObjective(new problem.objective.Maximise())
}

def f2 = new problem.DeratingOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction2())
  setObjective(new problem.objective.Maximise())
}

def f3 = new problem.DeratingOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction3())
  setObjective(new problem.objective.Maximise())
}

def f4 = new problem.DeratingOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction4())
  setObjective(new problem.objective.Maximise())
}

def f5 = new problem.DeratingOptimisationProblem() {
  setDomain("R(-5:5)^2")
  setFunction(new functions.continuous.unconstrained.MultimodalFunction5())
  setObjective(new problem.objective.Maximise())
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.multiple.MultipleSolutions())
  addMeasurement(new measurement.multiple.MultipleFitness())
}

def composite = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.SwarmSize())
  addMeasurement {
    val m = new measurement.multiple.CompositeMeasurement()
    m.addMeasurement(new measurement.single.SwarmSize())
    m.addMeasurement(new measurement.single.Solution())
    m
  }
}

//simulations
runner(simulation(deratingNichePSO, f1), composite, "data/deratingNichePSO-f1.txt", 10, 10)
runner(simulation(deratingNichePSO, f2), composite, "data/deratingNichePSO-f2.txt", 10, 10)
runner(simulation(deratingNichePSO, f3), composite, "data/deratingNichePSO-f3.txt", 10, 10)
runner(simulation(deratingNichePSO, f4), composite, "data/deratingNichePSO-f4.txt", 10, 10)
runner(simulation(deratingNichePSO, f5), composite, "data/deratingNichePSO-f5.txt", 10, 10)

runner(simulation(snt_pso, f1), fitness, "data/snt_pso-f1.txt", 10, 10)
runner(simulation(snt_pso, f2), fitness, "data/snt_pso-f2.txt", 10, 10)
runner(simulation(snt_pso, f3), fitness, "data/snt_pso-f3.txt", 10, 10)
runner(simulation(snt_pso, f4), fitness, "data/snt_pso-f4.txt", 10, 10)
runner(simulation(snt_pso, f5), fitness, "data/snt_pso-f5.txt", 10, 10)

runner(simulation(snt_ga, f1), fitness, "data/snt_ga-f1.txt", 10, 10)
runner(simulation(snt_ga, f2), fitness, "data/snt_ga-f2.txt", 10, 10)
runner(simulation(snt_ga, f3), fitness, "data/snt_ga-f3.txt", 10, 10)
runner(simulation(snt_ga, f4), fitness, "data/snt_ga-f4.txt", 10, 10)
runner(simulation(snt_ga, f5), fitness, "data/snt_ga-f5.txt", 10, 10)

