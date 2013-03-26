/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1mod_zhou;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the FDA1_mod problem defined in the following paper:
 * A. Zhou et al. Prediction-based population re-initialization for evolutionary dynamic
 * multi-objective optimization, In Proceedings of the International Conference on
 * Evolutionary Multi-Criterion Optimization (EMO), Lecture  * Notes in Computer Science,
 * 4403:832-846, Springer-Verlag Berlin/Heidelberg, 2007.
 *
 */

public class FDA1_g implements ContinuousFunction {

    private static final long serialVersionUID = 1721209032942724811L;

    //members
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;

    /**
     * Creates a new instance of FDA1_g.
     */
    public FDA1_g() {
        //initialize the members
        this.tau_t =  5;
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
        double t = (1.0/(double)n_t)*Math.floor((double)iteration/(double)this.tau_t);
        double G = Math.sin(0.5*Math.PI*t);
        double H = 1.5 + (double)G;

        double sum = 1.0;
        for (int k=1; k < x.size(); k++) {
        	sum += Math.pow(x.doubleValueOf(k) + G - Math.pow(x.doubleValueOf(0), H), 2);
        }
        return sum;
    }
}
