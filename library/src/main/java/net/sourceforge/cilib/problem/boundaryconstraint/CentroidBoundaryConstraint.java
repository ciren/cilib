/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is a wrapper class. It enforces the Boundary Constraint chosen by the user on each
 * ClusterCentroid held by a CentroidHolder
 */
public class CentroidBoundaryConstraint implements BoundaryConstraint{

    BoundaryConstraint delegate;

    /*
     * Default constructor for CentroidBoundaryConstraint
     */
    public CentroidBoundaryConstraint() {
        delegate = new UnconstrainedBoundary();
    }

    /*
     * Copy constructor for CentroidBoundaryConstraint
     */
    public CentroidBoundaryConstraint(CentroidBoundaryConstraint copy) {
        delegate = copy.delegate;
    }

    /*
     * Clone method for CentroidBoundaryConstraint
     * @return new instance of the CentroidBoundaryConstraint
     */
    @Override
    public BoundaryConstraint getClone() {
        return new CentroidBoundaryConstraint(this);
    }

    /*
     * Enforces the delegate's boundary constraint on each ClusterCentroid held by the CentoifHolder of the entity
     * @param entity The entity to be bound constrained
     */
    @Override
    public void enforce(Entity entity) {
        //System.out.println("Class: " + entity.getPosition().getClass().toString() + ", " + entity.getPosition());
        CentroidHolder holder = (CentroidHolder) entity.getPosition().getClone();
        CentroidHolder velocity = (CentroidHolder) entity.get(Property.VELOCITY).getClone();
        CentroidHolder bestPosition = (CentroidHolder) entity.get(Property.BEST_POSITION).getClone();
        CentroidHolder newSolution = new CentroidHolder();
        StandardParticle newParticle;
        ClusterCentroid centr;

        int index = 0;
        for(ClusterCentroid centroid : holder) {
            newParticle = new StandardParticle();
            newParticle.setPosition(centroid.toVector());
            newParticle.put(Property.VELOCITY, velocity.get(index).toVector());
            newParticle.put(Property.BEST_POSITION, bestPosition.get(index).toVector());

            delegate.enforce(newParticle);
            centr = new ClusterCentroid();
            centr.copy((Vector) newParticle.getPosition());
            newSolution.add(centr);
            index++;
        }
        entity.setPosition(newSolution);
    }

    /*
     * Sets the delegate BoundaryConstraint
     * @param constraint The BoundaryConstraint to be enforced
     */
    public void setDelegate(BoundaryConstraint constraint) {
        delegate = constraint;
    }

}
