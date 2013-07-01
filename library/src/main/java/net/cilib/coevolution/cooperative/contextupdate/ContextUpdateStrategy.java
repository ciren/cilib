/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.coevolution.cooperative.contextupdate;

import net.cilib.coevolution.cooperative.ContextEntity;
import net.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.cilib.type.types.container.Vector;
import net.cilib.util.Cloneable;

/**
 * This interface is used to update the context vector of a {@linkplain CooperativeCoevolutionAlgorithm}
 * with the best solution from a participating {@linkplain PopulationBasedAlgorithm}.
 */
public interface ContextUpdateStrategy extends Cloneable {
    /**
     * Decide if the given solution should be added to the given context vector.
     * @param context The current context vector.
     * @param solution The new participant solution.
     * @param allocation The {@linkplain DimensionAllocation} which indicates how the solution
     *      vector forms part of the context.
     */
    void updateContext(ContextEntity context, Vector solution, DimensionAllocation allocation);

    /**
     * {@inheritDoc}
     */
    @Override
    ContextUpdateStrategy getClone();

}
