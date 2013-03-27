/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dmop1;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the DMOP1 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */
public class DMOP1_g implements ContinuousFunction {

    private static final long serialVersionUID = -9042109015612284066L;
    //number of distinct steps in t
    private int n_t;
    //number of generations for which t remains fixed
    private int tau_t;

    /**
     * Creates a new instance of DMOP1_g.
     */
    public DMOP1_g() {
        this.n_t = 10;
        this.tau_t = 5;
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
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {

        double sum = 0.0;
        for (int k = 0; k < x.size(); k++) {
            sum += Math.pow(x.doubleValueOf(k), 2);
        }
        sum *= 9.0;
        sum += 1.0;
        return sum;
    }
}
