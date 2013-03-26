/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.problemdistribution;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The interface used to distribute the dimensions in a given
 * {@link Problem} into sub-problems for a
 * {@link CooperativeCoevolutionAlgorithm}.
 */
public interface ProblemDistributionStrategy {
    /**
     * Splits up the given {@link Problem} into
     * sub-problems, where each sub problem contains a portion of the problem
     * vector, and assigns them to all the participating {@link PopulationBasedAlgorithm}s.
     * @param populations The list of participating {@linkplain PopulationBasedAlgorithm}s.
     * @param problem The problem that needs to be re-distributed.
     * @param context The context vector maintained by the {@linkplain CooperativeCoevolutionAlgorithm}.
     */
    void performDistribution(List<PopulationBasedAlgorithm> populations, Problem problem, Vector context);
}
