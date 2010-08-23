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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Modified Schaffer Function 2.
 *
 * Characteristics:
 * <ul>
 *  <li>Multi-modal</li>
 *  <li>Non-Seperable</li>
 *  <li>Regular</li>
 * </ul>
 *
 * Reference:
 * @article{
 * mishra-some,
 * title={{Some new test functions for global optimization and performance of repulsive particle swarm method}},
 * author={Mishra, S.K. and Campus, N.}
 * }
 *
 * @author  Bennie Leonard
 */
public class Norwegian implements ContinuousFunction {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double product = 1.0;
        double value;

        for (int j = 0; j < input.size(); j++) {
            value = input.doubleValueOf(j);
            product *= Math.cos(Math.PI * Math.pow(value, 3)) * ((99 + value) / 100);
        }

        return new Double(product);
    }
}
