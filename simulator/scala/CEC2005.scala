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

//problems
//boilerplate code
def sphere = new functions.continuous.unconstrained.Spherical()
def schwefel1_2 = new functions.continuous.SchwefelProblem1_2()
def elliptic = new functions.continuous.unconstrained.Elliptic()
def schwefel2_6 = new functions.continuous.unconstrained.SchwefelProblem2_6()
def rosenbrock = new functions.continuous.unconstrained.Rosenbrock()
def griewank = new functions.continuous.unconstrained.Griewank()
def ackley = new functions.continuous.unconstrained.Ackley()
def rastrigin = new functions.continuous.unconstrained.Rastrigin()
def weierstrass = new functions.continuous.unconstrained.Weierstrass()
def schwefel2_13 = new functions.continuous.unconstrained.SchwefelProblem2_13()
def expSchaffer6 = new functions.continuous.decorators.ExpandedFunctionDecorator() {
  setFunction(new functions.continuous.unconstrained.Schaffer6())
}
def f8f2 = new functions.continuous.decorators.ExpandedFunctionDecorator() {
  setFunction {
    new functions.continuous.decorators.CompositeFunctionDecorator() {
      setInnerFunction(rosenbrock)
      setOuterFunction(griewank)
    }
  }
}

def shifted(f: functions.ContinuousFunction, vShift: Double = 0.0, hShift: Double = 0.0) = {
  val s = new functions.continuous.decorators.ShiftedFunctionDecorator()
  s.setHorizontalShift(controlparameter.ConstantControlParameter.of(hShift))
  s.setVerticalShift(controlparameter.ConstantControlParameter.of(vShift))
  s.setFunction(f)
  s
}

def rotated(f: functions.ContinuousFunction, mType: String, condition: Int=1) = {
  val r = new functions.continuous.decorators.RotatedFunctionDecorator()
  r.setMatrixType(mType)
  r.setCondition(condition)
  r.setFunction(f)
  r
}

def noisy(f: functions.ContinuousFunction) = {
  val n = new functions.continuous.decorators.NoisyFunctionDecorator()
  n.setFunction(f)
  n
}

def rounded(f: functions.ContinuousFunction) = {
  val r = new functions.continuous.decorators.RoundingFunctionDecorator()
  r.setFunction(f)
  r
}

def cec(domain: String, f: functions.ContinuousFunction) = {
  val p = new problem.FunctionOptimisationProblem()
  p.setDomain(domain)
  p.setFunction(f)
  p
}

def composite(pars: List[(Double, Double, String, Double, Double, Int, () => functions.ContinuousFunction)]) = {
  new functions.continuous.hybrid.HybridCompositionFunction() {
    pars foreach { x =>
      addFunction {
        new functions.continuous.hybrid.SingleFunction() {
          setSigma(x._1)
          setLambda(x._2)
          setMatrixType(x._3)
          setBias(x._4)
          setHorizontalShift(x._5)
          setCondition(x._6)
          setFunction(x._7())
        }
      }
    }
  }
}

// function definitions
def f1 = cec("R(-100:100)^10", shifted(sphere, -450, 20))
def f2 = cec("R(-100:100)^10", shifted(schwefel1_2, -450, 20))
def f3 = cec("R(-100:100)^10", shifted(rotated(elliptic, "orthonormal"), -450, 20))
def f4 = cec("R(-100:100)^10", shifted(noisy(schwefel1_2), -450, 20))
def f5 = cec("R(-100:100)^10", shifted(schwefel2_6, -310)) // The optimum is shifted to the bounds but is set in the function itself
def f6 = cec("R(-100:100)^10", shifted(rosenbrock, 390, 20))
def f7 = cec("R(0:600)^10", shifted(rotated(griewank, "linear_transformation", 3), -180, -60)) // TODO: add the noise to rotation matrix
def f8 = cec("R(-32:32)^10", shifted(rotated(ackley, "linear_transformation", 100), -140, -32))
def f9 = cec("R(-5:5)^10", shifted(rastrigin, -330, 1))
def f10 = cec("R(-5:5)^10", shifted(rotated(rastrigin, "linear_transformation", 2), -330, 1))
def f11 = cec("R(-0.5:0.5)^10", shifted(rotated(weierstrass, "linear_transformation", 5), 90, 0.1)) // Algorithms should be constrained for this function since better fitnesses are available out of the bounds
def f12 = cec("R(-3.14:3.14)^10", shifted(schwefel2_13, -460)) // This functions optimum is zero at a random position in [-pi, pi]^n
def f13 = cec("R(-5:5)^10", shifted(f8f2, -130, 1))
def f14 = cec("R(-100:100)^10", shifted(rotated(expSchaffer6, "linear_transformation", 3), -300, 20))

