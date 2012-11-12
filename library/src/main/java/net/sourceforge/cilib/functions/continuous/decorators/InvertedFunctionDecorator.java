/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
