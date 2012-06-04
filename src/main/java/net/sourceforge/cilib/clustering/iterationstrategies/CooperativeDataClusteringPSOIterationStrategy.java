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

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.CooperativePSO;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author Kristina
 */
public class CooperativeDataClusteringPSOIterationStrategy extends AbstractIterationStrategy<CooperativePSO>{
    ClusterParticle contextHolder;
    ClusterParticle previousContextHolder;
    boolean contextinitialized;
    
    public CooperativeDataClusteringPSOIterationStrategy() {
        contextHolder = new ClusterParticle();
        contextinitialized = false;
        previousContextHolder = new ClusterParticle();
    }
    
    public CooperativeDataClusteringPSOIterationStrategy(CooperativeDataClusteringPSOIterationStrategy copy) {
        contextHolder = copy.contextHolder;
        contextinitialized = copy.contextinitialized;
        previousContextHolder = copy.previousContextHolder;
    }
    
    @Override
    public CooperativeDataClusteringPSOIterationStrategy getClone() {
        return new CooperativeDataClusteringPSOIterationStrategy(this);
    }

    @Override
    public void performIteration(CooperativePSO algorithm) {
        int populationIndex = 0;
        ClusterParticle outerContext = new ClusterParticle();
        DataTable table = new StandardDataTable();
        
        if(!contextinitialized)
            initializeContextParticle(algorithm);
        
        for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
            table = ((StandardDataClusteringIterationStrategy) ((DataClusteringPSO) currentAlgorithm).getIterationStrategy()).getDataset();
            
//            if(((DataClusteringPSO) currentAlgorithm).reinitialized()) {
//            //    System.out.println("\nClusters: " + contextHolder.getPosition().toString());
//            //    System.out.println("\n\n");
//                //ProbabilityDistributionFuction uniform = new UniformDistribution();
//                //contextHolder.setCandidateSolution(currentAlgorithm.getTopology().get((int) uniform.getRandomNumber(0,currentAlgorithm.getTopology().size())).getCandidateSolution());
//                //System.out.println("\nBefore: " + contextHolder.getCandidateSolution().toString());
//               contextHolder = mutateContext(contextHolder, (ClusterParticle) currentAlgorithm.getTopology().get(0), table,(DataClusteringPSO) currentAlgorithm);
//                //System.out.println("After: " + contextHolder.getCandidateSolution().toString() + "\n");
//            }
            
            DataClusteringPSO pso = ((DataClusteringPSO) currentAlgorithm);
            Topology newTopology = ((DataClusteringPSO) currentAlgorithm).getTopology().getClone();
            newTopology.clear();
            
            outerContext = contextHolder.getClone();
            for(ClusterParticle particle : ((DataClusteringPSO) currentAlgorithm).getTopology()) {
                
//                //System.out.println(particle.isCharged()?"yes":"no");
//                if(particle.isCharged()) {
//                    for(ClusterParticle particle2 : ((DataClusteringPSO) currentAlgorithm).getTopology()) {
//                        if(particle2 != particle) {
//                            if(particlesAreClose(particle, particle2, populationIndex)) {
//                                particle.reinitialise();
//                                particle.setCharge(true);
//                            }
//                        }
//                    }
//                }
                
                //System.out.println("\nisCharged: " + particle.isCharged());
                particle.calculateFitness();
                ClusterParticle particleWithContext = new ClusterParticle();
                particleWithContext.setCandidateSolution(contextHolder.getCandidateSolution().getClone());
                particleWithContext.getProperties().put(EntityType.FITNESS, particle.getFitness().getClone());
                particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getBestPosition().getClone());
                particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getBestFitness().getClone());
                particleWithContext.getProperties().put(EntityType.Particle.VELOCITY, particle.getVelocity().getClone());
                particleWithContext.setNeighbourhoodBest(particleWithContext);
                ((CentroidHolder) particleWithContext.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex));
                ((CentroidHolder) particleWithContext.getVelocity()).set(populationIndex, ((CentroidHolder) particle.getVelocity()).get(populationIndex));
                //((CentroidHolder) particleWithContext.getBestPosition()).set(populationIndex, ((CentroidHolder) particle.getBestPosition()).get(populationIndex));
                particleWithContext.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER).getClone());
                particleWithContext.setCentroidInitialisationStrategy(particle.getCentroidInitializationStrategy().getClone());
                //System.out.println("After: " + particle.isCharged());
                
                particleWithContext.calculateFitness();
                particle.getFitness().newInstance(particleWithContext.getFitness().getValue());
                //particle = particleWithContext.getClone();
                
                if(!previousContextHolder.equals(contextHolder)) {
                    assignDataPatternsToContext((CentroidHolder) contextHolder.getCandidateSolution(), table);
                    contextHolder.calculateFitness();
                    previousContextHolder = contextHolder;
                }
                
                if(particle.getFitness().getValue() < particle.getBestFitness().getValue()) {
                    particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition());
                    particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
                    
                    particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition());
                    particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
                }
                
                if(particle.getBestFitness().getValue() < contextHolder.getFitness().getValue()) {
                       ((CentroidHolder) outerContext.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex));
                       //((CentroidHolder) outerContext.getVelocity()).set(populationIndex, ((CentroidHolder) particle.getVelocity()).get(populationIndex));
                       //((CentroidHolder) outerContext.getBestPosition()).set(populationIndex, ((CentroidHolder) particle.getBestPosition()).get(populationIndex));
                }
                
                newTopology.add(particleWithContext);
            }
            
