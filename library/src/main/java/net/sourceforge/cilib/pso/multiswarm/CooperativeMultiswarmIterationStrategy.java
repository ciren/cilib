/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 *
 */
public class CooperativeMultiswarmIterationStrategy extends AbstractCooperativeIterationStrategy<MultiPopulationBasedAlgorithm>{
    IterationStrategy<MultiPopulationBasedAlgorithm> delegate;

    /*
     * Default constructor for CooperativeMultiswarmIterationStrategy
     */
    public CooperativeMultiswarmIterationStrategy() {
        super();
        delegate = new StandardClusteringMultiSwarmIterationStrategy();
    }

    /*
     * Copy constructor for CooperativeMultiswarmIterationStrategy
     * @param copy The CooperativeMultiswarmIterationStrategy to be copied
     */
    public CooperativeMultiswarmIterationStrategy(CooperativeMultiswarmIterationStrategy copy) {
        super(copy);
        delegate = copy.delegate;
    }

    /*
     * Clone method for CooperativeMultiswarmIterationStrategy
     * @return new instance of the CooperativeMultiswarmIterationStrategy
     */
    @Override
    public CooperativeMultiswarmIterationStrategy getClone() {
        return new CooperativeMultiswarmIterationStrategy(this);
    }

    /*
     * Performs an iteration of the Cooperative Multiswarm Iteration Strategy
     * It handles the context particle.
     * It assigns the context to all particles with the appropriate dimension difference.
     * It updates personal and global best values.
     * It uses a multi-swarm iteration strategy on the different swarms.
     * @param algorithm The multi-population algorithm whose swarms must be treated as
     * co-operative swarms.
     */
    @Override
    public void performIteration(MultiPopulationBasedAlgorithm algorithm) {
        int populationIndex = 0;

        for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
                table = ((SinglePopulationDataClusteringIterationStrategy) ((DataClusteringPSO) currentAlgorithm).getIterationStrategy()).getDataset();

                if(!contextinitialised) {
                    ((DataClusteringPSO) currentAlgorithm).setIsExplorer(true);
                    initialiseContextParticle(algorithm);
                    contextinitialised = true;
                }

                if(!((DataClusteringPSO) currentAlgorithm).isExplorer()) {
                    for(ClusterParticle particle : ((DataClusteringPSO) currentAlgorithm).getTopology()) {
                        clearDataPatterns(contextParticle);
                        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
                        contextParticle.calculateFitness();

                        ClusterParticle particleWithContext = new ClusterParticle();
                        particleWithContext.setCandidateSolution(contextParticle.getCandidateSolution().getClone());
                        particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getBestPosition().getClone());
                        particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getBestFitness().getClone());
                        particleWithContext.getProperties().put(EntityType.Particle.VELOCITY, particle.getVelocity().getClone());
                        particleWithContext.setNeighbourhoodBest(contextParticle);
                        ((CentroidHolder) particleWithContext.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex).getClone());
                        particleWithContext.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER).getClone());
                        particleWithContext.setCentroidInitialisationStrategy(particle.getCentroidInitialisationStrategyCandidate().getClone());

                        clearDataPatterns(particleWithContext);
                        assignDataPatternsToParticle((CentroidHolder) particleWithContext.getCandidateSolution(), table);
                        particleWithContext.calculateFitness();

                        if(particleWithContext.getFitness().compareTo(particleWithContext.getBestFitness()) > 0) {
                            particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particleWithContext.getPosition().getClone());
                            particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particleWithContext.getFitness().getClone());
                        }

                        if(particleWithContext.getBestFitness().compareTo(contextParticle.getFitness()) > 0) {
                               ((CentroidHolder) contextParticle.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particleWithContext.getCandidateSolution()).get(populationIndex).getClone());
                        }

                        if(contextParticle.getFitness().compareTo(contextParticle.getBestFitness()) > 0) {
                            contextParticle.getProperties().put(EntityType.Particle.BEST_POSITION, contextParticle.getPosition().getClone());
                            contextParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, contextParticle.getFitness().getClone());
                        }

                        particle = particleWithContext.getClone();

                    }

                    populationIndex++;
                }

        }

        if(elitist) {
            contextParticle.getProperties().put(EntityType.CANDIDATE_SOLUTION, contextParticle.getBestPosition().getClone());
            contextParticle.getProperties().put(EntityType.FITNESS, contextParticle.getBestFitness().getClone());
        }

        MultiSwarm multiswarm = convertCooperativePSOToMultiswarm(algorithm);
        delegate.performIteration(multiswarm);
        convertMultiswarmToCooperative(multiswarm, algorithm);

    }

    /*
     * Assigns the current algorithm's topology to a multi-swarm in order to use the
     * multi-swarm iteration strategy needed.
     * @param algorithm The algorithm to be converted
     * @return multiswarm The new multi-swarm algorithm holding the topology of algorithm
     */
    private MultiSwarm convertCooperativePSOToMultiswarm(MultiPopulationBasedAlgorithm algorithm) {
        MultiSwarm multiSwarm = new MultiSwarm();
        multiSwarm.setPopulations(algorithm.getPopulations());
        multiSwarm.setOptimisationProblem(algorithm.getOptimisationProblem());

        return multiSwarm;
    }

    /*
     * Sets the topology of the multi-swarm received to be held by the current algorithm
     * @param multiswarm The multi-swarm with the new topology
     * @param algorithm The current algorithm to which the multi-swarm's topology will be assigned.
     */
    private void convertMultiswarmToCooperative(MultiSwarm multiswarm, MultiPopulationBasedAlgorithm algorithm) {
        algorithm.setPopulations(multiswarm.getPopulations());
    }

    /*
     * Sets the delegate iteration strategy to the one received as a parameter
     * @param newDelegate The new delegate iteration strategy
     */
    public void setDelegate(IterationStrategy newDelegate) {
        delegate = newDelegate;
    }

    /*
     * Returns the delegate iteration strategy
     * @return delegate The delegate iteration strategy
     */
    public IterationStrategy getDelegate() {
        return delegate;
    }


}
