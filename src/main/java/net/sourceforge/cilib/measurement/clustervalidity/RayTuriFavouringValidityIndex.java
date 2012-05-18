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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 *
 * @author Kristina
 */
public class RayTuriFavouringValidityIndex extends ValidityIndex{
    private ControlParameter scalingConstant;
    private RayTuriValidityIndex delegate;
    private ControlParameter mean;
    private ControlParameter standardDeviation;
    
    public RayTuriFavouringValidityIndex() {
        scalingConstant = ConstantControlParameter.of(20.0);
        delegate = new RayTuriValidityIndex();
        mean = ConstantControlParameter.of(0.0);
        standardDeviation = ConstantControlParameter.of(1.0);
    }
    
    public RayTuriFavouringValidityIndex(RayTuriFavouringValidityIndex copy) {
        scalingConstant = copy.scalingConstant;
        delegate = copy.delegate;
        mean = copy.mean;
        standardDeviation = copy.standardDeviation;
    }
    
    @Override
    public RayTuriFavouringValidityIndex getClone() {
        return new RayTuriFavouringValidityIndex(this);
    }
    
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double result = delegate.getValue(algorithm).doubleValue() * ((scalingConstant.getParameter() * getGaussianValue(holder)) + 1);
        
        return Real.valueOf(result);
    }
    
    protected double getGaussianValue(CentroidHolder holder) {
        double power = -1 * (Math.pow((holder.size() - mean.getParameter()), 2) / (2 * Math.pow(standardDeviation.getParameter(), 2)));
        double bottomOfEquation = Math.sqrt(2 * Math.PI * Math.pow(standardDeviation.getParameter(), 2));
        double result = (1 / bottomOfEquation) * Math.exp(power);
        
        return result;
    }
}
