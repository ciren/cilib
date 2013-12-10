/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.Property;
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
        for(ClusterCentroid centroid : (CentroidHolder) particle.getPosition()) {
            tmpParticle = new StandardParticle();
            neighbourhoodBestParticle = new StandardParticle();
            tmpParticle.setPosition(centroid.toVector());
            tmpParticle.put(Property.VELOCITY, particle.getVelocity().get(index).toVector());
            tmpParticle.put(Property.BEST_POSITION, particle.getBestPosition().get(index).toVector());
            tmpParticle.put(Property.BEST_FITNESS, particle.getBestFitness());
            tmpParticle.put(Property.FITNESS, particle.getFitness());

            neighbourhoodBestParticle.setPosition(((CentroidHolder) particle.getNeighbourhoodBest().getPosition()).get(index).toVector());
            neighbourhoodBestParticle.put(Property.VELOCITY, particle.getNeighbourhoodBest().getVelocity().get(index).toVector());
            neighbourhoodBestParticle.put(Property.BEST_POSITION, particle.getNeighbourhoodBest().getBestPosition().get(index).toVector());
            neighbourhoodBestParticle.put(Property.BEST_FITNESS, particle.getNeighbourhoodBest().getBestFitness());
            neighbourhoodBestParticle.put(Property.FITNESS, particle.getNeighbourhoodBest().getFitness());

            tmpParticle.setNeighbourhoodBest(neighbourhoodBestParticle);
            newCentroid = new ClusterCentroid();
            newCentroid.copy((Vector) delegate.get(tmpParticle));
            newCandidateSolution.add(newCentroid);
            index++;
        }
        
        return newCandidateSolution;
    }
}
