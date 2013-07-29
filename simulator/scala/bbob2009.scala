import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

//algorithms
def p = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

// 1.1 Sphere Function
def f1 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB1())
}

// 1.2 Ellipsoidal Function
def f2 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB2())
}

// 1.3 Rastrigin Function
def f3 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB3())
}

// 1.4 Buche-Rastrigin Function
def f4 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB4())
}

// 1.5 Linear Slope
def f5 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB5())
}

// 2.6 Attractive Sector
def f6 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB6())
}

// 2.7 Step Ellipsoidal Function
def f7 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB7())
}

// Rosenbrock Function, original
def f8 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB8())
}

// 2.9 Rosenbrock Function rotated
def f9 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB9())
}

// 3.10 Ellipsoidal Function
def f10 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB10())
}

// 3.11 Discus Function
def f11 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB11())
}

// 3.12 Bent Cigar Function
def f12 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB12())
}

// 3.13 Sharp Ridge Function
def f13 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB13())
}

// 3.14 Different Powers Function
def f14 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB14())
}

// 4.15 Rastrigin Function
def f15 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB15())
}

// 4.16 Weierstrass Function
def f16 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB16())
}

// 4.17 Schaffers F7 Function
def f17 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB17())
}

// 4.18 Schaffers F7 Function, moderately ill-conditioned
def f18 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB18())
}

// 4.19 Composite Griewank-Rosenbrock Function F8F2
def f19 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB19())
}

// 5.20 Schwefel Function
def f20 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB20())
}

// 5.21 Gallagher's Gaussian 101-me Peaks Function
def f21 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB21())
}

// 5.22 Gallagher's Gaussian 21-hi Peaks Function
def f22 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB22())
}

// 5.23 Katsuura Function
def f23 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB23())
}

// 5.24 Lunacek bi-Rastrigin Function
def f24 = new problem.FunctionOptimisationProblem() {
    setDomain("R(-5:5)^10")
    setFunction(new functions.continuous.bbob.BBOB24())
}

//measurements
def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.FitnessMinusOptimum())
}

//simulations
runner(simulation(p, f1), fitness, "data/bbob2009-pso-f1.txt", 10, 10)
runner(simulation(p, f2), fitness, "data/bbob2009-pso-f2.txt", 10, 10)
runner(simulation(p, f3), fitness, "data/bbob2009-pso-f3.txt", 10, 10)
runner(simulation(p, f4), fitness, "data/bbob2009-pso-f4.txt", 10, 10)
runner(simulation(p, f5), fitness, "data/bbob2009-pso-f5.txt", 10, 10)
runner(simulation(p, f6), fitness, "data/bbob2009-pso-f6.txt", 10, 10)
runner(simulation(p, f7), fitness, "data/bbob2009-pso-f7.txt", 10, 10)
runner(simulation(p, f8), fitness, "data/bbob2009-pso-f8.txt", 10, 10)
runner(simulation(p, f9), fitness, "data/bbob2009-pso-f9.txt", 10, 10)
runner(simulation(p, f10), fitness, "data/bbob2009-pso-f10.txt", 10, 10)
runner(simulation(p, f11), fitness, "data/bbob2009-pso-f11.txt", 10, 10)
runner(simulation(p, f12), fitness, "data/bbob2009-pso-f12.txt", 10, 10)
runner(simulation(p, f13), fitness, "data/bbob2009-pso-f13.txt", 10, 10)
runner(simulation(p, f14), fitness, "data/bbob2009-pso-f14.txt", 10, 10)
runner(simulation(p, f15), fitness, "data/bbob2009-pso-f15.txt", 10, 10)
runner(simulation(p, f16), fitness, "data/bbob2009-pso-f16.txt", 10, 10)
runner(simulation(p, f17), fitness, "data/bbob2009-pso-f17.txt", 10, 10)
runner(simulation(p, f18), fitness, "data/bbob2009-pso-f18.txt", 10, 10)
runner(simulation(p, f19), fitness, "data/bbob2009-pso-f19.txt", 10, 10)
runner(simulation(p, f20), fitness, "data/bbob2009-pso-f20.txt", 10, 10)
runner(simulation(p, f21), fitness, "data/bbob2009-pso-f21.txt", 10, 10)
runner(simulation(p, f22), fitness, "data/bbob2009-pso-f22.txt", 10, 10)
runner(simulation(p, f23), fitness, "data/bbob2009-pso-f23.txt", 10, 10)
runner(simulation(p, f24), fitness, "data/bbob2009-pso-f24.txt", 10, 10)

