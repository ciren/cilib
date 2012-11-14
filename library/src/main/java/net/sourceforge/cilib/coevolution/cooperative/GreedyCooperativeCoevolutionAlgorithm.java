/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.coevolution.cooperative;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contextupdate.ContextUpdateStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.problem.CooperativeCoevolutionProblemAdapter;
import net.sourceforge.cilib.coevolution.cooperative.problemdistribution.ProblemDistributionStrategy;
import net.sourceforge.cilib.measurement.single.ThresholdedStagnationFlag;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

/**
 * This class forms the basis for any co-operative coevolution optimization
 * algorithm implementations. A cooperative algorithm is an algorithm that
 * maintains a context solution and a list of participating algorithms. Each
 * participating algorithm optimizes only a subsection of the problem, and
 * fitness values are computed by inserting an enitie's solution into the
 * current context vector before it is evaluated. The context vector is simply
 * the concatenation of the best solutions from each participating population.
 *
 * Any algorithm that wishes to participate in a co-operative optimisation
 * algorithm must implement the {@link ParticipatingAlgorithm} interface. This class
 * also implements {@link ParticipatingAlgorithm}, meaning that co-operative
 * algorithms can be composed of co-operative algorithms again.
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> M. Potter and K.D. Jong, "A Cooperative Coevolutionary approach to function optimization,"
 * in Proceedings of the Third conference on Paralell Problem Solving from Nature, pp. 249-257,
 * Springer-Verlag, 1994.
 * </li>
 * <li> F. van den Bergh and A. Engelbrecht, "A cooperative approach to particle swarm optimization,"
 * IEEE Transactions on Evolutionary Computation, vol. 8, no. 3, pp 225-239, 2004.
 * </li>
 * </ul>
 * </p>
 *
 * @TODO: test this class.
 *
 */
public class GreedyCooperativeCoevolutionAlgorithm extends CooperativeCoevolutionAlgorithm {

    private static final long serialVersionUID = 3351497412601778L;
    private ThresholdedStagnationFlag stagnationFlag;
    /**
     * Constructor
     */
    public GreedyCooperativeCoevolutionAlgorithm() {
        super();
        this.stagnationFlag = new ThresholdedStagnationFlag();
    }

    /**
     * Copy constructor
     * @param copy The {@linkplain CooperativeCoevolutionAlgorithm} to make a copy of.
     */
    public GreedyCooperativeCoevolutionAlgorithm(GreedyCooperativeCoevolutionAlgorithm copy) {
        super(copy);
    }

    @Override
    public void algorithmInitialisation() {
        super.algorithmInitialisation();
        algorithmIterator.setAlgorithms(subPopulationsAlgorithms); // set the iterator right away!
        algorithmIterator.next(); // set iterator index to something sensible
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void algorithmIteration() {
        //System.out.println("Current algo index: " + (algorithmIterator.nextIndex() - 1));
         //get the optimisation problem from the algorithm
        CooperativeCoevolutionProblemAdapter problem = (CooperativeCoevolutionProblemAdapter) algorithmIterator.current().getOptimisationProblem();
        //update the context solution to point to the current context
        problem.updateContext(context.getCandidateSolution());
        //perform an iteration of the sub population algorithm
        algorithmIterator.current().performIteration();
        //select the contribution from the population
        contextUpdate.updateContext(context, ((ParticipatingAlgorithm) algorithmIterator.current()).getContributionSelectionStrategy().getContribution(algorithmIterator.current()), problem.getProblemAllocation());
        // check for stagnation:
        if(stagnationFlag.getValue(this).booleanValue()) { // stagnation! Therefore, go to the next subpopulation
            if(algorithmIterator.hasNext()) algorithmIterator.next();
            else {
                algorithmIterator.setAlgorithms(subPopulationsAlgorithms); // set the iterator right away!
                algorithmIterator.next(); // set iterator index to something sensible
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        return new OptimisationSolution(context.getCandidateSolution().getClone(), context.getFitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Arrays.asList(getBestSolution());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPopulationBasedAlgorithm(PopulationBasedAlgorithm algorithm) {
        // TODO: There should be a better way to perfrom this test, rather than using an instanceof.
        if (((ParticipatingAlgorithm) algorithm).getContributionSelectionStrategy() instanceof ZeroContributionSelectionStrategy) {
            ((ParticipatingAlgorithm) algorithm).setContributionSelectionStrategy(contributionSelection);
        }

        super.addPopulationBasedAlgorithm(algorithm);
    }

    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return contributionSelection;
    }

    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        contributionSelection = strategy;
    }

    @Override
    public void setContextUpdate(ContextUpdateStrategy contextUpdate) {
        this.contextUpdate = contextUpdate;
    }

    @Override
    public void setProblemDistribution(ProblemDistributionStrategy problemDistribution) {
        this.problemDistribution = problemDistribution;
    }

    @Override
    public ContextEntity getContext() {
        return context;
    }
}
