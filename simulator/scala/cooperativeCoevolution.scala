import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def gaAlg = () => new ec.EC()
def psoAlg = () => new pso.PSO()

def coop_pso = new coevolution.cooperative.CooperativeCoevolutionAlgorithm() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setContributionSelectionStrategy(new coevolution.cooperative.contributionselection.SocialFitnessContributionSelectionStrategy())
  (1 to 6) foreach { _ => addPopulationBasedAlgorithm(psoAlg()) }
}

def coop_ga = new coevolution.cooperative.CooperativeCoevolutionAlgorithm() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  (1 to 6) foreach { _ => addPopulationBasedAlgorithm(gaAlg()) }
}

//problems
def griewank = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Griewank())
  setDomain("R(-600:600)^36")
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

runner(simulation(coop_pso, griewank), fitness, "data/coop_pso_griewank.txt", 10, 10)
runner(simulation(coop_ga, griewank), fitness, "data/coop_ga_griewank.txt", 10, 10)

