/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda3;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the FDA3 problem defined on page 428 in
 * the following paper: M.Farina, K.Deb, P.Amato. Dynamic multiobjective
 * optimization problems: test cases, approximations and applications, IEEE
 * Transactions on Evolutionary Computation, 8(5): 425-442
 *
 */
public class FDA3_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 6810010897943653288L;
    //member
    ContinuousFunction fda3_g;
    ContinuousFunction fda3_h;
    FunctionOptimisationProblem fda3_g_problem;
    FunctionOptimisationProblem fda3_h_problem;

    //Domain("R(-1, 1)^30")

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setFDA3_g(FunctionOptimisationProblem problem) {
        this.fda3_g_problem = problem;
        this.fda3_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return fda3_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getFDA3_g_problem() {
        return this.fda3_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA3 problem without specifying
     * the problem.
     * @param fda3_g ContinuousFunction used for the g function.
     */
    public void setFDA3_g(ContinuousFunction fda3_g) {
        this.fda3_g = fda3_g;
    }

    /**
     * Returns the g function that is used in the FDA3 problem.
     * @return fda3_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getFDA3_g() {
        return this.fda3_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setFDA3_h(FunctionOptimisationProblem problem) {
        this.fda3_h_problem = problem;
        this.fda3_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return fda3_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getFDA3_h_problem() {
        return this.fda3_h_problem;
    }

    /**
     * Sets the h function that is used in the FDA3 problem.
     * @param fda3_h ContinuousFunction used for the h function.
     */
    public void setFDA3_h(ContinuousFunction fda3_h) {
        this.fda3_h = fda3_h;
    }

    /**
     * Returns the h function that is used in the FDA3 problem.
     * @return fda3_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getFDA3_h() {
        return this.fda3_h;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return this.apply(iteration, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public Double apply(int iteration, Vector x) {
        Vector y = x.copyOfRange(5, x.size());

        double g = ((FDA3_g) this.fda3_g).apply(iteration, y);
        double h = ((FDA3_h) this.fda3_h).apply(iteration, x);

        double value = g * h;

        return value;
    }
}
