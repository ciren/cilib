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

import java.util.Iterator;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.type.types.Randomizable;
import net.sourceforge.cilib.type.types.Type;

/**
 * Description for all objects that maintain a structure or collection of objects.
 *
 * @param <E> the type of object the {@linkplain Structure} may contain.
 */
public interface StructuredType<E> extends Iterable<E>, Type, Randomizable {

    @Override
    StructuredType<E> getClone();

    /**
     * Add the given <code>element</code> to the current {@linkplain Structure}.
     * @param element The object of type E to add
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    boolean add(E element);

    /**
     * Add add the objects of type {@code E}, within <code>structure</code>to the
     * current {@linkplain Structure}.
     * @param structure The containing object of elements to add.
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    boolean addAll(StructuredType<? extends E> structure);

    /**
     * Clear all contained object instances from the current {@linkplain Structure}.
     */
    void clear();

    /**
     * Determine if <code>element</code> is contained within the {@linkplain Structure}.
     * @param element The object that is tested for containment.
     * @return <code>true</code> if the {@linkplain Structure} contains the object,
     *         <code>false</code> otherwise.
     */
    boolean contains(E element);

    /**
     * Determine if the current {@linkplain Structure} is empty.
     * @return <code>true</code> if the {@linkplain Structure} is empty,
     *         else <code>false</code>.
     */
    boolean isEmpty();

    /**
     * {@inheritDoc}
     */
    @Override
    Iterator<E> iterator();

    /**
     * Remove the first <code>element</code> found within the {@linkplain Structure}.
     * @param element The object to remove
     * @return <code>true</code> if the removal was successful, <code>false</code> otherwise.
     */
    boolean remove(E element);

    /**
     * Remove the object at the specified <code>index</code>.
     * @param index The index at which an object is to be removed.
     * @return The removed instance
     */
    E remove(int index);

    /**
     * Remove all instances contained within <code>structure</code>, if contained.
     * @param structure The listing of instances to be removed.
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    boolean removeAll(StructuredType<E> structure);

    /**
     * Defines the size of the structure. This could be the length of
     * a vector (the vector dimensionality) or the number of elements within
     * a tree.
     * @return The size of the represented structure.
     */
    int size();

    /**
     * Accept the {@linkplain Visitor} instance and perform the actions within the
     * {@linkplain Visitor} on the objects contained within this {@linkplain Structure}.
     * @param visitor The {@linkplain Visitor} instance to execute.
     */
    void accept(Visitor<E> visitor);

}
