/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;

/**
 * Determines the neighbourhood best of a particle using one of two 
 * {@linkplain NeighbourhoodBestUpdateStrategy}s, selected based on the
 * {@linkplain #probability} {@linkplain ControlParameter}.
 */
public class ProbabilisticNeighbourhoodBestUpdateStrategy implements NeighbourhoodBestUpdateStrategy {
    protected NeighbourhoodBestUpdateStrategy strategy1;
    protected NeighbourhoodBestUpdateStrategy strategy2;
    protected ControlParameter probability;


    public ProbabilisticNeighbourhoodBestUpdateStrategy() {
        this.strategy1 = new IterationNeighbourhoodBestUpdateStrategy();
        this.strategy2 = new MemoryNeighbourhoodBestUpdateStrategy();
        this.probability = ConstantControlParameter.of(0.5);
    }

    /**
     * {@inheritDoc}
     */
    public ProbabilisticNeighbourhoodBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * Get the social best fitness (neighbourhood best) of the given 
     * {@linkplain Entity} using one of two 
     * {@link NeighbourhoodBestUpdateStrategy}s, selected based on the
     * {@linkplain #probability} {@linkplain ControlParameter}.
     * @param entity the {@linkplain Entity} to determine the social best fitness from
     * @return the social best (neighbourhood best) {@linkplain Fitness}
     */
    public Fitness getSocialBestFitness(Entity entity) {
        if (Rand.nextDouble() <= probability.getParameter()) {
            return strategy1.getSocialBestFitness(entity);
        } else {
            return strategy2.getSocialBestFitness(entity);
        }
    }

    /**
     * Set the The {@link NeighbourhoodBestUpdateStrategy} that will be used
     * when the {@linkplain #probability} {@linkplain ControlParameter} is
     * <em>greater</em> than a random number in [0, 1].
     * @param strategy1 the {@link NeighbourhoodBestUpdateStrategy} to set
     */
    public void setStrategy1(NeighbourhoodBestUpdateStrategy strategy1) {
        this.strategy1 = strategy1;
    }

    /**
     * Set the The {@link NeighbourhoodBestUpdateStrategy} that will be used
     * when the {@linkplain #probability} {@linkplain ControlParameter} is
     * <em>less than or equal</em> to a random number in [0, 1].
     * @param strategy2 the {@link NeighbourhoodBestUpdateStrategy} to set
     */
    public void setStrategy2(NeighbourhoodBestUpdateStrategy strategy2) {
        this.strategy2 = strategy2;
    }

    /** 
     * Set the {@linkplain #probability} {@linkplain ControlParameter} that
     * determines which {@link NeighbourhoodBestUpdateStrategy} is used to when
     * returning the social best fitness (neighbourhood best).
     * @param probability the {@linkplain ControlParameter} to set
     */
    public void setProbability(ControlParameter probability) {
      this.probability = probability;
    }
}
