/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
