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
 * This function is the h function of the FDA3 problem defined on page 429 in
 * the following paper: M.Farina, K.Deb, P.Amato. Dynamic multiobjective
 * optimization problems: test cases, approximations and applications, IEEE
 * Transactions on Evolutionary Computation, 8(5): 425-442
 *
 */
public class FDA3_h implements ContinuousFunction {

    private static final long serialVersionUID = 3845365231540108577L;
    //members
    ContinuousFunction fda3_f;
    ContinuousFunction fda3_g;
    FunctionOptimisationProblem fda3_f_problem;
    FunctionOptimisationProblem fda3_g_problem;

    //setDomain("R(-1, 1)^30")

    /**
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setFDA3_f(FunctionOptimisationProblem problem) {
        this.fda3_f_problem = problem;
        this.fda3_f = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return fda3_f_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getFDA3_f_problem() {
        return this.fda3_f_problem;
    }

    /**
     * Sets the f1 function that is used in the FDA3 problem without specifying
     * the problem.
     * @param fda3_f ContinuousFunction used for the f1 function.
     */
    public void setFDA3_f(ContinuousFunction fda3_f) {
        this.fda3_f = fda3_f;
    }

    /**
     * Returns the f1 function that is used in the FDA3 problem.
     * @return fda3_f ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getFDA3_f() {
        return this.fda3_f;
    }

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
        Vector z = x;

        if (x.size() > 1) {
            y = x.copyOfRange(5, x.size());
            z = x.copyOfRange(0, 5);
        }

        double f = ((FDA3_f1) this.fda3_f).apply(iteration, z);
        double g = ((FDA3_g) this.fda3_g).apply(iteration, y);

        double value = 1.0;
        value -= Math.sqrt((double) f / (double) g);

        return value;
    }
}
