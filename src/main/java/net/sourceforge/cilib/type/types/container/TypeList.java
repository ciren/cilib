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

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;

/**
 * General list container for all {@code Type} instances.
 *
 * @author gpampara
 */
public class TypeList extends AbstractList<Type> {
    private static final long serialVersionUID = 136711882764612609L;
    private List<Type> components;

    /**
     * Create a new instance.
     */
    public TypeList() {
        this.components = new ArrayList<Type>();
    }

    /**
     * Create a new instance with the predefined size.
     * @param size The predefined size.
     */
    public TypeList(int size) {
        this.components = new ArrayList<Type>();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public TypeList(TypeList copy) {
        this.components = new ArrayList<Type>(copy.components.size());

        for (Type type : copy.components)
            this.components.add(type.getClone());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeList getClone() {
        return new TypeList(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if ((o == null) || (this.getClass() != o.getClass()))
            return false;

        TypeList otherList = (TypeList) o;
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
    public Type get(int index) {
        return this.components.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int index, Type value) {
        this.components.set(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(int index, Type value) {
        this.components.add(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean append(AbstractList<Type> list) {
        for (Type type : list)
            this.components.add(type);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean prepend(AbstractList<Type> list) {
        for (int i = list.size()-1; i >= 0; i--)
            this.components.add(0, list.get(i));

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
        List<Type> result = this.components.subList(fromIndex, toIndex+1);
        TypeList sublist = new TypeList();

        for (Type type : result)
            sublist.add(type);

        return sublist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Type element) {
        return this.components.add(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(StructuredType<? extends Type> structure) {
        for (Type type : structure)
            this.components.add(type);

        return true;
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
    public boolean contains(Type element) {
        return this.components.contains(element);
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
    public Iterator<Type> iterator() {
        return this.components.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Type element) {
        return this.components.remove(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type remove(int index) {
        return this.components.remove(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(StructuredType<Type> structure) {
        for (Type type : structure)
            this.components.remove(type);

        return true;
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
    public void accept(Visitor<Type> visitor) {
        for (Type type : this.components)
            if (!visitor.isDone())
                visitor.visit(type);
    }

    @Override
    public void randomize(Random random) {
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

}
