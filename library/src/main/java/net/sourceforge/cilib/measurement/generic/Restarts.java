/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.MultistartOptimisationAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;

/**
 *
 */
public class Restarts implements Measurement<Int> {
    private static final long serialVersionUID = 3990735185462072444L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Restarts getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        MultistartOptimisationAlgorithm m = (MultistartOptimisationAlgorithm) algorithm;

        return Int.valueOf(m.getRestarts());
    }

}
