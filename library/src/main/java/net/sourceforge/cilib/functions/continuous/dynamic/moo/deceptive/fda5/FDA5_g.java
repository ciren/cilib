/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.deceptive.fda5;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the FDA5 problem defined on page 430 in
 * the following article: M.Farina, K.Deb, P.Amato. Dynamic multiobjective
 * optimization problems: test cases, approximations and applications, IEEE
 * Transactions on Evolutionary Computation, 8(5): 425-442
 */
public class FDA5_g implements ContinuousFunction {

    private static final long serialVersionUID = -3007792025594376529L;
    //	members
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;
    //counter
    private double counter;
    int M;
    int n;
    //value at which y is mapped to zero
    private double A;
    //apterture size of the well/basin leading to the global minimum at A
    private double B;
    //deceptive minima
    private double C;

    /**
     * Creates a new instance of FDA5_g.
     */
    public FDA5_g() {
        super();
        //set the dimension
        this.M = 3;
        this.n = M + 9;
        int dim = n - M + 1;
        //initialize the members
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 10;
        this.counter = 0.0;
        A = 0.35;
        B = 0.001;
        C = 0.05;
    }

    /**
     * Sets the number of objectives.
     *
     * @param m Number of objectives.
     */
    public void setM(int m) {
        this.M = m;
        this.n = this.M + 9;
    }

    /**
     * Returns the number of objectives.
     *
     * @return M Number of objectives.
     */
    public int getM() {
        return this.M;
    }

    /**
     * Sets the number of variables.
     *
     * @param n Number of variables.
     */
    public void setN(int n) {
        this.n = n;
        this.M = this.n - 9;
    }

    /**
     * Returns the number of variables.
     *
     * @return n Number of variables.
     */
    public int getN() {
        return this.n;
    }

    /**
     * Sets the iteration number.
     *
     * @param tau Iteration number.
     */
    public void setTau(int tau) {
        this.tau = tau;
    }

    /**
     * Returns the iteration number.
     *
     * @return tau Iteration number.
     */
    public int getTau() {
        return this.tau;
    }

    /**
     * Sets the frequency of change.
     *
     * @param tau_t Change frequency.
     */
    public void setTau_t(int tau_t) {
        this.tau_t = tau_t;
    }

    /**
     * Returns the frequency of change.
     *
     * @return tau_t Change frequency.
     */
    public int getTau_t() {
        return this.tau_t;
    }

    /**
     * Sets the severity of change.
     *
     * @param n_t Change severity.
     */
    public void setN_t(int n_t) {
        this.n_t = n_t;
    }

    /**
     * Returns the severity of change.
     *
     * @return n_t Change severity.
     */
    public int getN_t() {
        return this.n_t;
    }

    /**
     * Set the value at which y maps to zero
     *
     * @param a Value at which y mapts to zero
     */
    public void setA(double a) {
        this.A = a;
    }

    /**
     * Set the aperture size of the well/basin leading to the global minimum
     *
     * @param b Aperture size
     */
    public void setB(double b) {
        this.B = b;
    }

    /**
     * Set the value of the deceptive minima
     *
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
        return this.apply(this.tau, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public Double apply(int iteration, Vector x) {
        double t = (1.0 / (double) n_t) * Math.floor((double) iteration / (double) this.tau_t);
        double G = Math.abs(Math.sin(0.5 * Math.PI * t));

        double value = G;

        for (int k = 0; k < x.size(); k++) {
            if (G >= 1.0) {
                this.A = 1.0 - B;
            } else if (G > 0.35) {
                this.A = G;
            } else {
                this.A = 0.35;
            }

            double term1 = Math.floor(x.doubleValueOf(k) - A + B);
            double frac = ((A - B) / (B)) + 1 - C;
            term1 = (term1 * frac) / (A - B);
            double term2 = Math.floor(A + B - x.doubleValueOf(k));
            frac = ((1 - A - B) / (B)) + 1 - C;
            term2 = (term2 * frac) / (1 - A - B);
            double term3 = 1 / B;
            double y = 1 + (Math.abs(x.doubleValueOf(k) - A) - B) * (term1 + term2 + term3);

            value += Math.pow((y - G), 2);
        }

        return value;
    }
}
