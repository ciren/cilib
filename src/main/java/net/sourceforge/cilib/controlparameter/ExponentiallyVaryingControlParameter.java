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
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;

/**
 *
 * @author filipe
 */
public class ExponentiallyVaryingControlParameter implements ControlParameter {
    
    private double initialValue;
    private double finalValue;
    private double curve;
    
    public ExponentiallyVaryingControlParameter() {
        this.initialValue = 0.0;
        this.finalValue = 1.0;
        this.curve = 1.0;
    }
    
    public ExponentiallyVaryingControlParameter(ExponentiallyVaryingControlParameter copy) {
        this.initialValue = copy.initialValue;
        this.finalValue = copy.finalValue;
        this.curve = copy.curve;
    }

    @Override
    public ExponentiallyVaryingControlParameter getClone() {
        return new ExponentiallyVaryingControlParameter(this);
    }
    
    @Override
    public double getParameter() {
        return getParameter(initialValue, finalValue);
    }
    
    @Override
    public double getParameter(double initialVal, double finalVal) {
        return initialVal + (finalVal - initialVal) 
                * (Math.exp(AbstractAlgorithm.get().getPercentageComplete() * curve) - 1) 
                / (Math.exp(curve) - 1) ;
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setFinalValue(double finalValue) {
        this.finalValue = finalValue;
    }

    public double getFinalValue() {
        return finalValue;
    }

    public void setCurve(double curve) {
        this.curve = curve;
    }

    public double getCurve() {
        return curve;
    }
}
