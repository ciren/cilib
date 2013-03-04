/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.isolated.dmop2;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the DMOP2 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */
public class DMOP2_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 2382040029756700916L;
    //member
    ContinuousFunction dmop2_g;
    ContinuousFunction dmop2_h;
    FunctionOptimisationProblem dmop2_g_problem;
    FunctionOptimisationProblem dmop2_h_problem;

    //Domain = "R(0, 1)^10"

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setDMOP2_g(FunctionOptimisationProblem problem) {
        this.dmop2_g_problem = problem;
        this.dmop2_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return dmop2_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getDMOP2_g_problem() {
        return this.dmop2_g_problem;
    }

    /**
     * Sets the g function that is used in the DMOP2 problem without specifying
     * the problem.
     * @param dmop2_g ContinuousFunction used for the g function.
     */
    public void setDMOP2_g(ContinuousFunction dmop2_g) {
        this.dmop2_g = dmop2_g;
    }

    /**
     * Returns the g function that is used in the DMOP2 problem.
     * @return dmop2_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getDMOP2_g() {
        return this.dmop2_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setDMOP2_h(FunctionOptimisationProblem problem) {
        this.dmop2_h_problem = problem;
        this.dmop2_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return dmop2_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getDMOP2_h_problem() {
        return this.dmop2_h_problem;
    }

    /**
     * Sets the h function that is used in the DMOP2 problem.
     * @param dmop2_h ContinuousFunction used for the h function.
     */
    public void setDMOP2_h(ContinuousFunction dmop2_h) {
        this.dmop2_h = dmop2_h;
    }

    /**
     * Returns the h function that is used in the DMOP2 problem.
     * @return dmop2_h Function used for the h function.
     */
    public ContinuousFunction getDMOP2_h() {
        return this.dmop2_h;
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
        double g = ((DMOP2_g) this.dmop2_g).apply(iteration, y);
        double h = ((DMOP2_h) this.dmop2_h).apply(iteration, x);
        double value = g * h;

        return value;
    }
}
