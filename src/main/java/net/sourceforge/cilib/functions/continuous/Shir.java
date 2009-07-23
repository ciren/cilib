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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The Damavandi function obtained from O.M. Shir and T. Baeck,
 * "Dynamic Niching in Evolution Strategies with Covariance Matrix Adaptation"
 *
 * Global Maximin: f(x1,...,xn) = 1
 *
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * </ul>
 *
 * @author  Andries Engelbrecht
 */
public class Shir extends ContinuousFunction {
    private static final long serialVersionUID = 8157687561496975789L;

    private double l1, l2, l3, l4, l5, sharpness;

    /**
     * Create an instance of the function. The domain is set to "R(0, 1)^30" by default.
     */
    public Shir() {
        setDomain("R(0, 1)^30");

        l1 = 1.0;
        l2 = 1.0;
        l3 = 1.0;
        l4 = 1.0;
        l5 = 1.0;
        sharpness = 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shir getClone() {
        return new Shir();
    }

    /**
     * {@inheritDoc}
     */
    public Double getMaximum() {
        return 1.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double sinTerm;
        double expTerm;
        double product = 1.0;

        for (int i = 0; i < getDimension(); i++) {
            sinTerm = 1.0;
            for (int k = 1; k <= sharpness; k++)
                sinTerm *= Math.sin(l1*Math.PI*input.getReal(i) + l2);
            expTerm = Math.exp(-l3*((input.getReal(i)-l4)/l5)*((input.getReal(i)-l4)/l5));
            product *= (sinTerm * expTerm);
        }

        return product;
    }

}
