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

import net.sourceforge.cilib.type.types.Bounds;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter} instance
 * that is defined to operate within a specific range of values. The range is defined as a domain string.
 * Any time the parameter exceeds the bounded range, it will be clamped and brought back to remain on
 * the edges of the range specified.
 *
 */
public class BoundedControlParameter implements ControlParameter {
    
    private Bounds bounds;
    private ControlParameter controlParameter;
    
    public BoundedControlParameter() {
        this.bounds = new Bounds(-Double.MAX_VALUE, Double.MAX_VALUE);
        this.controlParameter = new LinearlyVaryingControlParameter();
    }
    
    public BoundedControlParameter(BoundedControlParameter copy) {
        this.bounds = copy.bounds;
        this.controlParameter = copy.controlParameter.getClone();
    }
    
    @Override
    public BoundedControlParameter getClone() {
        return new BoundedControlParameter(this);
    }
    
    @Override
    public double getParameter() {
        return getParameter(bounds.getLowerBound(), bounds.getUpperBound());
    }
    
    @Override
    public double getParameter(double min, double max) {
        return Math.max(min, Math.min(max, controlParameter.getParameter()));
    }

    public Bounds getBounds() {
        return this.bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public ControlParameter getControlParameter() {
        return controlParameter;
    }

    public void setControlParameter(ControlParameter controlParameter) {
        this.controlParameter = controlParameter;
    }
}