//            if(!outerContext.equals(contextHolder)) {
//                assignDataPatternsToContext((CentroidHolder) outerContext.getCandidateSolution(), table);
//                outerContext.calculateFitness();
//
//                if(outerContext.getFitness().getValue() < contextHolder.getFitness().getValue()) {
//                    contextHolder = outerContext.getClone();
//                    contextHolder.getProperties().put(EntityType.FITNESS, outerContext.getFitness());
//                    contextHolder.getProperties().put(EntityType.Particle.BEST_FITNESS, outerContext.getBestFitness());
//                }
//            }
            
            pso.setTopology(newTopology);
            pso.performIteration();
            
            populationIndex++;
        }
        
        
        //System.out.println("Population Index: " + populationIndex);
        
    }
    
    private boolean particlesAreClose(ClusterParticle particle1, ClusterParticle particle2, int dimension) {
        DistanceMeasure distance = new EuclideanDistanceMeasure();
            
        //System.out.println("Distance:" + distance.distance(((CentroidHolder)particle1.getCandidateSolution()).get(dimension).toVector(), ((CentroidHolder) particle2.getCandidateSolution()).get(dimension).toVector()));
        
        if(distance.distance(((CentroidHolder)particle1.getCandidateSolution()).get(dimension).toVector(), ((CentroidHolder) particle2.getCandidateSolution()).get(dimension).toVector()) < 3.0) {
            return true;
        }
        return false;
    }
    
    private ClusterParticle mutateContext(ClusterParticle context, ClusterParticle particle, DataTable table, DataClusteringPSO pso) {
       
        //context.setCentroidInitialisationStrategy(((ClusterParticle) algorithm.getTopology().get(0)).getCentroidInitializationStrategy());
        //context.initialise(algorithm.getOptimisationProblem());
//        ProbabilityDistributionFuction random = new GaussianDistribution();
//        for(ClusterCentroid centroid : (CentroidHolder) context.getCandidateSolution()) {
////            //int index = (int) random.getRandomNumber(0, centroid.size() - 1);
////            
//            for(int index = 0; index < centroid.size(); index ++)
//                    centroid.set(index, Real.valueOf(centroid.get(index).doubleValue() + random.getRandomNumber(0,2.0)));
////            
//        }
//        context.getVelocity().reinitialize();
//        context.getProperties().put(EntityType.Particle.BEST_POSITION, context.getCandidateSolution());
//        
//        assignDataPatternsToContext((CentroidHolder) context.getCandidateSolution(), table);
//        context.calculateFitness();
//        context.getProperties().put(EntityType.Particle.BEST_FITNESS, context.getFitness());
        
//        context.setCandidateSolution((CentroidHolder) pso.getBestSolution().getPosition());
//        context.getVelocity().reinitialize();
//        context.getProperties().put(EntityType.Particle.BEST_POSITION, context.getCandidateSolution());
//        
//        assignDataPatternsToContext((CentroidHolder) context.getCandidateSolution(), table);
//        context.calculateFitness();
//        context.getProperties().put(EntityType.Particle.BEST_FITNESS, context.getFitness());
        
        context = particle.getClone();
        context.reinitialise();
        assignDataPatternsToContext((CentroidHolder) context.getCandidateSolution(), table);
        context.calculateFitness();
        context.getProperties().put(EntityType.Particle.BEST_POSITION, context.getPosition());
        context.getProperties().put(EntityType.Particle.BEST_FITNESS, context.getFitness());
        return context;
        
    }
    
    private void assignDataPatternsToContext(CentroidHolder candidateSolution, DataTable dataset) {
        double euclideanDistance;
        Vector addedPattern;
        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        
        for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                Vector pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                //System.out.println("Data Item: " + pattern.toString());
                int centroidIndex = 0;
                int patternIndex = 0;
                for(ClusterCentroid centroid : candidateSolution) {
                    //System.out.append("Distance: " + distanceMeasure.distance(centroid.toVector(), pattern) + " < " + euclideanDistance);
                    if(distanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = distanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }
                
                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
                //System.out.println("Length: " + candidateSolution.get(patternIndex).getDataItemDistances().length);
            }
    }
    
    public ClusterParticle getContextParticle() {
        return contextHolder;
    }
    
    public void initializeContextParticle(CooperativePSO algorithm) {
        int populationIndex = 0;
        CentroidHolder solution = new CentroidHolder();
        CentroidHolder velocity = new CentroidHolder();
        CentroidHolder bestPosition = new CentroidHolder();
        
        for(PopulationBasedAlgorithm alg : algorithm) {
            solution.add(((CentroidHolder) alg.getTopology().get(0).getCandidateSolution()).get(populationIndex).getClone());
            velocity.add(((CentroidHolder) ((ClusterParticle) alg.getTopology().get(0)).getVelocity()).get(populationIndex).getClone());
            bestPosition.add(((CentroidHolder) ((ClusterParticle) alg.getTopology().get(0)).getBestPosition()).get(populationIndex).getClone());
            
            populationIndex++;
        }
        
        contextHolder.setCandidateSolution(solution);
        contextHolder.getProperties().put(EntityType.Particle.VELOCITY, velocity);
        contextHolder.getProperties().put(EntityType.Particle.BEST_POSITION, bestPosition);
        contextHolder.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        contextHolder.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        
        contextinitialized = true;
    }

}
