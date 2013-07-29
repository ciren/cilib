import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def predprey = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.SpecialisedPopulationInitialisationStrategy() {
      setEntityTypes(new pso.particle.StandardParticle() {
        setVelocityProvider(new pso.velocityprovider.PredatorVelocityProvider())
      }, 1)
      setEntityTypes(new pso.particle.StandardParticle() {
          setVelocityProvider {new pso.velocityprovider.PreyVelocityProvider() {
            setDelegate {
              new pso.velocityprovider.StandardVelocityProvider(
                new controlparameter.LinearlyVaryingControlParameter(0.5, 0.0),
                controlparameter.ConstantControlParameter.of(2.0),
                controlparameter.ConstantControlParameter.of(2.0)
              )
            }
            setA(controlparameter.ConstantControlParameter.of(1.0))
            setB(controlparameter.ConstantControlParameter.of(1.0))
            setFear(controlparameter.ConstantControlParameter.of(0.0005))
          }
        }
      }, 20)
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
runner(simulation(predprey, spherical), fitness, "data/predprey_spherical.txt", 10, 10)

