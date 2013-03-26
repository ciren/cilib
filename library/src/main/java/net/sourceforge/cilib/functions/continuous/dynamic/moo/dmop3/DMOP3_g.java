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
 * This function is the g function of the DMOP3 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */
public class DMOP3_g implements ContinuousFunction {

    private static final long serialVersionUID = -3942639679084167540L;
    //members
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;
    private ContinuousFunction dmop3_f1;
    private FunctionOptimisationProblem dmop3_f1_problem;

    /**
     * Creates a new instance of DMOP3_g.
     */
    public DMOP3_g() {
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
     * Sets the f1 function that is used in DMOP3 problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setDMOP3_f(FunctionOptimisationProblem problem) {
        this.dmop3_f1_problem = problem;
        this.dmop3_f1 = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return dmop3_f1_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getDMOP3_f_problem() {
        return this.dmop3_f1_problem;
    }

    /**
     * Sets the f1 function that is used in the DMOP3 problem without specifying
     * the problem.
     * @param dmop3_f1 ContinuousFunction used for the f1 function.
     */
    public void setDMOP3_f(ContinuousFunction dmop3_f1) {
        this.dmop3_f1 = dmop3_f1;
    }

    /**
     * Returns the f1 function that is used in the DMOP3 problem.
     * @return dmop3_f1 ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getDMOP3_f() {
        return this.dmop3_f1;
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
        double G = Math.sin(0.5 * Math.PI * t);

        double sum = 1.0;
        if (DMOP3_f1.getR() == -1) {
            this.dmop3_f1.apply(x);
        }

        for (int k = 0; k < x.size(); k++) {
            if (k != DMOP3_f1.getR()) {
                sum += Math.pow(x.doubleValueOf(k) - G, 2);
            }
        }
        return sum;
    }
}
