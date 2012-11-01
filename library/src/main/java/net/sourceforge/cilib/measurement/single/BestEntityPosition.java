/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Print the position of the best entity within a topology.
 *
 */
public class BestEntityPosition implements Measurement<StringType> {
    private static final long serialVersionUID = 5808686984197365658L;

    /**
     * {@inheritDoc}
     */
    @Override
    public BestEntityPosition getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringType getValue(Algorithm algorithm) {
        Vector solution = (Vector) algorithm.getBestSolution().getPosition();
        return new StringType(solution.toString());
    }

}
