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
    public StructuredType getClone();

    /**
     * Add the given <code>element</code> to the current {@linkplain Structure}.
     * @param element The object of type E to add
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    public boolean add(E element);

    /**
     * Add add the objects of type {@code E}, within <code>structure</code>to the
     * current {@linkplain Structure}.
     * @param structure The containing object of elements to add.
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    public boolean addAll(StructuredType<? extends E> structure);

    /**
     * Clear all contained object instances from the current {@linkplain Structure}.
     */
    public void clear();

    /**
     * Determine if <code>element</code> is contained within the {@linkplain Structure}.
     * @param element The object that is tested for containment.
     * @return <code>true</code> if the {@linkplain Structure} contains the object,
     *         <code>false</code> otherwise.
     */
    public boolean contains(E element);

    /**
     * Determine if the current {@linkplain Structure} is empty.
     * @return <code>true</code> if the {@linkplain Structure} is empty,
     *         else <code>false</code>.
     */
    public boolean isEmpty();

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> iterator();

    /**
     * Remove the first <code>element</code> found within the {@linkplain Structure}.
     * @param element The object to remove
     * @return <code>true</code> if the removal was successful, <code>false</code> otherwise.
     */
    public boolean remove(E element);

    /**
     * Remove the object at the specified <code>index</code>.
     * @param index The index at which an object is to be removed.
     * @return The removed instance
     */
    public E remove(int index);

    /**
     * Remove all instances contained within <code>structure</code>, if contained.
     * @param structure The listing of instances to be removed.
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    public boolean removeAll(StructuredType<E> structure);

    /**
     * Defines the size of the structure. This could be the length of
     * a vector (the vector dimensionality) or the number of elements within
     * a tree.
     * @return The size of the represented structure.
     */
    public int size();

    /**
     * Accept the {@linkplain Visitor} instance and perform the actions within the
     * {@linkplain Visitor} on the objects contained within this {@linkplain Structure}.
     * @param visitor The {@linkplain Visitor} instance to execute.
     */
    public void accept(Visitor<E> visitor);

}
