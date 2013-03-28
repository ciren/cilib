/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.ContextEntity;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This interface defines a problem re-distribution strategy. This strategy is
 * used to re-distribute a problem amongst a number of participating
 * {@link PopulationBasedAlgorithm}s in a {@link HeterogeneousCooperativeAlgorithm}.
 */
public interface ProblemRedistributionStrategy extends Cloneable {
    /**
     * Re-distribute the given problem amongst the participating
     * {@link PopulationBasedAlgorithm}s.
     *
     * @param populations           A {@link List} of participating {@link PopulationBasedAlgorithm}s.
     * @param problem               The {@link Problem} that is being optimised.
     * @param distributionStrategy  The {@link CooperativeCoevolutionAlgorithm}'s
     *                              original {@link ProblemDistributionStrategy},
     *                              which may be used to recalculate the distribution.
     * @param context               The current {@link ContextEntity} of the
     *                              {@link CooperativeCoevolutionAlgorithm}.
     */
    void redistributeProblem(List<PopulationBasedAlgorithm> populations, Problem problem, ProblemDistributionStrategy distributionStrategy, Vector context);

    /**
     * {@inheritDoc}
     */
    @Override
    ProblemRedistributionStrategy getClone();
}
