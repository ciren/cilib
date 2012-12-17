/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.UnconstrainedBoundary;

/**
 * Generic {@code IterationStrategy} class for all population based algorithms. Each and every {@link IterationStrategy}
 * will have a pipeline available to it in order to specify the needed operators that will be
 * required to be executed during the iteration. For the implementing classes, a default
 * pipeline is constructed and made available, this can (as with everything within CIlib) be overridden.
 *
 *
 * @param <E> The {@linkplain PopulationBasedAlgorithm} type.
 */
public abstract class AbstractIterationStrategy<E extends Algorithm> implements IterationStrategy<E> {

    private static final long serialVersionUID = -2922555178733552167L;
    protected BoundaryConstraint boundaryConstraint;

    /**
     * Create an instance of the {@linkplain IterationStrategy}.
     */
    public AbstractIterationStrategy() {
        this.boundaryConstraint = new UnconstrainedBoundary();
    }
    
    public AbstractIterationStrategy(AbstractIterationStrategy copy) {
        this.boundaryConstraint = copy.boundaryConstraint.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractIterationStrategy<E> getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void performIteration(E algorithm);

    /**
     * Get the currently associated {@linkplain BoundaryConstraint}.
     * @return The current {@linkplain BoundaryConstraint}.
     */
    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return boundaryConstraint;
    }

    /**
     * Set the {@linkplain BoundaryConstraint} to maintain within this {@linkplain IterationStrategy}.
     * @param boundaryConstraint The {@linkplain BoundaryConstraint} to set.
     */
    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.boundaryConstraint = boundaryConstraint;
    }
}
