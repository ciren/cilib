import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def alg = new pso.PSO() { //gbest pso
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

//problems
def spherical = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Spherical())
  setDomain("R(-5.12:5.12)^30")
}

//measurements
def pos = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.ParticlePositions())
}

//simulations
runner(simulation(alg, spherical), pos, "data/pso-positions.txt", 10, 100)

