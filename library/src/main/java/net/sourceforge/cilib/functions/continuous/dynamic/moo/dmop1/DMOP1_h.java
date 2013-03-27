/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dmop1;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the h function of the DMOP1 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */
public class DMOP1_h implements ContinuousFunction {

    private static final long serialVersionUID = -2696640132377696102L;
    //members
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;
    //functions
    private ContinuousFunction dmop1_g;
    private ContinuousFunction dmop1_f1;
    private FunctionOptimisationProblem dmop1_f1_problem;
    private FunctionOptimisationProblem dmop1_g_problem;

    /**
     * Creates a new instance of DMOP1_h.
     */
    public DMOP1_h() {
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
    public void setDMOP1_g(FunctionOptimisationProblem problem) {
        this.dmop1_g_problem = problem;
        this.dmop1_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return dmop1_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getDMOP1_g_problem() {
        return this.dmop1_g_problem;
    }

    /**
     * Sets the g function that is used in the DMOP1 problem without specifying
     * the problem.
     * @param dmop1_g ContinuousFunction used for the g function.
     */
    public void setDMOP1_g(ContinuousFunction dmop1_g) {
        this.dmop1_g = dmop1_g;
    }

    /**
     * Returns the g function that is used in the DMOP1 problem.
     * @return dmop1_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getDMOP1_g() {
        return this.dmop1_g;
    }

    /**
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setDMOP1_f(FunctionOptimisationProblem problem) {
        this.dmop1_f1_problem = problem;
        this.dmop1_f1 = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return dmop1_f1_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getDMOP1_f_problem() {
        return this.dmop1_f1_problem;
    }

    /**
     * Sets the f1 function that is used in the DMOP1 problem without specifying
     * the problem.
     * @param dmop1_f1 ContinuousFunction used for the f1 function.
     */
    public void setDMOP1_f(ContinuousFunction dmop1_f1) {
        this.dmop1_f1 = dmop1_f1;
    }

    /**
     * Returns the f1 function that is used in the DMOP1 problem.
     * @return dmop1_f1 ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getDMOP1_f() {
        return this.dmop1_f1;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        this.tau = AbstractAlgorithm.get().getIterations();
        return apply(this.tau, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public Double apply(int iteration, Vector x) {
        double t = (1.0 / (double) n_t) * Math.floor((double) iteration / (double) this.tau_t);
        double H = 0.75 * Math.sin(0.5 * Math.PI * t) + 1.25;

        //only the first element
        Vector y = x.copyOfRange(0, 1);
        //all the elements except the first element
        Vector z = x.copyOfRange(1, x.size());
        //evaluate the fda1_g function
        double g = this.dmop1_g.apply(z);
        //evaluate the fda1_f1 function
        double f1 = this.dmop1_f1.apply(y);

        double sum = 1.0;
        sum -= Math.pow(((double) f1 / (double) g), H);

        return sum;
    }
}
