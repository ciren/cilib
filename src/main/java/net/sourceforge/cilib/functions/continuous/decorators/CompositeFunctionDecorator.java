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
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Composes two given functions as per F13 and F14 in the CEC 2005 benchmark functions.
 * </p>
 * <p>
 * returns F_outer(F_inner(input))
 * </p>
 * @author filipe
 */
public class CompositeFunctionDecorator implements ContinuousFunction {
    private ContinuousFunction innerFunction;
    private ContinuousFunction outerFunction;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        return outerFunction.apply(Vector.of(innerFunction.apply(input)));
    }

    /**
     * Get the decorated function.
     * @return The decorated function.
     */
    public ContinuousFunction getInnerFunction() {
        return innerFunction;
    }

    /**
     * Set the function that is to be decorated.
     * @param function The function to decorated.
     */
    public void setInnerFunction(ContinuousFunction function) {
        this.innerFunction = function;
    }
    
    /**
     * Get the decorated function.
     * @return The decorated function.
     */
    public ContinuousFunction getOuterFunction() {
        return outerFunction;
    }

    /**
     * Set the function that is to be decorated.
     * @param function The function to decorated.
     */
    public void setOuterFunction(ContinuousFunction function) {
        this.outerFunction = function;
    }
}
