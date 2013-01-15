/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.type.types.Randomisable;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class implements the {@link WeightInitialisationStrategy} interface and initialises
 * the weights randomly within a domain.
 */
public class RandomWeightInitialisationStrategy implements WeightInitialisationStrategy {

    /**
     * Initialise the set of weights to random values within their domain bounds.
     * @param weights {@inheritDoc }
     */
    @Override
    public void initialise(Vector weights) {
        for (Randomisable weight : weights) {
            weight.randomise();
        }
    }
}
