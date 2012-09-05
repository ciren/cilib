/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractCooperativeIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.CooperativeDataClusteringPSOIterationStrategy;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the cooperative algorithm. It handles a list of sub-populations, each of which will deal with one dimension of the problem.
 * It handles the value of the global best
 */
public class CooperativePSO extends MultiPopulationBasedAlgorithm{
    private IterationStrategy<CooperativePSO> iterationStrategy;
    private int interval;
    OptimisationSolution bestSolution;

    /*
     * Default constructor for CooperativePSO
     */
    public CooperativePSO() {
        super();
        iterationStrategy = new CooperativeDataClusteringPSOIterationStrategy();
        interval = 0;
        bestSolution = new OptimisationSolution(Vector.of(Double.POSITIVE_INFINITY), new MinimisationFitness(Double.POSITIVE_INFINITY));

    }

    /*
     * Copy constructor for CooperativePSO
     * @param copy The CooperativePSO to be copied
     */
    public CooperativePSO(CooperativePSO copy) {
        super(copy);
        iterationStrategy = copy.iterationStrategy;
        interval = copy.interval;
        bestSolution = copy.bestSolution;
    }

    /*
     * Clone Method for CooperativePSO
     * @return new instance of CooperativePSO
     */
    @Override
    public CooperativePSO getClone() {
        return new CooperativePSO(this);
    }

    /*
     * Calls the iteration strategy's performiteration method.
     * Updates the best solution
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
        ClusterParticle particle = ((AbstractCooperativeIterationStrategy) iterationStrategy).getContextParticle();
        bestSolution = new OptimisationSolution(particle.getPosition(), particle.getFitness());
    }

    /*
     * Returns the best solution found by the algorithm
     * @return bestSolution The best solution
     */
    @Override
    public OptimisationSolution getBestSolution() {
        return bestSolution;
    }

    /*
     * Returns a list of the personal best solutions of the population
     * @return solutions The list of solutions
     */
    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
        for (PopulationBasedAlgorithm currentAlgorithm : this.getPopulations()) {
             for (OptimisationSolution solution : currentAlgorithm.getSolutions()) {
                 solutions.add(solution);
             }
        }
        return solutions;
    }

    /*
     * Initializes the algorithm and its sub-populations
     */
    @Override
    public void algorithmInitialisation()    {
        ClusteringProblem problem = (ClusteringProblem) getOptimisationProblem().getClone();//getCoevolutionOptimisationProblem();
        problem.setNumberOfClusters(subPopulationsAlgorithms.size());

        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            currentAlgorithm.setOptimisationProblem(problem);
            currentAlgorithm.performInitialisation();
        }

    }

    /*
     * Sets the iteration strategy to be used by the algorithm to the one received as a parameter
     * @param strategy The new iteration strategy
     */
    public void setIterationStrategy(IterationStrategy strategy) {
        iterationStrategy = strategy;
    }

    /*
     * Returns the iteration strategy currently being sued by the algorithm
     * @return strategy The iteration strategy
     */
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

}
