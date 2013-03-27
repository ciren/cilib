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
import net.sourceforge.cilib.algorithm.iterator.RandomAlgorithmIterator;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problem.DimensionAllocation;
import net.sourceforge.cilib.coevolution.cooperative.problem.SequentialDimensionAllocation;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@linkplain ProblemDistributionStrategy} performs a split by assigning a
 * sequential portion of the varying length to each participating
 * {@linkplain PopulationBasedAlgorithm}.
 * <p>
 * Defaults into a split of equal sizes if possible. The order in which the
 * algorithms are assigned is generated randomly.
 */
public class RandomAlgorithmImperfectSplitDistribution implements
        ProblemDistributionStrategy {

    /**
     * Splits up the given {@link Problem} into
     * sub-problems, where each sub problem contains a sequential
     * non-uniform sized) portion of the problem vector, and assigns them to all
     * the participating {@link Algorithm}s. This implementation assigns a
     * portion of length dimensionality/number of populations + 1 to
     * dimensionality % number of populations of the participating populations.
     * The order in which the algorithms are assigned is generated randomly.
     *
     * @param populations   The list of participating
     *                      {@linkplain PopulationBasedAlgorithm}s.
     * @param problem       The problem that needs to be re-distributed.
     * @param context       The context vector maintained by the
     *                      {@linkplain CooperativeCoevolutionAlgorithm}.
     */
    public void performDistribution(List<PopulationBasedAlgorithm> populations,
            Problem problem, Vector context) {
        Preconditions.checkArgument(populations.size() >= 2,
                "There should at least be two Cooperating populations in a Cooperative Algorithm");

        int dimension = problem.getDomain().getDimension() / populations.size();
        int oddDimensions = problem.getDomain().getDimension() % populations.size();
        int i = 0;
        int offset = 0;
        RandomAlgorithmIterator<PopulationBasedAlgorithm> iterator = new RandomAlgorithmIterator<PopulationBasedAlgorithm>(populations);
        while (iterator.hasNext()) {
            int actualDimension = dimension;
            if (i < oddDimensions) {
                actualDimension++;
            }

            DimensionAllocation problemAllocation = new SequentialDimensionAllocation(offset, actualDimension);
            iterator.next().setOptimisationProblem(new CooperativeCoevolutionProblemAdapter(problem, problemAllocation, context));
            offset += actualDimension;

            ++i;
        }
    }
}
