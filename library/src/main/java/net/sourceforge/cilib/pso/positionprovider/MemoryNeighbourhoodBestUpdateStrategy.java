/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;

/**
 * This class.....
 *
 */
public class MemoryNeighbourhoodBestUpdateStrategy implements NeighbourhoodBestUpdateStrategy {
    private static final long serialVersionUID = -6674766322219682030L;

    public MemoryNeighbourhoodBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * Get the social best fitness of the entity. This returns the fitness of the
     * entity's personal best.
     *
     * @return The fitness of the <code>Entity</code>'s personal best (pbest)
     */
    public Fitness getSocialBestFitness(Entity entity) {
        return entity.getBestFitness();
    }

}
