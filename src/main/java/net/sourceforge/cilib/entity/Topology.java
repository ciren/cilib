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
package net.sourceforge.cilib.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This is a generalization for all algorithms that maintain a collection of
 * {@linkplain Entity} objects. Examples of this would include PSO, EC and ACO.
 *
 * @author Gary Pampara
 * @param <E> All types derived from {@linkplain Entity}.
 */
public interface Topology<E extends Entity>
    extends Iterable<E>, List<E>, Cloneable, Serializable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Topology<E> getClone();

    /**
     * Adds an entity to the topology.
     * @param entity The entity to be added.
     * @return <code>true</code> if the addition is successful, <code>false</code> otherwise.
     */
    @Override
    public boolean add(E entity);

    /**
     * Removes an entity from the topology.
     * @param entity The entity to be removed.
     * @return boolean, true if remove operation was successful.
     */
    public boolean remove(E entity);

    /**
     * {@inheritDoc}
     */
    public E get(int index);

    /**
     * {@inheritDoc}
     */
    public E set(int index, E entity);

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty();

    /**
     * Remove all the entities from the topology.
     * {@inheritDoc}
     */
    public void clear();

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o);

    /**
     * {@inheritDoc}
     */
    public boolean containsAll(Collection<?> c);

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o);

    /**
     * {@inheritDoc}
     */
    public int hashCode();

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator();

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o);

    /**
     * {@inheritDoc}
     */
    public boolean removeAll(Collection<?> c);

    /**
     * {@inheritDoc}
     */
    public boolean retainAll(Collection<?> c);

    /**
     * Returns the size of the {@linkplain Topology}.
     * @return The size of {@linkplain Topology}.
     */
    public int size();

    /**
     * {@inheritDoc}
     */
    public Object [] toArray();

    /**
     * {@inheritDoc}
     */
    public <T> T[] toArray(T[] a);

    /**
     * Get the <code>id</code> associated with this {@linkplain Topology}, if
     * an id is defined.
     * @return The <code>id</code> for this {@linkplain Topology}.
     */
    public String getId();

    /**
     * Set the <code>id</code> for this {@linkplain Topology}.
     * @param id The value to set.
     */
    public void setId(String id);

    /**
     * Get all the entities within the topology.
     * @return Collection. Data collection of all the entities
     */
    public List<E> asList();

    /**
     * Obtain the current best entity within the {@code Topology}.
     * @return The best {@code Entity}.
     */
    public E getBestEntity();

    /**
     * Obtain the current best entity within the {@code Topology}, based
     * on the provided {@code Comparator}.
     * @param comparator The {@code Comparator} to use.
     * @return The best {@code Entity} based on the defined comparison.
     */
    public E getBestEntity(Comparator<? super E> comparator);

    /**
     * Accept a vistitor and perform the visitor actions on this
     * <tt>Topology</tt>.
     *
     * @param visitor The {@see net.sourceforge.cilib.container.visitor.Visitor} to accept
     */
    public void accept(Visitor<E> visitor);

    /**
     * Accept a {@code TopologyVisitor} into the {@code Topology} to perform the actions
     * defined within the {@code TopologyVisitor}.
     * @param visitor The instance to accept into the {@code Topology}.
     */
    public void accept(TopologyVisitor visitor);

    /**
     * Perform any required updates to the {@linkplain Topology} instance.
     * The method in has an empty implementation and needs to be overridden
     * within the required subclass.
     */
    public void update();

    /**
     * Returns an <code>Iterator</code> over all particles in the neighbourhood of
     * the particle referred to by the given <code>Iterator</code>.
     *
     * @param iterator An iterator that refers to a particle in this topology.
     * @return A particle iterator.
     */
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator);

}
