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
 * that is defined to return a proportional value.
 */
public class ProportionalControlParameter implements ControlParameter {
    private static final long serialVersionUID = 8415953407107514281L;
    private double proportion;

    /**
     * Create a new {@code ProportionalControlParameter} instance. The default proportion
     * value is defined to be 0.1 (10%).
     */
    public ProportionalControlParameter() {
        this.proportion = 0.1;
    }

    public ProportionalControlParameter(ProportionalControlParameter copy) {
        this.proportion = copy.proportion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProportionalControlParameter getClone() {
        return new ProportionalControlParameter(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getParameter() {
        return this.proportion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getParameter(double min, double max) {
        double diff = max - min;
        return this.proportion * diff;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParameter(double value) {
        if (value < 0)
            throw new IllegalArgumentException("The proportion must be positive");

        this.proportion = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateParameter() {
    }
}
