import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def psoAlg = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setPersonalBestUpdateStrategy(new pso.pbestupdate.BoundedRelaxedNonDominatedPersonalBestUpdateStrategy())
          setPositionProvider(new pso.positionprovider.StandardPositionProvider())
          setVelocityProvider(new pso.velocityprovider.MOVelocityProvider())
          setLocalGuideProvider(new pso.guideprovider.PBestGuideProvider())
          setGlobalGuideProvider {
            new pso.guideprovider.VEPSOGuideProvider() {
              setKnowledgeTransferStrategy {
                new algorithm.population.knowledgetransferstrategies.SelectiveKnowledgeTransferStrategy() {
                  setPopulationSelection(new util.selection.recipes.RingBasedPopulationSelector())
                  setEntitySelection(new util.selection.recipes.ElitistSelector())
                }
              }
            }
          }
        }
      }
    }
  }
  setIterationStrategy {
    new moo.iterationstrategies.ArchivingIterationStrategy[pso.PSO]() {
      setArchive {
        new moo.archive.constrained.SetBasedConstrainedArchive() {
          setCapacity(10000)
        }
      }
      setIterationStrategy {
        new pso.iterationstrategies.moo.DominantMOOSynchronousIterationStrategy() {
          setBoundaryConstraint(new problem.boundaryconstraint.ClampingBoundaryConstraint())
        }
      }
    }
  }
}

def vepso = {
  new algorithm.population.RespondingMultiPopulationCriterionBasedAlgorithm() {
    addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
    setObjectiveAssignmentStrategy(new moo.criterion.objectiveassignmentstrategies.SequentialObjectiveAssignmentStrategy())
    addPopulationBasedAlgorithm(psoAlg)
    addPopulationBasedAlgorithm(psoAlg)

    setIterationStrategy {
      new moo.iterationstrategies.HigherLevelArchivingIterationStrategy() {
        setArchive {
          new moo.archive.constrained.SetBasedConstrainedArchive() {
            setCapacity(100)
            setPruningSelection {
              new util.selection.recipes.DistanceBasedElitistSelector() {
                setComparator(new util.selection.arrangement.DistanceComparator().asInstanceOf[java.util.Comparator[Nothing]])
              }.asInstanceOf[util.selection.recipes.Selector[problem.solution.OptimisationSolution]]
            }
          }
        }
        setIterationStrategy {
          new pso.dynamic.HigherLevelChangedDynamicIterationStrategy() {
            setIterationStrategy {
              new pso.dynamic.HigherLevelChangedDynamicIterationStrategy() {
                setIterationStrategy {
                  new pso.iterationstrategies.moo.DominantMOOSynchronousIterationStrategy() {
                    setBoundaryConstraint(new problem.boundaryconstraint.ClampingBoundaryConstraint())
                  }
                }
              }
            }

            setDetectionStrategy {
              new pso.dynamic.detectionstrategies.MOORandomArchiveSentriesDetectionStrategy() {
                setIterationsModulus(1)
                setNumberOfSentries(controlparameter.ConstantControlParameter.of(2))
              }
            }

            setResponseStrategy {
              new pso.dynamic.responsestrategies.MultiReactionStrategy() {
                addResponseStrategy(new pso.dynamic.responsestrategies.ArchiveChangeSeverityResponseStrategy())
                addResponseStrategy {
                  new pso.dynamic.responsestrategies.ReinitialisationReevaluationReactionStrategy() {
                    setReinitialisationRatio(0.3)
                  }
                }
              }
            }
          }
        }
      }.asInstanceOf[algorithm.population.IterationStrategy[algorithm.population.MultiPopulationBasedAlgorithm]]
    }
  }
}

//problems
def dmop2_f1 = new problem.FunctionOptimisationProblem() {
  setDomain("R(0:1)")
  setFunction(new functions.continuous.dynamic.moo.dmop2.DMOP2_f1())
}

def dmop2_g = new problem.FunctionOptimisationProblem() {
  setDomain("R(0:1)^9")
  setFunction(new functions.continuous.dynamic.moo.dmop2.DMOP2_g() { setN_t(10); setTau_t(10) })
}

def dmop2_h = new problem.FunctionOptimisationProblem() {
  setDomain("R(0:1)^10")
  setFunction {
    new functions.continuous.dynamic.moo.dmop2.DMOP2_h() {
      setN_t(10); setTau_t(10)
      setDMOP2_g(dmop2_g)
      setDMOP2_f(dmop2_f1)
    }
  }
}

def dmop2_gh = new problem.FunctionOptimisationProblem() {
  setDomain("R(0:1)^10")
  setFunction {
    new functions.continuous.dynamic.moo.dmop2.DMOP2_f2() {
      setDMOP2_g(dmop2_g)
      setDMOP2_h(dmop2_h)
    }
  }
}

def moo_problem = new problem.MOOptimisationProblem() {
  add(dmop2_f1)
  add(dmop2_gh)
}

//measurements
def moo_quality = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.moo.NumberOfSolutions())
  addMeasurement(new measurement.multiple.moo.ParetoOptimalFront())
}

runner(simulation(vepso, moo_problem), moo_quality, "data/dvepso-DMOP2.txt", 5, 1)

