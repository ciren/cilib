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
package net.sourceforge.cilib.clustering.iterationstrategies;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.CooperativePSO;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 * This class an iteration of the cooperative data clustering iteration strategy.
 * If there is a change in the environment, this class re-initializes the context 
 * particle as well as part of or the whole population.
 */
public class DynamicCooperativeDataClusteringPSOIterationStrategy extends CooperativeDataClusteringPSOIterationStrategy{
    int reinitializationInterval;
    int iterationOfChange;
    int nextIterationOfChange;
    
    /*
     * Default constructor for DynamicCooperativeDataClusteringPSOIterationStrategy
     */
    public DynamicCooperativeDataClusteringPSOIterationStrategy() {
        super();
        reinitializationInterval = 1;
        iterationOfChange = 1;
        nextIterationOfChange = 1;
    }
    
    /*
     * Copy cosntructor for DynamicCooperativeDataClusteringPSOIterationStrategy
     */
    public DynamicCooperativeDataClusteringPSOIterationStrategy(DynamicCooperativeDataClusteringPSOIterationStrategy copy) {
        super(copy);
        reinitializationInterval = copy.reinitializationInterval;
        iterationOfChange = copy.iterationOfChange;
        nextIterationOfChange = copy.nextIterationOfChange;
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
     * followed by a check for change which leads to a re-initialization process if it is true.
     * @param algorithm The algorithm for which the iteration is veing performed
     */
    @Override
    public void performIteration(CooperativePSO algorithm) {
        super.performIteration(algorithm);
        
        if(nextIterationOfChange == AbstractAlgorithm.get().getIterations()) {
               nextIterationOfChange += iterationOfChange;
               this.reinitializeContext(algorithm);
               for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
                 Topology topology = currentAlgorithm.getTopology();

                 for(int i = 0; i < topology.size(); i+=reinitializationInterval) {
                    ((ClusterParticle) topology.get(i)).reinitialise();
                    clearDataPatterns((ClusterParticle) topology.get(i));
                    assignDataPatternsToParticle(((CentroidHolder)((ClusterParticle) topology.get(i)).getCandidateSolution()),
                            ((SinglePopulationDataClusteringIterationStrategy)(((DataClusteringPSO) currentAlgorithm).getIterationStrategy())).getDataset());
                }
             }
               
        }
        
        
    }
    
    /*
     * Re-initializes the context particle. It is used by the Dynamic co-operative data clustering 
     * @param currentAlgorithm The algorithm for wich the context must be re-initialied
     */
    public void reinitializeContext(CooperativePSO currentAlgorithm) {  
        contextParticle = ((DataClusteringPSO) currentAlgorithm.getPopulations().get(0)).getTopology().get(0).getClone();
        contextParticle.reinitialise();
        clearDataPatterns(contextParticle);
        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
        contextParticle.calculateFitness();
    }
    
    /*
     * Returns the iteration of change: the iteration at which a change in the dataset occurs.
     * This is until alternative methods of determining a change in the dataset are implemented
     * @return iterationOfChange The Iteration when a change will occur
     */
    public int getIterationOfChange() {
        return iterationOfChange;
    }
    
    /*
     * Sets the iteration of change: the iteration at which a change in the dataset occurs.
     * This is until alternative methods of determining a change in the dataset are implemented
     * @param changeIteration The new value for the iterationOfChange variable
     */
    public void setIterationOfChange(int changeIteration) {
        iterationOfChange = changeIteration;
        nextIterationOfChange = iterationOfChange;
    }
    
    /*
     * Returns the interval at which entities are re-initialized
     * @return reinitializationInterval The interval at which entities are re-initialized
     */
    public int getReinitializationInterval() {
        return reinitializationInterval;
    }
    
    /*
     * Sets the interval at which entities must be re-initialized
     * @param interval The interval at which entities must be
     */
    public void setReinitializationInterval(int interval) {
        reinitializationInterval = interval;
    }
    
}
