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

import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class BoundedModifiableControlParameter implements BoundedControlParameter {
    private Real parameter;
    private double velocity;
    private boolean wasInitialySetByUser;

    /**
     * Create an instance of {@code LinearDecreasingControlParameter}.
     */
    public BoundedModifiableControlParameter() {
        parameter = Real.valueOf(0.0);
        velocity = 0;
        wasInitialySetByUser = false;
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BoundedModifiableControlParameter(BoundedModifiableControlParameter copy) {
        this.parameter = copy.parameter.getClone();
        velocity = copy.velocity;
        wasInitialySetByUser = copy.wasInitialySetByUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundedModifiableControlParameter getClone() {
        return new BoundedModifiableControlParameter(this);
    }

    /**
     * Update the parameter linearly based on the current percentage complete of the running
     * {@linkplain net.sourceforge.cilib.algorithm.Algorithm algorithm}.
     * The update is done in an increasing manner.
     */
    @Override
    public void updateParameter() {
       
    }
    
    /*
     * Update the current parameter to the value provided
     * @param value The value to update the parameter to
     */
    public void updateParameter(double value) {
        parameter = Real.valueOf(value, parameter.getBounds());
    }

    /**
     * Set the lower bound of the parameter
     * @param lower The lower bound
     */
    @Override
    public void setLowerBound(double lower) {
        Bounds current = parameter.getBounds();
        this.parameter = Real.valueOf(lower, new Bounds(lower, current.getUpperBound()));
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
    
    /**
     * Get the lower bund of the parameter
     * @return The lower bound
     */
    @Override
    public double getLowerBound() {
        return parameter.getBounds().getLowerBound();
    }

    /**
     * Get the upper bund of the parameter
     * @return The upper bound
     */
    @Override
    public double getUpperBound() {
        return parameter.getBounds().getUpperBound();
    }

    /**
     * Set the upper bound of the parameter to the value provided
     * @param value The new value of the upper bound
     */
    @Override
    public void setUpperBound(double value) {
        this.parameter = Real.valueOf(value, new Bounds(parameter.getBounds().getLowerBound(), value));
    }

    /**
     * Set the value of the range to the one in the string provided
     * @param range The new range
     */
    @Override
    public void setRange(String range) {
        Vector v = (Vector) DomainParser.parse(range);

        if (v.size() != 1) {
            throw new RuntimeException("Range incorrect in BoundedUpdateStrategy! Please correct");
        } else {
            this.parameter = (Real) v.get(0);
        }
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
    
}
