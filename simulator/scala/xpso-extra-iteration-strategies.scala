import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def parentPbest = new pso.crossover.pbestupdate.IdentityOffspringPBestProvider()
def currentPos = new pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider()
def randomPos = new pso.crossover.pbestupdate.RandomOffspringPBestProvider()
def pcxPos = new pso.crossover.pbestupdate.CrossoverOffspringPBestProvider()
def noisyParentPbest = new pso.crossover.pbestupdate.NoisyPositionOffspringPBestProvider() {
  setDelegate(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
}

def parentVel = new pso.crossover.velocityprovider.IdentityOffspringVelocityProvider()
def zeroVel = new pso.crossover.velocityprovider.ZeroOffspringVelocityProvider()
def randomVel = new pso.crossover.velocityprovider.RandomOffspringVelocityProvider()
def worstParentVel = new pso.crossover.velocityprovider.WorstParentOffspringVelocityProvider()

def xpso_pcx_repeating(p: pso.crossover.pbestupdate.OffspringPBestProvider, v: pso.crossover.velocityprovider.OffspringVelocityProvider) = {
  new pso.PSO() {
    addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
    setIterationStrategy {
      new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
        setCrossoverOperation {
          new pso.crossover.operations.RepeatingCrossoverSelection() {
            setCrossoverStrategy {
              new pso.crossover.ParticleCrossoverStrategy() {
                setCrossoverStrategy {
                  new entity.operators.crossover.real.ParentCentricCrossoverStrategy() {
                    setNumberOfParents(controlparameter.ConstantControlParameter.of(3))
                  }
                }
                setPbestProvider(p)
                setVelocityProvider(v)
              }
            }
            setRetries(controlparameter.ConstantControlParameter.of(10))
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

runner(simulation(xpso_pcx_repeating(parentPbest, parentVel), spherical), fitness, "data/xpso_pcx_repeating_parent_parent-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(currentPos, parentVel), spherical), fitness, "data/xpso_pcx_repeating_current_parent-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(randomPos, parentVel), spherical), fitness, "data/xpso_pcx_repeating_random_parent-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(pcxPos, parentVel), spherical), fitness, "data/xpso_pcx_repeating_pcx_parent-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(noisyParentPbest, parentVel), spherical), fitness, "data/xpso_pcx_repeating_noisyParent_parent-spherical.txt", 5, 5)

runner(simulation(xpso_pcx_repeating(parentPbest, zeroVel), spherical), fitness, "data/xpso_pcx_repeating_parent_zero-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(currentPos, zeroVel), spherical), fitness, "data/xpso_pcx_repeating_current_zero-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(randomPos, zeroVel), spherical), fitness, "data/xpso_pcx_repeating_random_zero-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(pcxPos, zeroVel), spherical), fitness, "data/xpso_pcx_repeating_pcx_zero-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(noisyParentPbest, zeroVel), spherical), fitness, "data/xpso_pcx_repeating_noisyParent_zero-spherical.txt", 5, 5)

runner(simulation(xpso_pcx_repeating(parentPbest, randomVel), spherical), fitness, "data/xpso_pcx_repeating_parent_random-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(currentPos, randomVel), spherical), fitness, "data/xpso_pcx_repeating_current_random-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(randomPos, randomVel), spherical), fitness, "data/xpso_pcx_repeating_random_random-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(pcxPos, randomVel), spherical), fitness, "data/xpso_pcx_repeating_pcx_random-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(noisyParentPbest, randomVel), spherical), fitness, "data/xpso_pcx_repeating_noisyParent_random-spherical.txt", 5, 5)

runner(simulation(xpso_pcx_repeating(parentPbest, worstParentVel), spherical), fitness, "data/xpso_pcx_repeating_parent_worstParent-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(currentPos, worstParentVel), spherical), fitness, "data/xpso_pcx_repeating_current_worstParent-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(randomPos, worstParentVel), spherical), fitness, "data/xpso_pcx_repeating_random_worstParent-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(pcxPos, worstParentVel), spherical), fitness, "data/xpso_pcx_repeating_pcx_worstParent-spherical.txt", 5, 5)
runner(simulation(xpso_pcx_repeating(noisyParentPbest, worstParentVel), spherical), fitness, "data/xpso_pcx_repeating_noisyParent_worstParent-spherical.txt", 5, 5)

