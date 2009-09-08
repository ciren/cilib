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

import java.util.Iterator;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Visitor to determine the size of the diameter of the provided {@linkplain Topology}
 * within the <code>visit</code> method.
 */
public class DiameterVisitor extends TopologyVisitor {
    private double distance = -Double.MAX_VALUE;
    private boolean done;

    /**
     * Obtain the {@code diameter} of the provided {@code Topology}.
     * @param topology The topology to inspect.
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        double maxDistance = 0.0;

        Iterator<? extends Entity> k1 = topology.iterator();
        while (k1.hasNext()) {
            Entity p1 = (Entity) k1.next();
            Vector position1 = (Vector) p1.getCandidateSolution();

            Iterator<? extends Entity> k2 = topology.iterator();
            while (k2.hasNext()) {
                Entity p2 = (Entity) k2.next();
                Vector position2 = (Vector) p2.getCandidateSolution();

                double actualDistance = distanceMeasure.distance(position1, position2);
                if (actualDistance > maxDistance)
                    maxDistance = actualDistance;
            }
        }

        distance = maxDistance;
        done = true;
    }

    /**
     * Obtain the diameter value for the {@link net.sourceforge.cilib.entity.Topology} of
     * {@code Entity} instances.
     * @return The diameter value.
     */
    @Override
    public Double getResult() {
        return this.distance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return done;
    }

}
