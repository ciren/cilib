import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def nichePSO(fe: Double) = new niching.NichingAlgorithm() {
  addStoppingCondition {
    new stoppingcondition.MeasuredStoppingCondition() {
      setMeasurement(new measurement.single.FitnessEvaluations())
      setTarget(fe)
    }
  }

  setMainSwarm {
    new pso.PSO() { // cognitive only pso
      setInitialisationStrategy {
        new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
          setEntityNumber(30)
          setEntityType {
            new pso.particle.StandardParticle() {
              setVelocityProvider {
                new pso.velocityprovider.StandardVelocityProvider(
                  controlparameter.ConstantControlParameter.of(0.72),
                  controlparameter.ConstantControlParameter.of(0.0),
                  controlparameter.ConstantControlParameter.of(1.2)
                )
              }
            }
          }
        }
      }
      setIterationStrategy {
        new pso.iterationstrategies.SynchronousIterationStrategy() {
          setBoundaryConstraint(new problem.boundaryconstraint.ClampingBoundaryConstraint())
        }
      }
    }
  }

  setNicheCreator {
    new niching.creation.ClosestNeighbourNicheCreationStrategy() {
      setSwarmType {
        new pso.PSO() {
          setIterationStrategy {
            new pso.iterationstrategies.SynchronousIterationStrategy() {
              setBoundaryConstraint(new problem.boundaryconstraint.ClampingBoundaryConstraint())
            }
          }
        }
      }
      setSwarmBehavior {
        new pso.particle.ParticleBehavior() {
          setVelocityProvider {
            new pso.velocityprovider.GCVelocityProvider() {
              setRho(controlparameter.ConstantControlParameter.of(0.001)) // very problem dependant
              setRhoExpandCoefficient(controlparameter.ConstantControlParameter.of(2.0))
              setRhoContractCoefficient(controlparameter.ConstantControlParameter.of(0.5))
              setDelegate {
                new pso.velocityprovider.ClampingVelocityProvider() {
                  setVMax(controlparameter.ConstantControlParameter.of(1.0))
                  setDelegate {
                    new pso.velocityprovider.StandardVelocityProvider(
                      new controlparameter.LinearlyVaryingControlParameter(0.7, 0.2),
                      controlparameter.ConstantControlParameter.of(1.2),
                      controlparameter.ConstantControlParameter.of(1.2)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  setNicheDetector {
    new niching.creation.MaintainedFitnessNicheDetection() {
      setThreshold(controlparameter.ConstantControlParameter.of(1e-4))
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

  setSubSwarmIterator {
    new niching.iterators.AllSwarmsIterator() {
      setIterator(new niching.iterators.SingleNicheIteration())
    }
  }

  setMainSwarmIterator(new niching.iterators.SingleNicheIteration())
  setIterationStrategy(new niching.iterationstrategies.NichePSO())
}

//boilerplate
def sphere = new functions.continuous.unconstrained.Spherical()
def rosenbrock = new functions.continuous.unconstrained.Rosenbrock()
def griewank = new functions.continuous.unconstrained.Griewank()
def rastrigin = new functions.continuous.unconstrained.Rastrigin()
def weierstrass = new functions.continuous.unconstrained.Weierstrass()
def f8f2 = new functions.continuous.decorators.ExpandedFunctionDecorator() {
  setFunction {
    new functions.continuous.decorators.CompositeFunctionDecorator() {
      setInnerFunction(rosenbrock)
      setOuterFunction(griewank)
    }
  }
}

def scaled(f: functions.ContinuousFunction, vScale: Double = 1.0, hScale: Double = 1.0) = {
  val s = new functions.continuous.decorators.ScaledFunctionDecorator()
  s.setVerticalScale(controlparameter.ConstantControlParameter.of(vScale))
  s.setHorizontalScale(controlparameter.ConstantControlParameter.of(hScale))
  s.setFunction(f)
  s
}

def shifted(f: functions.ContinuousFunction, vShift: Double = 0, hShift: Double = 0) = {
  val s = new functions.continuous.decorators.ShiftedFunctionDecorator()
  s.setHorizontalShift(controlparameter.ConstantControlParameter.of(hShift))
  s.setVerticalShift(controlparameter.ConstantControlParameter.of(vShift))
  s.setFunction(f)
  s
}

def composite(pars: (Double, Double, String, Double, Int, functions.ContinuousFunction)*) = {
  new functions.continuous.hybrid.HybridCompositionFunction() {
    pars foreach { x =>
      addFunction {
        new functions.continuous.hybrid.SingleFunction() {
          setSigma(x._1)
          setLambda(x._2)
          setMatrixType(x._3)
          setBias(x._4)
          setRandomShift(true)
          setCondition(x._5)
          setFunction(x._6)
        }
      }
    }
  }
}

def max(d: String, g: functions.ContinuousFunction) = new problem.FunctionOptimisationProblem() {
  setDomain(d)
  setObjective(new problem.objective.Maximise())
  setFunction(g)
}

def min(d: String, g: functions.ContinuousFunction) = new problem.FunctionOptimisationProblem() {
  setDomain(d)
  setFunction(g)
}

def f1 = max("R(0:30)", scaled(new functions.continuous.unconstrained.FiveUnevenPeakTrap(), -1))
def f2 = max("R(0:1)", new functions.continuous.unconstrained.MultimodalFunction1())
def f3 = max("R(0:1)", new functions.continuous.unconstrained.MultimodalFunction4())
def f4 = max("R(-6:6)^2", shifted(scaled(new functions.continuous.unconstrained.Himmelblau(), -1), 200))
def f5 = max("R(-1.9:1.9),R(-1.1:1.1)", scaled(new functions.continuous.unconstrained.SixHumpCamelBack(), -4))
def f6(d: Int) = max("R(-10:10)^" + d.toString, scaled(new functions.continuous.unconstrained.Shubert(), -1))
def f7(d: Int) = max("R(0.25:10)^" + d.toString, scaled(new functions.continuous.unconstrained.Vincent(), -d))
def f8 = max("R(0:1)^2", new functions.continuous.unconstrained.RastriginNiching() { setK(3); setK(4) })
def f9 = min("R(-5:5)^2", composite(
  (1,1,"identity",0,1,griewank),
  (1,1,"identity",0,1,griewank),
  (1,8,"identity",0,1,weierstrass),
  (1,8,"identity",0,1,weierstrass),
  (1,0.2,"identity",0,1,sphere),
  (1,0.2,"identity",0,1,sphere)
))

def f10 = min("R(-5:5)^2", composite(
  (1,1,"identity",0,1,rastrigin),
  (1,1,"identity",0,1,rastrigin),
  (1,10,"identity",0,1,weierstrass),
  (1,10,"identity",0,1,weierstrass),
  (1,0.1,"identity",0,1,griewank),
  (1,0.1,"identity",0,1,griewank),
  (1,1.0/7,"identity",0,1,sphere),
  (1,1.0/7,"identity",0,1,sphere)
))

def f11(d: Int) = min("R(-5:5)^" + d.toString, composite(
  (1,0.25,"linear_transformation",0,1,f8f2),
  (1,0.1,"linear_transformation",0,1,f8f2),
  (1,2,"linear_transformation",0,1,weierstrass),
  (1,1,"linear_transformation",0,1,weierstrass),
  (1,2,"linear_transformation",0,1,griewank),
  (1,5,"linear_transformation",0,1,griewank)
))

def f12(d: Int) = min("R(-5:5)^2", composite(
  (1,4,"linear_transformation",0,1,rastrigin),
  (1,1,"linear_transformation",0,1,rastrigin),
  (1,4,"linear_transformation",0,1,f8f2),
  (1,1,"linear_transformation",0,1,f8f2),
  (1,0.1,"linear_transformation",0,1,weierstrass),
  (2,0.2,"linear_transformation",0,1,weierstrass),
  (2,0.1,"linear_transformation",0,1,griewank),
  (2,0.025,"linear_transformation",0,1,griewank)
))

def fitness(ph: Double, r: Double) = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.FitnessEvaluations())
  addMeasurement(new measurement.single.SwarmSize())
  addMeasurement {
    val m = new measurement.multiple.CompositeMeasurement()
    m.addMeasurement(new measurement.single.SwarmSize())
    m.addMeasurement(new measurement.single.Solution())
    m
  }
  Seq(1e-1, 1e-2, 1e-3, 1e-4, 1e-5) foreach { x => 
    val y = new measurement.single.Niches()
    y.setPeakHeight(ph)
    y.setRadius(r)
    y.setError(x)
    addMeasurement(y)
  }
}

runner(simulation(nichePSO(5e4), f1), fitness(200, 0.01), "data/nichePSO-cec13-niching-f1.txt", 5, 10)
runner(simulation(nichePSO(5e4), f2), fitness(1, 0.01), "data/nichePSO-cec13-niching-f2.txt", 5, 10)
runner(simulation(nichePSO(5e4), f3), fitness(1, 0.01), "data/nichePSO-cec13-niching-f3.txt", 5, 10)
runner(simulation(nichePSO(5e4), f4), fitness(200, 0.01), "data/nichePSO-cec13-niching-f4.txt", 5, 10)
runner(simulation(nichePSO(5e4), f5), fitness(1.03163, 0.5), "data/nichePSO-cec13-niching-f5.txt", 5, 10)
runner(simulation(nichePSO(2e5), f6(2)), fitness(186.731, 0.5), "data/nichePSO-cec13-niching-f6-2D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f6(3)), fitness(2709.0935, 0.5), "data/nichePSO-cec13-niching-f6-3D.txt", 5, 10)
runner(simulation(nichePSO(2e5), f7(2)), fitness(1, 0.2), "data/nichePSO-cec13-niching-f7-2D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f7(3)), fitness(1, 0.2), "data/nichePSO-cec13-niching-f7-3D.txt", 5, 10)
runner(simulation(nichePSO(2e5), f8), fitness(-2, 0.01), "data/nichePSO-cec13-niching-f8.txt", 5, 10)
runner(simulation(nichePSO(2e5), f9), fitness(0, 0.01), "data/nichePSO-cec13-niching-f9.txt", 5, 10)
runner(simulation(nichePSO(2e5), f10), fitness(0, 0.1), "data/nichePSO-cec13-niching-f10.txt", 5, 10)
runner(simulation(nichePSO(2e5), f11(2)), fitness(0, 0.01), "data/nichePSO-cec13-niching-f11-2D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f11(3)), fitness(0, 0.01), "data/nichePSO-cec13-niching-f11-3D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f11(5)), fitness(0, 0.01), "data/nichePSO-cec13-niching-f11-5D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f11(10)), fitness(0, 0.01), "data/nichePSO-cec13-niching-f11-10D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f12(3)), fitness(0, 0.01), "data/nichePSO-cec13-niching-f12-3D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f12(5)), fitness(0, 0.01), "data/nichePSO-cec13-niching-f12-5D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f12(10)), fitness(0, 0.01), "data/nichePSO-cec13-niching-f12-10D.txt", 5, 10)
runner(simulation(nichePSO(4e5), f12(20)), fitness(0, 0.01), "data/nichePSO-cec13-niching-f12-20D.txt", 5, 10)

