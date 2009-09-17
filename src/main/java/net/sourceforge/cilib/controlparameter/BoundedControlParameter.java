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

import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter} instance
 * that is defined to operate within a specific range of values. The range is defined as a domain string.
 * Any time the parameter exceeds the bounded range, it will be clamped and brought back to remain on
 * the edges of the range specified.
 *
 * @author Gary Pampara
 */
public abstract class BoundedControlParameter implements ControlParameter {
    private static final long serialVersionUID = 3658446987351378005L;

    protected Real parameter;
    protected String range = "";


    /**
     * Create an instance of the {@code BoundedControlParameter}.
     */
    public BoundedControlParameter() {
        this.parameter = new Real();
    }


    /**
     * Create a copy of the provided instance.
     * @param copy The instance which to copy.
     */
    public BoundedControlParameter(BoundedControlParameter copy) {
        this.parameter = copy.parameter.getClone();
        this.range = new String(copy.range);
    }


    /**
     * {@inheritDoc}
     */
    public abstract BoundedControlParameter getClone();


    /**
     * {@inheritDoc}
     */
    public double getParameter() {
        return parameter.getReal();
    }

    /**
     * {@inheritDoc}
     */
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("");
    }


    /**
     * {@inheritDoc}
     */
    public void setParameter(double value) {
        this.parameter.setReal(value);
    }

    /**
     * {@inheritDoc}
     */
    public void updateParameter() {
        update();
        clamp();
    }

    /**
     * Update the paramter.
     */
    protected abstract void update();

    /**
     * Clamp the current paramter vaue between the lower and upper bound values.
     */
    protected void clamp() {
        if (this.parameter.getReal() < this.parameter.getBounds().getLowerBound())
            this.parameter.setReal(this.parameter.getBounds().getLowerBound());
        else if (this.parameter.getReal() > this.parameter.getBounds().getUpperBound())
            this.parameter.setReal(this.parameter.getBounds().getUpperBound());
    }


    /**
     * Get the lower bound of the
     * {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control paramter}.
     * @return The lower bound value.
     */
    public double getLowerBound() {
        return this.parameter.getBounds().getLowerBound();
    }


    /**
     * Set the value of the lower bound.
     * @param lower The value to set.
     */
    public void setLowerBound(double lower) {
//        this.parameter.getBounds().setLowerBound(lower);
        Bounds bounds = parameter.getBounds();
        this.parameter.setBounds(lower, bounds.getUpperBound());
    }


    /**
     * Get the upper bound for the
     * {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}.
     * @return The upper bound value.
     */
    public double getUpperBound() {
        return this.parameter.getBounds().getUpperBound();
    }


    /**
     * Set the value for the upper bound.
     * @param value The value to set.
     */
    public void setUpperBound(double value) {
//        this.parameter.getBounds().setUpperBound(value);
        Bounds bounds = parameter.getBounds();
        this.parameter.setBounds(bounds.getLowerBound(), value);
    }


    /**
     * Get the range of the {@linkplain net.sourceforge.cilib.controlparameter.BoundedControlParameter}.
     * @return The string representing the range of the parameter.
     */
    public String getRange() {
        return range;
    }


    /**
     * Set the range of the parameter.
     * @param range The domain string representing the range.
     */
    public void setRange(String range) {
        this.range = range;
        Vector v = (Vector) DomainParser.parse(this.range);

        if (v.getDimension() != 1)
            throw new RuntimeException("Range incorrect in BoundedUpdateStrategy! Please correct");
        else
            this.parameter = (Real) v.get(0);
    }

}
