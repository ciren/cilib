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
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Function implementation to accept a function and to return the reciprocal
 * of that function value.
 *
 */
public class InvertedFunctionDecorator implements ContinuousFunction {

    private static final long serialVersionUID = -7506823207533866371L;
    private Function<Vector, Double> function;

    @Override
    public Double apply(Vector input) {
        double innerFunctionValue = function.apply(input);

        if (innerFunctionValue == 0) {
            throw new ArithmeticException("Inner function evaluation equated to 0. Division by zero is undefined");
        }

        return (1.0 / innerFunctionValue);
    }

    public Function<Vector, Double> getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}
