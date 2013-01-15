/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;

/**
 * Calculates the fitness by passing the entity's blackboard to the algorithm's optimisation problem
 */
public class PropertyBasedFitnessCalculator implements
        FitnessCalculator<Entity> {
    private static final long serialVersionUID = -5225410711497956675L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyBasedFitnessCalculator getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getFitness(Entity entity) {
        Algorithm algorithm = AbstractAlgorithm.get();
        return algorithm.getOptimisationProblem().getFitness(entity.getProperties());
    }
}
