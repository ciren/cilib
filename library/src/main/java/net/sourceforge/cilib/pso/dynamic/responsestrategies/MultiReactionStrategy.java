/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * This {@link EnvironmentChangeResponseStrategy reaction strategy} constructs a
 * constructs a list of {@link EnvironmentChangeResponseStrategy reaction strategies}.
 * When it is prompted to perform a reaction, it will call the response strategies
 * in the list, in sequence.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class MultiReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {

    protected ArrayList< EnvironmentChangeResponseStrategy<E> > responses;

    public MultiReactionStrategy() {
        this.responses = new ArrayList< EnvironmentChangeResponseStrategy<E> >();
    }

    public MultiReactionStrategy(MultiReactionStrategy<E> rhs) {
        super(rhs);
        for (EnvironmentChangeResponseStrategy<E> response : rhs.responses) {
            this.responses.add(response);
        }
    }

    @Override
    public MultiReactionStrategy<E> getClone() {
        return new MultiReactionStrategy<E>(this);
    }

    /**
     * Performs multiple reaction strategies, in the order in which they were
     * added via the addResponseStrategy method.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        for (EnvironmentChangeResponseStrategy<E> response : responses) {
            response.performReaction(algorithm);
        }
    }

    /**
     * Adds a response strategy to the end of the list of responses that have to
     * be performed.
     *
     * @param response The response strategy that has to be added.
     */
    public void addResponseStrategy(EnvironmentChangeResponseStrategy<E> response) {
        responses.add(response);
    }
}
