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
              setLowerBound(controlparameter.ConstantControlParameter.of(0))
              setUpperBound(new controlparameter.DomainProportionalControlParameter())
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
  val fcp = new controlparameter.FixedControlParameter()
  fcp.setControlParameter(new controlparameter.RandomControlParameter())
  s.setVerticalScale(fcp)
  s.setHorizontalScale(controlparameter.ConstantControlParameter.of(1.0))
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

def asym(f: functions.ContinuousFunction, beta: Double = 0.2) = {
  val a = new functions.continuous.decorators.AsymmetricFunctionDecorator()
  a.setBeta(controlparameter.ConstantControlParameter.of(beta))
  a.setFunction(f)
  a
}

def permutation(f: functions.ContinuousFunction) = {
  val p = new functions.continuous.decorators.PermutationFunctionDecorator()
  p.setFunction(f)
  p
}

def hybrid(fs: functions.ContinuousFunction*) = {
  val h = new functions.continuous.hybrid.SimpleHybridFunction()
  fs foreach { h.addFunction(_) }
  h
}

def range(f: functions.ContinuousFunction, start: Int = 0, end: Int = 50) = {
  val r = new functions.continuous.decorators.RangeFunctionDecorator()
  r.setEnd(controlparameter.ConstantControlParameter.of(end))
  r.setStart(controlparameter.ConstantControlParameter.of(start))
  r.setFunction(f)
  r
}

def hybridRange(g1: () => functions.ContinuousFunction, g2: () => functions.ContinuousFunction, rs: Int*) = {
  val h = hybrid(rs.zip(rs.tail) map { case (x,y) => scaled(range(g1(), x, y)) }:_*)
  h.addFunction(range(g2(), rs.last, 1000))
  h
}

def scaledHybridRange(g1: () => functions.ContinuousFunction, rs: Int*) = {
  val h = hybrid(rs.zip(rs.tail) map { case (x,y) => scaled(range(g1(), x, y)) }:_*)
  h
}

def overlapHybridRange(g1: () => functions.ContinuousFunction, rs: Int*) = {
  val h = hybrid(rs.zip(rs.tail) map { case (x,y) => scaled(range(g1(), x-5, y)) }:_*)
  h
}

def cec(d: String, f: functions.ContinuousFunction) = {
  val p = new problem.FunctionOptimisationProblem()
  p.setDomain(d)
  p.setFunction(f)
  p
}

def sphere = new functions.continuous.unconstrained.Spherical()
def elliptic = new functions.continuous.unconstrained.Elliptic()
def rastrigin = new functions.continuous.unconstrained.Rastrigin()
def ackley = new functions.continuous.unconstrained.Ackley()
def schwefel1_2 = new functions.continuous.SchwefelProblem1_2()
def rosenbrock = new functions.continuous.unconstrained.Rosenbrock()

def f1 = cec("R(-100:100)^1000", shifted(irregular(elliptic)))
def f2 = cec("R(-5:5)^1000", shifted(irregular(asym(ill(rastrigin)))))
def f3 = cec("R(-32:32)^1000", shifted(irregular(asym(ill(ackley)))))
def f4 = cec("R(-100:100)^1000", shifted(irregular(permutation(hybridRange(() => rotated(elliptic), () => elliptic,0,50,75,100,200,250,275,300)))))
def f5 = cec("R(-5:5)^1000", shifted(irregular(asym(ill(permutation(hybridRange(() => rotated(rastrigin), () => rastrigin,0,50,75,100,200,250,275,300)))))))
def f6 = cec("R(-32:32)^1000", shifted(irregular(asym(ill(permutation(hybridRange(() => rotated(ackley), () => ackley,0,50,75,100,200,250,275,300)))))))
def f7 = cec("R(-100:100)^1000", shifted(irregular(asym(permutation(hybridRange(() => schwefel1_2, () => sphere,0,50,75,100,200,250,275,300)), 0.2))))
def f8 = cec("R(-100:100)^1000", shifted(irregular(permutation(scaledHybridRange(() => rotated(elliptic),0,50,100,125,150,250,350,375,400,450,475,575,600,700,750,775,800,825,925,975,1000)))))
def f9 = cec("R(-5:5)^1000", shifted(irregular(asym(ill(permutation(scaledHybridRange(() => rotated(rastrigin),0,50,100,125,150,250,350,375,400,450,475,575,600,700,750,775,800,825,925,975,1000)))))))
def f10 = cec("R(-32:32)^1000", shifted(irregular(asym(ill(permutation(scaledHybridRange(() => rotated(ackley),0,50,100,125,150,250,350,375,400,450,475,575,600,700,750,775,800,825,925,975,1000)))))))
def f11 = cec("R(-100:100)^1000", shifted(irregular(asym(permutation(scaledHybridRange(() => schwefel1_2,0,50,100,125,150,250,350,375,400,450,475,575,600,700,750,775,800,825,925,975,1000))))))
def f12 = cec("R(-100:100)^1000", shifted(rosenbrock))
def f13 = cec("R(-100:100)^905", shifted(irregular(asym(permutation(overlapHybridRange(() => schwefel1_2,5,50,95,115,135,230,325,345,365,410,430,525,545,640,685,705,725,745,840,885,905))))))
def f14 = cec("R(-100:100)^905", permutation(overlapHybridRange(() => shifted(irregular(asym(schwefel1_2))),5,50,95,115,135,230,325,345,365,410,430,525,545,640,685,705,725,745,840,885,905)))
def f15 = cec("R(-100:100)^1000", shifted(irregular(asym(schwefel1_2))))

runner(simulation(p, f1), fitness, "data/cec13-lsgo_pso_f1.txt", 1, 10)
runner(simulation(p, f2), fitness, "data/cec13-lsgo_pso_f2.txt", 1, 10)
runner(simulation(p, f3), fitness, "data/cec13-lsgo_pso_f3.txt", 1, 10)
runner(simulation(p, f4), fitness, "data/cec13-lsgo_pso_f4.txt", 1, 10)
runner(simulation(p, f5), fitness, "data/cec13-lsgo_pso_f5.txt", 1, 10)
runner(simulation(p, f6), fitness, "data/cec13-lsgo_pso_f6.txt", 1, 10)
runner(simulation(p, f7), fitness, "data/cec13-lsgo_pso_f7.txt", 1, 10)
runner(simulation(p, f8), fitness, "data/cec13-lsgo_pso_f8.txt", 1, 10)
runner(simulation(p, f9), fitness, "data/cec13-lsgo_pso_f9.txt", 1, 10)
runner(simulation(p, f10), fitness, "data/cec13-lsgo_pso_f10.txt", 1, 10)
runner(simulation(p, f11), fitness, "data/cec13-lsgo_pso_f11.txt", 1, 10)
runner(simulation(p, f12), fitness, "data/cec13-lsgo_pso_f12.txt", 1, 10)
runner(simulation(p, f13), fitness, "data/cec13-lsgo_pso_f13.txt", 1, 10)
runner(simulation(p, f14), fitness, "data/cec13-lsgo_pso_f14.txt", 1, 10)
runner(simulation(p, f15), fitness, "data/cec13-lsgo_pso_f15.txt", 1, 10)

