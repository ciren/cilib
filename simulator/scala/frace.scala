import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def alg = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() { setTarget(100) })
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityProvider {
            new pso.velocityprovider.StandardVelocityProvider(
              new tuning.TuningControlParameter() { setIndex(0) },
              new tuning.TuningControlParameter() { setIndex(1) },
              new tuning.TuningControlParameter() { setIndex(2) }
            )
          }
        }
      }
    }
  }
}

def frace = new tuning.TuningAlgorithm() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() { setTarget(100) })
  setIterationStrategy {
    new tuning.IFRaceIterationStrategy() {
      setIterationStrategy {
        new tuning.FRaceIterationStrategy() {
          setMinProblems(controlparameter.ConstantControlParameter.of(1))
          setMinSolutions(controlparameter.ConstantControlParameter.of(2))
        }
      }
      setParameterChangeTrigger {
        new tuning.parameterchange.triggers.PeriodicParameterChangeTrigger() {
          setPeriod(controlparameter.ConstantControlParameter.of(10))
        }
      }
      setParameterChangeReaction {
        new tuning.parameterchange.reactions.MinMaxParameterChangeReaction() {
          setCount(controlparameter.ConstantControlParameter.of(100))
        }
      }
    }
  }
  setParameterProvider {
    new tuning.parameterlist.SobolParameterListProvider() {
      addParameterBounds {
        new tuning.parameters.TuningBounds() {
          setLowerBound(0) ; setUpperBound(2)
        }
      }
      addParameterBounds {
        new tuning.parameters.TuningBounds() {
          setLowerBound(0) ; setUpperBound(4)
        }
      }
      addParameterBounds {
        new tuning.parameters.TuningBounds() {
          setLowerBound(0) ; setUpperBound(4)
        }
      }
      setCount(100)
    }
  }
}

def frace2 = new tuning.TuningAlgorithm() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() { setTarget(100) })
  setIterationStrategy {
    new tuning.FRaceIterationStrategy() {
      setMinProblems(controlparameter.ConstantControlParameter.of(1))
      setMinSolutions(controlparameter.ConstantControlParameter.of(2))
    }
  }
  setParameterProvider {
    new tuning.parameterlist.FFDParameterListProvider() {
      addParameter {
        new tuning.parameters.DiscreteParameterGenerator() {
          addParameter(0.4) ; addParameter(0.8) ; addParameter(1.2) ; addParameter(1.6)
        }
      }
      addParameter {
        new tuning.parameters.DiscreteParameterGenerator() {
          addParameter(0.8) ; addParameter(1.6) ; addParameter(2.4) ; addParameter(3.2)
        }
      }
      addParameter {
        new tuning.parameters.DiscreteParameterGenerator() {
          addParameter(0.8) ; addParameter(1.6) ; addParameter(2.4) ; addParameter(3.2)
        }
      }
    }
  }
}

def shifted(f: functions.ContinuousFunction, l: Double, u: Double) = {
  val s = new functions.continuous.decorators.ShiftedFunctionDecorator()
  s.setHorizontalShift {
    new controlparameter.FixedControlParameter() {
      setControlParameter {
        new controlparameter.RandomControlParameter() {
          setDistribution {
            new math.random.UniformDistribution() {
              setLowerBound(controlparameter.ConstantControlParameter.of(l))
              setUpperBound(controlparameter.ConstantControlParameter.of(u))
            }
          }
        }
      }
    }
  }
  s.setFunction(f)
  s
}

def scaled(f: functions.ContinuousFunction) = {
  val s = new functions.continuous.decorators.ScaledFunctionDecorator()
  s.setVerticalScale {
    new controlparameter.FixedControlParameter() {
      setControlParameter {
        new controlparameter.RandomControlParameter() {
          setDistribution {
            new math.random.UniformDistribution() {
              setLowerBound(controlparameter.ConstantControlParameter.of(0.5))
              setUpperBound(controlparameter.ConstantControlParameter.of(1.5))
            }
          }
        }
      }
    }
  }
  s.setHorizontalScale {
    new controlparameter.FixedControlParameter() {
      setControlParameter {
        new controlparameter.RandomControlParameter() {
          setDistribution {
            new math.random.UniformDistribution() {
              setLowerBound(controlparameter.ConstantControlParameter.of(0.5))
              setUpperBound(controlparameter.ConstantControlParameter.of(1.5))
            }
          }
        }
      }
    }
  }
  s.setFunction(f)
  s
}

def rotated(f: functions.ContinuousFunction) = {
  val r = new functions.continuous.decorators.RotatedFunctionDecorator()
  r.setMatrixType("linear_transformation")
  r.setCondition(3)
  r.setFunction(f)
  r
}

def func(f: functions.ContinuousFunction, l: Double, u: Double) = {
  val p = new problem.FunctionOptimisationProblem()
  p.setDomain("R(" + l.toString + ":" + u.toString + ")^10")
  p.setFunction(shifted(scaled(rotated(f)), (u - l) / 2.0 + l - (u - l) / 4.0, (u - l) / 2.0 + l + (u - l) / 4.0))
  p
}

def tuningProb = new tuning.TuningProblem() {
  setDomain("R(-5:5)^10") // not used
  setSamples(1)
  setTargetAlgorithm(alg)
  setMeasurement(new measurement.single.Fitness())
  setProblemsProvider {
    new tuning.problem.MultiDimensionProblemGenerator() {
      setDimensionBounds {
        new tuning.parameters.TuningBounds() {
          setLowerBound(10) ; setUpperBound(100)
        }
      }
      setProblemsProvider {
        new tuning.problem.StandardProblemGenerator() {
          addProblem(func(new functions.continuous.unconstrained.Spherical(), -100, 100))
          addProblem(func(new functions.continuous.unconstrained.Rosenbrock(), -100, 100))
          addProblem(func(new functions.continuous.unconstrained.Griewank(), 0, 600))
          addProblem(func(new functions.continuous.unconstrained.Ackley(), -32, 32))
          addProblem(func(new functions.continuous.unconstrained.Rastrigin(), -5, 5))
        }
      }
    }
  }
}

def configs = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.multiple.MultipleSolutions())
}

runner(simulation(frace, tuningProb), configs, "data/ifrace1.txt", 1, 1)
runner(simulation(frace, tuningProb), configs, "data/ifrace1.txt", 1, 1)

