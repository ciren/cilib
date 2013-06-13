/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.iterationstrategies;

import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class handles an iteration of the data clustering algorithm described in :
 * <pre>
 * {@literal @}article{vanDerMerwe03,
 *  title={{Data Clustering using Particle Swarm Optimization }},
 *  author={van der Merwe, D.W.; Engelhrecht, A.P.},
 *  year={2003},
 *  journal={Congress on Evolutionary Computation},
 *  volume={1},
 *  pages={215-220}
 * }
 * </pre>
 *
 *
 */
public class StandardDataClusteringIterationStrategy extends SinglePopulationDataClusteringIterationStrategy {
    /*
     * Default constructor for StandardDataClusteringIterationStrategy
     */
    public StandardDataClusteringIterationStrategy() {
        super();
    }

    /*
     * Copy constructor for StandardDataClusteringIterationStrategy
     */
    public StandardDataClusteringIterationStrategy(StandardDataClusteringIterationStrategy copy) {
        super(copy);
    }

    /*
     * Clone method of StandardDataClusteringIterationStrategy
     */
    @Override
    public StandardDataClusteringIterationStrategy getClone() {
        return new StandardDataClusteringIterationStrategy(this);
    }

    /*
     * Performs an iteration of the standard data clustering algorithm.
     * Assigns data patterns to centroids, updates personal and neighbourhood
     * bests and updates the particles.
     * @param algorithm The algorithm that called this iteration strategy
     */
    @Override
    public void performIteration(DataClusteringPSO algorithm) {
        fj.data.List<ClusterParticle> topology = algorithm.getTopology();
        double euclideanDistance;
        Vector addedPattern;
        clearCentroidDistanceValues(topology);
        reinitialised = false;
        Vector pattern;

        for(ClusterParticle particle : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) particle.getCandidateSolution();
            for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
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

            particle.setCandidateSolution(candidateSolution);

            particle.calculateFitness();

            //update velocity
            {
                CentroidHolder newVelocity = new CentroidHolder();
                ClusterCentroid newCentroid;
                Particle tmpParticle;
                int index = 0;
                Particle neighbourhoodBestParticle;
                for(ClusterCentroid centroid : (CentroidHolder) particle.getCandidateSolution()) {
                    tmpParticle = new StandardParticle();
                    neighbourhoodBestParticle = new StandardParticle();
                    tmpParticle.setCandidateSolution(centroid.toVector());
                    tmpParticle.getProperties().put(EntityType.Particle.VELOCITY, particle.getVelocity().get(index).toVector());
                    tmpParticle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getBestPosition().get(index).toVector());
                    tmpParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getBestFitness());
                    tmpParticle.getProperties().put(EntityType.FITNESS, particle.getFitness());

                    neighbourhoodBestParticle.setCandidateSolution(((CentroidHolder) particle.getNeighbourhoodBest().getCandidateSolution()).get(index).toVector());
                    neighbourhoodBestParticle.getProperties().put(EntityType.Particle.VELOCITY, particle.getNeighbourhoodBest().getVelocity().get(index).toVector());
                    neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getNeighbourhoodBest().getBestPosition().get(index).toVector());
                    neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getNeighbourhoodBest().getBestFitness());
                    neighbourhoodBestParticle.getProperties().put(EntityType.FITNESS, particle.getNeighbourhoodBest().getFitness());

                    tmpParticle.setNeighbourhoodBest(neighbourhoodBestParticle);
                    newCentroid = new ClusterCentroid();
                    newCentroid.copy(particle.getParticleBehavior().getVelocityProvider().get(tmpParticle));
                    newVelocity.add(newCentroid);
                    index++;
                }
                
                particle.updateVelocity(newVelocity);
            }

            //update position
            {
                CentroidHolder newCandidateSolution = new CentroidHolder();
                ClusterCentroid newCentroid;
                Particle tmpParticle;
                Particle neighbourhoodBestParticle;
                int index = 0;
                for(ClusterCentroid centroid : (CentroidHolder) particle.getCandidateSolution()) {
                    tmpParticle = new StandardParticle();
                    neighbourhoodBestParticle = new StandardParticle();
                    tmpParticle.setCandidateSolution(centroid.toVector());
                    tmpParticle.getProperties().put(EntityType.Particle.VELOCITY, particle.getVelocity().get(index).toVector());
                    tmpParticle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getBestPosition().get(index).toVector());
                    tmpParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getBestFitness());
                    tmpParticle.getProperties().put(EntityType.FITNESS, particle.getFitness());

                    neighbourhoodBestParticle.setCandidateSolution(((CentroidHolder) particle.getNeighbourhoodBest().getCandidateSolution()).get(index).toVector());
                    neighbourhoodBestParticle.getProperties().put(EntityType.Particle.VELOCITY, particle.getNeighbourhoodBest().getVelocity().get(index).toVector());
                    neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getNeighbourhoodBest().getBestPosition().get(index).toVector());
                    neighbourhoodBestParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getNeighbourhoodBest().getBestFitness());
                    neighbourhoodBestParticle.getProperties().put(EntityType.FITNESS, particle.getNeighbourhoodBest().getFitness());

                    tmpParticle.setNeighbourhoodBest(neighbourhoodBestParticle);
                    newCentroid = new ClusterCentroid();
                    newCentroid.copy(particle.getParticleBehavior().getPositionProvider().get(tmpParticle));
                    newCandidateSolution.add(newCentroid);
                    index++;
                }
                
                particle.updatePosition(newCandidateSolution);
            }

            boundaryConstraint.enforce(particle);
        }

        for (ClusterParticle current : topology) {
            for (ClusterParticle other : algorithm.getNeighbourhood().f(topology, current)) {
                if (current.getSocialFitness().compareTo(other.getNeighbourhoodBest().getSocialFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
        }

        dataset = window.slideWindow();

    }

    /*
     * Removes all data items assigned to each centroid in each particle in the topology
     * @param topology The topology whose centroids need to be cleaned
     */
    private void clearCentroidDistanceValues(fj.data.List<ClusterParticle> topology) {
        CentroidHolder candidateSolution;
        for(ClusterParticle particle : topology) {
            candidateSolution = (CentroidHolder) particle.getCandidateSolution();

            for(ClusterCentroid centroid : candidateSolution) {
                centroid.clearDataItems();
            }
        }
    }


}
