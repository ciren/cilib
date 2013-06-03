/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import fj.data.List;

/**
 * Vistor to calculate the closest entity to the provided {@code targetEntity}
 * using a {@link DistanceMeasure}.
 */
public class ClosestEntityVisitor<E extends Entity> extends TopologyVisitor<E, E> {

    private E targetEntity;
    protected DistanceMeasure distanceMeasure;

    /**
     * Deault constructor.
     */
    public ClosestEntityVisitor() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Get the target entity for which this visitor bases it's calculations on.
     * @return The entity on which the visitor operates.
     */
    public Entity getTargetEntity() {
        return targetEntity;
    }

    /**
     * Set the entity for which the calculations are based on.
     * @param targetEntity The entity to set.
     */
    public void setTargetEntity(E targetEntity) {
        this.targetEntity = targetEntity;
    }

    /**
     * Gets the distance measure used.
     * @return the distance measure.
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * Sets the distance measure to use.
     * @param distanceMeasure the distance measure to use.
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    @Override
    public E f(List<E> topology) {
        E closestEntity = null;
        double closest = Double.MAX_VALUE;

        for (E entity : topology) {
            if (targetEntity == entity) {
                continue;
            }

            double distance = distanceMeasure.distance(targetEntity.getPosition(), entity.getPosition());
            if (distance < closest) {
                closestEntity = entity;
                closest = distance;
            }
        }

        return closestEntity;
    }
}
