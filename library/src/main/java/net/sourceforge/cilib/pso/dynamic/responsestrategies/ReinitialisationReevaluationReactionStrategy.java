/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.*;

/**
 * This reaction strategy reinitialises the specified
 * {@code ratio} of randomly chosen entities in the given {@link Topology}.
 *
 * @param <E> some {@link PopulationBasedAlgorithm}
 */
public class ReinitialisationReevaluationReactionStrategy<E extends PopulationBasedAlgorithm> extends ReinitialisationReactionStrategy<E> {

    public ReinitialisationReevaluationReactionStrategy() {
        super();
    }

    public ReinitialisationReevaluationReactionStrategy(ReinitialisationReactionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public ReinitialisationReevaluationReactionStrategy<E> getClone() {
        return new ReinitialisationReevaluationReactionStrategy<E>(this);
    }

    /**
     * Reinitialise the {@link Entity entities} inside the topology.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        Topology<? extends Entity> entities = algorithm.getTopology();
        int reinitializeCount = (int) Math.floor(reinitialisationRatio * entities.size());

        reinitialise(entities, reinitializeCount);

        //update the particles and neighbourhoodbest
        algorithm.performIteration();
    }
}

