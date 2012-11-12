/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Characteristics:
 *
 * Let c be a fixed positive number.
 *
 * Horizontal Scaling: If g(x) = f(cx), then (c > 1) means that g(x) is f(x)
 * compressed in the horizontal direction by a factor of c (0 < c < 1) means
 * that g(x) is f(x) stretched in the horizontal direction by a factor of 1/c
 *
 * Vertical Scaling: If g(x) = cf(x), then (c > 1) means that g(x) is f(x)
 * stretched in the vertical direction by a factor of c (0 < c < 1) means that
 * g(x) is f(x) compressed in the vertical direction by a factor of 1/c
 *
 */
public class ScaledFunctionDecorator implements ContinuousFunction {

    private static final long serialVersionUID = -5316734133098401441L;
    private ContinuousFunction function;
    private double verticalScale;
    private double horizontalScale;

    /**
     * Create an instance of the decorator. Domain is set to "R" by default.
     */
    public ScaledFunctionDecorator() {
        verticalScale = 1.0;
        horizontalScale = 1.0;
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public Double getMinimum() {
//        // adds the value of the verticalShift to the original function minimum
//        return Double.valueOf(function.getMinimum().doubleValue() * verticalScale);
//    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Vector tmp = Vector.copyOf(input);

        for (int i = 0; i < input.size(); i++) {
            tmp.setReal(i, (horizontalScale * input.doubleValueOf(i)));
        }

        return (verticalScale * function.apply(tmp));
    }

    /**
     * Get the decorated function.
     * @return The decorated function.
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * Set the function that is to be decorated.
     * @param function The function to decorated.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }

    /**
     * Get the horizontal scale value.
     * @return The horizontal scale value.
     */
    public double getHorizontalScale() {
        return horizontalScale;
    }

    /**
     * Set the value of the horizontal scale.
     * @param horizontalScale The value of the horizontal scale.
     */
    public void setHorizontalScale(double horizontalScale) {
        Preconditions.checkArgument(horizontalScale > 0, "Horizontal scale factor must be greater than zero!");
        this.horizontalScale = horizontalScale;
    }

    /**
     * Get the value of the vertical scale.
     * @return The vertical scale value.
     */
    public double getVerticalScale() {
        return verticalScale;
    }

    /**
     * Set the value of the vertical scale.
     * @param verticalScale The vertical scale to use.
     */
    public void setVerticalScale(double verticalScale) {
        Preconditions.checkArgument(verticalScale > 0, "Vertical scale factor must be greater than zero!");
        this.verticalScale = verticalScale;
    }
}
