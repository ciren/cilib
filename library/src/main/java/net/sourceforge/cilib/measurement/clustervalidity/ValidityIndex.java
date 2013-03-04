/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Abstract class for the cluster validity indexes
 */
public abstract class ValidityIndex implements Measurement<Real> {
    protected DistanceMeasure distanceMeasure;

    /*
     * Default constructor for ValidityIndex
     */
    public ValidityIndex() {
        distanceMeasure = new EuclideanDistanceMeasure();
    }

    /*
     * Copy constructor for ValidityIndex
     * @param copy The ValidityIndex to be copied
     */
    public ValidityIndex(ValidityIndex copy) {
        distanceMeasure = copy.distanceMeasure;
    }

    /*
     * Abstract Clone method of the ValidityIndex
     */
    @Override
    public abstract Measurement<Real> getClone();

    /*
     * Abstract method to get value for validity index
     * @param algorithm The algorithm for which validity indexes are being calculated
     */
    @Override
    public abstract Real getValue(Algorithm algorithm);

    /*
     * Sets the distanceMeasure that will be used when calculating validity indexes
     * @param distanceMeasure The new distance measure
     */
    public void setDistanceMeasure(DistanceMeasure measure) {
        distanceMeasure = measure;
    }

    /*
     * Returns the distanceMeasure that is being used when calculating validity indexes
     * @return distanceMeasure The distance measure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

}
