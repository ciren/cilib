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
 * <p><b>Chichinadze.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <iSome New Test Functions for Global Optimization and Performance of Repulsive Particle
 * Swarm Methods</i>, Technical report, North-Eastern Hill University, India, 2006</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = -43.31586206998933</li>
 * <li> <b>x</b>* = (5.90133, 0.5)</li>
 * <li> for x<sub>i</sub> in [-30,30]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 * R(-30, 30)^2
 */
public class Chichinadze implements ContinuousFunction {

	@Override
	public Double apply(Vector input) {
		double x1 = input.get(0).doubleValue();
		double x2 = input.get(1).doubleValue();
		
		double cos = Math.cos(Math.PI * x1 / 2);
		double sin = Math.sin(5*Math.PI*x1);
		double ePow = Math.exp(-0.5 * ((x2 - 0.5) * (x2 - 0.5))); 
		
		return (x1*x1) - 12 * (x1) + 11 + 10*cos + 8 * sin - Math.pow((1/5.0), 0.5)* ePow;
	}

}
