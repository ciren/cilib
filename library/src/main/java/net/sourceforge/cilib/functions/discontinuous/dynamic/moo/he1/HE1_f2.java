/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discontinuous.dynamic.moo.he1;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the HE1 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */
public class HE1_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 5927570367072540148L;
    //members
    ContinuousFunction he1_g;
    ContinuousFunction he1_h;
    FunctionOptimisationProblem he1_g_problem;
    FunctionOptimisationProblem he1_h_problem;

    //Domain = "R(0, 1)^10"

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setHE1_g(FunctionOptimisationProblem problem) {
        this.he1_g_problem = problem;
        this.he1_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return HE1_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getHE1_g_problem() {
        return this.he1_g_problem;
    }

    /**
     * Sets the g function that is used in the HE1 problem without specifying
     * the problem.
     * @param HE1_g ContinuousFunction used for the g function.
     */
    public void setHE1_g(ContinuousFunction HE1_g) {
        this.he1_g = HE1_g;
    }

    /**
     * Returns the g function that is used in the HE1 problem.
     * @return HE1_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getHE1_g() {
        return this.he1_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setHE1_h(FunctionOptimisationProblem problem) {
        this.he1_h_problem = problem;
        this.he1_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return HE1_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getHE1_h_problem() {
        return this.he1_h_problem;
    }

    /**
     * Sets the h function that is used in the HE1 problem without specifying
     * the problem.
     * @param HE1_h ContinuousFunction used for the h function.
     */
    public void setHE1_h(ContinuousFunction HE1_h) {
        this.he1_h = HE1_h;
    }

    /**
     * Returns the h function that is used in the HE1 problem.
     * @return HE1_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getHE1_h() {
        return this.he1_h;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        int it = AbstractAlgorithm.get().getIterations();
        return apply(it, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public Double apply(int iteration, Vector x) {

        Vector y = x.copyOfRange(1, x.size());
        double g = this.he1_g.apply(y);
        double h = ((HE1_h) this.he1_h).apply(iteration, x);

        double value = g * h;

        return value;
    }
}
