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

import java.util.Iterator;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class handles an iteration of the data clustering algorithm described in :
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
 * 
 */
public class StandardDataClusteringIterationStrategy extends SinglePopulationDataClusteringIterationStrategy {
    /*
     * Default constructor for StandardDataClusteringIterationStrategy
     */
    public StandardDataClusteringIterationStrategy() {
        super();
    }
    
    /*
     * Copy constructor for StandardDataClusteringIterationStrategy
     */
    public StandardDataClusteringIterationStrategy(StandardDataClusteringIterationStrategy copy) {
        super(copy);
    }
    
    /*
     * Clone method of StandardDataClusteringIterationStrategy
     */
    @Override
    public StandardDataClusteringIterationStrategy getClone() {
        return new StandardDataClusteringIterationStrategy(this);
    }

    /*
     * Performs an iteratiion of the standard data clustering algorithm.
     * Assigns data patterns to centroids, updates personal and neighbourhood 
     * bests and updates the particles.
     * @param algortihm The algorithm that called this iteration strategy
     */
    @Override
    public void performIteration(DataClusteringPSO algorithm) {
        Topology<ClusterParticle> topology = algorithm.getTopology();
        double euclideanDistance;
        Vector addedPattern;
        clearCentroidDistanceValues(topology);
        reinitialized = false;
        
        for(ClusterParticle particle : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) particle.getCandidateSolution();
            for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                Vector pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
                for(ClusterCentroid centroid : candidateSolution) {
                    if(distanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = distanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }
                
                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
            }
            
            particle.setCandidateSolution(candidateSolution);
            
            particle.calculateFitness();
            particle.updateVelocity();
            particle.updatePosition();
            
            boundaryConstraint.enforce(particle);
        }
        
        for (Iterator<? extends ClusterParticle> i = topology.iterator(); i.hasNext();) {
            ClusterParticle current = i.next();

            for (Iterator<? extends ClusterParticle> j = topology.neighbourhood(i); j.hasNext();) {
                ClusterParticle other = j.next();
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
            
        }
        
        dataset = window.slideWindow();
        
        
    }
    
    /*
     * Removes all data items assigned to each centroid in each particle in the topology
     * @param topology The topology whose centroids need to be cleaned
     */
    private void clearCentroidDistanceValues(Topology<ClusterParticle> topology) {
        for(ClusterParticle particle : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) particle.getCandidateSolution();
            
            for(ClusterCentroid centroid : candidateSolution) {
                centroid.clearDataItems();
            }
        }
    }
    
    
}
