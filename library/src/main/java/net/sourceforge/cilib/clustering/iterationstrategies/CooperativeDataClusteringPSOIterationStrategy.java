/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 * This class performs an iteration of the cooperative data clustering iteration strategy.
 * It holds a context particle, adapts the swarms to hold the context particle
 * with the appropriate dimension difference,updates the personal and global 
 * bests and then updates the particles.
 */
public class CooperativeDataClusteringPSOIterationStrategy extends AbstractCooperativeIterationStrategy<CooperativePSO>{
    /*
     * Default constructor for CooperativeDataClusteringPSOIterationStrategy
     */
    public CooperativeDataClusteringPSOIterationStrategy() {
        super();
        contextParticle = new ClusterParticle();
        contextinitialized = false;
        table = new StandardDataTable();
    }
    
    /*
     * Copy cosntructor for CooperativeDataClusteringPSOIterationStrategy
     * @param copy The CooperativeDataClusteringPSOIterationStrategy to be copied
     */
    public CooperativeDataClusteringPSOIterationStrategy(CooperativeDataClusteringPSOIterationStrategy copy) {
        super(copy);
        contextParticle = copy.contextParticle;
        contextinitialized = copy.contextinitialized;
        table = copy.table;
    }
    
    /*
     * Clone method of the CooperativeDataClusteringPSOIterationStrategy
     * @return new instance of the CooperativeDataClusteringPSOIterationStrategy
     */
    @Override
    public CooperativeDataClusteringPSOIterationStrategy getClone() {
        return new CooperativeDataClusteringPSOIterationStrategy(this);
    }

    /*
     * Performs an iteration of the standard co-operative algorithm.
     * It holds a context particle, adapts the swarms to hold the context particle
     * with the appropriate dimension difference,updates the personal and global 
     * bests and then updates the particles.
     */
    @Override
    public void performIteration(CooperativePSO algorithm) {
        int populationIndex = 0;
        table = new StandardDataTable();
        DataClusteringPSO pso ;
        Topology newTopology;
        ClusterParticle particleWithContext;
        
        for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
              
            table = ((SinglePopulationDataClusteringIterationStrategy) ((DataClusteringPSO) currentAlgorithm).getIterationStrategy()).getDataset();
            
            if(!contextinitialized) {
                initializeContextParticle(algorithm);
            }
            
            pso = ((DataClusteringPSO) currentAlgorithm);
            newTopology = ((DataClusteringPSO) currentAlgorithm).getTopology().getClone();
            newTopology.clear();
            
            for(ClusterParticle particle : ((DataClusteringPSO) currentAlgorithm).getTopology()) {
                clearDataPatterns(contextParticle);
                assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
                contextParticle.calculateFitness();
                    
                particleWithContext = new ClusterParticle();
                particleWithContext.setCandidateSolution(contextParticle.getCandidateSolution().getClone());
                particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getBestPosition().getClone());
                particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getBestFitness().getClone());
                particleWithContext.getProperties().put(EntityType.Particle.VELOCITY, particle.getVelocity().getClone());
                particleWithContext.setNeighbourhoodBest(particle.getNeighbourhoodBest());
                ((CentroidHolder) particleWithContext.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex));
                particleWithContext.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER).getClone());
                particleWithContext.setCentroidInitialisationStrategy(particle.getCentroidInitializationStrategyCandidate().getClone());
                
                clearDataPatterns(particleWithContext);
                assignDataPatternsToParticle((CentroidHolder) particleWithContext.getCandidateSolution(), table);
                particleWithContext.calculateFitness();
                
                
                if(particleWithContext.getFitness().compareTo(particleWithContext.getBestFitness()) > 0) {
                    particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition());
                    particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
                    
                    particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition());
                    particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
                }
                
                if(particleWithContext.getBestFitness().compareTo(contextParticle.getFitness()) > 0) {
                       ((CentroidHolder) contextParticle.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex));
                }
                
                if(contextParticle.getFitness().compareTo(contextParticle.getBestFitness()) > 0) {
                    contextParticle.getProperties().put(EntityType.Particle.BEST_POSITION, contextParticle.getPosition()).getClone();
                    contextParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, contextParticle.getFitness()).getClone();
                }
                
                newTopology.add(particleWithContext);
            }
            
            if(elitist) {
                contextParticle.getProperties().put(EntityType.CANDIDATE_SOLUTION, contextParticle.getBestPosition().getClone());
                contextParticle.getProperties().put(EntityType.FITNESS, contextParticle.getBestFitness().getClone());
            }
            
            pso.setTopology(newTopology);
            pso.performIteration();
            
            populationIndex++;
        }
        
        
    }
    

}
