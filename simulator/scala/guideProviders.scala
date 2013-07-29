import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def guide(g: pso.guideprovider.GuideProvider) = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setGlobalGuideProvider(g)
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              controlparameter.ConstantControlParameter.of(0.72),
              controlparameter.ConstantControlParameter.of(2.0),
              controlparameter.ConstantControlParameter.of(0.0)
            )
          }
        }
      }
    }
  }
}

def weighted = guide(new pso.guideprovider.WeightedGuideProvider() {
  setC1(controlparameter.ConstantControlParameter.of(1.0))
  setC2(controlparameter.ConstantControlParameter.of(2.0))
  setComponent1(new pso.guideprovider.PBestGuideProvider())
  setComponent2(new pso.guideprovider.GBestGuideProvider())
})

def xover_pbest_pos = guide(new pso.guideprovider.AlternateCrossoverGuideProvider() {
  setCrossoverStrategy {
    new entity.operators.crossover.UniformCrossoverStrategy() {
      setCrossoverPointProbabilityParameter(controlparameter.ConstantControlParameter.of(0.5))
    }
  }
  setParent1(new pso.guideprovider.PBestGuideProvider())
  setParent2(new pso.guideprovider.CurrentPositionGuideProvider())
})

def xover_gbest_pos = guide(new pso.guideprovider.AlternateCrossoverGuideProvider() {
  setCrossoverStrategy {
    new entity.operators.crossover.UniformCrossoverStrategy() {
      setCrossoverPointProbabilityParameter(controlparameter.ConstantControlParameter.of(0.5))
    }
  }
  setParent1(new pso.guideprovider.GBestGuideProvider())
  setParent2(new pso.guideprovider.CurrentPositionGuideProvider())
})

def xover_gbest_pbest = guide(new pso.guideprovider.AlternateCrossoverGuideProvider() {
  setCrossoverStrategy {
    new entity.operators.crossover.UniformCrossoverStrategy() {
      setCrossoverPointProbabilityParameter(controlparameter.ConstantControlParameter.of(0.5))
    }
  }
  setParent1(new pso.guideprovider.GBestGuideProvider())
  setParent2(new pso.guideprovider.PBestGuideProvider())
})

def sel_tournament_perEntity_pos = guide(new pso.guideprovider.SelectionGuideProvider() {
  setSelector(new util.selection.recipes.TournamentSelector())
  setComponent(new pso.guideprovider.CurrentPositionGuideProvider())
  setPerDimension(false)
})

def sel_tournament_perEntity_pbest = guide(new pso.guideprovider.SelectionGuideProvider() {
  setSelector(new util.selection.recipes.TournamentSelector())
  setComponent(new pso.guideprovider.PBestGuideProvider())
  setPerDimension(false)
})

def sel_tournament_perDimension_pos = guide(new pso.guideprovider.SelectionGuideProvider() {
  setSelector(new util.selection.recipes.TournamentSelector())
  setComponent(new pso.guideprovider.CurrentPositionGuideProvider())
  setPerDimension(true)
})

def sel_tournament_perDimension_pbest = guide(new pso.guideprovider.SelectionGuideProvider() {
  setSelector(new util.selection.recipes.TournamentSelector())
  setComponent(new pso.guideprovider.PBestGuideProvider())
  setPerDimension(true)
})

def sel_roulette_perEntity_pos = guide(new pso.guideprovider.SelectionGuideProvider() {
  setSelector(new util.selection.recipes.RouletteWheelSelector())
  setComponent(new pso.guideprovider.CurrentPositionGuideProvider())
  setPerDimension(false)
})

def sel_roulette_perEntity_pbest = guide(new pso.guideprovider.SelectionGuideProvider() {
  setSelector(new util.selection.recipes.RouletteWheelSelector())
  setComponent(new pso.guideprovider.PBestGuideProvider())
  setPerDimension(false)
})

def sel_roulette_perDimension_pos = guide(new pso.guideprovider.SelectionGuideProvider() {
  setSelector(new util.selection.recipes.RouletteWheelSelector())
  setComponent(new pso.guideprovider.CurrentPositionGuideProvider())
  setPerDimension(true)
})

def sel_roulette_perDimension_pbest = guide(new pso.guideprovider.SelectionGuideProvider() {
  setSelector(new util.selection.recipes.RouletteWheelSelector())
  setComponent(new pso.guideprovider.PBestGuideProvider())
  setPerDimension(true)
})

//problems
def spherical = new problem.FunctionOptimisationProblem() {
  setFunction(new functions.continuous.unconstrained.Spherical())
  setDomain("R(-5.12:5.12)^30")
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

runner(simulation(weighted, spherical), fitness, "data/guide_weighted_spherical.txt", 10, 10)
runner(simulation(xover_gbest_pbest, spherical), fitness, "data/guide_xover_gbest_pbest_spherical.txt", 10, 10)
runner(simulation(xover_gbest_pos, spherical), fitness, "data/guide_xover_gbest_pos_spherical.txt", 10, 10)
runner(simulation(xover_pbest_pos, spherical), fitness, "data/guide_xover_pbest_pos_spherical.txt", 10, 10)

runner(simulation(sel_roulette_perDimension_pbest, spherical), fitness, "data/guide_sel_roulette_perDimension_pbest_spherical.txt", 10, 10)
runner(simulation(sel_roulette_perDimension_pos, spherical), fitness, "data/guide_sel_roulette_perDimension_pos_spherical.txt", 10, 10)
runner(simulation(sel_roulette_perEntity_pbest, spherical), fitness, "data/guide_sel_roulette_perEntity_pbest_spherical.txt", 10, 10)
runner(simulation(sel_roulette_perEntity_pos, spherical), fitness, "data/guide_sel_roulette_perEntity_pos_spherical.txt", 10, 10)

runner(simulation(sel_tournament_perDimension_pbest, spherical), fitness, "data/guide_sel_tournament_perDimension_pbest_spherical.txt", 10, 10)
runner(simulation(sel_tournament_perDimension_pos, spherical), fitness, "data/guide_sel_tournament_perDimension_pos_spherical.txt", 10, 10)
runner(simulation(sel_tournament_perEntity_pbest, spherical), fitness, "data/guide_sel_tournament_perEntity_pbest_spherical.txt", 10, 10)
runner(simulation(sel_tournament_perEntity_pos, spherical), fitness, "data/guide_sel_tournament_perEntity_pos_spherical.txt", 10, 10)

