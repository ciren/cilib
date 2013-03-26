/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dmop3;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the h function of the DMOP3 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */
public class DMOP3_h implements ContinuousFunction {

    private static final long serialVersionUID = -6164428590204835825L;
    //members
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;
    //functions
    private ContinuousFunction dmop3_g;
    private ContinuousFunction dmop3_f;
    private FunctionOptimisationProblem dmop3_f_problem;
    private FunctionOptimisationProblem dmop3_g_problem;

    /**
     * Creates a new instance of DMOP3_h.
     */
    public DMOP3_h() {
        //initialize the members
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 10;
    }

    /**
     * Sets the iteration number.
     * @param tau Iteration number.
     */
    public void setTau(int tau) {
        this.tau = tau;
    }

    /**
     * Returns the iteration number.
     * @return tau Iteration number.
     */
    public int getTau() {
        return this.tau;
    }

    /**
     * Sets the frequency of change.
     * @param tau_t Change frequency.
     */
    public void setTau_t(int tau_t) {
        this.tau_t = tau_t;
    }

    /**
     * Returns the frequency of change.
     * @return tau_t Change frequency.
     */
    public int getTau_t() {
        return this.tau_t;
    }

    /**
     * Sets the severity of change.
     * @param n_t Change severity.
     */
    public void setN_t(int n_t) {
        this.n_t = n_t;
    }

    /**
     * Returns the severity of change.
     * @return n_t Change severity.
     */
    public int getN_t() {
        return this.n_t;
    }

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setDMOP3_g(FunctionOptimisationProblem problem) {
        this.dmop3_g_problem = problem;
        this.dmop3_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return dmop3_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getDMOP3_g_problem() {
        return this.dmop3_g_problem;
    }

    /**
     * Sets the g function that is used in the DMOP3 problem without specifying
     * the problem.
     * @param dmop3_g ContinuousFunction used for the g function.
     */
    public void setDMOP3_g(ContinuousFunction dmop3_g) {
        this.dmop3_g = dmop3_g;
    }

    /**
     * Returns the g function that is used in the DMOP3 problem.
     * @return dmop3_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getDMOP3_g() {
        return this.dmop3_g;
    }

    /**
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setDMOP3_f(FunctionOptimisationProblem problem) {
        this.dmop3_f_problem = problem;
        this.dmop3_f = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return dmop3_f_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getDMOP3_f_problem() {
        return this.dmop3_f_problem;
    }

    /**
     * Sets the f1 function that is used in the DMOP3 problem.
     * @param dmop3_f ContinuousFunction used for the f1 function.
     */
    public void setDMOP3_f(ContinuousFunction dmop3_f) {
        this.dmop3_f = dmop3_f;
    }

    /**
     * Returns the f1 function that is used in the DMOP3 problem.
     * @return dmop3_f Function used for the f1 function.
     */
    public ContinuousFunction getDMOP3_f() {
        return this.dmop3_f;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        this.tau = AbstractAlgorithm.get().getIterations();
        return this.apply(this.tau, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public Double apply(int iteration, Vector x) {
        //evaluate the fda1_f1 function
        double f1 = ((DMOP3_f1) this.dmop3_f).apply(iteration, x);
        //evaluate the fda1_g function
        double g = ((DMOP3_g) this.dmop3_g).apply(iteration, x);

        double value = 1.0;
        value -= Math.sqrt((double) f1 / (double) g);

        return value;
    }
}
