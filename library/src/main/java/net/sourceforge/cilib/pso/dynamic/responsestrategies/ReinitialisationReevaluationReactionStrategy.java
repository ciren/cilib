/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import fj.data.List;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.*;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * This reaction strategy reinitialises the specified
 * {@code ratio} of randomly chosen entities in the given {@link Topology}.
 *
 * @param <E> some {@link PopulationBasedAlgorithm}
 */
public class ReinitialisationReevaluationReactionStrategy<E extends SinglePopulationBasedAlgorithm> extends ReinitialisationReactionStrategy<E> {

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
    public <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(A algorithm) {
        List<P> entities = algorithm.getTopology();
        int reinitializeCount = (int) Math.floor(reinitialisationRatio * entities.length());

        reinitialise(entities, reinitializeCount);

        //update the particles and neighbourhoodbest
        algorithm.performIteration();
    }
}

