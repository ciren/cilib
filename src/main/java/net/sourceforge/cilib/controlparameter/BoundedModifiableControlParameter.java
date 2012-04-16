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

import net.sourceforge.cilib.type.types.Real;

/**
 * This class represents a control parameter which can be changed by a ParametizedParticle when
 * the particle's position changes. The updateParameter(double value) method is used for this
 * update.
 * 
 */
public class BoundedModifiableControlParameter implements ParameterAdaptingControlParameter {
    private Real parameter;
    private double velocity;
    private boolean wasInitialySetByUser;
    private ParameterAdaptingControlParameter bestValue;
    private double lowerBound;
    private double upperBound;

    /**
     * Create an instance of {@code LinearDecreasingControlParameter}.
     */
    public BoundedModifiableControlParameter() {
        parameter = Real.valueOf(0.0);
        velocity = 0;
        wasInitialySetByUser = false;
        bestValue = new BoundedModifiableControlParameter(this);
        lowerBound = 0.1;
        upperBound = 0.9;
    }
    
    public BoundedModifiableControlParameter(double value, double lowerBoundIn, double upperBoundIn) {
        parameter = Real.valueOf(value);
        velocity = 0;
        wasInitialySetByUser = false;
        bestValue = new BoundedModifiableControlParameter(this);
        lowerBound = lowerBoundIn;
        upperBound = upperBoundIn;
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BoundedModifiableControlParameter(BoundedModifiableControlParameter copy) {
        this.parameter = copy.parameter.getClone();
        velocity = copy.velocity;
        wasInitialySetByUser = copy.wasInitialySetByUser;
        bestValue = copy.bestValue;
        lowerBound = copy.lowerBound;
        upperBound = copy.upperBound;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundedModifiableControlParameter getClone() {
        return new BoundedModifiableControlParameter(this);
    }
    
    /*
     * Update the current parameter to the value provided
     * @param value The value to update the parameter to
     */
    @Override
    public void updateParameter(double value) {
        parameter = Real.valueOf(value, parameter.getBounds());
    }

    /**
     * Get the parameter value
     * @return The parameter value
     */
    @Override
    public double getParameter() {
        return parameter.doubleValue();
    }

    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Set the value of the parameter to the value provided
     * @param value The new value of the parameter
     */
    @Override
    public void setParameter(double value) {
        this.parameter = Real.valueOf(value, parameter.getBounds());
        this.wasInitialySetByUser = true;
    }
    
    @Override
    public boolean wasSetByUser() {
        return wasInitialySetByUser;
    }
    
    /*
     * Get the current velocity of the parameter
     * @return The current velocity of the parameter
     */
    @Override
    public double getVelocity() {
        return velocity;
    }
    
    /*
     * Set the current velocity of the parameter
     */
    @Override
    public void setVelocity(double value) {
        velocity = value;
    }
    
    @Override
    public void setBestValue(double value) {
        bestValue.setParameter(value);
    }
    
    @Override
    public ParameterAdaptingControlParameter getBestValue() {
        return bestValue;
    }
    
    public double getLowerBound() {
        return lowerBound;
    }
    
    public double getUpperBound() {
        return upperBound;
    }
    
    public void setLowerBound(double bound) {
        lowerBound = bound;
    }
    
    public void setUpperBound(double bound) {
        upperBound = bound;
    }
}
