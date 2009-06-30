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
package net.sourceforge.cilib.entity.topologies;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.entity.Entity;

/**
 * @author Gareth David
 * @param <E> The {@linkplain Entity} type.
 */
public class HypercubeTopology<E extends Entity> extends GBestTopology<E> {
    private static final long serialVersionUID = -8328600903928335004L;
    private int neighbourhoodSize;

    public HypercubeTopology() {
        super();
        neighbourhoodSize = 5;
    }

    public HypercubeTopology(HypercubeTopology<E> copy) {
        super(copy);
        this.neighbourhoodSize = copy.neighbourhoodSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HypercubeTopology<E> getClone() {
        return new HypercubeTopology<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        return new HypercubeNeighbourhoodIterator<E>(this, (IndexedIterator<E>) iterator);
    }

    /**
     * Sets the number particles in the neighbourhood of each particle. The default is 5.
     *
     * @param neighbourhoodSize The size of the neighbourhood.
     */
    public void setNeighbourhoodSize(int neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }

    /**
     * Accessor for the number of particles in a neighbourhood.
     *
     * @return The size of the neighbourhood.
     */
    public int getNeighbourhoodSize() {
           return neighbourhoodSize;
    }

    private class HypercubeNeighbourhoodIterator<T extends Entity> implements IndexedIterator<T> {
        private HypercubeTopology<T> topology;
        private int index;
        private int count;

        public HypercubeNeighbourhoodIterator(HypercubeTopology<T> topology, IndexedIterator<T> iterator) {
            if (iterator.getIndex() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            index = iterator.getIndex();

            if (index < 0)
                index += topology.size();

            count = 0;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public boolean hasNext() {
            return (count < topology.getNeighbourhoodSize());
        }

        @Override
        public T next() {
            if (count >= topology.getNeighbourhoodSize()) {
                throw new NoSuchElementException();
            }
            int i = index ^ Double.valueOf(Math.pow(2, count)).intValue();
            count++;

            return topology.entities.get(i);
        }

        @Override
        public void remove() {
            topology.entities.remove(index);
            index = index ^ Double.valueOf(Math.pow(2, count)).intValue();
            if (index < 0) {
                index += topology.size();
            }
        }
    }
}
