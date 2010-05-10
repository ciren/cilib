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
 * Maximum: 1.0
 * R(0, 1)^30
 *
 * @author  Andries Engelbrecht
 */
public class Shir implements ContinuousFunction {

    private static final long serialVersionUID = 8157687561496975789L;
    private double l1, l2, l3, l4, l5, sharpness;

    /**
     * Create an instance of the function. The domain is set to "R(0, 1)^30" by default.
     */
    public Shir() {
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
    @Override
    public Double apply(Vector input) {
        double sinTerm;
        double expTerm;
        double product = 1.0;

        for (int i = 0; i < input.size(); i++) {
            sinTerm = 1.0;
            for (int k = 1; k <= sharpness; k++) {
                sinTerm *= Math.sin(l1 * Math.PI * input.doubleValueOf(i) + l2);
            }
            expTerm = Math.exp(-l3 * ((input.doubleValueOf(i) - l4) / l5) * ((input.doubleValueOf(i) - l4) / l5));
            product *= (sinTerm * expTerm);
        }

        return product;
    }
}
