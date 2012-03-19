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
 * Easom function as taken from
 * www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Not separable</li>
 * <li>Regular</li>
 * </ul>
 *
 * f(x) = -1.0;  x = (Pi, Pi);
 *
 * R(-100, 100)^2
 *
 */
public class Easom implements ContinuousFunction {

    private static final long serialVersionUID = 7173528343222997045L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double powerTerm1 = -((input.doubleValueOf(0)-Math.PI)*(input.doubleValueOf(0)-Math.PI));
        double powerTerm2 = -((input.doubleValueOf(1)-Math.PI)*(input.doubleValueOf(1)-Math.PI));
        double power = powerTerm1 + powerTerm2;
        return -Math.cos(input.doubleValueOf(0)) * Math.cos(input.doubleValueOf(1)) * Math.exp(power);
    }
}
