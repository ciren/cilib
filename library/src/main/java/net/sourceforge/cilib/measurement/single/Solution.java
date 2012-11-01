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
 */
public class Solution implements Measurement<StringType> {
    private static final long serialVersionUID = -7485598441585703760L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Solution getClone() {
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
