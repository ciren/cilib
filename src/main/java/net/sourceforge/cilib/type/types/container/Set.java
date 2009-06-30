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
package net.sourceforge.cilib.type.types.container;


import java.util.HashSet;
import java.util.Iterator;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.random.generator.Random;


/**
 * TODO: Fix the copy constructor. and Complete javadoc.
 * @author mneethling
 * @param <E> The type.
 */
public class Set<E> implements StructuredType<E> {
    private static final long serialVersionUID = 3697379819132292696L;
    private HashSet<E> elements;

    /**
     * Create a new Set.
     */
    public Set() {
        elements = new HashSet<E>();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public Set(Set<E> copy) {
        this();

        for (E element : copy.elements) {
            this.elements.add(element);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Set<E> getClone() {
        return new Set<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(E obj) {
        return elements.add(obj);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(StructuredType<? extends E> s) {
        for (E element : s)
            this.elements.add(element);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        elements.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return elements.size();
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if ((other == null) || (this.getClass() != other.getClass()))
            return false;

        Set<?> otherSet = (Set<?>) other;
        return this.elements.equals(otherSet.elements);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.elements == null ? 0 : this.elements.hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    public Object[] toArray() {
        Object[] result = new Object[size()];
        Iterator<E> e = iterator();
        for (int i=0; e.hasNext(); i++)
            result[i] = e.next();
        return result;
    }


    public <T> T[] toArray(T[] a) {
        int size = this.elements.size();
        Object [] result = a;
        if (a.length < size) {
            result = (Object []) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        Iterator<E> it = iterator();
        //Object[] result = a;
        for (int i=0; i<size; i++)
            result[i] = it.next();
        if (a.length > size)
        a[size] = null;
        return a;
    }

    /**
     * {@inheritDoc}
     */
    public int getDimension() {
        return elements.size();
    }

    /**
     * Not supported for this container.
     */
    public void randomise() {
        throw new UnsupportedOperationException("randomise() not implemented for " + this.getClass().getName());
    }

    /**
     * Not supported for this container.
     */
    public void reset() {
        throw new UnsupportedOperationException("reset() not implemented for " + this.getClass().getName());
    }

    /**
     * Not supported for this container.
     */
    public String toString() {
        throw new UnsupportedOperationException("toString() not implemented for " + this.getClass().getName());
    }

    /**
     * Not supported for this container.
     */
    public String getRepresentation() {
        throw new UnsupportedOperationException("getRepresentation() not implemented for " + this.getClass().getName());
    }

    /**
     * Not supported for this container.
     */
    public void accept(Visitor<E> visitor) {
        throw new UnsupportedOperationException("writeExternal() not implemented for " + this.getClass().getName());
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(E element) {
        return this.elements.contains(element);
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(E element) {
        return this.elements.remove(element);
    }

    /**
     * {@inheritDoc}
     */
    public E remove(int index) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeAll(StructuredType<E> structure) {
        boolean result = true;


        for (E element : structure) {
            if (!this.elements.contains(element))
                result = false;
            else
                this.elements.remove(element);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInsideBounds() {
        return false;
    }

    @Override
    public void randomize(Random random) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
