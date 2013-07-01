/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.util.selection.weighting;

import net.cilib.entity.Entity;
import net.cilib.entity.SocialEntity;
import net.cilib.problem.solution.Fitness;

/**
 * Obtain the social best fitness value from a {@link SocialEntity}.
 * @param <E> 	The type that is both an {@code Entity} and a
 *				{@code SocialEntity}. An example of such an entity is the
 *				{@link net.cilib.pso.particle.Particle}.
 */
public class SocialBestFitness<E extends Entity & SocialEntity> implements EntityFitness<E> {

    /**
     * {@inheritDoc}
     * Obtains the social based fitness from the {@link SocialEntity}.
     */
    @Override
    public Fitness getFitness(E entity) {
        return entity.getSocialFitness();
    }

}
