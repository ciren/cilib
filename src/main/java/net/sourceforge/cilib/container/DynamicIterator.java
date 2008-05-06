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
package net.sourceforge.cilib.container;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of an <tt>Iterator</tt> that is resilient to concurrent
 * modifications.
 * 
 * @author Gary Pampara
 * @param <E> The element type.
 */
public class DynamicIterator<E> implements Iterator<E>, ListIterator<E> {
	
	private List<E> collection;
	private int index;
	private int lastRet;
	
	public DynamicIterator(List<E> collection) {
		this.collection = collection;
		this.index = 0;
		this.lastRet = -1;
	}
	
	public DynamicIterator(List<E> collection, int beginIndex) {
		this.collection = collection;
		this.index = beginIndex;
		this.lastRet = -1;
	}

	public boolean hasNext() {
		return (this.index != this.collection.size()) & (this.index < this.collection.size());
	}

	public E next() {
		try {
			E next = this.collection.get(index);
			lastRet = index++;
			return next;			
		}
		catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}

	public void remove() {
		if (lastRet == -1) {
			throw new IllegalStateException();
		}
		
		this.collection.remove(lastRet);
		if (lastRet < index)
			index--;
		lastRet = -1;
	}

	public boolean hasPrevious() {
		return this.index != 0;
	}

	public E previous() {
		try {
			int i = index - 1;
			E previous = this.collection.get(i);
			lastRet = index = i;
			return previous;
		}
		catch (IndexOutOfBoundsException indexOutOfBoundsException) {
			throw new NoSuchElementException();
		}
	}

	public int nextIndex() {
		return this.index;
	}

	public int previousIndex() {
		return this.index-1;
	}

	public void set(E o) {
		if (this.lastRet == -1)
			throw new IllegalStateException();
		
		this.collection.set(lastRet, o);
	}

	public void add(E o) {
		this.collection.add(index++, o);
		this.lastRet = -1;		
	}

}
