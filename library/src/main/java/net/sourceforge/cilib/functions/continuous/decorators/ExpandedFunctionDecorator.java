/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import static com.google.common.base.Preconditions.checkState;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Expands a given function
 * </p>
 * <p>
 * Using an n-D function F(x1, x2,..., x_n) as a starting function, corresponding expanded function is:<br/>
 * EF(x1, x2,..., x_D) = F(x1, x2) + F(x2, x3) + ... + F(x_D - 1, x_D) + F(x_D , x1)
 * </p>
 */
public class ExpandedFunctionDecorator implements ContinuousFunction {
    private ContinuousFunction function;
    private int splitSize;
    
    /**
     * Default constructor.
     */
    public ExpandedFunctionDecorator() {
        this.splitSize = 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        checkState(input.size() >= splitSize, "Input vector is too small, check that noOfSplits is correct.");
        
        double sum = 0.0;
        
        for (int i = splitSize; i < input.size(); i++) {
            sum += function.apply(input.copyOfRange(i - splitSize, i + splitSize));
        }
        
        Vector finalAddition = Vector.newBuilder().copyOf(input.copyOfRange(input.size() - splitSize, input.size())).add(input.get(0)).build();
        sum += function.apply(finalAddition);
        
        return sum;
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
     * Sets the size of each split.
     * @param noOfSplits 
     */
    public void setSplitSize(int noOfSplits) {
        this.splitSize = noOfSplits;
    }
}
