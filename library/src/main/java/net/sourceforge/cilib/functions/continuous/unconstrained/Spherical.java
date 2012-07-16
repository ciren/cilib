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
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Function;

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
        return input.foldLeft(0.0, new Function<Numeric, Double>() {
            @Override
            public Double apply(Numeric x) {
                return x.doubleValue() * x.doubleValue();
            }            
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getGradient(Vector x) {
        return x.map(new Function<Numeric, Numeric>() {
            @Override
            public Numeric apply(Numeric x) {
                return Real.valueOf(x.doubleValue() * 2);
            }            
        });
    }
}