def f15 = cec("R(-5:5)^10", shifted(composite(List(
  (1,1,"identity",0,1,1,() => rastrigin),
  (1,1,"identity",100,0,1,() => rastrigin),
  (1,10,"identity",200,-1,1,() => weierstrass),
  (1,10,"identity",300,2,1,() => weierstrass),
  (1,5.0/60,"identity",400,-2,1,() => griewank),
  (1,5.0/60,"identity",500,3,1,() => griewank),
  (1,5.0/32,"identity",600,-3,1,() => ackley),
  (1,5.0/32,"identity",700,4,1,() => ackley),
  (1,5.0/100,"identity",800,-4,1,() => sphere),
  (1,5.0/100,"identity",900,5,1,() => sphere)
  )
), 120))

def f16 = cec("R(-5:5)^10", shifted(composite(List(
  (1,1,"linear_transformation",0,1,2,() => rastrigin),
  (1,1,"linear_transformation",100,0,2,() => rastrigin),
  (1,10,"linear_transformation",200,-1,2,() => weierstrass),
  (1,10,"linear_transformation",300,2,2,() => weierstrass),
  (1,5.0/60,"linear_transformation",400,-2,2,() => griewank),
  (1,5.0/60,"linear_transformation",500,3,2,() => griewank),
  (1,5.0/32,"linear_transformation",600,-3,2,() => ackley),
  (1,5.0/32,"linear_transformation",700,4,2,() => ackley),
  (1,5.0/100,"linear_transformation",800,-4,2,() => sphere),
  (1,5.0/100,"linear_transformation",900,5,2,() => sphere)
  )
), 120))

def f17 = cec("R(-5:5)^10", shifted(noisy(composite(List(
  (1,1,"linear_transformation",0,1,2,() => rastrigin),
  (1,1,"linear_transformation",100,0,2,() => rastrigin),
  (1,10,"linear_transformation",200,-1,2,() => weierstrass),
  (1,10,"linear_transformation",300,2,2,() => weierstrass),
  (1,5.0/60,"linear_transformation",400,-2,2,() => griewank),
  (1,5.0/60,"linear_transformation",500,3,2,() => griewank),
  (1,5.0/32,"linear_transformation",600,-3,2,() => ackley),
  (1,5.0/32,"linear_transformation",700,4,2,() => ackley),
  (1,5.0/100,"linear_transformation",800,-4,2,() => sphere),
  (1,5.0/100,"linear_transformation",900,5,2,() => sphere)
  )
)), 120))

def f18 = cec("R(-5:5)^10", shifted(composite(List(
  (1,10.0/32,"linear_transformation",0,1,2,() => ackley),
  (2,5.0/32,"linear_transformation",100,5,3,() => ackley),
  (1.5,2,"linear_transformation",200,-1,2,() => rastrigin),
  (1.5,1,"linear_transformation",300,2,3,() => rastrigin),
  (1,0.1,"linear_transformation",400,-2,2,() => sphere),
  (1,0.05,"linear_transformation",500,3,3,() => sphere),
  (1.5,20,"linear_transformation",600,-3,20,() => weierstrass),
  (1.5,10,"linear_transformation",700,4,30,() => weierstrass),
  (2,10.0/60,"linear_transformation",800,-4,200,() => griewank),
  (2,5.0/60,"linear_transformation",900,0,300,() => griewank)
  )
), 10))

