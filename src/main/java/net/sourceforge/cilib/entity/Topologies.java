/**
 * Copyright (C) 2003 - 2008
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

package net.sourceforge.cilib.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Topology related utilities.
 * @author gpampara
 */
public final class Topologies {

    private Topologies() {
    }

    /**
     * Gather the best entity of each neighbourhood (in this {@link Topology}) in a
     * {@link Set} (duplicates are not allowed) and return them. A single {@link Entity} may
     * dominate in more than one neighbourhood, but we just want unique entities.
     *
     * @param <E> The entity type.
     * @param topology The topology to query.
     * @return a {@link Set} cosisting of the best entity of each neighbourhood in the
     *         topology
     * @author Theuns Cloete
     */
    public static <E extends Entity> Set<E> getNeighbourhoodBestEntities(Topology<E> topology) {
        // a Set does not allow duplicates
        Set<E> neighbourhoodBests = new HashSet<E>(topology.size());
        Iterator<E> topologyIterator = topology.iterator();

        // iterate over all entities in the topology
        while (topologyIterator.hasNext()) {
            topologyIterator.next();
            Iterator<E> neighbourhoodIterator = topology.neighbourhood(topologyIterator);
            E currentBestEntity = null;

            // iterate over the neighbours of the current entity
            while (neighbourhoodIterator.hasNext()) {
                E anotherEntity = neighbourhoodIterator.next();
                // keep track of the best entity
                if (currentBestEntity == null || currentBestEntity.compareTo(anotherEntity) > 0) {
                    currentBestEntity = anotherEntity;
                }
            }
            // only gather unique entities
            if (currentBestEntity != null) {
                neighbourhoodBests.add(currentBestEntity);
            }
        }

        return neighbourhoodBests;
    }
}
