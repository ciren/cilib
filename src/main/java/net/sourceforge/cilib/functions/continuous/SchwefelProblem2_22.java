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
 * SchwefelProblem2_22.
 *
 * Characteristics:
 *
 * f(x) = 0;
 *
 * x e [-10,10]
 *
 * R(-10, 10)^30
 *
 * @author  Andries Engelbrecht
 */
// TODO: Check discontinuous / continuous
public class SchwefelProblem2_22 implements ContinuousFunction {

    private static final long serialVersionUID = -5004170862929300400L;

    /**
     * Creates an new instance. Domain is set to R(-10,10)^30 by default.
     */
    public SchwefelProblem2_22() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchwefelProblem2_22 getClone() {
        return new SchwefelProblem2_22();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;
        double product = 0;
        for (int i = 0; i < input.size(); ++i) {
            sum += Math.abs(input.doubleValueOf(i));
            if (i == 0) {
                product = Math.abs(input.doubleValueOf(i));
            } else {
                product *= Math.abs(input.doubleValueOf(i));
            }
        }

        return sum + product;
    }
}
