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
def shifted(f: functions.ContinuousFunction) = {
  val s = new functions.continuous.decorators.ShiftedFunctionDecorator()
  s.setHorizontalShift {
    new controlparameter.FixedControlParameter() {
      setControlParameter {
        new controlparameter.RandomControlParameter() {
          setDistribution {
            new math.random.UniformDistribution() {
              setLowerBound(controlparameter.ConstantControlParameter.of(0.0))
              setUpperBound(new controlparameter.DomainProportionalControlParameter())
            }
          }
        }
      }
    }
  }
  //s.setVerticalShift(controlparameter.ConstantControlParameter.of(vShift))
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

def range(f: functions.ContinuousFunction, start: Int = 0, end: Int = 50) = {
  val r = new functions.continuous.decorators.RangeFunctionDecorator()
  r.setEnd(controlparameter.ConstantControlParameter.of(end))
  r.setStart(controlparameter.ConstantControlParameter.of(start))
  r.setFunction(f)
  r
}

def permutation(f: functions.ContinuousFunction) = {
  val p = new functions.continuous.decorators.PermutationFunctionDecorator()
  p.setFunction(f)
  p
}

def summation(f: functions.ContinuousFunction, lower: Int = 0, upper: Int = 10, group: Int = 50) = {
  val s = new functions.continuous.decorators.SummationRangeFunctionDecorator()
  s.setLower(controlparameter.ConstantControlParameter.of(lower))
  s.setUpper(controlparameter.ConstantControlParameter.of(upper))
  s.setGroupSize(group)
  s.setFunction(f)
  s
}

def hybrid(fs: functions.ContinuousFunction*) = {
  val h = new functions.continuous.hybrid.SimpleHybridFunction()
  fs foreach { h.addFunction(_) }
  h
}

def cec(domain: String, f: functions.ContinuousFunction) = {
  val p = new problem.FunctionOptimisationProblem()
  p.setDomain(domain)
  p.setFunction(f)
  p
}

def sphere = new functions.continuous.unconstrained.Spherical()
def elliptic = new functions.continuous.unconstrained.Elliptic()
def rastrigin = new functions.continuous.unconstrained.Rastrigin()
def ackley = new functions.continuous.unconstrained.Ackley()
def schwefel1_2 = new functions.continuous.SchwefelProblem1_2()
def rosenbrock = new functions.continuous.unconstrained.Rosenbrock()

//problems
//Shifted Elliptic
def f1 = cec("R(-100:100)^1000", shifted(elliptic))

//Shifted Rastrigin
def f2 = cec("R(-5:5)^1000", shifted(rastrigin))

//Shifted Ackley
def f3 = cec("R(-32:32)^1000", shifted(ackley))

//Single-group Shifted and m-rotated Elliptic
def f4 = cec("R(-100:100)^1000", shifted(permutation(hybrid(
  scaled(range(rotated(elliptic)), 1e6),
  range(elliptic, 50, 1000)
))))

//Single-group Shifted and m-rotated Rastrigin
def f5 = cec("R(-5:5)^1000", shifted(permutation(hybrid(
  scaled(range(rotated(rastrigin)), 1e6),
  range(rastrigin, 50, 1000)
))))

//Single-group Shifted and m-rotated Ackley
def f6 = cec("R(-32:32)^1000", shifted(permutation(hybrid(
  scaled(range(rotated(ackley)), 1e6),
  range(ackley, 50, 1000)
))))

//Single-group Shifted m-dimensional SchwefelProblem1_2
def f7 = cec("R(-100:100)^1000", shifted(permutation(hybrid(
  scaled(range(rotated(schwefel1_2)), 1e6),
  range(sphere, 50, 1000)
))))

//Single-group Shifted m-dimensional Rosenbrock
def f8 = cec("R(-100:100)^1000", shifted(permutation(hybrid(
  scaled(range(rotated(rosenbrock)), 1e6),
  range(sphere, 50, 1000)
))))

//D/2m-group Shifted and m-rotated Elliptic
def f9 = cec("R(-100:100)^1000", shifted(permutation(hybrid(
  summation(rotated(elliptic)),
  range(elliptic, 500, 1000)
))))

//D/2m-group Shifted and m-rotated Rastrigin
def f10 = cec("R(-5:5)^1000", shifted(permutation(hybrid(
  summation(rotated(rastrigin)),
  range(rastrigin, 500, 1000)
))))

//D/2m-group Shifted and m-rotated Ackley
def f11 = cec("R(-32:32)^1000", shifted(permutation(hybrid(
  summation(rotated(ackley)),
  range(ackley, 500, 1000)
))))

//D/2m-group Shifted m-dimensional SchwefelProblem1_2
def f12 = cec("R(-100:100)^1000", shifted(permutation(hybrid(
  summation(rotated(schwefel1_2)),
  range(sphere, 500, 1000)
))))

//D/2m-group Shifted m-dimensional Rosenbrock
def f13 = cec("R(-100:100)^1000", shifted(permutation(hybrid(
  summation(rotated(rosenbrock)),
  range(sphere, 500, 1000)
))))

//D/m-group Shifted m-rotated Elliptic
def f14 = cec("R(-100:100)^1000", shifted(permutation(summation(rotated(elliptic), 0, 20))))

//D/m-group Shifted m-rotated Rastrigin
def f15 = cec("R(-5:5)^1000", shifted(permutation(summation(rotated(rastrigin), 0, 20))))

//D/m-group Shifted m-rotated Ackley
def f16 = cec("R(-32:32)^1000", shifted(permutation(summation(rotated(ackley), 0, 20))))

//D/m-group Shifted m-dimensional SchwefelProblem1_2
def f17 = cec("R(-100:100)^1000", shifted(permutation(summation(rotated(schwefel1_2), 0, 20))))

//D/m-group Shifted m-dimensional Rosenbrock
def f18 = cec("R(-100:100)^1000", shifted(permutation(summation(rotated(rosenbrock), 0, 20))))

//Shifted SchwefelProblem1_2
def f19 = cec("R(-100:100)^1000", shifted(schwefel1_2))

//Shifted Rosenbrock
def f20 = cec("R(-100:100)^1000", shifted(rosenbrock))

//simulations
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

