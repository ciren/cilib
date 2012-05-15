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

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * to represent a constant value. The specified value will be maintained until it is altered.
 */
public class ConstantControlParameter implements ParameterAdaptingControlParameter{
    private static final long serialVersionUID = 8847038781478109426L;
    protected double parameter;
    private ParameterAdaptingControlParameter bestValue;
    
    public static ParameterAdaptingControlParameter of(double value) {
        return new ConstantControlParameter(value);
    }

    private double velocity;

    /**
     * Create a new instance of {@code ConstantControlParameter}.
     */
    public ConstantControlParameter() {
        velocity = 0;
        bestValue = new ConstantControlParameter(this);
    }

    /**
     * Create a new instance of {@linkplain net.sourceforge.cilib.controlparameter.ConstantControlParameter}
     * with the provided value as the value for the {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter}.
     * @param value The value to set.
     */
    protected ConstantControlParameter(double value) {
        this.parameter = value;
        velocity = 0;
        bestValue = new ConstantControlParameter();
        bestValue.setParameter(value);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ConstantControlParameter(ConstantControlParameter copy) {
        this.parameter = copy.parameter;
        this.bestValue = copy.bestValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConstantControlParameter getClone() {
        return new ConstantControlParameter(this);
    }

    /**
     * Get the parameter value
     * @return The parameter value
     */
    @Override
    public double getParameter() {
        return parameter;
    }
    
    /**
     * Get the parameter value
     * @return The parameter value
     */
    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("ConstantControlParameter has no bounds. Use a BoundedControlParameter instead.");
    }

    /**
     * Set the value of the parameter to the value provided
     * @param value The new value of the parameter
     */
    @Override
    public void setParameter(double value) {
        this.parameter = value;
    }
    
    @Override
    public boolean wasSetByUser() {
        return true;
    }
    
    /*
     * Not applicable to this class as it is constant
     * @param value The value to update the parameter to
     */
    @Override
    public void updateParameter(double value) {
        //Nothing to update
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
     * Set the current velocity of the parameter to the value provided. Nothing happens as it is a constant class
     * @param value The new velocity value
     */
    @Override
    public void setVelocity(double value) {
        //Nothing to change
    }
    
    @Override
    public void setBestValue(double value) {
        bestValue.setParameter(value);
    }
    
    @Override
    public ParameterAdaptingControlParameter getBestValue() {
        return bestValue;
    }
}
