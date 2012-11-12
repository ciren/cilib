/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the h function of the FDA2 problem defined on page 429 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 * R(-1, 1)^31
 *
 */
public class FDA2_h implements ContinuousFunction {

    private static final long serialVersionUID = -637862405309737323L;

    private ContinuousFunction fda2_f;
    private ContinuousFunction fda2_g;
    private int tau_t; //number of generations for which t remains fixed
    private int tau; //generation counter
    private int n_t; //number of distinct steps in t

    /**
     * Default constructor
     */
    public FDA2_h() {
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 10;
    }

    /**
     * copy constructor
     * @param copy
     */
    public FDA2_h(FDA2_h copy) {
        this.tau = copy.tau;
        this.tau_t = copy.tau_t;
        this.n_t = copy.n_t;
        this.fda2_f = copy.fda2_f;
        this.fda2_g = copy.fda2_g;
    }

    /**
     * sets the f function that is used the FDA2 function
     * @param fda1_f
     */
    public void setFDA2_f(ContinuousFunction fda2_f) {
        this.fda2_f = fda2_f;
    }

    /**
     * Returns the f function that is used in the FDA2 function
     * @return
     */
    public ContinuousFunction getFDA2_f() {
        return this.fda2_f;
    }

    /**
     * Sets the g function that is used in the FDA2 function
     * @param fda1_g
     */
    public void setFDA2_g(ContinuousFunction fda2_g) {
        this.fda2_g = fda2_g;
    }

    /**
     * Returns the g function that is used in the FDA2 function
     * @return
     */
    public ContinuousFunction getFDA2_g() {
        return this.fda2_g;
    }

    /**
     * sets the iteration number
     * @param tau
     */
    public void setTau(int tau) {
        this.tau = tau;
    }

    /**
     * returns the iteration number
     * @return tau
     */
    public int getTau() {
        return this.tau;
    }

    /**
     * sets the frequency of change
     * @param tau
     */
    public void setTau_t(int tau_t) {
        this.tau_t = tau_t;
    }

    /**
     * returns the frequency of change
     * @return tau_t
     */
    public int getTau_t() {
        return this.tau_t;
    }

    /**
     * sets the severity of change
     * @param n_t
     */
    public void setN_t(int n_t) {
        this.n_t = n_t;
    }

    /**
     * returns the severity of change
     * @return n_t
     */
    public int getN_t() {
        return this.n_t;
    }

    /**
     * Evaluates the function
     * h(X_III, f_1, g) = 1-(f_1/g)^(H(t) + sum(x_i-H(t))^2)^(-1)
     */
    @Override
    public Double apply(Vector input) {
        this.tau = AbstractAlgorithm.get().getIterations();

        double t = (1.0 / (double) n_t) * Math.floor((double) this.tau / (double) this.tau_t);
        double H = 0.75 + 0.7 * (Math.sin(0.5 * Math.PI * t));

        Vector xI = input;
        Vector xII = input;
        Vector xIII = input;
        if (input.size() > 1) {
            xI = input.copyOfRange(0, 1);
            xII = input.copyOfRange(1, 16);
            xIII = input.copyOfRange(16, input.size());
        }

        double f = this.fda2_f.apply(xI);
        double g = this.fda2_g.apply(xII);

        double value = 1.0;
        double power = H;

        for (int k=0; k < xIII.size(); k++) {
            power += Math.pow(xIII.doubleValueOf(k) - H, 2);
        }

        power = Math.pow(power, -1);
        double f_div_g = f / g;
        value -= Math.pow(f_div_g, power);

        return value;
    }
}
