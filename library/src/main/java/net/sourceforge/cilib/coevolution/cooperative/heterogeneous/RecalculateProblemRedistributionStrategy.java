/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.PerfectSplitDistributionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@link ProblemRedistributionStrategy} simply re-calculates the problem distribution by making use of the given {@link ProblemRedistributionStrategy}.
 * This strategy would not make sense if, for example, the current {@link ProblemDistributionStrategy} is an instance of {@link PerfectSplitDistributionStrategy}, since
 * the distribution would always be the same no matter how many times it is re-calculated.
 */
public class RecalculateProblemRedistributionStrategy implements
        ProblemRedistributionStrategy {

    private static final long serialVersionUID = -4213059436118061377L;

    /**
     * {@inheritDoc}
     */
    public void redistributeProblem(List<PopulationBasedAlgorithm> populations, Problem problem, ProblemDistributionStrategy distributionStrategy, Vector context) {
        distributionStrategy.performDistribution(populations, problem, context);
    }

    /**
     * {@inheritDoc}
     */
    public RecalculateProblemRedistributionStrategy getClone() {
        return new RecalculateProblemRedistributionStrategy();
    }
}
