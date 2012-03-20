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
 * <p><b>The Five Uneven Peak Trap Function.</b></p>
 *
 * <p>Global Minimum:
 * <ul>
 * <li>f(x,y) = 0</li>
 * <li>Many global minima where ever x = 0</li>
 * </ul>
 * </p>
 * 
 * <p>Characteristics:
 * <ul>
 * <li>Two-dimensional only</li>
 * <li>Multimodal</li>
 * <li>Non-Separable</li>
 * <li>One global optimum, and nine local optima</li>
 * </ul>
 * </p>
 *
 * R(-0.9,1.2), R(-1.2,1.2)
 *
 */
public class Waves implements ContinuousFunction {
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        
        return -(Math.pow(0.3*x, 3)-(y*y-4.5*y*y)*x*y-4.7*Math.cos(3*x-y*y*(2+x))*Math.sin(2.5*Math.PI*x));
    }
}
