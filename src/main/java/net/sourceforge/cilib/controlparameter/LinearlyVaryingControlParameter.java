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
 * A Control parameter that varies linearly according to the completed percentage of the algorithm.
 */
public class LinearlyVaryingControlParameter implements ControlParameter {
    
    private double initialValue;
    private double finalValue;
    
    public LinearlyVaryingControlParameter() {
        this(0.0, Double.MAX_VALUE);
    }

    public LinearlyVaryingControlParameter(double initialValue, double finalValue) {
        this.initialValue = initialValue;
        this.finalValue = finalValue;
    }
    
    public LinearlyVaryingControlParameter(LinearlyVaryingControlParameter copy) {
        this.initialValue = copy.initialValue;
        this.finalValue = copy.finalValue;
    }
    
    @Override
    public LinearlyVaryingControlParameter getClone() {
        return new LinearlyVaryingControlParameter(this);
    }

    @Override
    public double getParameter() {
        return getParameter(initialValue, finalValue);
    }
    
    @Override
    public double getParameter(double initialVal, double finalVal) {
        return initialVal + (finalVal - initialVal) * AbstractAlgorithm.get().getPercentageComplete();
    } 

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public void setFinalValue(double finalValue) {
        this.finalValue = finalValue;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public double getFinalValue() {
        return finalValue;
    }
}
