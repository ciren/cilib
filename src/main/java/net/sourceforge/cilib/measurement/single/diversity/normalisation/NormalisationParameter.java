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
package net.sourceforge.cilib.measurement.single.diversity.normalisation;

import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * TODO: Complete this javadoc.
 */
public class NormalisationParameter {

    protected double normalisationParameter;
    protected DistanceMeasure distanceMeasure;

    public NormalisationParameter() {
        normalisationParameter = 1.0;
        distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * @return the normalising parameter
     */
    public double getValue() {
        return this.normalisationParameter;
    }

    /**
     * @param
     *
     * set the value to be used as a normalisation parameter
     */
    public void setNormalisationParameter(double value) {
        this.normalisationParameter = value;
    }

    /**
     * @return the distance measure used in the calculation
     */
    public DistanceMeasure getDistanceMeasure() {
        return this.distanceMeasure;
    }

    /**
     * @param distance The distance measure to be used in the normalisation calculation
     */
    public void setDistanceMeasure(DistanceMeasure distance) {
        this.distanceMeasure = distance;
    }

}
