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
 * <p><b>The rastrigin function.</b></p>
 *
 *<p><b>Reference:</b> X. Yao and Y. Liu and G. Liu, <i>Evolutionary Programming Made Faster</i>,
 * IEEE Transactions on Evolutionary Computation, vol 3, number 2, pages 82--102, 1999.</p>
 *
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * <li>Seperable</li>
 * </ul>
 *
 * f(x) = 0; x = (0,0,...,0);
 *
 * x e [-5.12, 5.12];
 *
 * R(-5.12, 5.12)^30
 *
 */
public class Rastrigin implements ContinuousFunction, Differentiable {

    private static final long serialVersionUID = 447701182683968035L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double tmp = 0;
        for (int i = 0; i < input.size(); ++i) {
            tmp += input.doubleValueOf(i) * input.doubleValueOf(i) - 10.0 * Math.cos(2 * Math.PI * input.doubleValueOf(i));
        }
        return 10 * input.size() + tmp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector input) {
        Vector tmp = new Vector();

        for (int i = 0; i < input.size(); ++i) {
            tmp.setReal(i, (2.0 * input.doubleValueOf(i)) + (20 * Math.PI * Math.sin(2.0 * Math.PI * input.doubleValueOf(i))));
        }

        return tmp;
    }
}
