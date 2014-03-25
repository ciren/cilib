/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.moo.iterationstrategies;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.util.Cloneable;

public interface HigherLevelIterationStrategy extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    HigherLevelIterationStrategy getClone();

    /**
     * Perform the iteration of the Algorithm.
     * <p>
     * Due to the nature of the Algorithms, the actual manner in which the algorithm's
     * iteration is performed is deferred to the specific iteration strategy being used.
     * <p>
     * This implies that the general structure of the iteration for a specific flavor of
     * algorithm is constant with modifications on that algorithm being made. For example,
     * within a Genetic Algorithm you would expect:
     * <ol>
     *   <li>Parent individuals to be <b>selected</b> in some manner</li>
     *   <li>A <b>crossover</b> process to be done on the selected parent individuals to create
     *       the offspring</li>
     *   <li>A <b>mutation</b> process to alter the generated offspring</li>
     *   <li><b>Recombine</b> the existing parent individuals and the generated offspring to create
     *       the next generation</li>
     * </ol>
     *
     * @param algorithm The algorithm to perform the iteration process on.
     */
    void performIteration(Algorithm algorithm);

    /**
     * Get the currently associated {@linkplain BoundaryConstraint}.
     * @return The current {@linkplain BoundaryConstraint}.
     */
    BoundaryConstraint getBoundaryConstraint();

    /**
     * Set the {@linkplain BoundaryConstraint} to maintain within this {@linkplain IterationStrategy}.
     * @param boundaryConstraint The {@linkplain BoundaryConstraint} to set.
     */
    void setBoundaryConstraint(BoundaryConstraint boundaryConstraint);
}
