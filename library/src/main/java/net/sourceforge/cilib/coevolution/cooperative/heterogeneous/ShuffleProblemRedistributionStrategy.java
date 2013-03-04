/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.coevolution.cooperative.heterogeneous;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.iterator.RandomAlgorithmIterator;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This {@link ProblemRedistributionStrategy} works by shuffling the problems of the participating {@link PopulationBasedAlgorithm}'s. A list of
 * all the current participating algorithm's {@link CooperativeCoevolutionProblemAdapter}s is created, and then randomly re-assigned to the list
 * of algorithms.
 *
 */
public class ShuffleProblemRedistributionStrategy implements
        ProblemRedistributionStrategy {
    private static final long serialVersionUID = 1852933965918949622L;

    /**
     * {@inheritDoc}
     */
    public void redistributeProblem(List<PopulationBasedAlgorithm> populations, Problem problem, ProblemDistributionStrategy distributionStrategy, Vector context) {
        List<Problem> problems = new ArrayList<Problem>();
        for(PopulationBasedAlgorithm algorithm: populations){
            problems.add(algorithm.getOptimisationProblem().getClone());
        }

        RandomAlgorithmIterator<PopulationBasedAlgorithm> iterator = new RandomAlgorithmIterator<PopulationBasedAlgorithm>(populations);
        while(iterator.hasNext()){
            iterator.next().setOptimisationProblem(problems.get(0));
            problems.remove(0);
        }
    }

    @Override
    public ShuffleProblemRedistributionStrategy getClone(){
        return new ShuffleProblemRedistributionStrategy();
    }

}
