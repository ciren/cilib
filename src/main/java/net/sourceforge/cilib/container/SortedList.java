/*
 * SortedList.java
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
package net.sourceforge.cilib.container;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 
 * @author Gary Pampara
 * @param <E> The {@linkplain Comparable} type.
 */
public class SortedList<E extends Comparable<? super E>> extends LinkedList<E> {

	private static final long serialVersionUID = 4170822549076470223L;
	private Comparator<E> comparator = null;
	
	
	/**
	 * 
	 *
	 */
	public SortedList() {
		super();
	}
	
	
	/**
	 * 
	 * @param comparator
	 */
	public SortedList(Comparator<E> comparator) {
		super();
		this.comparator = comparator;
	}
	
	
	/**
	 * 
	 * @param copy
	 */
	public SortedList(SortedList<E> copy) {
		super();
		this.addAll(copy);
	}
	
	
	public SortedList<E> getClone() {
		return new SortedList<E>(this);
	}
	
	
	/**
	 * 
	 */
	public boolean add(E e) {
		int index = Collections.binarySearch(this, e, comparator);
		
		if (index < 0)
			super.add(-index-1, e);
		else
			super.add(index, e);
		
		return true;
	}
	
	
	/**
	 * 
	 */
	public boolean addAll(Collection<? extends E> c) {
		for (E element : c)
			this.add(element);
		
		return true;		
	}

	
	/**
	 * @return Returns the comparator.
	 */
	public Comparator<E> getComparator() {
		return comparator;
	}


	/**
	 * @param comparator The comparator to set.
	 */
	public void setComparator(Comparator<E> comparator) {
		this.comparator = comparator;
	}
	
	
	public Iterator<E> iterator() {
		return new DynamicIterator<E>(this);
	}
	
	public ListIterator<E> listIterator() {
		return this.listIterator(0);
	}
	
	public ListIterator<E> listIterator(int index) {
		return new DynamicIterator<E>(this, index);
	}
	
}
