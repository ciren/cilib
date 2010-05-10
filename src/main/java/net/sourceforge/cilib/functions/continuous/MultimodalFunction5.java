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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Minimum: 0.0
 * R(-6, 6)^2
 */
public class MultimodalFunction5 implements ContinuousFunction {

    private static final long serialVersionUID = -8704025552791904890L;

    /**
     * Create a new instance of {@linkplain MultimodalFunction5}.
     */
    public MultimodalFunction5() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultimodalFunction5 getClone() {
        return new MultimodalFunction5();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        return 200 - Math.pow((x*x + y - 11), 2) - Math.pow((x + y*y - 7), 2);
    }
}
