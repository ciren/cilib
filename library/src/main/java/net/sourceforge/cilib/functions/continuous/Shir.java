/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The Damavandi function obtained from O.M. Shir and T. Baeck,
 * "Dynamic Niching in Evolution Strategies with Covariance Matrix Adaptation"
 *
 * Global Maximin: f(x1,...,xn) = 1
 *
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * </ul>
 *
 * Maximum: 1.0
 * R(0, 1)^30
 *
 */
public class Shir implements ContinuousFunction {

    private static final long serialVersionUID = 8157687561496975789L;
    private double l1, l2, l3, l4, l5, sharpness;

    /**
     * Create an instance of the function. The domain is set to "R(0, 1)^30" by default.
     */
    public Shir() {
        l1 = 1.0;
        l2 = 1.0;
        l3 = 1.0;
        l4 = 1.0;
        l5 = 1.0;
        sharpness = 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sinTerm;
        double expTerm;
        double product = 1.0;

        for (int i = 0; i < input.size(); i++) {
            sinTerm = 1.0;
            for (int k = 1; k <= sharpness; k++) {
                sinTerm *= Math.sin(l1 * Math.PI * input.doubleValueOf(i) + l2);
            }
            expTerm = Math.exp(-l3 * ((input.doubleValueOf(i) - l4) / l5) * ((input.doubleValueOf(i) - l4) / l5));
            product *= (sinTerm * expTerm);
        }

        return product;
    }
}
