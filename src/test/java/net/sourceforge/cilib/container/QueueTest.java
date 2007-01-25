/*
 * QueueTest.java
 * JUnit based test
 *
 * Created on January 04, 2005, 4:45 PM
 *
 * 
 * Copyright (C) 2003 - 2006 
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

package net.sourceforge.cilib.container;

import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.*;

import net.sourceforge.cilib.container.Queue;



/**
 * This Unit test tests all the needed operations of the Queue container class.
 *
 * @author Gary Pampara
 */
public class QueueTest {

	public QueueTest() {
	}

	@Test
	public void testQueueCreation() {
		Queue<Double> q = new Queue<Double>();
		assertNotNull(q);
	}

	@Test
	public void testRemoveEmptyQueue() {
		Queue<Double> q = new Queue<Double>();
		Double result1 = q.remove();
		Double result2 = q.dequeue();

		assertNull(result1);
		assertNull(result2);
	}

	@Test
	public void testAddingElement() {
		Queue<Double> q = new Queue<Double>();

		Double tmp = new Double(5.0);
		q.enqueue(tmp);
		q.add(tmp);

		assertEquals(2, q.size());
	}

	@Test
	public void testClearQueue() {
		Queue<Double> q = new Queue<Double>();

		q.add(new Double(4.0));
		q.clear();

		assertEquals(true, q.isEmpty());
	}
	
	@Test
	@SuppressWarnings("unused")
	public void concurrentIteratorTest() {
		Queue<Double> q = new Queue<Double>();
		q.add(4.0);
		q.add(3.0);
		q.add(2.0);
		q.add(1.0);
		
		for (Iterator<Double> i = q.iterator(); i.hasNext(); ) {
			double d = i.next();
		}
		
		assertEquals(4, q.size());
		
		for (Iterator<Double> i = q.iterator(); i.hasNext(); ) {
			double d = i.next();
			
			for (Iterator<Double> j = q.iterator(); j.hasNext(); ) {
				double dj = j.next();
				j.remove();
			}
		}
		
		assertEquals(0, q.size());
	}
}
