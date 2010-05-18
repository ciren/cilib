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
package net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 */
public class SpatialCenterInitialisationStrategy implements CenterInitialisationStrategy {

    @Override
    public Vector getCenter() {
        PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.get();
        int numberOfEntities = algorithm.getTopology().size();//getPopulationSize();

        Iterator<? extends Entity> averageIterator = algorithm.getTopology().iterator();
        Entity entity = averageIterator.next();
        Vector averageEntityPosition = (Vector) entity.getCandidateSolution().getClone();

        while (averageIterator.hasNext()) {
            entity = averageIterator.next();
            Vector entityContents = (Vector) entity.getCandidateSolution();
            for (int j = 0; j < averageEntityPosition.getDimension(); ++j)
               averageEntityPosition.setReal(j, averageEntityPosition.getReal(j)+entityContents.getReal(j));
        }

        for (int j = 0; j < averageEntityPosition.getDimension(); ++j)
           averageEntityPosition.setReal(j, averageEntityPosition.getReal(j)/numberOfEntities);

        return averageEntityPosition;
    }

}
