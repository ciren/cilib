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
 * <p><b>The Colville Function.</b></p>
 *
 * <p><b>Reference:</b> Doo-Hyun Choi, Cooperative mutation based evolutionary programming for continuous function optimization, Operations Research Letters, Volume 30, Issue 3, June 2002, Pages 195-201/p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (1, 1, 1, 1)</li>
 * <li> for x_i in [-10, 10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Non-seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * R(-10, 10)^4
 *
 */
public class Colville implements ContinuousFunction {

    private static final long serialVersionUID = 4561331100889232057L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double a = input.doubleValueOf(0);
        double b = input.doubleValueOf(1);
        double c = input.doubleValueOf(2);
        double d = input.doubleValueOf(3);

        return 100 * (b - a * a) * (b - a * a)
                + (1 - a) * (1 - a)
                + 90 * (d - c * c) * (d - c * c)
                + (1 - c) * (1 - c)
                + 10.1 * ((c - 1) * (c - 1) + (d - 1) * (d - 1))
                + 19.8 * (b - 1) * (d - 1);
    }
}
