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
package net.sourceforge.cilib.pso.multiswarm;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * This class performs an iteration of a clustering multi-swarm iteration strategy where ClusterParticles are used.
 * If all swarms have converged, the weakest is re-initialized.
 * If two swarms are too lose together, one of them is re-initialized.
 * It is based on the algorithm described in Blackwell, but some calculations have been adapted to handle ClusterParticles.
 * 
 * {@literal @}article{blackwell51pso,
 *  title={{Particle swarm optimization in dynamic environments}},
 *  author={Blackwell, T.},
 *  journal={Evolutionary Computatation in Dynamic and Uncertain Environments},
 *  volume={51},
 *  pages={29--49}
 * }
 */
public class StandardClusteringMultiSwarmIterationStrategy extends AbstractIterationStrategy<MultiPopulationBasedAlgorithm> {

    private double exclusionRadius = 1.0;

    /*
     * Default constructor for StandardClusteringMultiSwarmIterationStrategy
     */
    public StandardClusteringMultiSwarmIterationStrategy() {
        super();
    }

    /*
     * Copy constructor for StandardClusteringMultiSwarmIterationStrategy
     * @param copy The StandardClusteringMultiSwarmIterationStrategy to be copied
     */
    public StandardClusteringMultiSwarmIterationStrategy(StandardClusteringMultiSwarmIterationStrategy copy) {
        super();
        this.exclusionRadius = copy.exclusionRadius;
    }

    /*
     * Clone method for StandardClusteringMultiSwarmIterationStrategy
     */
    @Override
    public StandardClusteringMultiSwarmIterationStrategy getClone() {
        return new StandardClusteringMultiSwarmIterationStrategy(this);
    }

    /*
     * Returns the value of the exclusion radius
     * @return exclusionRadius The exclusion radius
     */
    public double getExclusionRadius() {
        return exclusionRadius;
    }

    /*
     * Sets the value of the exclusion radius
     * @param newRadius The new exclusion radius
     */
    public void setExclusionRadius(double exlusionRadius) {
        this.exclusionRadius = exlusionRadius;
    }

    /*
     * Calculates the radius that is used to determine whether a swarm must be repelled from another swarm
     * @param algorithm The current algorithm
     * @return radius The radius
     */
    double calculateRadius(MultiPopulationBasedAlgorithm algorithm) {
        double dimensions = algorithm.getOptimisationProblem().getDomain().getDimension();
        double X = ((Vector) algorithm.getOptimisationProblem().getDomain().getBuiltRepresentation()).get(0).getBounds().getUpperBound()
                - ((Vector) algorithm.getOptimisationProblem().getDomain().getBuiltRepresentation()).get(0).getBounds().getLowerBound();
        double populationSize = ((MultiSwarm) algorithm).getPopulations().size();
        return X / (2 * Math.pow(populationSize, 1 / dimensions));
    }

