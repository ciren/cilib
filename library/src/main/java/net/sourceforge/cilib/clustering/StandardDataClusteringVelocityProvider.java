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
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * An implementation of the Guaranteed Convergence PSO algorithm. The GCPSO is a simple extension
 * to the normal PSO algorithm and the modifications to the algorithm is implemented as
 * a simple {@link VelocityProvider}.
 * <p>
 * References:
 * <p><ul><li>
 * F. van den Bergh and A. Engelbrecht, "A new locally convergent particle swarm optimizer,"
 * in Proceedings of IEEE Conference on Systems, Man and Cybernetics,
 * (Hammamet, Tunisia), Oct. 2002.
 * </li><li>
 * F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 * PhD thesis, Department of Computer Science,
 * University of Pretoria, South Africa, 2002.
 * </li></ul>
 * <p>
 * TODO: The Rho value should be a vector to hold the rho value for each dimension!
 * <p>
 * It is very important to realise the importance of the <code>rho</code> values. <code>rho</code>
 * determines the local search size of the global best particle and depending on the domain
 * this could result in poor performance if the <code>rho</code> value is too small or too large depending
 * on the specified problem domain. For example, a <code>rho</code> value of 1.0 is not a good
 * value within problems which have a domain that spans <code>[0,1]</code>
 */
public class StandardDataClusteringVelocityProvider implements VelocityProvider {

    private VelocityProvider delegate;

    /**
     * Create an instance of the GC Velocity Update strategy.
     */
    public StandardDataClusteringVelocityProvider() {
        this.delegate = new StandardVelocityProvider();
    }

    /**
     * Copy constructor. Copy the given instance.
     * @param copy The instance to copy.
     */
    public StandardDataClusteringVelocityProvider(StandardDataClusteringVelocityProvider copy) {
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardDataClusteringVelocityProvider getClone() {
        return new StandardDataClusteringVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CentroidHolder get(Particle sParticle) {
        ClusterParticle particle = (ClusterParticle) sParticle;
        
        CentroidHolder newVelocity = new CentroidHolder();
        ClusterCentroid newCentroid;
        Particle tmpParticle;
        int index = 0;
        Particle neighbourhoodBestParticle;
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
            newVelocity.add(newCentroid);
            index++;
        }
        
        return newVelocity;
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }
}
