/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * A fitness calculator that determines the fitness value of a StructuredType.
 *
 * @param <T> The structured type to determine the fitness of.
 */
public class StructuredTypeFitnessCalculator<T extends StructuredType<?>> implements FitnessCalculator<T> {
    private static final long serialVersionUID = 8578856272481600577L;

    /**
     * {@inheritDoc}
     */
    @Override
    public FitnessCalculator<T> getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getFitness(T structure) {
        Algorithm algorithm = AbstractAlgorithm.get();
        return algorithm.getOptimisationProblem().getFitness(structure);
    }

}
