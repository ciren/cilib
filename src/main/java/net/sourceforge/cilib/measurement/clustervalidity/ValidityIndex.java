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
 *
 * @author Kristina
 */
public class ValidityIndex implements Measurement<Real> {
    protected DistanceMeasure distanceMeasure;
    
    public ValidityIndex() {
        distanceMeasure = new EuclideanDistanceMeasure();
    }
    
    public ValidityIndex(ValidityIndex copy) {
        distanceMeasure = copy.distanceMeasure;
    }
    
    @Override
    public Measurement<Real> getClone() {
        return new ValidityIndex(this);
    }

    @Override
    public String getDomain() {
        return "R";
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        return Real.valueOf(algorithm.getBestSolution().getFitness().getValue().doubleValue());
    }

    public void setDistanceMeasure(DistanceMeasure measure) {
        distanceMeasure = measure;
    }
    
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }
    
}
