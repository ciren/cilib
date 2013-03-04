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
    protected DataTable table;
    protected boolean elitist;
    /*
     * Default constructor for AbstractCooperativeIterationStrategy
     */
    public AbstractCooperativeIterationStrategy() {
        contextParticle = new ClusterParticle();
        contextinitialised = false;
        table = new StandardDataTable();
        elitist = false;
    }

    /*
     * Copy constructor for AbstractCooperativeIterationStrategy
     * @param copy The AbstractCooperativeIterationStrategy to be copied
     */
    public AbstractCooperativeIterationStrategy(AbstractCooperativeIterationStrategy copy) {
        contextParticle = copy.contextParticle;
        contextinitialised = copy.contextinitialised;
        table = copy.table;
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
     * Adds the data patterns closest to a centroid to its data pattern list
     * @param candidateSolution The solution holding all the centroids
     * @param dataset The dataset holding all the data patterns
     */
    public void assignDataPatternsToParticle(CentroidHolder candidateSolution, DataTable dataset) {
        double euclideanDistance;
        Vector addedPattern;
        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        int centroidIndex;
        int patternIndex;

        for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                Vector pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                centroidIndex = 0;
                patternIndex = 0;
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
    }

    /*
     * Returns the context particle
     * @return contextParticle The context particle
     */
    public ClusterParticle getContextParticle() {
        clearDataPatterns(contextParticle);
        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
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

        for(PopulationBasedAlgorithm alg : algorithm.getPopulations()) {
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


    /*
     * Removes all data patterns held by cluster centroids held by the particle received
     * @param particle The particle whose centroids must be cleared
     */
    public void clearDataPatterns(ClusterParticle particle) {
        for(ClusterCentroid centroid : (CentroidHolder) particle.getCandidateSolution()) {
            centroid.clearDataItems();
        }
    }

    public boolean getIsElitist() {
        return elitist;
    }

    public void setIsElitist(Boolean elitist) {
        this.elitist = elitist;
    }

}
