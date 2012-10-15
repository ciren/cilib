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
 * <p><b>Zettle.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <iSome New Test Functions for Global Optimization and Performance of Repulsive Particle
 * Swarm Methods</i>, Technical report, North-Eastern Hill University, India, 2006</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = -0.003791</li>
 * <li> <b>x</b>* = (-0.0299, 0)</li>
 * <li> for x<sub>i</sub> in [-5,5]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Non-Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 * R(-5, 5)^2
 */
public class Zettle implements ContinuousFunction {
    
	@Override
	public Double apply(Vector input) {
		double x1 = input.get(0).doubleValue();
        double x2 = input.get(1).doubleValue();
        return (Math.pow(((x1 * x1) + (x2 * x2) - 2*x1), 2) + 0.25*x1);   
	}
}
