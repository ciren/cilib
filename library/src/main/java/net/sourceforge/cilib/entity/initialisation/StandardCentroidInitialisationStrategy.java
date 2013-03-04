/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *This class initialises a ClusterParticle to contain a CentroidHolder as the candidate solution, velocity and best position
 */
public class StandardCentroidInitialisationStrategy <E extends Entity> extends DataDependantInitialisationStrategy<E>{

    /*
     * Default constructor for the StandardCentroidInitialisationStrategy
     */
    public StandardCentroidInitialisationStrategy() {
        super();
    }

    /*
     * Copy constructor for the StandardCentroidInitialisationStrategy
     * @param copy The StandardCentroidInitialisationStrategy to be copied
     */
    public StandardCentroidInitialisationStrategy(StandardCentroidInitialisationStrategy copy) {
        super(copy);
    }

    /*
     * The clone method for the StandardCentroidInitialisationStrategy
     */
    @Override
    public StandardCentroidInitialisationStrategy getClone() {
        return new StandardCentroidInitialisationStrategy(this);
    }

    /*
     * Initialises the entity provided accordingly
     * If the RandomBoundedInitialisationStrategy is chosen as a delegate, the bounds are set to be those of the dataset
     * @param key The key stating which property of the entity must be initialised
     * @param entity The entity to be initialised
     */
    @Override
    public void initialise(Enum<?> key, E entity) {
        CentroidHolder centroidHolder = (CentroidHolder) entity.getProperties().get(key);
        Entity particle;
        int index;
        Real r;

        if(initialisationStrategy instanceof RandomBoundedInitialisationStrategy) {
            //setBounds();

            ((RandomBoundedInitialisationStrategy) initialisationStrategy).setBoundsPerDimension(bounds);
            for(ClusterCentroid centroid : centroidHolder) {
                index = 0;
                for(Numeric n : centroid) {
                    r = Real.valueOf(n.doubleValue(), new Bounds(bounds.get(index)[0].getParameter(), bounds.get(index)[1].getParameter()));
                    n = r.getClone();
                    index++;
                }
            }
        }

        for(ClusterCentroid centroid : centroidHolder) {
            particle = new StandardParticle();

            particle.setCandidateSolution(centroid.toVector());

            initialisationStrategy.initialise(key, (E) particle);

            centroid.copy((Vector) particle.getCandidateSolution());
        }

        entity.getProperties().put(key, (Type) centroidHolder);

    }

    /*
     * Reinitialises the entity using the same settings as the ones that originally initialised it
     * @param key The key stating which property of the entity must be reinitialised
     * @param entity The entity to be reinitialised
     */
    public void reinitialise(Enum<?> key, E entity) {
        CentroidHolder centroidHolder = (CentroidHolder) entity.getProperties().get(key);
        Entity particle;

        for(ClusterCentroid centroid : centroidHolder) {
            particle = new StandardParticle();
            particle.setCandidateSolution(centroid.toVector());

            initialisationStrategy.initialise(key, (E) particle);

            centroid.copy((Vector) particle.getCandidateSolution());
        }

        entity.getProperties().put(key, (Type) centroidHolder);
    }

}
