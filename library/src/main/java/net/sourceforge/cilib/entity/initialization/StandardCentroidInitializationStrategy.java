/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialization;

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
 *This class initializes a ClusterParticle to contain a CentroidHolder as the candidate solution, velocity and best position
 */
public class StandardCentroidInitializationStrategy <E extends Entity> extends DataDependantInitializationStrategy<E>{

    /*
     * Default constructor for the StandardCentroidInitializationStrategy
     */
    public StandardCentroidInitializationStrategy() {
        super();
    }
    
    /*
     * Copy constructor for the StandardCentroidInitializationStrategy
     * @param copy The StandardCentroidInitializationStrategy to be copied
     */
    public StandardCentroidInitializationStrategy(StandardCentroidInitializationStrategy copy) {
        super(copy);
    }
    
    /*
     * The clone method for the StandardCentroidInitializationStrategy
     */
    @Override
    public StandardCentroidInitializationStrategy getClone() {
        return new StandardCentroidInitializationStrategy(this);
    }

    /*
     * Initializes the entity provided accordingly
     * If the RandomBoundedInitializationStrategy is chosen as a delegate, the bounds are set to be thsoe of the dataset
     * @param key The key stating which property of the entity must be initialized
     * @param entity The entity to be initialized
     */
    @Override
    public void initialize(Enum<?> key, E entity) {
        CentroidHolder centroidHolder = (CentroidHolder) entity.getProperties().get(key);
        Entity particle;
        int index;
        Real r;
        
        if(initialisationStrategy instanceof RandomBoundedInitializationStrategy) {
            //setBounds();
            
            ((RandomBoundedInitializationStrategy) initialisationStrategy).setBoundsPerDimension(bounds);
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
            
            initialisationStrategy.initialize(key, (E) particle);
            
            centroid.copy((Vector) particle.getCandidateSolution());
        }
        
        entity.getProperties().put(key, (Type) centroidHolder);
        
    }
    
    /*
     * Reinitializes the entity using the same settings as the ones that originally initialized it
     * @param key The key stating which property of the entity must be reinitialized
     * @param entity The entity to be reinitialized
     */
    public void reinitialize(Enum<?> key, E entity) {
        CentroidHolder centroidHolder = (CentroidHolder) entity.getProperties().get(key);
        Entity particle;
        
        for(ClusterCentroid centroid : centroidHolder) {
            particle = new StandardParticle();
            particle.setCandidateSolution(centroid.toVector());
            
            initialisationStrategy.initialize(key, (E) particle);
            
            centroid.copy((Vector) particle.getCandidateSolution());
        }
        
        entity.getProperties().put(key, (Type) centroidHolder);
    }
    
}
