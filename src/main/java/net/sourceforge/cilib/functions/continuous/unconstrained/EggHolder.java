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
 * <p><b>The Egg Holder.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle
 * Swarm Methods</i>, Technical Report, North-Eastern Hill University,
 * India, 2006</p>
 *
 * <p>Note: n <= 2</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) approx -959.64 </li>
 * <li> <b>x</b>* = (512,404.2319) for n=2</li>
 * <li> for x_i in [-512,512]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Non-seperable</li>
 * <li>Not regular</li>
 * </ul>
 * </p>
 *
 * R(-512.0,512.0)^30
 *
 * @author gpampara
 */
public class EggHolder implements ContinuousFunction {

    private static final long serialVersionUID = 358993985066821115L;

    @Override
    public ContinuousFunction getClone() {
        return this;
    }

    @Override
    public Double apply(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size() - 1; i++) {
            sum += (-1*(input.doubleValueOf(i+1) + 47)
                    *Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i+1) + input.doubleValueOf(i)/2 + 47)))
                    + Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i) - (input.doubleValueOf(i+1)+47))))
                    *(-1*input.doubleValueOf(i)));
        }
        return sum;
    }
}
