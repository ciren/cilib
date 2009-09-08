/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * Vistor to calculate the closest entity to the provided {@code targetEntity}
 * using a {@link net.sourceforge.cilib.util.DistanceMeasure}.
 *
 * @author gpampara
 */
public class ClosestEntityVisitor extends TopologyVisitor {
    private Entity closestEntity;
    private Entity targetEntity;
    private double closest = Double.MAX_VALUE;
    private boolean done;

    /**
     * Perform the search for the closest entity.
     * @param topology The topology to search.
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        closestEntity = null;

        for (Entity entity : topology) {
            if (targetEntity == entity)
                continue;

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

}