    /*
     * Checks if a swarm has converged
     * @param algorithm The algorithm holding the swarm being checked
     * @param ca The current multi-population algorithm 
     * @return true if the swarm has converged, false otherwise
     */
    boolean isConverged(PopulationBasedAlgorithm algorithm, MultiPopulationBasedAlgorithm ca) {
        double r = calculateRadius(ca);
        int converged = 0;
        
        DistanceMeasure dm = new EuclideanDistanceMeasure();
        
        for(ClusterParticle particle : ((DataClusteringPSO) algorithm).getTopology()) {
            for(ClusterParticle particle2 : ((DataClusteringPSO) algorithm).getTopology()) {
                ClusterParticle particleCopy = particle2.getClone();
                if(!particle.getCandidateSolution().containsAll(particleCopy.getCandidateSolution())) {
                    for(int i = 0; i < ((CentroidHolder) particle.getCandidateSolution()).size(); i++) {
                        ClusterCentroid closestCentroid = getClosestCentroid((((CentroidHolder) particle.getCandidateSolution()).get(i)),
                                ((CentroidHolder) particleCopy.getCandidateSolution()));
                        particleCopy.getCandidateSolution().remove(closestCentroid);
                        if(dm.distance(((CentroidHolder) particle.getCandidateSolution()).get(i), closestCentroid) > r) {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    /*
     * Gets the closest centroid to the one received
     * @param centroid The centroid received
     * @param holder The centroid holder with all the other centroids
     * @return closestCentroid The closest centroid to centroid
     */
    private ClusterCentroid getClosestCentroid(ClusterCentroid centroid, CentroidHolder holder) {
        DistanceMeasure dm = new EuclideanDistanceMeasure();
        ClusterCentroid closestCentroid = holder.get(0);
        double distance = Double.POSITIVE_INFINITY;
        
        for(int i = 0; i < holder.size(); i++) {
            if(distance > dm.distance(centroid.toVector(), holder.get(i).toVector())) {
                distance = dm.distance(centroid.toVector(), holder.get(i).toVector());
                closestCentroid = holder.get(i);
            }
        }
        
        return closestCentroid;
    }

    /*
     * Performs an iteration of the Clustering Multi-swarm Iteration Strategy.
     * If all swarms have converged, it re-initializes the weakest one.
     * It performs an iteration of the algorithm holding each swarm.
     * It re-initializes a swarm if two swarms get too close to each other.
     * @param ca The multi-swarm algorithm
     */
    @Override
    public void performIteration(MultiPopulationBasedAlgorithm ca) {
        int converged = 0;
        for (PopulationBasedAlgorithm current : ca.getPopulations()) {
            if (isConverged(current, ca)) {
                converged++;
            }
        }
        
        if (converged == ca.getPopulations().size()) {
            PopulationBasedAlgorithm weakest = null;
            for (PopulationBasedAlgorithm current : ca.getPopulations()) {
                ((DataClusteringPSO) current).setIsExplorer(false);
                if (weakest == null || weakest.getBestSolution().compareTo(current.getBestSolution()) > 0) {
                    weakest = current;
                    ((DataClusteringPSO) weakest).setIsExplorer(true);
                }
            }
            reInitialise((DataClusteringPSO) weakest);
        }

        for (PopulationBasedAlgorithm current : ca.getPopulations()) {
            current.performIteration();
        }

        for (PopulationBasedAlgorithm current : ca.getPopulations()) {
            for (PopulationBasedAlgorithm other : ca.getPopulations()) {
                CentroidHolder currentPosition, otherPosition;
                if (!current.equals(other)) {
                    currentPosition = (CentroidHolder) ((DataClusteringPSO) current).getBestSolution().getPosition(); //getBestParticle().getPosition();
                    otherPosition = (CentroidHolder) ((DataClusteringPSO) other).getBestSolution().getPosition();
                    boolean aDistanceIsSmallerThanRadius = aDistanceIsSmallerThanRadius(currentPosition, otherPosition);
                    
                    if (aDistanceIsSmallerThanRadius) {
                        if (((DataClusteringPSO) current).getBestSolution().getFitness().compareTo(((DataClusteringPSO) other).getBestSolution().getFitness()) > 0) {
                            reInitialise((DataClusteringPSO) current);
                        } else {
                            reInitialise((DataClusteringPSO) other);
                        }
                    }
                }
            }
        }
        
    }
    
    /*
     * Checks if the distance between two centroid holders is smaller than the radius.
     * It does it by comparing each centroid of the current position to that of the other position
     * @param currentPosition The current position being checked
     * @param otherPosition The position that currentPosition needs to be checked against
     * @return true if the swarms are too close, false otherwise
     */
    private boolean aDistanceIsSmallerThanRadius(CentroidHolder currentPosition, CentroidHolder otherPosition) {
        DistanceMeasure dm = new EuclideanDistanceMeasure();
        
        for(int i = 0; i < currentPosition.size(); i++) {
            if(dm.distance(currentPosition.get(i).toVector(), otherPosition.get(i).toVector()) < exclusionRadius)
                return true;
        }
        return false;
    }

    /*
     * Reinitializes a swarm
     * @param algorithm The algorithm holding the swarm
     */
    public void reInitialise(DataClusteringPSO algorithm) {     
        for(ClusterParticle particle : algorithm.getTopology()) {
            particle.reinitialise();
            assignDataPatternsToParticle((CentroidHolder) particle.getCandidateSolution(), 
                    ((SinglePopulationDataClusteringIterationStrategy) algorithm.getIterationStrategy()).getWindow().getCurrentDataset());
        }
    }
    
     /*
     * Adds the data patterns closest to a centrid to its data pattern list
     * @param candidateSolution The solution holding all the centroids
     * @param dataset The dataset holding all the data patterns
     */
    public void assignDataPatternsToParticle(CentroidHolder candidateSolution, DataTable dataset) {
        double euclideanDistance;
        Vector addedPattern;
        DistanceMeasure aDistanceMeasure = new EuclideanDistanceMeasure();
        
        for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                Vector pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
                for(ClusterCentroid centroid : candidateSolution) {
                    if(aDistanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = aDistanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }
                
                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
            }
    }
}
