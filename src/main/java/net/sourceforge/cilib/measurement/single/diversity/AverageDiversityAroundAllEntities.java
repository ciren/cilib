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
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
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
    @Override
<<<<<<< HEAD
    public Real getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;
        int numberOfEntities = populationBasedAlgorithm.getTopology().size();
=======
    public Real getValue(Algorithm currentAlgorithm) {
        PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) currentAlgorithm; //this class did not override the Interface's getValue. Changed now.
        int numberOfEntities = algorithm.getTopology().size();
>>>>>>> /bad-path/

        Iterator<? extends Entity> populationCenterIterator = populationBasedAlgorithm.getTopology().iterator();

        double totalDistanceSum = 0.0;

        while (populationCenterIterator.hasNext()) {
            Vector currentCenter = (Vector) (((Entity) populationCenterIterator.next()).getCandidateSolution());
            Iterator<? extends Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();
            double currentDistanceSum = 0.0;

            while (populationIterator.hasNext()) {
                Vector currentEntityPosition = (Vector) (((Entity) populationIterator.next()).getCandidateSolution());
                currentDistanceSum += distanceMeasure.distance(currentCenter, currentEntityPosition);
            }

            totalDistanceSum += currentDistanceSum / numberOfEntities;
        }

        totalDistanceSum /= numberOfEntities;
        totalDistanceSum /= normalisationParameter.getNormalisationParameter(populationBasedAlgorithm);

        return Real.valueOf(totalDistanceSum);
    }
   
}
