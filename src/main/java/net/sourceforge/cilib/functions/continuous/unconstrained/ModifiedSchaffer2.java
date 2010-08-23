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
public class ModifiedSchaffer2 implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0.0;
        double square1, square2, numerator, denominator;

        for (int j = 0; j < input.size() - 1; j++) {
            square1 = input.doubleValueOf(j) * input.doubleValueOf(j);
            square2 = input.doubleValueOf(j + 1) * input.doubleValueOf(j + 1);
            numerator = Math.pow(Math.sin(square1 - square2), 2) - 0.5;
            denominator = Math.pow((1 + 0.001 * (square1 + square2)), 2);

            sum += 0.5 + (numerator / denominator);
        }

        return new Double(sum);
    }
}
