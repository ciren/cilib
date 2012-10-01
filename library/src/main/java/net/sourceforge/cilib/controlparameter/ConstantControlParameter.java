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

import com.google.common.base.Objects;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * to represent a constant value. The specified value will be maintained until it is altered.
 */
public class ConstantControlParameter implements ControlParameter {
    private static final long serialVersionUID = 8847038781478109426L;
    protected double parameter;


    public static ControlParameter of(double value) {
        return new ConstantControlParameter(value);
    }

    /**
     * Create a new instance of {@code ConstantControlParameter}.
     */
    public ConstantControlParameter() {

    }

    /**
     * Create a new instance of {@linkplain net.sourceforge.cilib.controlparameter.ConstantControlParameter}
     * with the provided value as the value for the {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter}.
     * @param value The value to set.
     */
    protected ConstantControlParameter(double value) {
        this.parameter = value;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ConstantControlParameter(ConstantControlParameter copy) {
        this.parameter = copy.parameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConstantControlParameter getClone() {
        return new ConstantControlParameter(this);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ConstantControlParameter) && equals((ConstantControlParameter)o);
    }

    private boolean equals(ConstantControlParameter other) {
        return Objects.equal(parameter, other.getParameter());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getParameter() {
        return parameter;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("ConstantControlParameter has no bounds. Use a BoundedControlParameter instead.");
    }

    /**
     * Sets the constant parameter.
     * 
     * @param value The new constant parameter.
     */
    public void setParameter(double value) {
        this.parameter = value;
    }
}
