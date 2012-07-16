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
 * <p><b>The Vincent Function.</b></p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multi-dimensional only</li>
 * <li>Multimodal</li>
 * <li>Non-Separable</li>
 * <li>for n=1, 6 global minima and no local minima</li>
 * <li>for n=2, 36 global minima and no local minima</li>
 * </ul>
 * </p>
 *
 * R(0.25,10)^n
 *
 */
public class Vincent implements ContinuousFunction {
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double result = 1.0;
        for (int i = 0; i < input.size(); ++i)
            result += Math.sin(10*Math.log(input.doubleValueOf(i)));
        return -result;
    }
}
