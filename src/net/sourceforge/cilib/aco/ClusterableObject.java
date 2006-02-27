/*
 * ClusterableObject.java
 *
 * Created on Jun 30, 2004
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.aco;

/**
 * Generic wrapper class for general objects. Assign a number to a specific type of object.
 * @author Gary Pampara
 */
public class ClusterableObject<T> {
	private T object;
	private int type;

	/**
	 * Constructor to create an object consisting of a type <code>T</code>
	 * @param obj The object to wrap
	 * @param type An integer specifying the object type numberically
	 */
	public ClusterableObject(T obj, int type) {
		this.object = obj;
		this.type = type;
	}
	
	/**
	 * Return the wrapped object
	 * @return The object of type T that was originally wrapped
	 */	
	public T getObject() {
		return object;
	}
	
	/**
	 * Get the integer represenation of the current object
	 * @return The integer representing this object numerically
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Set the numerical type of the current <code>ClusterableObject&lt;T&gt;</code> 
	 * @param typeId The numerical representation of the current object
	 */
	public void setType(int typeId) {
		type = typeId;
	}
}
