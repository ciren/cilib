/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.CooperativePSO;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.util.changeDetection.ChangeDetectionStrategy;
import net.sourceforge.cilib.util.changeDetection.IterationBasedChangeDetectionStrategy;

/**
 * This class an iteration of the cooperative data clustering iteration strategy.
 * If there is a change in the environment, this class re-initialises the context
 * particle as well as part of or the whole population.
 */
public class DynamicCooperativeDataClusteringPSOIterationStrategy extends CooperativeDataClusteringPSOIterationStrategy{
    int reinitialisationInterval;
    ChangeDetectionStrategy changeDetectionStrategy;

    /*
     * Default constructor for DynamicCooperativeDataClusteringPSOIterationStrategy
     */
    public DynamicCooperativeDataClusteringPSOIterationStrategy() {
        super();
        reinitialisationInterval = 1;
        changeDetectionStrategy = new IterationBasedChangeDetectionStrategy();
    }

    /*
     * Copy constructor for DynamicCooperativeDataClusteringPSOIterationStrategy
     */
    public DynamicCooperativeDataClusteringPSOIterationStrategy(DynamicCooperativeDataClusteringPSOIterationStrategy copy) {
        super(copy);
        reinitialisationInterval = copy.reinitialisationInterval;
        changeDetectionStrategy = copy.changeDetectionStrategy;
    }

    /*
     * Clone method for DynamicCooperativeDataClusteringPSOIterationStrategy
     * @return new instance of the DynamicCooperativeDataClusteringPSOIterationStrategy
     */
    @Override
    public DynamicCooperativeDataClusteringPSOIterationStrategy getClone() {
        return new DynamicCooperativeDataClusteringPSOIterationStrategy(this);
    }

    /*
     * Performs an iteration of DynamicCooperativeDataClusteringPSOIterationStrategy
     * First it performs an iteration of the cooperative data clustering iteration strategy,
     * followed by a check for change which leads to a re-initialisation process if it is true.
     * @param algorithm The algorithm for which the iteration is being performed
     */
    @Override
    public void performIteration(CooperativePSO algorithm) {
        Topology topology;
        if(changeDetectionStrategy.detectChange()) {
               this.reinitialiseContext(algorithm);
               for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
                 topology = currentAlgorithm.getTopology();

                 for(int i = 0; i < topology.size(); i+=reinitialisationInterval) {
                    ((ClusterParticle) topology.get(i)).reinitialise();
                    clearDataPatterns((ClusterParticle) topology.get(i));
                    assignDataPatternsToParticle(((CentroidHolder)((ClusterParticle) topology.get(i)).getCandidateSolution()),
                            ((SinglePopulationDataClusteringIterationStrategy)(((DataClusteringPSO) currentAlgorithm).getIterationStrategy())).getDataset());
                }
             }

        }

        super.performIteration(algorithm);
    }

    /*
     * Re-initialises the context particle. It is used by the Dynamic co-operative data clustering
     * @param currentAlgorithm The algorithm for which the context must be re-initialised
     */
    public void reinitialiseContext(CooperativePSO currentAlgorithm) {
        contextParticle = ((DataClusteringPSO) currentAlgorithm.getPopulations().get(0)).getTopology().get(0).getClone();
        contextParticle.reinitialise();
        clearDataPatterns(contextParticle);
        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
        contextParticle.calculateFitness();
    }

    /*
     * Returns the interval at which entities are re-initialised
     * @return reinitialisationInterval The interval at which entities are re-initialised
     */
    public int getReinitialisationInterval() {
        return reinitialisationInterval;
    }

    /*
     * Sets the interval at which entities must be re-initialised
     * @param interval The interval at which entities must be
     */
    public void setReinitialisationInterval(int interval) {
        reinitialisationInterval = interval;
    }

    /*
     * Sets the change detection strategy to be used
     * @param changeStrategy The new changeDetectionStrategy
     */
    public void setChangeDetectionStrategy(ChangeDetectionStrategy changeStrategy) {
        changeDetectionStrategy = changeStrategy;
    }

    /*
     * Returns the change detection strategy being used
     * @return cahngeDetectionStrategy The current change detection strategy
     */
    public ChangeDetectionStrategy getChangeDetectionStrategy() {
        return changeDetectionStrategy;
    }

}
