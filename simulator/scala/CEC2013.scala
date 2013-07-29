import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def p = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.Fitness())
}

//boilerplate code
def shifted(f: functions.ContinuousFunction, vShift: Double) = {
  val s = new functions.continuous.decorators.ShiftedFunctionDecorator()
  s.setHorizontalShift {
    new controlparameter.FixedControlParameter() {
      setControlParameter {
        new controlparameter.RandomControlParameter() {
          setDistribution {
            new math.random.UniformDistribution() {
              setLowerBound(controlparameter.ConstantControlParameter.of(-80))
              setUpperBound(controlparameter.ConstantControlParameter.of(80))
            }
          }
        }
      }
    }
  }
  s.setVerticalShift(controlparameter.ConstantControlParameter.of(vShift))
  s.setFunction(f)
  s
}

def hShift(f: functions.ContinuousFunction, hShift: Double) = {
  val s = new functions.continuous.decorators.ShiftedFunctionDecorator()
  s.setHorizontalShift(controlparameter.ConstantControlParameter.of(hShift))
  s.setFunction(f)
  s
}

def scaled(f: functions.ContinuousFunction, vScale: Double = 1.0, hScale: Double = 1.0) = {
  val s = new functions.continuous.decorators.ScaledFunctionDecorator()
  s.setVerticalScale(controlparameter.ConstantControlParameter.of(vScale))
  s.setHorizontalScale(controlparameter.ConstantControlParameter.of(hScale))
  s.setFunction(f)
  s
}


def rotated(f: functions.ContinuousFunction, mType: String = "orthonormal", condition: Int=1) = {
  val r = new functions.continuous.decorators.RotatedFunctionDecorator()
  r.setMatrixType(mType)
  r.setCondition(condition)
  r.setFunction(f)
  r
}

def irregular(f: functions.ContinuousFunction) = {
  val i = new functions.continuous.decorators.IrregularFunctionDecorator()
  i.setFunction(f)
  i
}

def ill(f: functions.ContinuousFunction, alpha: Double = 10) = {
  val i = new functions.continuous.decorators.IllConditionedFunctionDecorator()
  i.setAlpha(controlparameter.ConstantControlParameter.of(alpha))
  i.setFunction(f)
  i
}

def asym(f: functions.ContinuousFunction, beta: Double = 0.5) = {
  val a = new functions.continuous.decorators.AsymmetricFunctionDecorator()
  a.setBeta(controlparameter.ConstantControlParameter.of(beta))
  a.setFunction(f)
  a
}

def rounded(f: functions.ContinuousFunction) = {
  val r = new functions.continuous.decorators.RoundingFunctionDecorator()
  r.setFunction(f)
  r
}

def cec(f: functions.ContinuousFunction) = {
  val p = new problem.FunctionOptimisationProblem()
  p.setDomain("R(-100:100)^10")
  p.setFunction(f)
  p
}

def composite(pars: List[(Double, Double, Double, () => functions.ContinuousFunction)]) = {
  new functions.continuous.hybrid.cec2013.HybridCompositionFunction() {
    pars foreach { x =>
      addFunction {
        new functions.continuous.hybrid.cec2013.SingleFunction() {
          setSigma(x._1)
          setLambda(x._2)
          setBias(x._3)
          setFunction(x._4())
        }
      }
    }
  }
}

def sphere = new functions.continuous.unconstrained.Spherical()
def elliptic = new functions.continuous.unconstrained.Elliptic()
def rastrigin = new functions.continuous.unconstrained.Rastrigin()
def ackley = new functions.continuous.unconstrained.Ackley()
def schwefel1_2 = new functions.continuous.SchwefelProblem1_2()
def rosenbrock = new functions.continuous.unconstrained.Rosenbrock()
def cigar = new functions.continuous.unconstrained.BentCigar()
def discus = new functions.continuous.unconstrained.Discus()
def powers = new functions.continuous.unconstrained.DifferentPowers()
def schaffer7 = new functions.continuous.unconstrained.Schaffer7()
def weierstrass = new functions.continuous.unconstrained.Weierstrass()
def griewank = new functions.continuous.unconstrained.Griewank()
def schwefelCEC = new functions.continuous.unconstrained.SchwefelCEC()
def katsuura = new functions.continuous.unconstrained.Katsuura()
def modRastrigin = new functions.continuous.unconstrained.ModifiedRastrigin()

