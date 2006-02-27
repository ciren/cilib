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
package net.sourceforge.cilib.Container;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author gpampara
 */
public class Queue {
	private ArrayList queue;
	
	public Queue() {
		queue = new ArrayList();
	}
	
	/**
	 * Add the given <code>Object</code> to the Queue. This addition will occour at the
	 * back of the queue, due to the LIFO nature of the data structure 
	 * @param o The Object to be added to the queue
	 */
	public void enqueue(Object o) {
		queue.add(o); // To enqueue we add the object to the end of the list
	}
	
	/**
	 * This method removes an Object and returns the referance to the object to the
	 * callee.
	 * @return The Object of the object removed from the Queue
	 */
	public Object dequeue() {
		if (!queue.isEmpty())
			return queue.remove(0); // Remove the entry in the front of the list
		else return null;
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
	public boolean contains(Object arg0) {
		if (queue.contains(arg0))
			return true;
		else return false;
	}

	/**
	 * Returns an iterator to the queue
	 */
	public Iterator iterator() {
		return queue.iterator();
	}
}
