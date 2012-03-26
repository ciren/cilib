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

import com.google.common.collect.Lists;
import java.util.*;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.AbstractTopology;
import net.sourceforge.cilib.entity.Entity;

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

    protected LinkedList<E> entities;
    protected ControlParameter neighbourhoodSize;
    protected int id;

    /**
     * Creates a new instance of <code>LBestTopology</code>. The default
     * {@link #neighbourhoodSize} is a {@linkplain ConstantControlParameter} with it's parameter set to
     * 3.
     */
    public LBestTopology() {
        this.entities = Lists.<E>newLinkedList();
        this.neighbourhoodSize = ConstantControlParameter.of(3);
    }

    /**
     * Copy constructor. Copy the provided instance.
     * @param copy The instance to copy.
     */
    public LBestTopology(LBestTopology<E> copy) {
        this.neighbourhoodSize = copy.neighbourhoodSize;
        this.entities = Lists.<E>newLinkedList();
        
        for (E entity : copy.entities) {
            this.entities.add((E) entity.getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LBestTopology<E> getClone() {
        return new LBestTopology<E>(this);
    }
    
    @Override
    public Iterator<E> iterator() {
        return new LBestTopologyIterator<E>(this);
    }

    /**
     * Recalculate the {@link #neighbourhoodSize} by updating the
     * {@link ControlParameter} and then construct a new iterator to be returned.
     * @param iterator The {@linkplain Iterator} to wrap.
     * @return a new iterator for this topology.
     */
    @Override
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        neighbourhoodSize.updateParameter();
        return new LBestNeighbourhoodIterator<E>(this, (IndexedIterator<E>) iterator);
    }

    /**
     * Sets the {@linkplain ControlParameter} that should be used to determine the
     * number of particles in the neighbourhood of each particle. The default is a
     * {@linkplain ConstantControlParameter} with the parameter set to 3.
     * @param neighbourhoodSize The {@linkplain ControlParameter} to use.
     */
    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }

    /**
     * Accessor for the number of particles in a neighbourhood. NOTE: This method does not return the
     * {@linkplain ControlParameter} but the parameter that is changed / updated by it
     * rounded to the nearest integer.
     * @return The size of the neighbourhood.
     */
    public int getNeighbourhoodSize() {
        int rounded = Long.valueOf(Math.round(neighbourhoodSize.getParameter())).intValue();

        if (size() == 0) // to show a sensible default value in CiClops
            return rounded;

        return Math.min(rounded, size());
    }
    
    @Override
    public boolean add(E element) {
        return entities.add(element);
    }

    @Override
    public boolean addAll(Collection<? extends E> set) {
        return this.entities.addAll(set);
    }

    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public E get(int index) {
        return entities.get(index);
    }

    @Override
    public E set(int index, E entity) {
        entities.set(index, entity);
        return entity;
    }

    @Override
    public boolean isEmpty() {
        return this.entities.isEmpty();
    }

    @Override
    public void clear() {
        this.entities.clear();
    }

    @Override
    public boolean contains(Object entity) {
        return this.entities.contains(entity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GBestTopology<E> other = (GBestTopology<E>) obj;
        if (this.entities != other.entities && (this.entities == null || !this.entities.equals(other.entities))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.entities != null ? this.entities.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Method not supported in LBestTopology");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Method not supported in LBestTopology");
    }

    @Override
    public Object[] toArray() {
        return this.entities.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Method not supported in LBestTopology");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Method not supported in LBestTopology");
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Method not supported in LBestTopology");
    }

    @Override
    public int indexOf(Object o) {
        return this.entities.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Method not supported in LBestTopology");
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.entities.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return this.entities.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Method not supported in LBestTopology");
    }

    @Override
    public boolean remove(Object o) {
        return this.entities.remove(o);
    }

    @Override
    public E remove(int index) {
        return this.entities.remove(index);
    }
    
    /**
     * Interface to define the manner in which the iterator is to be constructed for Array types.
     *
     * @param <T> The {@linkplain Entity} type.
     */
    protected interface IndexedIterator<T extends Entity> extends Iterator<T> {
        public int getIndex();
    }

    protected class LBestTopologyIterator<T extends Entity> implements IndexedIterator<T> {

        public LBestTopologyIterator(LBestTopology<T> topology) {
            this.topology = topology;
            this.index = -1;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public boolean hasNext() {
            int lastIndex = topology.entities.size() - 1;
            return (index != lastIndex) && (lastIndex >= 0);
        }

        @Override
        public T next() {
            int lastIndex = topology.entities.size() - 1;
            if (index == lastIndex) {
                throw new NoSuchElementException();
            }

            ++index;

            return topology.entities.get(index);
        }

        @Override
        public void remove() {
            if (index == -1) {
                throw new IllegalStateException();
            }

            topology.entities.remove(index);
            --index;
        }

        private int index;
        private LBestTopology<T> topology;
    }

    protected class LBestNeighbourhoodIterator<T extends Entity> implements IndexedIterator<T> {
        protected LBestTopology<T> topology;
        protected int index;
        protected int count;

        public LBestNeighbourhoodIterator(LBestTopology<T> topology, IndexedIterator<T> iterator) {
            if (iterator.getIndex() == -1) {
                throw new IllegalStateException();
            }
            
            this.count = 0;
            this.topology = topology;
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
            return topology.entities.get(index);
        }

        @Override
        public void remove() {
            topology.entities.remove(index);
            --index;
            if (index < 0) {
                index += topology.size();
            }
        }
    }
}