def lunacek(g: functions.ContinuousFunction, vShift: Double) = new functions.continuous.decorators.ShiftedFunctionDecorator() {
  setFunction {
    new functions.continuous.unconstrained.LunacekBiRastrigin() {
      setRastriginDecorator(g)
      setHorizontalShift {
        new controlparameter.FixedControlParameter() {
          setControlParameter {
            new controlparameter.RandomControlParameter() {
              setDistribution {
                new math.random.UniformDistribution() {
                  setLowerBound(controlparameter.ConstantControlParameter.of(-80))
                  setUpperBound(controlparameter.ConstantControlParameter.of(80))
                }
              }
            }
          }
        }
      }
    }
  }
  setVerticalShift(controlparameter.ConstantControlParameter.of(vShift))
}

def f8f2 = new functions.continuous.decorators.ExpandedFunctionDecorator() {
  setFunction {
    new functions.continuous.decorators.CompositeFunctionDecorator() {
      setInnerFunction(rosenbrock)
      setOuterFunction(griewank)
    }
  }
}

def expSchaffer6 = new functions.continuous.decorators.ExpandedFunctionDecorator() {
  setFunction(new functions.continuous.unconstrained.Schaffer6())
}

def f1 = cec(shifted(sphere, -1400))
def f2 = cec(shifted(rotated(irregular(elliptic)), -1300))
def f3 = cec(shifted(rotated(asym(rotated(cigar))), -1200))
def f4 = cec(shifted(rotated(irregular(discus)), -1100))
def f5 = cec(shifted(powers, -1000))
def f6 = cec(shifted(scaled(rotated(hShift(rosenbrock, 1.0)), 1.0, 2.048e-2), -900))
def f7 = cec(shifted(rotated(asym(rotated(ill(schaffer7)))), -800))
def f8 = cec(shifted(rotated(asym(rotated(ill(ackley)))), -700))
def f9 = cec(shifted(scaled(rotated(asym(rotated(ill(weierstrass)))), 1.0, 0.5e-2), -600))
def f10 = cec(shifted(scaled(rotated(ill(griewank, 100)), 1.0, 6.0), -500))
def f11 = cec(shifted(scaled(irregular(asym(ill(rastrigin), 0.2)), 1.0, 5.12e-2), -400))
def f12 = cec(shifted(scaled(rotated(irregular(asym(rotated(ill(rotated(rastrigin))), 0.2))), 1.0, 5.12e-2), -300))
def f13 = cec(shifted(scaled(rotated(rounded(irregular(asym(rotated(ill(rotated(rastrigin))), 0.2)))), 1.0, 5.12e-2), -200))
def f14 = cec(shifted(scaled(ill(hShift(schwefelCEC, 4.209687462275036e2)), 1.0, 10.0), -100))
def f15 = cec(shifted(scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10), 100))
def f16 = cec(shifted(scaled(rotated(ill(rotated(katsuura), 100)), 1.0, 5e-2), 200))
def f17 = cec(lunacek(ill(modRastrigin, 100), 300))
def f18 = cec(lunacek(rotated(ill(rotated(modRastrigin), 100)), 400))
def f19 = cec(shifted(scaled(rotated(hShift(f8f2, 1.0)), 1.0, 5e-2), 500))
def f20 = cec(shifted(rotated(asym(rotated(expSchaffer6))), 600))

def f21 = cec(shifted(composite(List(
  (10, 1, 0, () => scaled(rotated(hShift(rosenbrock, 1.0)), 1.0, 2.048e-2)),
  (20, 1e-6, 100, () => rotated(powers)),
  (30, 1e-26, 200, () => rotated(asym(rotated(cigar)))),
  (40, 1e-6, 300, () => rotated(irregular(discus))),
  (50, 0.1, 400, () => sphere)
)), 700))

def f22 = cec(shifted(composite(List(
  (20, 1, 0, () => scaled(ill(hShift(schwefelCEC, 4.209687462275036e2)), 1.0, 10)),
  (20, 1, 100, () => scaled(ill(hShift(schwefelCEC, 4.209687462275036e2)), 1.0, 10)),
  (20, 1, 200, () => scaled(ill(hShift(schwefelCEC, 4.209687462275036e2)), 1.0, 10))
)), 800))

def f23 = cec(shifted(composite(List(
  (20, 1, 0, () => scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10)),
  (20, 1, 100, () => scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10)),
  (20, 1, 200, () => scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10))
)), 900))

def f24 = cec(shifted(composite(List(
  (20, 0.25, 0, () => scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10)),
  (20, 1, 100, () => scaled(rotated(irregular(asym(rotated(ill(rotated(rastrigin))), 0.2))), 1.0, 5.12e-2)),
  (20, 2.5, 200, () => scaled(rotated(asym(rotated(ill(weierstrass)))), 1.0, 0.5e-2))
)), 1000))

