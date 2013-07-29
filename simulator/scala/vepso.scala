import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def pso_sel(sel: util.selection.recipes.Selector[algorithm.population.SinglePopulationBasedAlgorithm[_ <: entity.Entity]]) = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setLocalGuideProvider(new pso.guideprovider.PBestGuideProvider())
          setGlobalGuideProvider {
            new pso.guideprovider.VEPSOGuideProvider() {
              setKnowledgeTransferStrategy {
                new algorithm.population.knowledgetransferstrategies.SelectiveKnowledgeTransferStrategy() {
                  setPopulationSelection(sel)
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
        new pso.iterationstrategies.SynchronousIterationStrategy() {
          setBoundaryConstraint(new problem.boundaryconstraint.ClampingBoundaryConstraint())
        }.asInstanceOf[algorithm.population.IterationStrategy[pso.PSO]]
      }
    }
  }
}

def pso_standard = () => pso_sel(new util.selection.recipes.RingBasedPopulationSelector())
def pso_rand = () => pso_sel(new util.selection.recipes.RandomSelector())

def vepso(alg: () => pso.PSO, count: Int) = {
  new algorithm.population.MultiPopulationCriterionBasedAlgorithm() {
    addStoppingCondition {
      new stoppingcondition.MeasuredStoppingCondition() {
        setTarget(250)
      }
    }
    setObjectiveAssignmentStrategy(new moo.criterion.objectiveassignmentstrategies.SequentialObjectiveAssignmentStrategy())
    (1 to count) foreach { _ => addPopulationBasedAlgorithm(alg()) }
  }
}

//problems
def zdt1 = new functions.continuous.moo.zdt.ZDT1()
def zdt2 = new functions.continuous.moo.zdt.ZDT2()
def zdt3 = new functions.continuous.moo.zdt.ZDT3()
def zdt4 = new functions.continuous.moo.zdt.ZDT4()
def zdt6 = new functions.continuous.moo.zdt.ZDT6()

def wfg1 = new functions.continuous.moo.wfg.WFG1() { setM(3); setL(20) }
def wfg2 = new functions.continuous.moo.wfg.WFG2() { setM(3); setL(20) }
def wfg3 = new functions.continuous.moo.wfg.WFG3() { setM(3); setL(20) }
def wfg4 = new functions.continuous.moo.wfg.WFG4() { setM(3); setL(20) }
def wfg5 = new functions.continuous.moo.wfg.WFG5() { setM(3); setL(20) }
def wfg6 = new functions.continuous.moo.wfg.WFG6() { setM(3); setL(20) }
def wfg7 = new functions.continuous.moo.wfg.WFG7() { setM(3); setL(20) }
def wfg8 = new functions.continuous.moo.wfg.WFG8() { setM(3); setL(20) }
def wfg9 = new functions.continuous.moo.wfg.WFG9() { setM(3); setL(20) }

//measurements
def moo_quality = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.moo.NumberOfSolutions())
  addMeasurement(new measurement.single.moo.SolutionDistribution())
  addMeasurement(new measurement.single.moo.ParetoFrontExtent())
  addMeasurement(new measurement.multiple.moo.ParetoOptimalFront())
}

//simulations
List(pso_standard -> "standard", pso_rand -> "random") foreach {
  a => {
    runner(simulation(vepso(a._1, 2), zdt1), moo_quality, "data/vepso-" + a._2 + "-bi-zdt1.txt", 5, 1)
    runner(simulation(vepso(a._1, 2), zdt2), moo_quality, "data/vepso-" + a._2 + "-bi-zdt2.txt", 5, 1)
    runner(simulation(vepso(a._1, 2), zdt3), moo_quality, "data/vepso-" + a._2 + "-bi-zdt3.txt", 5, 1)
    runner(simulation(vepso(a._1, 2), zdt4), moo_quality, "data/vepso-" + a._2 + "-bi-zdt4.txt", 5, 1)
    runner(simulation(vepso(a._1, 2), zdt6), moo_quality, "data/vepso-" + a._2 + "-bi-zdt6.txt", 5, 1)

    runner(simulation(vepso(a._1, 3), wfg1), moo_quality, "data/vepso-" + a._2 + "-tri-wfg1.txt", 5, 1)
    runner(simulation(vepso(a._1, 3), wfg2), moo_quality, "data/vepso-" + a._2 + "-tri-wfg2.txt", 5, 1)
    runner(simulation(vepso(a._1, 3), wfg3), moo_quality, "data/vepso-" + a._2 + "-tri-wfg3.txt", 5, 1)
    runner(simulation(vepso(a._1, 3), wfg4), moo_quality, "data/vepso-" + a._2 + "-tri-wfg4.txt", 5, 1)
    runner(simulation(vepso(a._1, 3), wfg5), moo_quality, "data/vepso-" + a._2 + "-tri-wfg5.txt", 5, 1)
    runner(simulation(vepso(a._1, 3), wfg6), moo_quality, "data/vepso-" + a._2 + "-tri-wfg6.txt", 5, 1)
    runner(simulation(vepso(a._1, 3), wfg7), moo_quality, "data/vepso-" + a._2 + "-tri-wfg7.txt", 5, 1)
    runner(simulation(vepso(a._1, 3), wfg8), moo_quality, "data/vepso-" + a._2 + "-tri-wfg8.txt", 5, 1)
    runner(simulation(vepso(a._1, 3), wfg9), moo_quality, "data/vepso-" + a._2 + "-tri-wfg9.txt", 5, 1)
  }
}

