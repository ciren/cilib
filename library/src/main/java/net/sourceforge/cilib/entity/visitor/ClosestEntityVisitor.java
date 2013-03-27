/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Vistor to calculate the closest entity to the provided {@code targetEntity}
 * using a {@link DistanceMeasure}.
 */
public class ClosestEntityVisitor implements TopologyVisitor {

    private Entity closestEntity;
    private Entity targetEntity;
    private double closest;
    private boolean done;
    protected DistanceMeasure distanceMeasure;

    /**
     * Deault constructor.
     */
    public ClosestEntityVisitor() {
        this.closest = Double.MAX_VALUE;
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Perform the search for the closest entity.
     * @param topology The topology to search.
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        closestEntity = null;

        for (Entity entity : topology) {
            if (targetEntity == entity) {
                continue;
            }

            double distance = distanceMeasure.distance(targetEntity.getCandidateSolution(), entity.getCandidateSolution());
            if (distance < closest) {
                this.closestEntity = entity;
                this.closest = distance;
            }
        }

        done = true;
    }

    /**
     * Get the result of the visitor. IE: Get the located entity that
     * is the closest entity spatially to the provided {@code targetEntity}.
     * @return The entity that is the closest to the {@code targetEntity}.
     */
    @Override
    public Entity getResult() {
        return this.closestEntity;
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
    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return done;
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
}
