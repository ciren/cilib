package net.cilib
package simulator

object Simulation {
  import net.sourceforge.cilib.algorithm.AbstractAlgorithm
  import net.sourceforge.cilib.problem.Problem

  def simulation(alg: AbstractAlgorithm, prob: Problem) = {
    alg.setOptimisationProblem(prob)
    alg.run
  }
}
