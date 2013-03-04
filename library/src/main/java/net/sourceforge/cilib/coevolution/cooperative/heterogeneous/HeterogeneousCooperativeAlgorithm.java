/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import net.sourceforge.cilib.coevolution.cooperative.CooperativeCoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.RandomAlgorithmImperfectSplitDistribution;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;

/**
 * An implementation of a Heterogeneous co-operative coevolution algorithm, which is a cooperative algorithm where the sub population algorithms consist of different algorithm implementations
 * (For example an cooperative algorithm with a PSO as well as a GA as participating algorithms).
 * This functionality is already supported by the {@link CooperativeCoevolutionAlgorithm} class, the purpose of this class is to add a {@link ProblemRedistributionStrategy} to the
 * optimisation process. During this step, after each {@link CooperativeCoevolutionAlgorithm} iteration, the problem is re-distributed amongst the participating algorithms. A parameter is
 * used to regulate the frequency of this re-distribution.
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> O. Olorunda and A.P. Engelbrecht, "An Analysis of Heterogeneous Cooperative Algorithms," IEEE Congress on Evolutionary Computation, 2009.
 * </li>
 * </ul>
 * </p>
 *
 */
public class HeterogeneousCooperativeAlgorithm extends CooperativeCoevolutionAlgorithm {
    private static final long serialVersionUID = 3680479678156166964L;
    protected ControlParameter problemAllocationSwapIteration;
    private ProblemRedistributionStrategy redistributionStrategy;

    public HeterogeneousCooperativeAlgorithm() {
        problemDistribution = new RandomAlgorithmImperfectSplitDistribution();
        problemAllocationSwapIteration = ConstantControlParameter.of(1);
        redistributionStrategy = new ShuffleProblemRedistributionStrategy();
    }

    /**
     * @param copy
     */
    public HeterogeneousCooperativeAlgorithm(
            HeterogeneousCooperativeAlgorithm copy) {
        super(copy);
        problemAllocationSwapIteration = copy.problemAllocationSwapIteration.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {
        super.algorithmIteration();

        if ((getIterations() + 1) % (int)problemAllocationSwapIteration.getParameter() == 0) {
             redistributionStrategy.redistributeProblem(subPopulationsAlgorithms, optimisationProblem, problemDistribution, context.getCandidateSolution());
        }
    }

    public void setRedistributionStrategy(
            ProblemRedistributionStrategy redistributionStrategy) {
        this.redistributionStrategy = redistributionStrategy;
    }
}
