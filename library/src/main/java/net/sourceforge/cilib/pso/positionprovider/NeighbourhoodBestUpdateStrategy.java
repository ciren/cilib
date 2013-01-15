/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface specifying the manner in which the neighbourhood best of the
 * {@linkplain Entity} is obtained.
 */
public interface NeighbourhoodBestUpdateStrategy extends Cloneable {

    /**
     * {@inheritDoc}
     */
    NeighbourhoodBestUpdateStrategy getClone();

    /**
     * Get the social best fitness (neighbourhood best) of the given {@linkplain Entity}.
     * @param entity The Entity to determine the social best fitness from.
     * @return The social best (neighbourhood best) {@linkplain Fitness}.
     */
    Fitness getSocialBestFitness(Entity entity);
}
