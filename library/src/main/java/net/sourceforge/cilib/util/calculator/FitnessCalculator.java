/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Perform the calculation of the fitness for the given <code>Entity</code>, decoupling the
 * <code>Entity</code> from the <code>Problem</code>.
 * @param <T> The type to which a fitness calculation is to be performed.
 */
public interface FitnessCalculator<T> extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    FitnessCalculator<T> getClone();

    /**
     * Get the fitness, given the <code>position</code>.
     * @param entity The <code>Type</code> to base the calculation on.
     * @return A <code>Fitness</code> object representing the fitness of the <code>position</code>.
     */
    Fitness getFitness(T entity);

}