def f19 = cec("R(-5:5)^10", shifted(composite(List(
  (0.1,0.5/32,"linear_transformation",0,1,2,() => ackley),
  (2,5.0/32,"linear_transformation",100,5,3,() => ackley),
  (1.5,2,"linear_transformation",200,-1,2,() => rastrigin),
  (1.5,1,"linear_transformation",300,2,3,() => rastrigin),
  (1,0.1,"linear_transformation",400,-2,2,() => sphere),
  (1,0.05,"linear_transformation",500,3,3,() => sphere),
  (1.5,20,"linear_transformation",600,-3,20,() => weierstrass),
  (1.5,10,"linear_transformation",700,4,30,() => weierstrass),
  (2,10.0/60,"linear_transformation",800,-4,200,() => griewank),
  (2,5.0/60,"linear_transformation",900,0,300,() => griewank)
  )
), 10))

def f20 = cec("R(-5:5)^10", shifted(composite(List(
  (1,10.0/32,"linear_transformation",0,5,2,() => ackley),
  (2,5.0/32,"linear_transformation",100,1,3,() => ackley),
  (1.5,2,"linear_transformation",200,-1,2,() => rastrigin),
  (1.5,1,"linear_transformation",300,2,3,() => rastrigin),
  (1,0.1,"linear_transformation",400,-2,2,() => sphere),
  (1,0.05,"linear_transformation",500,3,3,() => sphere),
  (1.5,20,"linear_transformation",600,-3,20,() => weierstrass),
  (1.5,10,"linear_transformation",700,4,30,() => weierstrass),
  (2,10.0/60,"linear_transformation",800,-4,200,() => griewank),
  (2,5.0/60,"linear_transformation",900,0,300,() => griewank)
  )
), 10))

def f21 = cec("R(-5:5)^10", shifted(composite(List(
  (1,0.25,"orthonormal",0,1,1,() => expSchaffer6),
  (1,0.05,"orthonormal",100,0,1,() => expSchaffer6),
  (1,5,"orthonormal",200,-1,1,() => rastrigin),
  (1,1,"orthonormal",300,2,1,() => rastrigin),
  (1,5,"orthonormal",400,-2,1,() => f8f2),
  (2,1,"orthonormal",500,3,1,() => f8f2),
  (2,50,"orthonormal",600,-3,1,() => weierstrass),
  (2,10,"orthonormal",700,4,1,() => weierstrass),
  (2,25.0/200,"orthonormal",800,-4,1,() => griewank),
  (2,5.0/200,"orthonormal",900,5,1,() => griewank)
  )
), 360))

def f22 = cec("R(-5:5)^10", shifted(composite(List(
  (1,0.25,"linear_transformation",0,1,10,() => expSchaffer6),
  (1,0.05,"linear_transformation",100,0,20,() => expSchaffer6),
  (1,5,"linear_transformation",200,-1,50,() => rastrigin),
  (1,1,"linear_transformation",300,2,100,() => rastrigin),
  (1,5,"linear_transformation",400,-2,200,() => f8f2),
  (2,1,"linear_transformation",500,3,1000,() => f8f2),
  (2,50,"linear_transformation",600,-3,2000,() => weierstrass),
  (2,10,"linear_transformation",700,4,3000,() => weierstrass),
  (2,25.0/200,"linear_transformation",800,-4,4000,() => griewank),
  (2,5.0/200,"linear_transformation",900,5,5000,() => griewank)
  )
), 360))

def f23 = cec("R(-5:5)^10", shifted(rounded(composite(List(
  (1,0.25,"orthonormal",0,1,1,() => expSchaffer6),
  (1,0.05,"orthonormal",100,0,1,() => expSchaffer6),
  (1,5,"orthonormal",200,-1,1,() => rastrigin),
  (1,1,"orthonormal",300,2,1,() => rastrigin),
  (1,5,"orthonormal",400,-2,1,() => f8f2),
  (2,1,"orthonormal",500,3,1,() => f8f2),
  (2,50,"orthonormal",600,-3,1,() => weierstrass),
  (2,10,"orthonormal",700,4,1,() => weierstrass),
  (2,25.0/200,"orthonormal",800,-4,1,() => griewank),
  (2,5.0/200,"orthonormal",900,5,1,() => griewank)
  )
)), 360))

