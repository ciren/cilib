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
 * This reaction strategy reinitializes the specified
 * {@link #setReinitializationRatio(double) ratio} of randomly chosen entities in the given
 * {@link Topology}.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class ReinitializationReevaluationReactionStrategy<E extends PopulationBasedAlgorithm> extends ReinitializationReactionStrategy<E> {
            
    public ReinitializationReevaluationReactionStrategy() {
        super();        
    }

    public ReinitializationReevaluationReactionStrategy(ReinitializationReactionStrategy<E> rhs) {
        super(rhs);        
    }

    @Override
    public ReinitializationReevaluationReactionStrategy<E> getClone() {
        return new ReinitializationReevaluationReactionStrategy<E>(this);
    }

    /**
     * Reinitialize the {@link Entity entities} inside the topology.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        Topology<? extends Entity> entities = algorithm.getTopology();
        int reinitializeCount = (int) Math.floor(reinitializationRatio * entities.size());

        reinitialize(entities, reinitializeCount);
                
        //update the particles and neighbourhoodbest
        algorithm.performIteration();                        
    }
    
  
    
}

    