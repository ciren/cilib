/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Creates a fixed permutation vector of indices.
 * Using the permuted indices, this decorator permutes the input
 * vector before applying the decorated function.
 */
public class PermutationFunctionDecorator extends ContinuousFunction {

    private ContinuousFunction function;
    private Vector permutedIndices;

    /**
     * Default constructor that initialises an empty indices vector.
     */
    public PermutationFunctionDecorator() {
        this.permutedIndices = Vector.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        if (permutedIndices.size() != input.size()) {
            permutedIndices = Vector.newBuilder()
                .range(0, input.size(), 1)
                .build()
                .permute();
        }

        Vector.Builder newInput = Vector.newBuilder();

        for(Numeric index : permutedIndices) {
            newInput.add(input.get(index.intValue()));
        }

        return function.f(newInput.build());
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
}
