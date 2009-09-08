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

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Determine the spatial radius of the visited object.
 */
public class SpatialRadiusVisitor extends TopologyVisitor {

    private double radius = -Double.MAX_VALUE;
    private boolean done;

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        // set radius value to be returned to zero
        double maxDistance = 0.0;

        // get number of entities in the population
        int numberOfEntities = ((PopulationBasedAlgorithm) this.currentAlgorithm).getTopology().size();

        // initialize iterator to be used to calculate spatial center
        Iterator<? extends Entity> calculateCenterIterator = ((PopulationBasedAlgorithm) this.currentAlgorithm).getTopology().iterator();
        Entity entity = calculateCenterIterator.next();
        Vector spatialCenter = (Vector) entity.getCandidateSolution().getClone();

        // calculate center - evaluate sum total of population entity contents
        while (calculateCenterIterator.hasNext()) {
            entity = calculateCenterIterator.next();
            Vector entityContents = (Vector) entity.getCandidateSolution();
            for (int j = 0; j < spatialCenter.getDimension(); ++j)
               spatialCenter.setReal(j, spatialCenter.getReal(j)+entityContents.getReal(j));
        }

        // calculate center - evaluate average position of entity contents (spatial center)
        for (int j = 0; j < spatialCenter.getDimension(); ++j)
           spatialCenter.setReal(j, spatialCenter.getReal(j)/numberOfEntities);

        // initialize iterator to be used to calculate radius
        Iterator<?> calculateRadiusIterator = topology.iterator();

        // calculate radius
        while(calculateRadiusIterator.hasNext()) {
            Entity populationEntity = (Entity) calculateRadiusIterator.next();
            Vector entityContents = (Vector) populationEntity.getCandidateSolution();

            double currentDistance = distanceMeasure.distance(spatialCenter, entityContents);

            if (currentDistance > maxDistance)
                maxDistance = currentDistance;
        }

        radius = maxDistance;
        done = true;
    }

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
