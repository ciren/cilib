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
 * The continuous Step function. It is the same as the normal step function, however,
 * it is continuous and not discrete.
 *
 * <p>
 * The default domain of the function is defined to be R(-100.0, 100.0)^30
 *
 */
public class ContinuousStep implements ContinuousFunction {

    private static final long serialVersionUID = 4962101545621686038L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); ++i) {
            sum += (input.doubleValueOf(i) + 0.5) * (input.doubleValueOf(i) + 0.5);
        }
        return sum;
    }
}