def f24 = cec("R(-5:5)^10", shifted(composite(List(
  (2,10,"orthonormal",0,1,100,() => weierstrass),
  (2,5.0/20,"orthonormal",100,0,50,() => expSchaffer6),
  (2,1,"orthonormal",200,-1,30,() => f8f2),
  (2,5.0/32,"orthonormal",300,2,10,() => ackley),
  (2,1,"orthonormal",400,-2,5,() => rastrigin),
  (2,5.0/100,"orthonormal",500,3,5,() => griewank),
  (2,0.1,"orthonormal",600,-3,4,() => rounded(expSchaffer6)),
  (2,1,"orthonormal",700,4,3,() => rounded(rastrigin)),
  (2,5.0/100,"orthonormal",800,-4,2,() => elliptic),
  (2,5.0/100,"orthonormal",900,5,2,() => noisy(sphere))
  )
), 260))

def f25 = cec("R(2:5)^10", shifted(composite(List(
  (2,10,"orthonormal",0,-2,100,() => weierstrass),
  (2,5.0/20,"orthonormal",100,0,50,() => expSchaffer6),
  (2,1,"orthonormal",200,-1,30,() => f8f2),
  (2,5.0/32,"orthonormal",300,2,10,() => ackley),
  (2,1,"orthonormal",400,1,5,() => rastrigin),
  (2,5.0/100,"orthonormal",500,3,5,() => griewank),
  (2,0.1,"orthonormal",600,-3,4,() => rounded(expSchaffer6)),
  (2,1,"orthonormal",700,4,3,() => rounded(rastrigin)),
  (2,5.0/100,"orthonormal",800,-4,2,() => elliptic),
  (2,5.0/100,"orthonormal",900,5,2,() => noisy(sphere))
  )
), 260))

runner(simulation(p, f1), fitness, "data/pso_cec05_f1.txt", 10, 10)
runner(simulation(p, f2), fitness, "data/pso_cec05_f2.txt", 10, 10)
runner(simulation(p, f3), fitness, "data/pso_cec05_f3.txt", 10, 10)
runner(simulation(p, f4), fitness, "data/pso_cec05_f4.txt", 10, 10)
runner(simulation(p, f5), fitness, "data/pso_cec05_f5.txt", 10, 10)
runner(simulation(p, f6), fitness, "data/pso_cec05_f6.txt", 10, 10)
runner(simulation(p, f7), fitness, "data/pso_cec05_f7.txt", 10, 10)
runner(simulation(p, f8), fitness, "data/pso_cec05_f8.txt", 10, 10)
runner(simulation(p, f9), fitness, "data/pso_cec05_f9.txt", 10, 10)
runner(simulation(p, f10), fitness, "data/pso_cec05_f10.txt", 10, 10)
runner(simulation(p, f11), fitness, "data/pso_cec05_f11.txt", 10, 10)
runner(simulation(p, f12), fitness, "data/pso_cec05_f12.txt", 10, 10)
runner(simulation(p, f13), fitness, "data/pso_cec05_f13.txt", 10, 10)
runner(simulation(p, f14), fitness, "data/pso_cec05_f14.txt", 10, 10)
runner(simulation(p, f15), fitness, "data/pso_cec05_f15.txt", 10, 10)
runner(simulation(p, f16), fitness, "data/pso_cec05_f16.txt", 10, 10)
runner(simulation(p, f17), fitness, "data/pso_cec05_f17.txt", 10, 10)
runner(simulation(p, f18), fitness, "data/pso_cec05_f18.txt", 10, 10)
runner(simulation(p, f19), fitness, "data/pso_cec05_f19.txt", 10, 10)
runner(simulation(p, f20), fitness, "data/pso_cec05_f20.txt", 10, 10)
runner(simulation(p, f21), fitness, "data/pso_cec05_f21.txt", 10, 10)
runner(simulation(p, f22), fitness, "data/pso_cec05_f22.txt", 10, 10)
runner(simulation(p, f23), fitness, "data/pso_cec05_f23.txt", 10, 10)
runner(simulation(p, f24), fitness, "data/pso_cec05_f24.txt", 10, 10)
runner(simulation(p, f25), fitness, "data/pso_cec05_f25.txt", 10, 10)
