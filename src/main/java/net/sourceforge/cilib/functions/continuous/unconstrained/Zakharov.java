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
 * <p><b>The Zakharov Function.</b></p>
 *
 * <p><b>Reference:</b> M. Laguna, R. Martı´ <i>Experimental testing of advanced scatter search designs for global optimization of multimodal functions</i>,
 * Journal of Global Optimization, 2005</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-5, 10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * </ul>
 * </p>
 *
 * R(-5, 10)^30
 *
 */
public class Zakharov implements ContinuousFunction {

    private static final long serialVersionUID = -635648546100966058L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum1 = 0;
        double sum2 = 0;
        for(int i = 0; i < input.size(); ++i){
            sum1 += input.doubleValueOf(i) * input.doubleValueOf(i);
            sum2 += 0.5 * (i + 1) * input.doubleValueOf(i);
        }
        return sum1 + (sum2 * sum2) + (sum2 * sum2 * sum2 * sum2);
    }
}
