/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * Vistor to calculate the closest entity to the provided {@code targetEntity}
 * using a {@link net.sourceforge.cilib.util.DistanceMeasure}.
 *
 * @author gpampara
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
     * @return 
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * Sets the distance measure to use.
     * @param distanceMeasure 
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }
}
