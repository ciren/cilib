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

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;

/**
 * Diversity normalisation based on a control parameter.
 */
public class NormalisationParameter implements DiversityNormalisation {

    protected ControlParameter normalisationParameter;

    /**
     * Default constructor.
     */
    public NormalisationParameter() {
        normalisationParameter = ConstantControlParameter.of(1.0);
    }

    /**
     * @return the normalising parameter
     */
    @Override
    public double getNormalisationParameter(PopulationBasedAlgorithm algorithm) {
        return this.normalisationParameter.getParameter();
    }

    /**
     * Set the value to be used as a normalisation parameter
     * @param value The new normalisation parameter.
     */
    public void setNormalisationParameter(ControlParameter value) {
        this.normalisationParameter = value;
    }

    /**
     * Gets the normalisation parameter.
     * @return 
     */
    public ControlParameter getNormalisationParameter() {
        return normalisationParameter;
    }
}
