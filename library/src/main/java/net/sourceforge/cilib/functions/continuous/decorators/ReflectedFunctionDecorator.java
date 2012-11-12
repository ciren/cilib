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
 * <p>
 * Class to reflect the wrapped {@linkplain Function} in a horizontal or
 * vertical fashion.
 *
 * Characteristics:
 *
 * Sets f(x) to f(-x) or -f(x) or -f(-x) based on what is required, by
 * reflecting on a specific axis.
 *
 * Setting values in xml works the same as setting string values
 *
 */
public class ReflectedFunctionDecorator implements ContinuousFunction {
    private static final long serialVersionUID = -5042848697343918398L;
    private ContinuousFunction function;
    private boolean horizontalReflection;
    private boolean verticalReflection;

    public ReflectedFunctionDecorator() {
        horizontalReflection = false;
        verticalReflection = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Vector tmp = Vector.copyOf(input);

        if (horizontalReflection) {
            for (int i = 0; i < input.size(); i++) {
                tmp.setReal(i, -input.doubleValueOf(i));
            }
        }

        if (verticalReflection) {
            return -function.apply(tmp);
        }

        return function.apply(tmp);
    }

    /**
     * Get the decorated function contained by this instance.
     * @return the function
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * Set the wrapped function.
     * @param function the function to set.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }

    /**
     * Get the horizonal reflection.
     * @return the horizontalReflection.
     */
    public boolean getHorizontalReflection() {
        return horizontalReflection;
    }

    /**
     * Invoking this method sets horizontalReflection to true.
     */
    public void setHorizontalReflection(boolean horizontalReflection) {
        this.horizontalReflection = horizontalReflection;
    }

    /**
     * Invoking this method sets horizontalReflection to true.
     */
    public void setHorizontalReflection(String horizontalReflection) {
        this.horizontalReflection = Boolean.parseBoolean(horizontalReflection);
    }

    /**
     * Get the vertical reflection.
     * @return the verticalReflection.
     */
    public boolean getVerticalReflection() {
        return verticalReflection;
    }

    /**
     * Invoking this method sets verticalReflection to true.
     */
    public void setVerticalReflection(boolean verticalReflection) {
        this.verticalReflection = verticalReflection;
    }

    /**
     * Invoking this method sets verticalReflection to true.
     */
    public void setVerticalReflection(String verticalReflection) {
        this.verticalReflection = Boolean.parseBoolean(verticalReflection);
    }
}
