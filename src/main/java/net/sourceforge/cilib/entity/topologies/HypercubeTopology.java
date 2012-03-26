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
package net.sourceforge.cilib.entity.topologies;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.AbstractTopology;
import net.sourceforge.cilib.entity.Entity;

/**
 * @param <E> The {@linkplain Entity} type.
 */
public class HypercubeTopology<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = -8328600903928335004L;

    /**
     * Default constructor.
     */
    public HypercubeTopology() {
        super();
        this.neighbourhoodSize = ConstantControlParameter.of(5);
    }

    /**
     * Copy constructor.
     */
    public HypercubeTopology(HypercubeTopology<E> copy) {
        super(copy);
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
     * {@inheritDoc}
     */
    @Override
    public int getNeighbourhoodSize() {
        return (int) Math.round(neighbourhoodSize.getParameter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }

    /**
     * Iterator to traverse the hypercube topology.
     */
    private class HypercubeNeighbourhoodIterator<T extends Entity> extends NeighbourhoodIterator<T> {
        private int count;

        public HypercubeNeighbourhoodIterator(AbstractTopology<T> topology, IndexedIterator<T> iterator) {
            super(topology, iterator);
            this.count = 0;
        }

        @Override
        public T next() {
            if (count >= topology.getNeighbourhoodSize()) {
                throw new NoSuchElementException();
            }
            
            int i = index ^ Double.valueOf(Math.pow(2, count)).intValue();
            count++;

            return topology.get(i);
        }

        @Override
        public void remove() {
            topology.remove(index);
            index = index ^ Double.valueOf(Math.pow(2, count)).intValue();
            
            if (index < 0) {
                index += topology.size();
            }
        }

        @Override
        public boolean hasNext() {
            return count < topology.getNeighbourhoodSize();
        }
    }
}
