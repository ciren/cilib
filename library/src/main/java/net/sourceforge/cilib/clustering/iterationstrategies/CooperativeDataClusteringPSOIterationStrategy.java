/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.AbstractCooperativeIterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.CooperativePSO;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 * This class performs an iteration of the cooperative data clustering iteration strategy.
 * It holds a context particle, adapts the swarms to hold the context particle
 * with the appropriate dimension difference, updates the personal and global
 * bests and then updates the particles.
 */
public class CooperativeDataClusteringPSOIterationStrategy extends AbstractCooperativeIterationStrategy<CooperativePSO>{
    /*
     * Default constructor for CooperativeDataClusteringPSOIterationStrategy
     */
    public CooperativeDataClusteringPSOIterationStrategy() {
        super();
        contextParticle = new ClusterParticle();
        contextinitialised = false;
    }

    /*
     * Copy constructor for CooperativeDataClusteringPSOIterationStrategy
     * @param copy The CooperativeDataClusteringPSOIterationStrategy to be copied
     */
    public CooperativeDataClusteringPSOIterationStrategy(CooperativeDataClusteringPSOIterationStrategy copy) {
        super(copy);
        contextParticle = copy.contextParticle;
        contextinitialised = copy.contextinitialised;
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
     * with the appropriate dimension difference, updates the personal and global
     * bests and then updates the particles.
     */
    @Override
    public void performIteration(CooperativePSO algorithm) {
        int populationIndex = 0;
        DataClusteringPSO pso ;
        fj.data.List<ClusterParticle> newTopology;
        ClusterParticle particleWithContext;

        for(SinglePopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {

            if(!contextinitialised) {
                initialiseContextParticle(algorithm);
            }

            pso = ((DataClusteringPSO) currentAlgorithm);
            newTopology = fj.data.List.nil();

            for(ClusterParticle particle : ((DataClusteringPSO) currentAlgorithm).getTopology()) {
                ((CentroidHolder) contextParticle.getCandidateSolution()).clearAllCentroidDataItems();
                contextParticle.calculateFitness();

                particleWithContext = new ClusterParticle();
                particleWithContext.setCandidateSolution(contextParticle.getCandidateSolution().getClone());
                particleWithContext.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
                particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getBestPosition().getClone());
                particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getBestFitness().getClone());
                particleWithContext.getProperties().put(EntityType.Particle.VELOCITY, particle.getVelocity().getClone());
                particleWithContext.setNeighbourhoodBest(particle.getNeighbourhoodBest());
                ((CentroidHolder) particleWithContext.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex));
                particleWithContext.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER).getClone());
                particleWithContext.setCentroidInitialisationStrategy(particle.getCentroidInitialisationStrategyCandidate().getClone());

                ((CentroidHolder) particleWithContext.getCandidateSolution()).clearAllCentroidDataItems();
                particleWithContext.calculateFitness();


                if(particleWithContext.getFitness().compareTo(particleWithContext.getBestFitness()) > 0) {
                    particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getCandidateSolution());
                    particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());

                    particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getCandidateSolution());
                    particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
                }

                if(particleWithContext.getBestFitness().compareTo(contextParticle.getFitness()) > 0) {
                       ((CentroidHolder) contextParticle.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex));
                }

                if(contextParticle.getFitness().compareTo(contextParticle.getBestFitness()) > 0) {
                    contextParticle.getProperties().put(EntityType.Particle.BEST_POSITION, contextParticle.getCandidateSolution()).getClone();
                    contextParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, contextParticle.getFitness()).getClone();
                }

                newTopology = fj.data.List.cons(particleWithContext, newTopology);
            }

            if(elitist) {
                contextParticle.getProperties().put(EntityType.CANDIDATE_SOLUTION, contextParticle.getBestPosition().getClone());
                contextParticle.getProperties().put(EntityType.FITNESS, contextParticle.getBestFitness().getClone());
            }

            pso.setTopology(newTopology.reverse());
            pso.performIteration();

            populationIndex++;
        }


    }


}
