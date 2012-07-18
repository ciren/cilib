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
 * <p><b>The Central Two-Peak Trap Function.</b></p>
 *
 * <p>Global Minimum:
 * <ul>
 * <li> f(x*) = -200</li>
 * <li> x* = 20</li>
 * <li> for x_i in [0, 20]</li>
 * </ul>
 * </p>
 * <p>Local Minimum:
 * <ul>
 * <li> f(x*) = -160</li>
 * <li> x* = 10</li>
 * <li> for x_i in [0, 20]</li>
 * </ul>
 * </p>
 * 
 * <p>Characteristics:
 * <ul>
 * <li>One-dimensional only</li>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>One global minimum on the boundary, and a local minimum that may trap algorithms</li>
 * </ul>
 * </p>
 *
 * R(0,20)^1
 *
 */
public class Central2PeakTrap implements ContinuousFunction {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        
        if (x < 0)
            return 0.0;
        else if (x<=10)
            return -160/10*x;
        else if (x <= 15)
            return -160/5*(15-x);
        else if (x <= 20)
            return -200/5*(x-15);
        else
            return -200.0;
    }
}
