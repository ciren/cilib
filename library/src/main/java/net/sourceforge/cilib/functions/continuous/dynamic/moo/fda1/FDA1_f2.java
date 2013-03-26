/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g*h function of the FDA1 problem defined on page 428 in
 * the following paper: M.Farina, K.Deb, P.Amato. Dynamic multiobjective
 * optimization problems: test cases, approximations and applications, IEEE
 * Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 */
public class FDA1_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 6369118486095689078L;
    //member
    ContinuousFunction fda1_g;
    ContinuousFunction fda1_h;
    FunctionOptimisationProblem fda1_g_problem;
    FunctionOptimisationProblem fda1_h_problem;

    //Domain("R(-1, 1)^20")

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setFDA1_g(FunctionOptimisationProblem problem) {
        this.fda1_g_problem = problem;
        this.fda1_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return fda1_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getFDA1_g_problem() {
        return this.fda1_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA1 problem without specifying
     * the problem.
     * @param fda1_g ContinuousFunction used for the g function.
     */
    public void setFDA1_g(ContinuousFunction fda1_g) {
        this.fda1_g = fda1_g;
    }

    /**
     * Returns the g function that is used in the FDA1 problem.
     * @return fda1_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getFDA1_g() {
        return this.fda1_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setFDA1_h(FunctionOptimisationProblem problem) {
        this.fda1_h_problem = problem;
        this.fda1_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return fda1_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getFDA1_h_problem() {
        return this.fda1_h_problem;
    }

    /**
     * Sets the h function that is used in the FDA1 problem without specifying
     * the problem.
     * @param fda1_h ContinuousFunction used for the h function.
     */
    public void setFDA1_h(ContinuousFunction fda1_h) {
        this.fda1_h = fda1_h;
    }

    /**
     * Sets the f1 function that is used in the FDA1 problem without specifying
     * the problem.
     * @return fda1_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getFDA1_h() {
        return this.fda1_h;
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
        Vector y = input.copyOfRange(1, input.size());
        double g = ((FDA1_g) this.fda1_g).apply(iteration, y);
        double h = ((FDA1_h) this.fda1_h).apply(iteration, input);

        double value = g * h;

        return value;
    }
}
