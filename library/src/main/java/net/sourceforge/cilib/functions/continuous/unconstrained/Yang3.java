/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;

/**
 * Yang Function 3
 *
 * NOTE: f(x*) is random, rather than a fixed value.
 * <p>
 * Reference:
 * <p>
 * X.S. Yang, "Firefly algorithm, stochastic test functions and design
 * optimisation." International Journal of Bio-Inspired Computation 2.2 (2010):
 * 78-84.
 * <p>
 *
 * -(K^2 + 5) &lt;= f(x*) &lt;= -5; x* = (pi,pi)
 *
 * R(0:K)^2
 */
public class Yang3 extends ContinuousFunction {
    private ControlParameter alpha;
    private ControlParameter beta;
    private ControlParameter K;

    public Yang3() {
        alpha = ConstantControlParameter.of(1);
        beta = ConstantControlParameter.of(2);
        K = ConstantControlParameter.of(10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "Yang3 function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        double term1 = -5.0 * Math.exp(-beta.getParameter()
            * (Math.pow(x - Math.PI, 2) + Math.pow(y - Math.PI, 2)));

        double term2 = 0;
        int k = (int) K.getParameter();
        for (int j = 0; j < k; j++) {
            for (int i = 0; i < k; i++) {
                term2 += Rand.nextDouble() * Math.exp(-alpha.getParameter()
                    * (Math.pow(x - (i + 1), 2) + Math.pow(y - (j + 1), 2)));
            }
        }

        return term1 - term2;
    }

    /**
     * Set the {@code alpha} {@linkplain ControlParameter}.
     *
     * @param alpha  the {@code alpha} {@linkplain ControlParameter}.
     */
    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
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
     * Set the {@code K} {@linkplain ControlParameter}.
     *
     * @param K  the {@code K} {@linkplain ControlParameter}.
     */
    public void setK(ControlParameter K) {
        this.K = K;
    }
}
