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
package net.sourceforge.cilib.type.types.container;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;

/**
 * General list container for all {@code Type} instances.
 *
 * @author gpampara
 */
public class TypeList<T extends Type> extends AbstractList<T> {
    private static final long serialVersionUID = 136711882764612609L;
    private List<T> components;

    /**
     * Create a new instance.
     */
    public TypeList() {
        this.components = new ArrayList<T>();
    }

    /**
     * Create a new instance with the predefined size.
     * @param size The predefined size.
     */
    @Deprecated
    private TypeList(int size) {
        this.components = new ArrayList<T>(size);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    @Deprecated
    public TypeList(TypeList<T> copy) {
        this.components = new ArrayList<T>(copy.components.size());

        for (T type : copy.components)
            this.components.add((T) type.getClone());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeList<T> getClone() {
        return new TypeList<T>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }

        TypeList<T> otherList = (TypeList<T>) o;
        return this.components.equals(otherList.components);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.components == null ? 0 : this.components.hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(int index) {
        return this.components.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int index, T value) {
        this.components.set(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(int index, T value) {
        this.components.add(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean append(AbstractList<T> list) {
        for (T type : list) {
            this.components.add(type);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean prepend(AbstractList<T> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            this.components.add(0, list.get(i));
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return this.components.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeList subList(int fromIndex, int toIndex) {
        // Need to bump up the toIndex because the List.subList() operation is upper bound exclusive.
        List<T> result = this.components.subList(fromIndex, toIndex + 1);
        TypeList<T> sublist = new TypeList<T>();

        for (T type : result) {
            sublist.add(type);
        }

        return sublist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(T element) {
        return this.components.add(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return this.components.addAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.components.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return this.components.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.components.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<T> iterator() {
        return this.components.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return this.components.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return this.components.removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.components.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(Visitor<T> visitor) {
        for (T type : this.components) {
            if (!visitor.isDone()) {
                visitor.visit(type);
            }
        }
    }

    @Override
    public void randomize(RandomProvider random) {
        for (int i = 0; i < components.size(); i++) {
            Type type = components.get(i);
            if (type instanceof Numeric) {
                Numeric numeric = (Numeric) type;
                numeric.randomize(random);
            }

            if (type instanceof StructuredType) {
                StructuredType structuredType = (StructuredType) type;
                structuredType.randomize(random);
            }
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.components.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.components.containsAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.components.retainAll(c);
    }
}
