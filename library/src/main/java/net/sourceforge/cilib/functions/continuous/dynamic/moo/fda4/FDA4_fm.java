/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda4;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the fm function of the FDA4 problem defined on page 430 in
 * the following paper: M.Farina, K.Deb, P.Amato. Dynamic multiobjective
 * optimization problems: test cases, approximations and applications, IEEE
 * Transactions on Evolutionary Computation, 8(5): 425-442
 *
 */
public class FDA4_fm implements ContinuousFunction {

    private static final long serialVersionUID = 2580157997419837986L;
    //members
    FunctionOptimisationProblem fda4_g_problem;
    ContinuousFunction fda4_g;
    int M;
    int n;

    /**
     * Creates a new instance of FDA4_fm.
     */
    public FDA4_fm() {
        super();
        this.M = 3;
        this.n = this.M + 9;
    }

    /**
     * Sets the number of objectives.
     * @param m Number of objectives.
     */
    public void setM(int m) {
        this.M = m;
        this.n = this.M + 9;
    }

    /**
     * Returns the number of objectives.
     * @return M Number of objectives.
     */
    public int getM() {
        return this.M;
    }

    /**
     * Sets the number of variables.
     * @param n Number of variables.
     */
    public void setN(int n) {
        this.n = n;
        this.M = n - 9;
    }

    /**
     * Returns the number of variables.
     * @return n Number of variables.
     */
    public int getN() {
        return this.n;
    }

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setFDA4_g(FunctionOptimisationProblem problem) {
        this.fda4_g_problem = problem;
        this.fda4_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return fda4_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getFDA4_g_problem() {
        return this.fda4_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA4 problem without specifying
     * the problem.
     * @param g ContinuousFunction used for the g function.
     */
    public void setFDA4_g(ContinuousFunction g) {
        this.fda4_g = g;
    }

    /**
     * Returns the g function that is used in the FDA4 problem.
     * @return fda4_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getFDA4_g() {
        return this.fda4_g;
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

        Vector y = x;

        if (x.size() > 1) {
            y = x.copyOfRange(this.M - 1, this.n);
        }

        double g = ((FDA4_g) this.fda4_g).apply(iteration, y);

        double value = 1.0 + g;
        value *= Math.sin(x.doubleValueOf(0) * Math.PI / 2.0);

        return value;
    }
}
