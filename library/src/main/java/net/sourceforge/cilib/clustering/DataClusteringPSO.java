/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class holds the functionality of the Standard Data Clustering PSO described in:
 * <pre>
 * {@literal @}article{vanDerMerwe03,
 *  title={{Data Clustering using Particle Swarm Optimization }},
 *  author={van der Merwe, D.W.; Engelhrecht, A.P.},
 *  year={2003},
 *  journal={Congress on Evolutionary Computation},
 *  volume={1},
 *  pages={215-220}
 * }
 * </pre>
 *
 * This is so if the StandardDataClusteringIterationStrategy is used. Variations of the algorithm
 * in the article above can then be tested by using other iteration strategies, such as the
 * ReinitialisingDataClusteringIterationStrategy
 */
public class DataClusteringPSO extends SinglePopulationBasedAlgorithm<ClusterParticle> {

    private SlidingWindow window;
    private IterationStrategy<DataClusteringPSO> iterationStrategy;
    private boolean isExplorer;
    private int numberOfCentroids;

    /*
     * Default Constructor for DataClusteringPSO
     */
    public DataClusteringPSO() {
        initialisationStrategy = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();
        window = new SlidingWindow();
        iterationStrategy = new StandardDataClusteringIterationStrategy();
        isExplorer = false;
        numberOfCentroids = 1;
    }

    /*
     * copy constructor for DataClusteringPSO
     * @param copy Th DataClusteringPSO to be copied
     */
    public DataClusteringPSO(DataClusteringPSO copy) {
        super(copy);
        window = copy.window.getClone();
        iterationStrategy = copy.iterationStrategy.getClone();
        isExplorer = copy.isExplorer;
        numberOfCentroids = copy.numberOfCentroids;
    }

    /*
     * Clone method for the DataClusteringPSO
     * @return new instance of the DataClusteringPSO
     */
    @Override
    public DataClusteringPSO getClone() {
        return new DataClusteringPSO(this);
    }

    /*
     * Calls the IterationStrategy's performIteration method
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /*
     * Initialises the algorithm. This includes the SlidingWindow and topology
     */
    @Override
    public void algorithmInitialisation() {
        DataTable dataset = window.initialiseWindow();

        Vector pattern = ((StandardPattern) dataset.getRow(0)).getVector();
        ((ClusteringProblem) this.optimisationProblem).setDimension((int) pattern.size());

        ((DataDependantPopulationInitialisationStrategy) initialisationStrategy).setDataset(window.getCompleteDataset());
        Iterable<ClusterParticle> particles = (Iterable<ClusterParticle>) this.initialisationStrategy.initialise(this.getOptimisationProblem());

        topology.clear();
        topology.addAll(Lists.<ClusterParticle>newLinkedList(particles));

        ((SinglePopulationDataClusteringIterationStrategy) iterationStrategy).setWindow(window);

        for(ClusterParticle particle : topology) {
            particle.calculateFitness();
        }
    }

    /*
     * Returns the global best solution found by the algorithm so far
     * @return solution The global best solution
     */
    @Override
    public OptimisationSolution getBestSolution() {
        ClusterParticle bestEntity = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<ClusterParticle>());
        return new OptimisationSolution(bestEntity.getBestPosition(), bestEntity.getBestFitness());
    }

    /*
     * Returns a list with each particle's best solution
     * @return solutions The list of solutions held by the Topology
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newLinkedList();
        for (ClusterParticle e : Topologies.getNeighbourhoodBestEntities(topology, new SocialBestFitnessComparator<ClusterParticle>())) {
            solutions.add(new OptimisationSolution(e.getBestPosition(), e.getBestFitness()));
        }
        return solutions;

    }

    /*
     * Sets the window's source URL. This source URL is the path to the file containing
     * the dataset to be clustered.
     * @param sourceURL The path to the dataset
     */
    public void setSourceURL(String sourceURL) {
        window.setSourceURL(sourceURL);
    }

    /*
     * Sets the SlidingWindow to the one received as a parameter
     * @param slidingWindow The new sliding window
     */
    public void setWindow(SlidingWindow slidingWindow) {
        String url = window.getSourceURL();
        window = slidingWindow;
        window.setSourceURL(url);
    }

    /*
     * Sets the iteration strategy to the one provided as a parameter
     * @param strategy The new iterations strategy
     */
    public void setIterationStrategy(IterationStrategy strategy) {
        iterationStrategy = strategy;
    }

    /*
     * Returns the current iteration strategy
     * @return iterationStrategy The current iteration strategy
     */
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

    /*
     * Sets the boolean value of isExplorer to the one provided as a parameter.
     * This is used in the co-operative multi-swarm as one of the swarms must be
     * an explorer.
     * @param value The new boolean value of isExplorer
     */
    public void setIsExplorer(boolean value) {
        isExplorer = value;
    }

    /*
     * Returns the value of isExplorer, i.e. it checks if the algorithm is currently an explorer
     * @return isExplorer The value of isExplorer
     */
    public boolean isExplorer() {
        return isExplorer;
    }

}
