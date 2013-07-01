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
 * This {@linkplain ContextUpdateStrategy} only updates the given context if the new
 * participant solution improves the fitness of the context vector.
 */
public class SelectiveContextUpdateStrategy implements ContextUpdateStrategy {
    private static final long serialVersionUID = -7457923682790990569L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateContext(ContextEntity context, Vector solution,
            DimensionAllocation allocation) {
        ContextEntity testContext = context.getClone();
        testContext.copyFrom(solution, allocation);
        testContext.calculateFitness();
        if (testContext.getFitness().compareTo(context.getFitness()) > 0) {
            context.setCandidateSolution(testContext.getCandidateSolution());
            context.setFitness(testContext.getFitness());
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ContextUpdateStrategy getClone() {
        return new SelectiveContextUpdateStrategy();
    }

}
