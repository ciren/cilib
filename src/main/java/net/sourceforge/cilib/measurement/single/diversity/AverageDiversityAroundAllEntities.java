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
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Olusegun Olorunda
 *
 * This extends the concept of diversity around the population center by evaluating the average distance
 * around each entity in the population, i.e. each population entity is used as a center, and then
 * calculating the average over all these distances.
 *
 */
public class AverageDiversityAroundAllEntities extends Diversity {
    private static final long serialVersionUID = 898972772053582980L;

    /**
     * {@inheritDoc}
     */
    public Real getValue() {
        PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.get();
        int numberOfEntities = algorithm.getTopology().size();

        Iterator<? extends Entity> populationCenterIterator = algorithm.getTopology().iterator();

        double totalDistanceSum = 0.0;

        while (populationCenterIterator.hasNext()) {
            Vector currentCenter = (Vector) (((Entity) populationCenterIterator.next()).getCandidateSolution());
            Iterator<? extends Entity> populationIterator = algorithm.getTopology().iterator();
            double currentDistanceSum = 0.0;

            while (populationIterator.hasNext()) {
                Vector currentEntityPosition = (Vector) (((Entity) populationIterator.next()).getCandidateSolution());
                currentDistanceSum += distanceMeasure.distance(currentCenter, currentEntityPosition);
            }

            totalDistanceSum += currentDistanceSum/numberOfEntities;
        }

        totalDistanceSum /= numberOfEntities;

        normalisationParameter.setDistanceMeasure(distanceMeasure);
        totalDistanceSum /= normalisationParameter.getValue();

        return new Real(totalDistanceSum);
    }

}
