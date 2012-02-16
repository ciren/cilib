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
 * <p><b>The Shubert Function.</b></p>
 *
 * <p>Global Minimum:
 * <ul>
 * <li>&fnof;(<b>x</b>*) = -186.7309088</li>
 * <li> Many global minima: n=1 has 3, n=2 has 18, n=3 has 81, n=4 has 324, n has pow(3,n)</li>
 * <li> All unevenly spaced</li>
 * <li> for x<sub>i</sub> in [-10,10]</li>
 * </ul>
 * </p>
 * 
 * <p>Local Minimum:
 * <ul>
 * <li> Many local minima</li>
 * </ul>
 * </p>
 * 
 * <p>Characteristics:
 * <ul>
 * <li>Multi-dimensional</li>
 * <li>Multimodal</li>
 * <li>Non-Separable</li>
 * </ul>
 * </p>
 *
 * @author Clive Naicker
 */
public class Shubert implements ContinuousFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double result = 1.0;
        for (int i = 0; i < input.size(); ++i) {
            double result2 = 0.0;
            for (int j = 1; j <= 5; j++) {
                result2 += j*Math.cos((j+1)*input.doubleValueOf(i) + j);
            }
            result *= result2;
        }
        return result;
    }
}
