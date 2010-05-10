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
 * <p><b>The Quartic function.</b></p>
 * <p>Source: Evolutionary Programming Made Faster (Xin Yao)</p>
 *
 * <p>
 * Minimum:
 * <ul>
 * <li>&fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-1.28,1.28]</li>
 * </ul>
 * </p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * </ul>
 * </p>
 *
 * R(-1.28, 1.28)^30
 *
 * @author Gary Pampara
 */
public class Quartic implements ContinuousFunction {

    private static final long serialVersionUID = 1830591489569513511L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double result = 0.0;

        for (int i = 0; i < input.size(); i++) {
            double square = input.doubleValueOf(i) * input.doubleValueOf(i);
            double square2 = square * square;

            result += (i + 1) * square2;
        }

        return result;
    }
}
