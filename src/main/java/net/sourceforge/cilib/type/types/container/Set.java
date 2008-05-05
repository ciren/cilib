/*
 * Set.java
 *
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
package net.sourceforge.cilib.type.types.container;


import java.util.HashSet;
import java.util.Iterator;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.type.types.AbstractType;


/**
 * TODO: Fix the copy constructor. and Complete javadoc.
 * @author mneethling
 * @param <E> The type.
 */
public class Set<E> extends AbstractType implements Structure<E> {
	private static final long serialVersionUID = 3697379819132292696L;
	private HashSet<E> elements;

	public Set() {
		elements = new HashSet<E>();
	}
	
	public Set(Set<E> copy) {
		this();
		
		for (E element : copy.elements) {
			this.elements.add(element);
		}
	}	
	
	public Set<E> getClone() {
		return new Set<E>(this);
	}

	
	public boolean add(E obj) {
		return elements.add(obj);
	}
	
	public boolean addAll(Structure<? extends E> s) {
		for (E element : s)
			this.elements.add(element);
		
		return true;
	}
	
	public void clear() {
		elements.clear();
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
		
	public int size() {
		return elements.size();
	}
	
	public boolean equals(Object other)  {
		return this.elements.equals(other);			
	}

	public int hashCode() {		
		return this.elements.hashCode();
	}

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

	
	public int getDimension() {
		return elements.size();
	}

	public void randomise() {
		throw new UnsupportedOperationException("randomise() not implemented for " + this.getClass().getName());
	}
	
	public void reset() {
		throw new UnsupportedOperationException("reset() not implemented for " + this.getClass().getName());
	}
	
	public String toString() {
		throw new UnsupportedOperationException("toString() not implemented for " + this.getClass().getName());
	}

	public String getRepresentation() {
		throw new UnsupportedOperationException("getRepresentation() not implemented for " + this.getClass().getName());
	}


	public void accept(Visitor<E> visitor) {
		throw new UnsupportedOperationException("writeExternal() not implemented for " + this.getClass().getName());
	}


	public boolean contains(E element) {
		return this.elements.contains(element);
	}


	public boolean remove(E element) {
		// TODO Auto-generated method stub
		return false;
	}


	public E remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean removeAll(Structure<E> structure) {
		boolean result = true;
		

		for (E element : structure) {
			if (!this.elements.contains(element))
				result = false;
			else
				this.elements.remove(element);
		}
			
		return result;
	}


	public boolean isInsideBounds() {
		// TODO Auto-generated method stub
		return false;
	}

}
