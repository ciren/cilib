/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.isolated.dmop2;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the DMOP2 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */
public class DMOP2_g implements ContinuousFunction {

    private static final long serialVersionUID = -7743860912793353675L;
    //members
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;
    //value at which y is mapped to zero
    private double A;
    //apterture size of the well/basin leading to the global minimum at A
    private double B;
    //deceptive minima
    private double C;

    /**
     * Creates a new instance of DMOP2_g.
     */
    public DMOP2_g() {
        //initialize the members
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 10;
        A = 0.35;
        B = 0.001;
        C = 0.05;
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
     * Set the value at which y maps to zero
     * @param a Value at which y mapts to zero
     */
    public void setA(double a) {
        this.A = a;
    }

    /**
     * Set the aperture size of the well/basin leading to the global minimum
     * @param b Aperture size
     */
    public void setB(double b) {
        this.B = b;
    }

    /**
     * Set the value of the deceptive minima
     * @param c Value of deceptive minima
     */
    public void setC(double c) {
        this.C = c;
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
        double G = Math.sin(0.5 * Math.PI * t);

        double sum = 1.0;
        for (int k = 0; k < x.size(); k++) {
            this.A = G;
            double term1 = Math.min(0, Math.floor(x.doubleValueOf(k) - B));
            double frac = A * (B - x.doubleValueOf(k)) / B;
            term1 = term1 * frac;
            double term2 = Math.min(0, Math.floor(C - x.doubleValueOf(k)));
            frac = ((1 - A) * (x.doubleValueOf(k) - C)) / (1.0 - C);
            term2 = term2 * frac;

            double y = A + term1 - term2;
            sum += Math.pow(y - G, 2);
        }
        return sum;
    }
}
