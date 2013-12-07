/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.positionprovider.StandardPositionProvider;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the normal position update as described by Kennedy and Eberhart.
 */
public class StandardDataClusteringPositionProvider implements PositionProvider {

    private PositionProvider delegate;

    /**
     * Create an new instance of {@code StandardPositionProvider}.
     */
    public StandardDataClusteringPositionProvider() {
        this.delegate = new StandardPositionProvider();
    }

    /**
     * Copy constructor. Copy the provided instance.
     * @param copy The instance to copy.
     */
    public StandardDataClusteringPositionProvider(StandardDataClusteringPositionProvider copy) {
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardDataClusteringPositionProvider getClone() {
        return new StandardDataClusteringPositionProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CentroidHolder get(Particle sParticle) {
        ClusterParticle particle = (ClusterParticle) sParticle;

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
            newCentroid.copy((Vector) delegate.get(tmpParticle));
            newCandidateSolution.add(newCentroid);
            index++;
        }
        
        return newCandidateSolution;
    }
}
