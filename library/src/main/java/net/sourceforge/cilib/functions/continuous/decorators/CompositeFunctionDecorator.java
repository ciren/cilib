/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
