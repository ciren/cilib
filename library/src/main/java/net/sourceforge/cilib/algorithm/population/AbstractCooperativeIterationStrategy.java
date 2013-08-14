/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * This class holds the functionality that is common to a number of cooperative iteration strategies
 */
public abstract class AbstractCooperativeIterationStrategy<E extends Algorithm> extends AbstractIterationStrategy<E> {
    protected ClusterParticle contextParticle;
    protected boolean contextinitialised;
    protected boolean elitist;
    /*
     * Default constructor for AbstractCooperativeIterationStrategy
     */
    public AbstractCooperativeIterationStrategy() {
        contextParticle = new ClusterParticle();
        contextinitialised = false;
        elitist = false;
    }

    /*
     * Copy constructor for AbstractCooperativeIterationStrategy
     * @param copy The AbstractCooperativeIterationStrategy to be copied
     */
    public AbstractCooperativeIterationStrategy(AbstractCooperativeIterationStrategy copy) {
        contextParticle = copy.contextParticle;
        contextinitialised = copy.contextinitialised;
        elitist = copy.elitist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractIterationStrategy<E> getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void performIteration(E algorithm);

    /*
     * Returns the context particle
     * @return contextParticle The context particle
     */
    public ClusterParticle getContextParticle() {
        ((CentroidHolder) contextParticle.getCandidateSolution()).clearAllCentroidDataItems();
        contextParticle.calculateFitness();
        return contextParticle;
    }

    /*
     * Initialises the context particle for the first time
     * @param algorithm The algorithm whose context particle needs to be initialised
     */
    public void initialiseContextParticle(MultiPopulationBasedAlgorithm algorithm) {
        int populationIndex = 0;
        CentroidHolder solution = new CentroidHolder();
        CentroidHolder velocity = new CentroidHolder();
        CentroidHolder bestPosition = new CentroidHolder();

        for(SinglePopulationBasedAlgorithm alg : algorithm.getPopulations()) {
            if(!((DataClusteringPSO) alg).isExplorer()) {
                solution.add(((CentroidHolder) alg.getBestSolution().getPosition()).get(populationIndex).getClone());
                velocity.add(((CentroidHolder) alg.getBestSolution().getPosition()).get(populationIndex).getClone());
                bestPosition.add(((CentroidHolder) alg.getBestSolution().getPosition()).get(populationIndex).getClone());

                populationIndex++;
            }
        }

        contextParticle.setCandidateSolution(solution);
        contextParticle.getProperties().put(EntityType.Particle.VELOCITY, velocity);
        contextParticle.getProperties().put(EntityType.Particle.BEST_POSITION, bestPosition);
        contextParticle.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        contextParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        contextinitialised = true;
    }

    public boolean getIsElitist() {
        return elitist;
    }

    public void setIsElitist(Boolean elitist) {
        this.elitist = elitist;
    }

}
