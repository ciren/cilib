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
 * Ripple function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * Minimum: 2.2
 * R(0, 1)^2
 * 
 * @author Clive Naicker
 * @version 1.0
 */
public class Ripple implements ContinuousFunction {

    private static final long serialVersionUID = 2956377362140947929L;

    /**
     * Create an instance of the function. The default domain is defined to be R(0, 1)^2
     */
    public Ripple() {
        //constraint.add(new DimensionValidator(2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ripple getClone() {
        return new Ripple();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        double term1 = Math.exp(-1.0 * Math.log(2) * Math.pow(((x - 0.1) / 0.8), 2));
        double term2 = Math.pow(Math.sin(5 * Math.PI * x), 6) + 0.1 * Math.pow(Math.cos(500 * Math.PI * x), 2);
        double term3 = Math.exp(-2.0 * Math.log(2) * Math.pow(((y - 0.1) / 0.8), 2));
        double term4 = Math.sin(5 * Math.PI * y) + 0.1 * Math.pow(Math.cos(500 * Math.PI * y), 2);

        return term1 * term2 + term3 * term4;
    }
}
