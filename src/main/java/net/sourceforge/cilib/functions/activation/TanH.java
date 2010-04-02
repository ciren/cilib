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

/**
 * Hyperbollic Tangent Function.
 *
 * @author leo
 */

public class TanH extends ActivationFunction {
    private static final long serialVersionUID = -5843046986587459333L;

    /**
     * Create a new instance of {@code TanH}.
     */
    public TanH() {
        setDomain("R(-1.0, 1.0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TanH getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real evaluate(Real input) {
        return Real.valueOf(this.evaluate(input.doubleValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double evaluate(double input) {
        double a = Math.exp(input);
        double b = Math.exp(-input);
        return ((a-b)/(a+b));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getMaximum() {
        return Real.valueOf(1.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getMinimum() {
        return Real.valueOf(0.0);
    }

    @Override
    public Vector getGradient(Vector x) {
        throw new RuntimeException("Implement me");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGradient(double number) {
        throw new RuntimeException("Implement me");
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
