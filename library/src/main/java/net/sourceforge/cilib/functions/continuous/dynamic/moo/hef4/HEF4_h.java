/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.hef4;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the h function of the adapted F4 problem defined in the
 * following paper: H. Li and Q. Zhang. Multiobjective optimization problems
 * with complicated Pareto sets, MOEA/D and NSGA-II, IEEE Transactions on
 * Evolutionary Computation, 13(2):284-302, 2009.
 *
 * The problem has been adapted by Helbig and Engelbrecht to make it a DMOOP.
 *
 */
public class HEF4_h implements ContinuousFunction {

    //members
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;
    private ContinuousFunction hef4_g;
    private ContinuousFunction hef4_f1;
    private FunctionOptimisationProblem hef4_f1_problem;
    private FunctionOptimisationProblem hef4_g_problem;

    //Domain("R(-1, 1)^20")

    /**
     * Creates a new instance of HEF4_g.
     */
    public HEF4_h() {
        //initialize the members
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 10;
    }

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setHEF4_g(FunctionOptimisationProblem problem) {
        this.hef4_g_problem = problem;
        this.hef4_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return hef4_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getHEF4_g_problem() {
        return this.hef4_g_problem;
    }

    /**
     * Sets the g function that is used in the HEF4 problem without specifying
     * the problem.
     * @param hef4_g ContinuousFunction used for the g function.
     */
    public void setHEF4_g(ContinuousFunction hef4_g) {
        this.hef4_g = hef4_g;
    }

    /**
     * Returns the g function that is used in the HEF4 problem.
     * @return hef4_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getHEF4_g() {
        return this.hef4_g;
    }

    /**
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setHEF4_f(FunctionOptimisationProblem problem) {
        this.hef4_f1_problem = problem;
        this.hef4_f1 = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return hef4_f1_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getHEF4_f_problem() {
        return this.hef4_f1_problem;
    }

    /**
     * Sets the f1 function that is used in the HEF4 problem without specifying
     * the problem.
     * @param hef4_f1 ContinuousFunction used for the f1 function.
     */
    public void setHEF4_f(ContinuousFunction hef4_f1) {
        this.hef4_f1 = hef4_f1;
    }

    /**
     * Returns the f1 function that is used in the HEF4 problem.
     * @return hef4_f1 ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getHEF4_f() {
        return this.hef4_f1;
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
        double t = (1.0 / (double) n_t) * Math.floor((double) iteration / (double) this.tau_t);
        double H = 0.75 * Math.sin(0.5 * Math.PI * t) + 1.25;

        double g = ((HEF4_g) this.hef4_g).apply(x);
        //evaluate the hef4_f1 function
        double f1 = this.hef4_f1.apply(x);

        double value = 1.0;
        value -= Math.pow((double) f1 / (double) g, H);

        return value;
    }
}
