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
import com.google.inject.Provider;
import fj.data.Option;

import java.util.Iterator;

import net.cilib.collection.Topology;
import net.cilib.entity.Entities;
import net.cilib.entity.Entity;
import net.cilib.entity.FitnessComparator;

/**
 * @author gpampara
 */
public class NeighborhoodBest implements Guide {

    private final Provider<Topology> topologyProvider;
    private final FitnessComparator comparator;

    @Inject
    public NeighborhoodBest(Provider<Topology> topologyProvider, FitnessComparator comparator) {
        this.topologyProvider = topologyProvider;
        this.comparator = comparator;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * A neighborhood best {@code Entity} is the entity which is the most fit,
     * within the neighborhood.
     */
    @Override
    public Option<Entity> of(Entity target) {
        Topology<Entity> topology = topologyProvider.get();
        Iterator<Entity> neighborhoodOf = topology.neighborhoodOf(target);
        Entity result = Entities.dummy();
        while (neighborhoodOf.hasNext()) {
            Entity current = neighborhoodOf.next();
            if (result == Entities.dummy()) {
                result = current;
                continue;
            }
            if (comparator.isMoreFit(current, result)) {
                result = current;
            }
        }
        return (result != Entities.dummy()) ? Option.some(result) : Option.<Entity>none();
    }
}
