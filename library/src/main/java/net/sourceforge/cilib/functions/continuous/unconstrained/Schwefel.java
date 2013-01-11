/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Schwefel function.
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Separable</li>
 * <li>Discontinuous</li>
 * </ul>
 *
 * f(x) = 0; x = (-420.9687,...,-420.9687);
 *
 * x e [-512.03,511.97]
 *
 * R(-512.03, 511.97)^30
 *
 */
public class Schwefel implements ContinuousFunction {

    private static final long serialVersionUID = 3835871629510784855L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;
        for (int i = 0; i < input.size(); ++i) {
            sum += -input.doubleValueOf(i) * Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i))));
        }
        return sum + input.size() * 4.18982887272434686131e+02;
    }
}
