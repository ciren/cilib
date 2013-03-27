/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.hef6;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the fm function of the adapted F6 problem defined in the
 * following paper: H. Li and Q. Zhang. Multiobjective optimization problems
 * with complicated Pareto sets, MOEA/D and NSGA-II, IEEE Transactions on
 * Evolutionary Computation, 13(2):284-302, 2009.
 *
 * The problem has been adapted by Helbig and Engelbrecht to make it a DMOOP.
 *
 */
public class HEF6_fm implements ContinuousFunction {

    private static final long serialVersionUID = 2580157997419837986L;
    //members
    FunctionOptimisationProblem hef6_g_problem;
    ContinuousFunction hef6_g;
    int M;
    int n;

    /**
     * Creates a new instance of HEF6_fm.
     */
    public HEF6_fm() {
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
    public void setHEF6_g(FunctionOptimisationProblem problem) {
        this.hef6_g_problem = problem;
        this.hef6_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return hef6_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getHEF6_g_problem() {
        return this.hef6_g_problem;
    }

    /**
     * Sets the g function that is used in the HEF6 problem without specifying
     * the problem.
     * @param g ContinuousFunction used for the g function.
     */
    public void setHEF6_g(ContinuousFunction g) {
        this.hef6_g = g;
    }

    /**
     * Returns the g function that is used in the HEF6 problem.
     * @return hef6_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getHEF6_g() {
        return this.hef6_g;
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

        double g = ((HEF6_g) this.hef6_g).apply(iteration, y);

        double value = 1.0 + g;
        double val1 = Math.sin(0.5 * Math.abs(x.doubleValueOf(0)) * Math.PI);
        double sum = 0.0;
        int index = 0;

        for (int i = 2; i < x.size(); i++) {
            if ((i + 1) % 3 == 0) {
                sum += Math.pow(Math.abs(x.doubleValueOf(i)) - 2.0 * Math.abs(x.doubleValueOf(1)) * Math.sin(2.0 * Math.PI * Math.abs(x.doubleValueOf(0)) + (i * Math.PI / x.size())), 2);
                index++;
            }
        }
        sum *= 2.0 / index;
        sum += val1;
        sum *= value;
        return sum;
    }
}
