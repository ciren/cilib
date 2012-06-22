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

import net.sourceforge.cilib.algorithm.population.AbstractCooperativeIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.CooperativePSO;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 *
 * @author Kristina
 */
public class CooperativeDataClusteringPSOIterationStrategy extends AbstractCooperativeIterationStrategy<CooperativePSO>{
    
    public CooperativeDataClusteringPSOIterationStrategy() {
        super();
        contextParticle = new ClusterParticle();
        contextinitialized = false;
        table = new StandardDataTable();
    }
    
    public CooperativeDataClusteringPSOIterationStrategy(CooperativeDataClusteringPSOIterationStrategy copy) {
        super(copy);
        contextParticle = copy.contextParticle;
        contextinitialized = copy.contextinitialized;
        table = copy.table;
    }
    
    @Override
    public CooperativeDataClusteringPSOIterationStrategy getClone() {
        return new CooperativeDataClusteringPSOIterationStrategy(this);
    }

    @Override
    public void performIteration(CooperativePSO algorithm) {
        int populationIndex = 0;
        table = new StandardDataTable();
        
        for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
              
            table = ((SinglePopulationDataClusteringIterationStrategy) ((DataClusteringPSO) currentAlgorithm).getIterationStrategy()).getDataset();
            
            if(!contextinitialized)
                initializeContextParticle(algorithm);
            
            
            DataClusteringPSO pso = ((DataClusteringPSO) currentAlgorithm);
            Topology newTopology = ((DataClusteringPSO) currentAlgorithm).getTopology().getClone();
            newTopology.clear();
            
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
                
                newTopology.add(particleWithContext);
            }
            
            pso.setTopology(newTopology);
            pso.performIteration();
            
            populationIndex++;
        }
        
        
    }
    
    public void reinitializeContext(CooperativePSO currentAlgorithm) {  
        contextParticle = ((DataClusteringPSO) currentAlgorithm.getPopulations().get(0)).getTopology().get(0).getClone();
        contextParticle.reinitialise();
        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
        contextParticle.calculateFitness();
        System.out.println("Reinitialized");
    }
    
   
    

}
