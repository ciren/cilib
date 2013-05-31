/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * This {@link EnvironmentChangeResponseStrategy reaction strategy} constructs a
 * constructs a list of {@link EnvironmentChangeResponseStrategy reaction strategies}.
 * When it is prompted to perform a reaction, it will call the response strategies
 * in the list, in sequence.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class MultiReactionStrategy<E extends SinglePopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy {

    protected ArrayList<EnvironmentChangeResponseStrategy> responses;

    public MultiReactionStrategy() {
        this.responses = Lists.newArrayList();
    }

    public MultiReactionStrategy(MultiReactionStrategy<E> rhs) {
        super(rhs);
        
        for (EnvironmentChangeResponseStrategy response : rhs.responses) {
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
	protected <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(
			A algorithm) {
        for (EnvironmentChangeResponseStrategy response : responses) {
            response.performReaction(algorithm);
        }
    }

    /**
     * Adds a response strategy to the end of the list of responses that have to
     * be performed.
     *
     * @param response The response strategy that has to be added.
     */
    public void addResponseStrategy(EnvironmentChangeResponseStrategy response) {
        responses.add(response);
    }
}
