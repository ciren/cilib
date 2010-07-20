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
 * f(x) = 0.0 (Minimum)
 * R(-100, 100)^2
 * @author  engel
 */
public class Schaffer2 implements ContinuousFunction {

    private static final long serialVersionUID = 7289010453718555694L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum_squares = input.doubleValueOf(0) * input.doubleValueOf(0) + input.doubleValueOf(1) * input.doubleValueOf(1);
        double term1 = Math.pow(sum_squares, 0.25);
        double term2 = Math.pow(50 * Math.pow(sum_squares, 0.1), 2) + 1;
        return term1 * term2;
    }
}
