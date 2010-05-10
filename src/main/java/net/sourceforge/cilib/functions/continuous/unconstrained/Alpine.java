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
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Alpine Function.</b></p>
 *
 * <p><b>Reference:</b> S. Rahnamayan, H. R. Tizhoosh, M. M. A. Salama <i>A novel population initialization method for accelerating evolutionary algorithms</i>,
 * Computers and Mathematics with Applications, 2007</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-10, 10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * </ul>
 * </p>
 *
 * R(-10, 10)^30
 * 
 * @author leo
 */
public class Alpine implements ContinuousFunction {

    private static final long serialVersionUID = -1365268075451075465L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector x) {
        double sum = 0;
        for (Numeric n : x) {
            sum += Math.abs((n.doubleValue() * Math.sin(n.doubleValue())) + (0.1 * n.doubleValue()));
        }
        return sum;
    }
}
