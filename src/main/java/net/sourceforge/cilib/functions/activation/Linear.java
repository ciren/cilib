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
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The linear activation function, f(x) = x; f '(x) = 1; Since it
 * is unbounded, the linear function has no active range, and
 * these values are set to postive and negative max double.
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
    public Real apply(Real input) {
        return Real.valueOf(apply(input.doubleValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double apply(double input) {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getMaximum() {
        return Real.valueOf(Double.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getMinimum() {
        return Real.valueOf(-Double.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector x) {
        return Vector.of(this.getGradient(x.doubleValueOf(0)));
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
        return -Double.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return Double.MAX_VALUE;
    }

    @Override
    public String getDomain() {
        return "R";
    }
}
