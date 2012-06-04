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

import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 *
 * @author Kristina
 */
public class ReinitializingDataClusteringIterationStrategy extends SinglePopulationDataClusteringIterationStrategy{
    private SinglePopulationDataClusteringIterationStrategy delegate;
    
    public ReinitializingDataClusteringIterationStrategy() {
        delegate = new StandardDataClusteringIterationStrategy();
    }
    
    public ReinitializingDataClusteringIterationStrategy(ReinitializingDataClusteringIterationStrategy copy) {
        delegate = copy.delegate;
    }
    
    @Override
    public ReinitializingDataClusteringIterationStrategy getClone() {
        return new ReinitializingDataClusteringIterationStrategy(this);
    }

    @Override
    public void performIteration(DataClusteringPSO algorithm) {
        delegate.setWindow(this.window);
        delegate.performIteration(algorithm);
        
        if(delegate.getWindow().hasSlid()) {
            System.out.println("\n" + algorithm.getBestSolution().getPosition().toString());
            reinitializePosition(algorithm.getTopology());
        }
        
    }
    
    public SinglePopulationDataClusteringIterationStrategy getDelegate() {
        return delegate;
    }
    
    
    public void setDelegate(SinglePopulationDataClusteringIterationStrategy newDelegate){
        delegate = newDelegate;
    }
    
    private void reinitializePosition(Topology<ClusterParticle> topology) {
        int index = 0;
        for(int i = index; i < topology.size(); i+=reinitialisationInterval) {
                ((ClusterParticle) topology.get(i)).reinitialise();
        }
        
    }
    
}
