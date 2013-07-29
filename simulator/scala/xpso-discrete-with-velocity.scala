import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def onePoint = new entity.operators.crossover.OnePointCrossoverStrategy()
def twoPoint = new entity.operators.crossover.TwoPointCrossoverStrategy()
def uniform = new entity.operators.crossover.UniformCrossoverStrategy()

def pbest = new pso.guideprovider.PBestGuideProvider()
def gbest = new pso.guideprovider.GBestGuideProvider()
def nbest = new pso.guideprovider.NBestGuideProvider()
def arithmetic = new pso.guideprovider.ArithmeticGuideProvider()

def sync(cs: entity.operators.crossover.DiscreteCrossoverStrategy, pp: pso.guideprovider.GuideProvider) = new pso.PSO() {
  setIterationStrategy {
    new pso.iterationstrategies.PSOCrossoverIterationStrategy() {
      setCrossoverOperation {
        new pso.crossover.operations.DiscreteCrossoverOperation() {
          setCrossoverStrategy {
            new pso.crossover.DiscreteVelocityCrossoverStrategy() {
              setCrossoverStrategy(cs)
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
            }
          }
          setParentProvider(pp)
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.5))
          setParentReplacementStrategy(new pso.crossover.parentupdate.AlwaysReplaceParentReplacementStrategy())
        }
      }
    }
  }
  setNeighbourhood(new entity.topologies.LBestNeighbourhood())
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

def async(cs: entity.operators.crossover.DiscreteCrossoverStrategy, pp: pso.guideprovider.GuideProvider) = new pso.PSO() {
  setIterationStrategy {
    new pso.iterationstrategies.ASynchronousIterationStrategy() {
      setAdditionalStep {
        new pso.crossover.CrossoverReplaceFunction() {
          setCrossoverStrategy {
            new pso.crossover.DiscreteVelocityCrossoverStrategy() {
              setCrossoverStrategy(cs)
              setPbestProvider(new pso.crossover.pbestupdate.IdentityOffspringPBestProvider())
            }
          }
          setParentProvider(pp)
          setCrossoverProbability(controlparameter.ConstantControlParameter.of(0.5))
          setParentReplacementStrategy(new pso.crossover.parentupdate.AlwaysReplaceParentReplacementStrategy())
        }
      }
    }
  }
  setNeighbourhood(new entity.topologies.LBestNeighbourhood())
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
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

//sync
runner(simulation(sync(onePoint, pbest), spherical), fitness, "data/xpso_discrete_1point_pbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(twoPoint, pbest), spherical), fitness, "data/xpso_discrete_2point_pbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(uniform, pbest), spherical), fitness, "data/xpso_discrete_uniform_pbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(onePoint, gbest), spherical), fitness, "data/xpso_discrete_1point_gbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(twoPoint, gbest), spherical), fitness, "data/xpso_discrete_2point_gbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(uniform, gbest), spherical), fitness, "data/xpso_discrete_uniform_gbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(onePoint, nbest), spherical), fitness, "data/xpso_discrete_1point_nbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(twoPoint, nbest), spherical), fitness, "data/xpso_discrete_2point_nbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(uniform, nbest), spherical), fitness, "data/xpso_discrete_uniform_nbest_sync-spherical.txt", 5, 5)
runner(simulation(sync(onePoint, arithmetic), spherical), fitness, "data/xpso_discrete_1point_arithmetic_sync-spherical.txt", 5, 5)
runner(simulation(sync(twoPoint, arithmetic), spherical), fitness, "data/xpso_discrete_2point_arithmetic_sync-spherical.txt", 5, 5)
runner(simulation(sync(uniform, arithmetic), spherical), fitness, "data/xpso_discrete_uniform_arithmetic_sync-spherical.txt", 5, 5)

//async
runner(simulation(async(onePoint, pbest), spherical), fitness, "data/xpso_discrete_1point_pbest_async-spherical.txt", 5, 5)
runner(simulation(async(twoPoint, pbest), spherical), fitness, "data/xpso_discrete_2point_pbest_async-spherical.txt", 5, 5)
runner(simulation(async(uniform, pbest), spherical), fitness, "data/xpso_discrete_uniform_pbest_async-spherical.txt", 5, 5)
runner(simulation(async(onePoint, gbest), spherical), fitness, "data/xpso_discrete_1point_gbest_async-spherical.txt", 5, 5)
runner(simulation(async(twoPoint, gbest), spherical), fitness, "data/xpso_discrete_2point_gbest_async-spherical.txt", 5, 5)
runner(simulation(async(uniform, gbest), spherical), fitness, "data/xpso_discrete_uniform_gbest_async-spherical.txt", 5, 5)
runner(simulation(async(onePoint, nbest), spherical), fitness, "data/xpso_discrete_1point_nbest_async-spherical.txt", 5, 5)
runner(simulation(async(twoPoint, nbest), spherical), fitness, "data/xpso_discrete_2point_nbest_async-spherical.txt", 5, 5)
runner(simulation(async(uniform, nbest), spherical), fitness, "data/xpso_discrete_uniform_nbest_async-spherical.txt", 5, 5)
runner(simulation(async(onePoint, arithmetic), spherical), fitness, "data/xpso_discrete_1point_arithmetic_async-spherical.txt", 5, 5)
runner(simulation(async(twoPoint, arithmetic), spherical), fitness, "data/xpso_discrete_2point_arithmetic_async-spherical.txt", 5, 5)
runner(simulation(async(uniform, arithmetic), spherical), fitness, "data/xpso_discrete_uniform_arithmetic_async-spherical.txt", 5, 5)

