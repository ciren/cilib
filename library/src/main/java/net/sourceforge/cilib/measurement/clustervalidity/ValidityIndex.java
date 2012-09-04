/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

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
     * Abstract method to get value fo validity index
     * @param algorithm The algorithm for which validity indexes are being calculated
     */
    @Override
    public abstract Real getValue(Algorithm algorithm);

    /*
     * Sets the distanceMeasure that will be used when calculating validity inexes
     * @param distanceMeasure The new distance measure
     */
    public void setDistanceMeasure(DistanceMeasure measure) {
        distanceMeasure = measure;
    }

    /*
     * Retruns the distanceMeasure that is being used when calculating validity inexes
     * @return distanceMeasure The distance measure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

}
