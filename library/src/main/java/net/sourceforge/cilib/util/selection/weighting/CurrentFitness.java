/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.weighting;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;

/**
 * Obtain the current fitness of an entity.
 * @param <E> The entity type.
 */
public class CurrentFitness<E extends Entity> implements EntityFitness<E> {

    /**
     * {@inheritDoc}
     * Get the current fitness of the Entity.
     */
    @Override
    public Fitness getFitness(E entity) {
        return entity.getFitness();
    }
}
