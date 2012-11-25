/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.type.types.Randomizable;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class implements the {@link WeightInitializationStrategy} interface and initializes
 * the weights randomly within a domain.
 */
public class RandomWeightInitializationStrategy implements WeightInitializationStrategy {

    /**
     * Initialize the set of weights to random values within their domain bounds.
     * @param weights {@inheritDoc }
     */
    @Override
    public void initialize(Vector weights) {
        for (Randomizable weight : weights) {
            weight.randomize();
        }
    }
}
