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
 * An implementation of the Elliptic function.
 * The number 10^6 is called condition number, which is used to transform
 * a Sphere function to an Elliptic function.
 *
 * Reference:
 *<pre>
 * {@literal @}article{tang2010benchmark,
 * title={{Benchmark Functions for the CEC’2010 Special Session and Competition on Large-Scale Global Optimization}},
 * author={Tang, K. and Li, X. and Suganthan, PN and Yang, Z. and Weise, T.},
 * year={2010}}
 * </pre>
 *
 * @author Bennie Leonard
 */
public class Elliptic extends ContinuousFunction {

    //the condition number 10^6 is used to transform a sphere to an elliptic function
    static final double CONDITION_NUMBER = 1000000;

    public Elliptic() {
        setDomain("R(-100,100)^30");
    }

    @Override
    public ContinuousFunction getClone() {
        return this;
    }

    @Override
    public Double apply(Vector input) {
        double sum = 0;

        for (int i = 0; i < input.size(); i++) {
            sum += Math.pow(CONDITION_NUMBER, i / (input.size() - 1)) * input.doubleValueOf(i) * input.doubleValueOf(i);
        }

        return sum;
    }
}
