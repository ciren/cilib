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
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * The linear activation function, f(x) = x; f '(x) = 1; Since it
 * is unbounded, the linear function has no active range, and
 * these values are set to postive and neg. max double.
 * @author andrich
 */
public class Linear extends ActivationFunction {
    private static final long serialVersionUID = -6826800182176063079L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Linear getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real evaluate(Real input) {
        return new Real(this.evaluate(input.getReal()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double evaluate(double input) {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getMaximum() {
        return new Real(Double.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getMinimum() {
        return new Real(Double.MIN_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector x) {
        return Vectors.create(this.getGradient((Real) x.get(0)).getReal());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        return 1.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLowerActiveRange() {
        return Double.MIN_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return Double.MAX_VALUE;
    }
}
