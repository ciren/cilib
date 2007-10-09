/*
 * Structure.java
 * 
 * Copyright (C) 2004 - CIRG@UP 
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

public interface Structure<E> extends Iterable<E> {
	
	public boolean add(E element);
	
	public boolean addAll(Structure<? extends E> structure);
	
	public void clear();
	
	public boolean contains(E element);
	
	public boolean isEmpty();
	
	public Iterator<E> iterator();
	
	public boolean remove(E element);
	
	public E remove(int index);
	
	public boolean removeAll(Structure<E> structure);
	
	/**
	 * Defines the size of the structure. This could be the length of
	 * a vector or the number of elements within a tree.
	 * @return The size of the represented structure.
	 */
	public int size();
	
	public void accept(Visitor<E> visitor);
	
}
