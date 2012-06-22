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

import net.sourceforge.cilib.algorithm.population.AbstractCooperativeIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 *
 * @author Kristina
 */
public class CooperativeMultiswarmIterationStrategy extends AbstractCooperativeIterationStrategy<MultiPopulationBasedAlgorithm>{
    IterationStrategy<MultiPopulationBasedAlgorithm> delegate;
    
    public CooperativeMultiswarmIterationStrategy() {
        super();
        delegate = new StandardClusteringMultiSwarmIterationStrategy();
    }
    
    public CooperativeMultiswarmIterationStrategy(CooperativeMultiswarmIterationStrategy copy) {
        super(copy);
        delegate = copy.delegate;
    }
    
    @Override
    public CooperativeMultiswarmIterationStrategy getClone() {
        return new CooperativeMultiswarmIterationStrategy(this);
    }
    
    @Override
    public void performIteration(MultiPopulationBasedAlgorithm algorithm) {
        int populationIndex = 0;
        
        for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
                table = ((SinglePopulationDataClusteringIterationStrategy) ((DataClusteringPSO) currentAlgorithm).getIterationStrategy()).getDataset();

                if(!contextinitialized) {
                    ((DataClusteringPSO) currentAlgorithm).setIsExplorer(true);
                    initializeContextParticle(algorithm);
                }

                if(!((DataClusteringPSO) currentAlgorithm).isExplorer()) {
                    for(ClusterParticle particle : ((DataClusteringPSO) currentAlgorithm).getTopology()) {
                        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
                        contextParticle.calculateFitness();

                        ClusterParticle particleWithContext = new ClusterParticle();
                        particleWithContext.setCandidateSolution(contextParticle.getCandidateSolution().getClone());
                        particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getBestPosition().getClone());
                        particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getBestFitness().getClone());
                        particleWithContext.getProperties().put(EntityType.Particle.VELOCITY, particle.getVelocity().getClone());
                        particleWithContext.setNeighbourhoodBest(particle.getNeighbourhoodBest());
                        ((CentroidHolder) particleWithContext.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex));
                        particleWithContext.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER).getClone());
                        particleWithContext.setCentroidInitialisationStrategy(particle.getCentroidInitializationStrategyCandidate().getClone());

                        assignDataPatternsToParticle((CentroidHolder) particleWithContext.getCandidateSolution(), table);
                        particleWithContext.calculateFitness();


                        if(particleWithContext.getFitness().getValue() < particleWithContext.getBestFitness().getValue()) {
                            particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition());
                            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());

                            particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition());
                            particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
                        }

                        if(particleWithContext.getBestFitness().getValue() < contextParticle.getFitness().getValue()) {
                               ((CentroidHolder) contextParticle.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex));
                        }

                        particle = particleWithContext.getClone();

                    }

                    populationIndex++;
                }
        }
        
        MultiSwarm multiswarm = convertCooperativePSOToMultiswarm(algorithm);
        delegate.performIteration(multiswarm);
        convertMultiswarmToCooperative(multiswarm, algorithm);
        
        if(((SinglePopulationDataClusteringIterationStrategy) ((DataClusteringPSO) algorithm.getPopulations().get(0)).getIterationStrategy()).getWindow().hasSlid()) {
            System.out.println("\n" + algorithm.getBestSolution().getPosition().toString());
        }
        
        if(algorithm.getIterations() == ((MeasuredStoppingCondition) algorithm.getStoppingConditions().get(0)).getTarget() - 1)
            System.out.println("\n" + algorithm.getBestSolution().getPosition().toString());
        
    }
   
    
    private MultiSwarm convertCooperativePSOToMultiswarm(MultiPopulationBasedAlgorithm algorithm) {
        MultiSwarm multiSwarm = new MultiSwarm();
        multiSwarm.setPopulations(algorithm.getPopulations());
        multiSwarm.setOptimisationProblem(algorithm.getOptimisationProblem());
        
        return multiSwarm;
    }
    
    private void convertMultiswarmToCooperative(MultiSwarm multiswarm, MultiPopulationBasedAlgorithm algorithm) {
        algorithm.setPopulations(multiswarm.getPopulations());
    }
    
    public void setDelegate(IterationStrategy newDelegate) {
        delegate = newDelegate;
    }
    
    public IterationStrategy getDelegate() {
        return delegate;
    }
    
    
}
