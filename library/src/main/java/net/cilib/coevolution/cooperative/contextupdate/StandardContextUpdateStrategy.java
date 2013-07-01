/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.coevolution.cooperative.contextupdate;

import net.cilib.coevolution.cooperative.ContextEntity;
import net.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.cilib.type.types.container.Vector;

/**
 * This {@linkplain ContextUpdateStrategy} always updates the given context with
 * the given participant solution.
 *
 */
public class StandardContextUpdateStrategy implements ContextUpdateStrategy {
    private static final long serialVersionUID = -6765131126784196793L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateContext(ContextEntity context, Vector solution,
            DimensionAllocation allocation) {
        context.copyFrom(solution, allocation);
        context.calculateFitness();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContextUpdateStrategy getClone() {
        return new StandardContextUpdateStrategy();
    }
}
