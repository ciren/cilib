/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.pheromoneupdate;

import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Interface to calculate the change in pheromone level for the adaptive HPSO using
 * pheromones
 */
public interface PheromoneUpdateStrategy {

    /**
     * Calculates the amount that a behavior's pheromone level should change
     *
     * @param e the particle which affects the behavior's pheromone level
     * @return the change in pheromone for a particle's behavior
     */
    double updatePheromone(Particle e);
}
