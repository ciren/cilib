/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.entropy;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * An interface to allow entropy measurements to be performed on an algorithm.
 *
 */
public abstract class EntropyMeasurement implements Measurement<TypeList> {
    protected int intervals; //the number of intervals over which entropy is measured

    @Override
    public abstract TypeList getValue(Algorithm algorithm);

    /**
     * Set the number of intervals over which entropy is measured.
     *
     * @param k The number of intervals to set.
     */
    public void setIntervals(int k) {
        intervals = k;
    }

    /**
     * Get the number of intervals over which entropy is measured.
     *
     * @return The number of intervals over which entropy is measured.
     */
    public int getIntervals() {
        return intervals;
    }

}
