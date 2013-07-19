/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 * This measurement functions as a wrapper for Measurement<Real>. It keeps track
 * of the maximum returned by the wrapped measurement.
 *
 * This wrapper does not ensure that measurements are taken at each iteration;
 * it only takes new measurements when it is called. This implies that the
 * measurement resolution must be set to 1 for it to function correctly.
 */
public class MaximumOfMeasurement implements Measurement<Real> {

    private Measurement<Real> measurement;
    private double maximum;

    public MaximumOfMeasurement() {
        maximum = Double.NEGATIVE_INFINITY;
    }

    public MaximumOfMeasurement(MaximumOfMeasurement rhs) {
        measurement = rhs.measurement.getClone();
        maximum = rhs.maximum;
    }

    @Override
    public MaximumOfMeasurement getClone() {
        return new MaximumOfMeasurement(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        maximum = Math.max(maximum, measurement.getValue(algorithm).doubleValue());
        return Real.valueOf(maximum);
    }

    public void setMeasurement(Measurement<Real> measurement) {
        this.measurement = measurement;
    }
}
