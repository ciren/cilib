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
 * <p><b>The Quadric Function</b></p>
 *
 * <p>
 * Minimum:
 * <ul>
 * <li>&fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-100,100]</li>
 * </ul>
 * </p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * </ul>
 * </p>
 *
 * @author  Edwin Peer
 */
public class Quadric extends ContinuousFunction {

    private static final long serialVersionUID = -2555670302543357421L;

    public Quadric() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Quadric getClone() {
        return new Quadric();
    }

    /**
     * Get the minimum of the function. It is defined to be a value of <code>0.0</code>.
     * @return The function minimum value.
     */
    public Double getMinimum() {
        return 0.0;
    }

    public Double getMaximum() {
        return 1248.2;
    }

    /**
     * {@inheritDoc}
     */
    public Double apply(Vector input) {
        double sumsq = 0;
        for (int i = 0; i < input.size(); ++i) {
            double sum = 0;
            for (int j = 0; j <= i; ++j) {
                sum += input.doubleValueOf(j);
            }
            sumsq += sum * sum;
        }
        return sumsq;
    }

    @Override
    public String getDomain() {
        return "R(-100, 100)^30";
    }
}
