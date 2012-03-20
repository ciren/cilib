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
import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Spherical function.</p>
 *
 * <p><b>Reference:</b> X. Yao, Y. Liu, G. Liu, <i>Evolutionary Programming
 * Made Faster</i>,  IEEE Transactions on Evolutionary Computation,
 * 3(2):82--102, 1999</p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * <li>Separable</li>
 * <li>Regular</li>
 * <li>Convex</li>
 * </ul>
 *
 * f(x) = 0; x = (0,0,...,0)
 * x e [-5.12, 5.12]
 *
 * R(-5.12, 5.12)^30
 *
 */
public class Spherical implements ContinuousFunction, Differentiable {

    private static final long serialVersionUID = 5811377575647995206L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double tmp = 0;
        for (int i = 0; i < input.size(); i++) {
            double value = input.doubleValueOf(i);
            tmp += value * value;
        }
        return tmp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector x) {
        Vector tmp = new Vector();

        for (int i = 0; i < x.size(); ++i) {
            tmp.setReal(i, 2*x.doubleValueOf(i));
        }

        return tmp;
    }
}
