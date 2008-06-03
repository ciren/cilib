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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import net.sourceforge.cilib.util.Cloneable;

/**
 * A collection that always provides a list of elements that is sorted. The sort is
 * specified by the provied {@linkplain Comparator} instance.
 * @author Gary Pampara
 * @param <E> The {@linkplain Comparable} type.
 */
public class SortedList<E extends Comparable<? super E>> extends LinkedList<E> implements Cloneable {

	private static final long serialVersionUID = 4170822549076470223L;
	private Comparator<E> comparator = null;
	
	
	/**
	 * Create a new instance of {@linkplain SortedList} without a {@linkplain Comparator}
	 * defined.
	 */
	public SortedList() {
		super();
	}
	
	
	/**
	 * Create a new instance of {@linkplain SortedList} with the provided {@linkplain Comparator}. 
	 * @param comparator The {@linkplain Comparator} to use.
	 */
	public SortedList(Comparator<E> comparator) {
		super();
		this.comparator = comparator;
	}
	
	
	/**
	 * Create a copy of the provided instance.
	 * @param copy The instance to copy.
	 */
	public SortedList(SortedList<E> copy) {
		super();
		this.comparator = copy.comparator;
		this.addAll(copy);
	}
	

	/**
	 * {@inheritDoc}
	 */
	public SortedList<E> getClone() {
		return new SortedList<E>(this);
	}
	
	
	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	public boolean addAll(Collection<? extends E> c) {
		for (E element : c)
			this.add(element);
		
		return true;		
	}

	
	/**
	 * Get the current {@linkplain Comparator} instance.
	 * @return Returns the comparator.
	 */
	public Comparator<E> getComparator() {
		return comparator;
	}


	/**
	 * Set the {@linkplain Comparator} to use.
	 * @param comparator The comparator to set.
	 */
	public void setComparator(Comparator<E> comparator) {
		this.comparator = comparator;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Iterator<E> iterator() {
		return new DynamicIterator<E>(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ListIterator<E> listIterator() {
		return this.listIterator(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ListIterator<E> listIterator(int index) {
		return new DynamicIterator<E>(this, index);
	}
	
}