def f25 = cec(shifted(composite(List(
  (10, 0.25, 0, () => scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10)),
  (30, 1, 100, () => scaled(rotated(irregular(asym(rotated(ill(rotated(rastrigin))), 0.2))), 1.0, 5.12e-2)),
  (50, 2.5, 200, () => scaled(rotated(asym(rotated(ill(weierstrass)))), 1.0, 0.5e-2))
)), 1100))

def f26 = cec(shifted(composite(List(
  (10, 0.25, 0, () => scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10)),
  (10, 1, 100, () => scaled(rotated(irregular(asym(rotated(ill(rotated(rastrigin))), 0.2))), 1.0, 5.12e-2)),
  (10, 1e-7, 200, () => rotated(irregular(elliptic))),
  (10, 2.5, 300, () => scaled(rotated(asym(rotated(ill(weierstrass)))), 1.0, 0.5e-2)),
  (10, 10, 400, () => scaled(rotated(ill(griewank, 100)), 1.0, 6))
)), 1200))

def f27 = cec(shifted(composite(List(
  (10, 100, 0, () => scaled(rotated(ill(griewank, 100)), 1.0, 6)),
  (10, 10, 100, () => scaled(rotated(irregular(asym(rotated(ill(rotated(rastrigin))), 0.2))), 1.0, 5.12e-2)),
  (10, 2.5, 200, () => scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10)),
  (20, 25, 300, () => scaled(rotated(asym(rotated(ill(weierstrass)))), 1.0, 0.5e-2)),
  (20, 0.1, 400, () => sphere)
)), 1300))

def f28 = cec(shifted(composite(List(
  (10, 2.5, 0, () => scaled(rotated(hShift(f8f2, 1.0)), 1.0, 5e-2)),
  (20, 2.5e-3, 100, () => rotated(asym(rotated(ill(schaffer7))))),
  (30, 2.5, 200, () => scaled(rotated(ill(hShift(schwefelCEC, 4.209687462275036e2))), 1.0, 10)),
  (40, 5e-4, 300, () => rotated(asym(rotated(ill(expSchaffer6))))),
  (50, 0.1, 400, () => sphere)
)), 1400))

runner(simulation(p, f1), fitness, "data/pso_cec10_f1.txt", 10, 10)
runner(simulation(p, f2), fitness, "data/pso_cec10_f2.txt", 10, 10)
runner(simulation(p, f3), fitness, "data/pso_cec10_f3.txt", 10, 10)
runner(simulation(p, f4), fitness, "data/pso_cec10_f4.txt", 10, 10)
runner(simulation(p, f5), fitness, "data/pso_cec10_f5.txt", 10, 10)
runner(simulation(p, f6), fitness, "data/pso_cec10_f6.txt", 10, 10)
runner(simulation(p, f7), fitness, "data/pso_cec10_f7.txt", 10, 10)
runner(simulation(p, f8), fitness, "data/pso_cec10_f8.txt", 10, 10)
runner(simulation(p, f9), fitness, "data/pso_cec10_f9.txt", 10, 10)
runner(simulation(p, f10), fitness, "data/pso_cec10_f10.txt", 10, 10)
runner(simulation(p, f11), fitness, "data/pso_cec10_f11.txt", 10, 10)
runner(simulation(p, f12), fitness, "data/pso_cec10_f12.txt", 10, 10)
runner(simulation(p, f13), fitness, "data/pso_cec10_f13.txt", 10, 10)
runner(simulation(p, f14), fitness, "data/pso_cec10_f14.txt", 10, 10)
runner(simulation(p, f15), fitness, "data/pso_cec10_f15.txt", 10, 10)
runner(simulation(p, f16), fitness, "data/pso_cec10_f16.txt", 10, 10)
runner(simulation(p, f17), fitness, "data/pso_cec10_f17.txt", 10, 10)
runner(simulation(p, f18), fitness, "data/pso_cec10_f18.txt", 10, 10)
runner(simulation(p, f19), fitness, "data/pso_cec10_f19.txt", 10, 10)
runner(simulation(p, f20), fitness, "data/pso_cec10_f20.txt", 10, 10)
runner(simulation(p, f21), fitness, "data/pso_cec10_f21.txt", 10, 10)
runner(simulation(p, f22), fitness, "data/pso_cec10_f22.txt", 10, 10)
runner(simulation(p, f23), fitness, "data/pso_cec10_f23.txt", 10, 10)
runner(simulation(p, f24), fitness, "data/pso_cec10_f24.txt", 10, 10)
runner(simulation(p, f25), fitness, "data/pso_cec10_f25.txt", 10, 10)
runner(simulation(p, f26), fitness, "data/pso_cec10_f26.txt", 10, 10)
runner(simulation(p, f27), fitness, "data/pso_cec10_f27.txt", 10, 10)
runner(simulation(p, f28), fitness, "data/pso_cec10_f28.txt", 10, 10)

