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
package net.sourceforge.cilib.controlparameter;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * to represent a constant value. The specified value will be maintained until it is altered.
 *
 * @author Gary Pampara
 */
public class ConstantControlParameter implements ControlParameter {
    private static final long serialVersionUID = 8847038781478109426L;
    protected double parameter;

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
    public ConstantControlParameter(double value) {
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
    public ConstantControlParameter getClone() {
        return new ConstantControlParameter(this);
    }

    /**
     * {@inheritDoc}
     */
    public double getParameter() {
        return parameter;
    }

    /**
     * {@inheritDoc}
     */
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("This method is not supported");
    }

    /**
     * {@inheritDoc}
     */
    public void setParameter(double value) {
        this.parameter = value;
    }

    /**
     * {@inheritDoc}
     */
    public void updateParameter() {
        // Nothing to update - This paramter is constant.
    }
}
