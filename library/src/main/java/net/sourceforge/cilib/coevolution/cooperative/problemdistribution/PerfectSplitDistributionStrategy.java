/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.problemdistribution;

import com.google.common.base.Preconditions;
import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequentialDimensionAllocation;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@linkplain ProblemDistributionStrategy} performs a split by assigning a
 * sequential portion of the same length to each participating
 * {@linkplain PopulationBasedAlgorithm}.
 */
public class PerfectSplitDistributionStrategy implements ProblemDistributionStrategy {

    /**
     * Splits up the given {@link Problem} into sub-problems, where each
     * sub-problem contains a sequential, uniform length, portion of the problem
     * vector, and assigns all the sub-problems to the sub population
     * {@link Algorithm}s.
     *
     * @param populations   The list of participating
     *                      {@linkplain PopulationBasedAlgorithm}s.
     * @param problem       The problem that needs to be re-distributed.
     * @param context       The context vector maintained by the
     *                      {@linkplain CooperativeCoevolutionAlgorithm}.
     */
    @Override
    public void performDistribution(List<PopulationBasedAlgorithm> populations,
            Problem problem, Vector context) {
        Preconditions.checkArgument(populations.size() >= 2, "There should at least be two Cooperating populations in a Cooperative Algorithm");
        Preconditions.checkArgument(problem.getDomain().getDimension() % populations.size() == 0,
                "A Problem with dimension " + problem.getDomain().getDimension() + " cannot be split into parts of equal size when using " + populations.size() + " populations");

        int dimension = problem.getDomain().getDimension() / populations.size();
        int offset = 0;
        for (Algorithm population : populations) {
            DimensionAllocation allocation = new SequentialDimensionAllocation(offset, dimension);
            population.setOptimisationProblem(new CooperativeCoevolutionProblemAdapter(problem, allocation, context));
            offset += dimension;
        }
    }
}
