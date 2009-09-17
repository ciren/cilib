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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.entity.AbstractTopology;
import net.sourceforge.cilib.entity.Entity;

/**
 * <p>
 * Implementation of the gbest neighbourhood topology.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @param <E> The {@linkplain Entity} type.
 * @author Edwin Peer
 */
public class GBestTopology<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = 3190027340582769112L;

    protected LinkedList<E> entities;

    /**
     * Creates a new instance of <code>GBestTopology</code>.
     */
    public GBestTopology() {
        entities = new LinkedList<E>();
    }

    public GBestTopology(GBestTopology<E> copy) {
        this.entities = new LinkedList<E>();
        for (E entity : copy.entities) {
            entity.reinitialise();
            this.entities.add((E) entity.getClone());
        }
    }

    @Override
    public GBestTopology<E> getClone() {
        return new GBestTopology<E>(this);
    }

    @Override
    public Iterator<E> iterator() {
        return new GBestTopologyIterator<E>(this);
    }

    @Override
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        return new GBestTopologyIterator<E>(this);
    }

    @Override
    public boolean add(E particle) {
        return entities.add(particle);
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
    public boolean remove(E indiv) {
        return entities.remove(indiv);
    }

    @Override
    public E get(int index) {
        return this.entities.get(index);
    }

    @Override
    public E set(int index, E particle) {
        this.entities.set(index, particle);
        return particle;
    }

    @Override
    public List<E> asList() {
        return new ArrayList<E>(this.entities);
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
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("Method not supported in GBestTopology");
    }

    @Override
    public int hashCode() {
        return this.entities.hashCode();
    }

    @Override
    public boolean remove(Object o) {
        E entity = (E) o;
        return this.remove(entity);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Method not supported in GBestTopology");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Method not supported in GBestTopology");
    }

    @Override
    public Object[] toArray() {
        return this.entities.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Method not supported in GBestTopology");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Method not supported in GBestTopology");
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Method not supported in GBestTopology");
    }

    @Override
    public E remove(int index) {
        return this.entities.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.entities.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Method not supported in GBestTopology");
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
        throw new UnsupportedOperationException("Method not supported in GBestTopology");
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    /**
     * Interface to define the manner in which the iterator is to be constructed for Array types.
     * @author gpampara
     *
     * @param <T> The {@linkplain Entity} type.
     */
    protected interface IndexedIterator<T extends Entity> extends Iterator<T> {
        public int getIndex();
    }

    private class GBestTopologyIterator<T extends Entity> implements IndexedIterator<T> {

        public GBestTopologyIterator(GBestTopology<T> topology) {
            this.topology = topology;
            index = -1;
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
        private GBestTopology<T> topology;
    }

}

