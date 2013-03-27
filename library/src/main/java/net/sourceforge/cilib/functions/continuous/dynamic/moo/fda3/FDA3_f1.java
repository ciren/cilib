/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda3;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the FDA3 problem defined on page 428 in
 * the following paper: M.Farina, K.Deb, P.Amato. Dynamic multiobjective
 * optimization problems: test cases, approximations and applications, IEEE
 * Transactions on Evolutionary Computation, 8(5): 425-442
 *
 */
public class FDA3_f1 implements ContinuousFunction {

    private static final long serialVersionUID = -3439671771146219737L;
    //members
    //	number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;

    /**
     * Creates a new instance of FDA3_f1.
     */
    public FDA3_f1() {
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
        double power = 2 * Math.sin(0.5 * Math.PI * t);
        double F = Math.pow(10, power);

        double value = 0.0;
        int dim = x.size();
        if (dim > 5) {
            dim = 5;
        }

        for (int k = 0; k < dim; k++) {
            value += Math.pow(Math.abs(x.doubleValueOf(k)), F);
        }

        return value;
    }
}
