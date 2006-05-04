/*
 * HypercubeTopology.java
 *
 * Created on January 17, 2003, 8:07 PM
 *
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.entity.topologies;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.entity.Entity;

/**
 * @author Gareth David
 */

public class HypercubeTopology<E extends Entity> extends GBestTopology<E> {

	public HypercubeTopology() {
		super();
		neighbourhoodSize = 5;
	}

	public Iterator<E> neighbourhood(Iterator<E> iterator) {
        return new HypercubeNeighbourhoodIterator<E>(this, (ArrayIterator<E>) iterator);
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
    	if (super.size() == 0) { // to show a sensible default value in CiClops
    		return neighbourhoodSize;
    	}
        else if (neighbourhoodSize > super.size()) {
            return super.size();
        }
        else {
            return neighbourhoodSize;
        }
    }

	private int neighbourhoodSize;

	private class HypercubeNeighbourhoodIterator<T extends Entity> implements ArrayIterator<T> {

        public HypercubeNeighbourhoodIterator(HypercubeTopology<T> topology, ArrayIterator iterator) {
            if (iterator.getIndex() == -1) {
                throw new IllegalStateException();
            }
            this.topology = topology;
            index = iterator.getIndex();
            if (index < 0) {
                index += topology.size();
            }
            count = 0;
        }

        public int getIndex() {
            return index;
        }

        public boolean hasNext() {
            return (count < topology.getNeighbourhoodSize());
        }

        public T next() {
            if (count >= topology.getNeighbourhoodSize()) {
                throw new NoSuchElementException();
            }
			int i = index^((int)Math.pow(2, count));
			count++;

            return topology.particles.get(i);
        }

        public void remove() {
            topology.particles.remove(index);
            index = index^((int)Math.pow(2, count));
            if (index < 0) {
            	index += topology.size();
            }
        }

        private HypercubeTopology<T> topology;
        private int index;
        private int count;
    }
}
