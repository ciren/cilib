/*
 * Copyright (C) 2003 - 2008
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
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.util.Cloneable;

/**
 * This is a generalization for all algorithms that maintain a collection of
 * {@linkplain Entity} objects. Examples of this would include PSO, EC and ACO.
 * 
 * @author Gary Pampara
 * @author otter
 * @param <E> All types derived from {@linkplain Entity}.
 */
public abstract class EntityCollection<E extends Entity> 
	implements Iterable<E>, List<E>, Cloneable, Serializable {

	/**
	 * {@inheritDoc}
	 */
	public abstract EntityCollection<E> getClone();
       
    /**
     * Adds an entity to the topology.
     * @param entity The entity to be added.
     * @return <code>true</code> if the addition is successful, <code>false</code> otherwise.
     */
    public abstract boolean add(E entity);
    
    
    /**
     * Removes an entity from the topology.
     * @param entity The entity to be removed.
     * @return boolean, true if remove operation was successful.
     */
    public abstract boolean remove(E entity);
    
    
    /**
     * {@inheritDoc}
     */
    public abstract E get(int index);
    
    
    /**
     * {@inheritDoc}
     */
    public abstract E set(int index, E entity);    
    
    /**
     * {@inheritDoc}
     */
    public abstract boolean isEmpty();
    
    
    /**
     * Wipe all the entities within the topology.
     * {@inheritDoc}
     */ 
    public abstract void clear();
    
    /**
     * {@inheritDoc}
     */
    public abstract boolean contains(Object o);
    
    /**
     * {@inheritDoc}
     */
    public abstract boolean containsAll(Collection<?> c);
    
    /**
     * {@inheritDoc}
     */
    public abstract boolean equals(Object o);
    
    /**
     * {@inheritDoc}
     */
    public abstract int hashCode();
    
    /**
     * {@inheritDoc}
     */
    public abstract Iterator<E> iterator();
    
    /**
     * {@inheritDoc}
     */
    public abstract boolean remove(Object o);
    
    /**
     * {@inheritDoc}
     */
    public abstract boolean removeAll(Collection<?> c);
    
    /**
     * {@inheritDoc}
     */
    public abstract boolean retainAll(Collection<?> c);
    
    /**
     * Returns the size of the {@linkplain EntityCollection}. 
     * @return The size of {@linkplain EntityCollection}.
     */
    public abstract int size();
    
    /**
     * {@inheritDoc}
     */
    public abstract Object [] toArray();
    
    /**
     * {@inheritDoc}
     */
    public abstract <T> T[] toArray(T[] a);    

    /**
     * Get the <code>id</code> associated with this {@linkplain EntityCollection}.
     * @return The <code>id</code> for this {@linkplain EntityCollection}.
     */
    public abstract String getId();
    
    /**
     * Set the <code>id</code> for this {@linkplain EntityCollection}.
     * @param id The value to set.
     */
    public abstract void setId(String id);
}
