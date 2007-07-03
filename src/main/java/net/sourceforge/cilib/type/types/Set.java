/*
 * Set.java
 * 
 * Created on 2005/05/17
 *
 * Copyright (C) 2003, 2005 - CIRG@UP 
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
 * 
 */
package net.sourceforge.cilib.type.types;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import net.sourceforge.cilib.container.visitor.Visitor;

/**
 * TODO: Fix the copy constructor
 * @author mneethling
 */

public class Set<E> implements Graph<E> {
	
	private static final long serialVersionUID = 3697379819132292696L;
	private HashSet<E> elements;

	public Set() {
		elements = new HashSet<E>();
	}
	
	
	public Set(Set<E> copy) {
		this();
		
		for (Iterator<E> item = copy.elements.iterator(); item.hasNext(); ) {
			this.elements.add(item.next());
		}
	}	
	
	public Set<E> clone() {
		return new Set<E>(this);
	}
	

	
	public boolean add(E obj) {
		//if (obj instanceof Type) {
			return elements.add((E)obj);
		//} else {
			//throw new IllegalArgumentException("Argument is not of type: net.sourceforge.cilib.Type.Type");
		//}
	}
	
	public boolean addAll(Structure<E> s) {
		//if (s instanceof Set) {
//			Set<T> tmp = (Set<T>) s;
		for (E element : s)
			this.elements.add(element);
		
	//	} else {
		//	throw new IllegalArgumentException("Argument is not of type: net.sourceforge.cilib.Type.Set");
		//}
		
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
		//if (other instanceof Set) {
		//	Set tmp = (Set) other;
			return this.elements.equals(other);			
		//}			
		//else {
		//	throw new IllegalArgumentException("Argument is not of type: net.sourceforge.cilib.Type.Set");
		//}		
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
		// TODO Auto-generated method stub
		
	}
	
	public void reset() {
		
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	

	public boolean add(E parent, E element) {
		throw new UnsupportedOperationException("Within a set there is no concept of a parent, all elements are equal");
	}


	public int edges() {
		return 0;
	}


	public int verticies() {
		return this.elements.size();
	}


	public void accept(Visitor<E> visitor) {
		// TODO Auto-generated method stub
		
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


	public boolean containsAll(Collection<?> c) {
		return this.elements.containsAll(c);
	}


	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
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


	public boolean addEdge(E a, E b) {
		return false;
	}


	public boolean isConnected(E a, E b) {
		return false;
	}


	public boolean isInsideBounds() {
		// TODO Auto-generated method stub
		return false;
	}

}
