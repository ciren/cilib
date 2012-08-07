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
import net.sourceforge.cilib.entity.IndexedIterator;

/**
 * <p>
 * Implementation of the Local Best Neighbourhood topology.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @param <E> The {@linkplain Entity} type.
 */
public class LBestTopology<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = 93039445052676571L;

    /**
     * Default constructor. The default {@link #neighbourhoodSize} is 3.
     */
    public LBestTopology() {
        super();
        this.neighbourhoodSize = ConstantControlParameter.of(3);
    }

    /**
     * Copy constructor..
     */
    public LBestTopology(LBestTopology<E> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LBestTopology<E> getClone() {
        return new LBestTopology<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        return new LBestNeighbourhoodIterator<E>(this, (IndexedIterator<E>) iterator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNeighbourhoodSize() {
        int rounded = Long.valueOf(Math.round(neighbourhoodSize.getParameter())).intValue();

        if (size() == 0) // to show a sensible default value in CiClops
            return rounded;

        return Math.min(rounded, size());
    }
    
    /**
     * Iterator to traverse the lBest topology.
     */
    protected class LBestNeighbourhoodIterator<T extends Entity> extends NeighbourhoodIterator<T> {
        protected int count;

        public LBestNeighbourhoodIterator(AbstractTopology<T> topology, IndexedIterator<T> iterator) {
            super(topology, iterator);
            
            this.count = 0;
            this.index = iterator.getIndex() - (topology.getNeighbourhoodSize() / 2) - 1;
            
            if (index < 0) {
                index += topology.size();
            }
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public boolean hasNext() {
            return (count != topology.getNeighbourhoodSize());
        }

        @Override
        public T next() {
            if (count == topology.getNeighbourhoodSize()) {
                throw new NoSuchElementException();
            }
            ++index;
            ++count;
            if (index == topology.size()) {
               index = 0;
            }
            return topology.get(index);
        }

        @Override
        public void remove() {
            topology.remove(index);
            --index;
            if (index < 0) {
                index += topology.size();
            }
        }
    }
}
