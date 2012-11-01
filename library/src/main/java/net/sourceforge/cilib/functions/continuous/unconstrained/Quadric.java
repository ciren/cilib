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
 * <p><b>The Quadric Function</b></p>
 *
 * <p>
 * Minimum:
 * <ul>
 * <li>&fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-100,100]</li>
 * </ul>
 * </p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * </ul>
 * </p>
 *
 * R(-100, 100)^30
 *
 */
public class Quadric implements ContinuousFunction {

    private static final long serialVersionUID = -2555670302543357421L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sumsq = 0;
        for (int i = 0; i < input.size(); ++i) {
            double sum = 0;
            for (int j = 0; j <= i; ++j) {
                sum += input.doubleValueOf(j);
            }
            sumsq += sum * sum;
        }
        return sumsq;
    }
}
