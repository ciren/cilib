/*
 * Queue.java
 *
 * Created on Jun 23, 2004
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
package net.sourceforge.cilib.container;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Gary Pampara
 */
public class Queue<E> { 
	
	private LinkedList<E> queue;
		
	public Queue() {
		this.queue = new LinkedList<E>();
	}
	
	/**
	 * TODO: What about cloning the internal objects?
	 * @param copy
	 */
	public Queue(Queue<E> copy) {
		this.queue = new LinkedList<E>();
		
		for (Iterator<E> i = copy.iterator(); i.hasNext(); ) {
			this.queue.add(i.next());
		}
		
	}
	
	
	public Queue clone() {
		return new Queue<E>(this);
	}
	
	/**
	 * Add the given <code>Object</code> to the Queue. This addition will occour at the
	 * back of the queue, due to the LIFO nature of the data structure 
	 * @param o The Object to be added to the queue
	 */
	public void enqueue(E o) {
		queue.add(o); // To enqueue we add the object to the end of the list
	}
	
	public void add(E o) {
		enqueue(o);
	}
	
	/**
	 * This method removes an Object and returns the referance to the object to the
	 * callee.
	 * @return The Object of the object removed from the Queue
	 */
	public E dequeue() {
		if (!queue.isEmpty())
			return queue.remove(0); // Remove the entry in the front of the list
		else return null;
	}
	
	public E remove() {
		return dequeue();
	}

	/**
	 * Returns the current size of the Queue
	 * @returns The size of the queue container
	 */
	public int size() {
		return queue.size();
	}

	/**
	 * Clears all the elements currently contained inside the Queue
	 */
	public void clear() {
		queue.clear();
	}

	/**
	 * Tests if the queue is empty or not
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * Test if the current Queue contains the desired element.
	 * @return true If the object is contained
	 * @return false If the object is not contained 
	 */
	public boolean contains(E object) {
		return queue.contains(object);
	}

	/**
	 * Returns an iterator to the queue
	 */
	public Iterator<E> iterator() {
		return queue.listIterator();
	}
}
