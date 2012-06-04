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

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author Kristina
 */
public class ClusteringMultiSwarmIterationStrategy extends AbstractIterationStrategy<MultiSwarm> {

    private double exclusionRadius = 2.0;

    public ClusteringMultiSwarmIterationStrategy() {
        super();
    }

    public ClusteringMultiSwarmIterationStrategy(ClusteringMultiSwarmIterationStrategy copy) {
        super();
        this.exclusionRadius = copy.exclusionRadius;
    }

    @Override
    public ClusteringMultiSwarmIterationStrategy getClone() {
        return new ClusteringMultiSwarmIterationStrategy(this);
    }

    public double getExclusionRadius() {
        return exclusionRadius;
    }

    public void setExclusionRadius(double exlusionRadius) {
        this.exclusionRadius = exlusionRadius;
    }

    double calculateRadius() {
        double dimensions = AbstractAlgorithm.get().getOptimisationProblem().getDomain().getDimension();
        //    double X = ((Vector) Algorithm.get().getOptimisationProblem().getDomain().getBuiltRepresenation()).getNumeric(0).getBounds().getUpperBound()
        //            - ((Vector) Algorithm.get().getOptimisationProblem().getDomain().getBuiltRepresenation()).getNumeric(0).getBounds().getLowerBound();
        double X = ((Vector) AbstractAlgorithm.get().getOptimisationProblem().getDomain().getBuiltRepresenation()).get(0).getBounds().getUpperBound()
                - ((Vector) AbstractAlgorithm.get().getOptimisationProblem().getDomain().getBuiltRepresenation()).get(0).getBounds().getLowerBound();
        double populationSize = ((MultiSwarm) (AbstractAlgorithm.get())).getPopulations().size();
        return X / (2 * Math.pow(populationSize, 1 / dimensions));
    }

    boolean isConverged(PopulationBasedAlgorithm algorithm) {
        double r = calculateRadius();
        int converged = 0;
        
        DistanceMeasure dm = new EuclideanDistanceMeasure();
        
        // Some other form of convergence check
        for(ClusterParticle particle : ((DataClusteringPSO) algorithm).getTopology()) {
            for(ClusterParticle particle2 : ((DataClusteringPSO) algorithm).getTopology()) {
                ClusterParticle particleCopy = particle2.getClone();
                if(!particle.getCandidateSolution().containsAll(particleCopy.getCandidateSolution())) {
                    for(int i = 0; i < ((CentroidHolder) particle.getCandidateSolution()).size(); i++) {
                        ClusterCentroid closestCentroid = getClosestCentroid((((CentroidHolder) particle.getCandidateSolution()).get(i)),
                                ((CentroidHolder) particleCopy.getCandidateSolution()));
                        particleCopy.getCandidateSolution().remove(closestCentroid);
//                        System.out.println("Centroid 1: " + ((CentroidHolder) particle.getCandidateSolution()).get(i).toString());
//                        System.out.println("Centroid 2: " + closestCentroid.toString());
                       // System.out.println(dm.distance(((CentroidHolder) particle.getCandidateSolution()).get(i), closestCentroid) + " < " + r);
                        if(dm.distance(((CentroidHolder) particle.getCandidateSolution()).get(i), closestCentroid) > r) {
                            return false;
                        }
                    }
                }
            }
        }
        
        //if(converged >= algorithm.getTopology().size() * 90 / 100)
            //return false;
        
        return true;
    }
    
    private ClusterCentroid getClosestCentroid(ClusterCentroid centroid, CentroidHolder holder2) {
        DistanceMeasure dm = new EuclideanDistanceMeasure();
        ClusterCentroid closestCentroid = holder2.get(0);
        double distance = Double.POSITIVE_INFINITY;
        
        for(int i = 0; i < holder2.size(); i++) {
            if(distance > dm.distance(centroid.toVector(), holder2.get(i).toVector())) {
                distance = dm.distance(centroid.toVector(), holder2.get(i).toVector());
                closestCentroid = holder2.get(i);
            }
        }
        
        return closestCentroid;
    }

    @Override
    public void performIteration(MultiSwarm ca) {
        int converged = 0;
        for (PopulationBasedAlgorithm current : ca.getPopulations()) {
            if (isConverged(current)) {
                converged++;
            }
        }
        //all swarms have converged-> must re-initialise worst swarm
        if (converged == ca.getPopulations().size()) {
            PopulationBasedAlgorithm weakest = null;
            for (PopulationBasedAlgorithm current : ca.getPopulations()) {
                if (weakest == null || weakest.getBestSolution().compareTo(current.getBestSolution()) > 0) {
                    weakest = current;
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
        
        if((ca.getIterations() == 2999)) {
            System.out.println("Clusters: " + AbstractAlgorithm.get().getBestSolution().getPosition().toString());
            System.out.println("\n\n");
        }
        
    }
    
    private boolean aDistanceIsSmallerThanRadius(CentroidHolder currentPosition, CentroidHolder otherPosition) {
        DistanceMeasure dm = new EuclideanDistanceMeasure();
        
        for(int i = 0; i < currentPosition.size(); i++) {
            if(dm.distance(currentPosition.get(i).toVector(), otherPosition.get(i).toVector()) < exclusionRadius)
                return true;
        }
        return false;
    }

    public void reInitialise(DataClusteringPSO algorithm) {     
        for(ClusterParticle particle : algorithm.getTopology()) {
            particle.reinitialise();
        }
    }
}
