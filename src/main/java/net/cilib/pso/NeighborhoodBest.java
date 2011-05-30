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
package net.cilib.pso;

import com.google.inject.Inject;
import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.entity.Entities;
import net.cilib.entity.Entity;
import net.cilib.entity.FitnessComparator;

/**
 * @author gpampara
 */
public class NeighborhoodBest extends Guide {

    private final FitnessComparator comparator;

    @Inject
    public NeighborhoodBest(FitnessComparator comparator) {
        this.comparator = comparator;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * A neighborhood best {@code Entity} is the most fit entity within
     * a neighborhood.
     */
    @Override
    public Option<Entity> f(Entity entity, Topology topology) {
        Iterable<Entity> neighbourhoodOf = topology.neighborhoodOf(entity);
        Entity result = Entities.dummy();
        for (Entity n : neighbourhoodOf) {
            if (n == entity) {
                continue;
            }
            if (comparator.isMoreFit(n, result)) {
                result = n;
            }
        }
        return (result != Entities.dummy()) ? Option.some(result) : Option.<Entity>none();
    }
}
