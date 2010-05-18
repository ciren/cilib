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

import java.util.Iterator;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Determine the radius of the current {@linkplain net.sourceforge.cilib.entity.Topology topology}.
 */
public class RadiusVisitor extends TopologyVisitor {

    private double radius = -Double.MAX_VALUE;
    private boolean done;

    /**
     * {@inheritDoc}
     *
     * @param topology
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        double maxDistance = 0.0;

        Vector swarmBestParticlePosition = (Vector) this.currentAlgorithm.getBestSolution().getPosition();
        Iterator<?> swarmIterator = topology.iterator();

        while(swarmIterator.hasNext()) {
            Entity swarmParticle = (Entity) swarmIterator.next();
            Vector swarmParticlePosition = (Vector) swarmParticle.getCandidateSolution();

            double actualDistance = distanceMeasure.distance(swarmBestParticlePosition, swarmParticlePosition);

            if (actualDistance > maxDistance)
                maxDistance = actualDistance;
        }

        radius = maxDistance;
        done = true;
    }

    /**
     * Obtain the value of the {@code radius} for the visited {@code Topology}.
     * @return The value of the radius.
     */
    @Override
    public Double getResult() {
        return this.radius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return done;
    }

}
