/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;

/**
 * Yang Function 1
 * <p>
 * Reference:
 * <p>
 * X.S. Yang, "Firefly algorithm, stochastic test functions and design
 * optimisation." International Journal of Bio-Inspired Computation 2.2 (2010):
 * 78-84.
 * <p>
 *
 * f(x*) = -1; x* = (pi,pi,...,pi); Beta = 15
 *
 * R(-20:20)^D
 */
public class Yang1 extends ContinuousFunction {
    private ControlParameter beta;
    private ControlParameter m;

    public Yang1() {
        this.beta = ConstantControlParameter.of(15);
        this.m = ConstantControlParameter.of(5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum1 = 0;
        double sum2 = 0;
        double product = 1;

        for (Numeric n : input) {
            double x = n.doubleValue();
            sum1 += Math.pow(x / beta.getParameter(), 2 * m.getParameter());
            sum2 += (x - Math.PI) * (x - Math.PI);
            product *= Math.cos(x) * Math.cos(x);
        }

        return (Math.exp(-sum1) - 2.0 * Math.exp(-sum2)) * product;
    }

    /**
     * Set the {@code beta} {@linkplain ControlParameter}.
     *
     * @param beta  the {@code beta} {@linkplain ControlParameter}.
     */
    public void setBeta(ControlParameter beta) {
        this.beta = beta;
    }

    /**
     * Set the {@code m} {@linkplain ControlParameter}.
     *
     * @param m  the {@code m} {@linkplain ControlParameter}.
     */
    public void setM(ControlParameter m) {
        this.m = m;
    }
}
