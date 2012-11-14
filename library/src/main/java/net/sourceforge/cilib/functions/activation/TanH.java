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
 * Hyperbolic Tangent Function.
 *
 */
public class TanH implements ActivationFunction {

    private static final long serialVersionUID = -5843046986587459333L;

    @Override
    public Object getClone() {
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
        double a = Math.exp(input);
        double b = Math.exp(-input);
        return ((a - b) / (a + b));
    }

    @Override
    public Vector getGradient(Vector x) {
        return Vector.of(this.getGradient(x.getReal(0)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        return 1 - number * number;
    }

    /**
     * {@inheritDoc}
     * The active range is -Sqrt(3) - Sqrt(3), and Sqrt(3) = 1.732050808
     */
    @Override
    public double getLowerActiveRange() {
        return -1.732050808;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getUpperActiveRange() {
        return 1.732050808;
    }
}
