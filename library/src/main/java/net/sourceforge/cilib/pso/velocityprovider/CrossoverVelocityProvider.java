/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import java.util.Arrays;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Updates a particle's solution to the crossover of its current position,
 * personal best and global best. Use with a LinearPositionProvider. Default
 * crossover strategy is PCX.
 */
public class CrossoverVelocityProvider implements VelocityProvider {

    private CrossoverStrategy crossoverStrategy;

    /**
     * Default constructor.
     */
    public CrossoverVelocityProvider() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
    }

    /**
     * Copy constructor.
     * @param copy
     */
    public CrossoverVelocityProvider(CrossoverVelocityProvider copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }

    /**
     * Clones this instance
     *
     * @return the clone
     */
    @Override
    public CrossoverVelocityProvider getClone() {
        return new CrossoverVelocityProvider(this);
    }

    /**
     * Returns the new position
     *
     * @param particle The particle to update
     * @return  the particle's new position
     */
    @Override
    public Vector get(Particle particle) {
        Entity parent1 = particle.getClone();
        Entity parent2 = particle.getClone();
        Entity parent3 = particle.getClone();

        parent2.setCandidateSolution(particle.getBestPosition());
        parent3.setCandidateSolution(particle.getNeighbourhoodBest().getBestPosition());

        return (Vector) crossoverStrategy.crossover(Arrays.asList(parent1, parent2, parent3))
                .get(0).getCandidateSolution();
    }

    /**
     * Sets the crossover strategy to use.
     *
     * @param crossoverStrategy
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }
}
