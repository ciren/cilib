/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.hef9;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the adapted F9 problem defined in the
 * following paper: H. Li and Q. Zhang. Multiobjective optimization problems
 * with complicated Pareto sets, MOEA/D and NSGA-II, IEEE Transactions on
 * Evolutionary Computation, 13(2):284-302, 2009.
 *
 * The problem has been adapted by Helbig and Engelbrecht to make it a DMOOP.
 *
 */
public class HEF9_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 6369118486095689078L;
    //member
    ContinuousFunction hef9_g;
    ContinuousFunction hef9_h;
    FunctionOptimisationProblem hef9_g_problem;
    FunctionOptimisationProblem hef9_h_problem;

    //Domain("R(-1, 1)^20")

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setHEF9_g(FunctionOptimisationProblem problem) {
        this.hef9_g_problem = problem;
        this.hef9_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return hef9_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getHEF9_g_problem() {
        return this.hef9_g_problem;
    }

    /**
     * Sets the g function that is used in the HEF9 problem without specifying
     * the problem.
     * @param hef9_g ContinuousFunction used for the g function.
     */
    public void setHEF9_g(ContinuousFunction hef9_g) {
        this.hef9_g = hef9_g;
    }

    /**
     * Returns the g function that is used in the HEF9 problem.
     * @return hef9_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getHEF9_g() {
        return this.hef9_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setHEF9_h(FunctionOptimisationProblem problem) {
        this.hef9_h_problem = problem;
        this.hef9_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return hef9_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getHEF9_h_problem() {
        return this.hef9_h_problem;
    }

    /**
     * Sets the h function that is used in the HEF9 problem without specifying
     * the problem.
     * @param hef9_h ContinuousFunction used for the h function.
     */
    public void setHEF9_h(ContinuousFunction hef9_h) {
        this.hef9_h = hef9_h;
    }

    /**
     * Sets the f1 function that is used in the HEF9 problem without specifying
     * the problem.
     * @return hef9_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getHEF9_h() {
        return this.hef9_h;
    }

    /**
     * Evaluates the function. g*h
     */
    @Override
    public Double apply(Vector input) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return apply(iteration, input);
    }

    /**
     * Evaluates the function for a specific iteration. g*h
     */
    public Double apply(int iteration, Vector input) {
        double g = ((HEF9_g) this.hef9_g).apply(input);
        double h = ((HEF9_h) this.hef9_h).apply(iteration, input);

        double value = g * h;

        return value;
    }
}
