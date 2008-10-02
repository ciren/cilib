/**
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GraphTest {
	
	@Test
	public void creation() {
		Graph<Double> graph = new StandardGraph<Double>();
		
		graph.add(2.0);
		graph.add(1.0);
		graph.add(3.0);
		
		graph.addEdge(1.0, 2.0);
		
		assertEquals(1, graph.edges());
		
		graph.remove(1.0);
		
		assertEquals(0, graph.edges());
	}
	
	@Test
	public void numberOfEgdesInGraph() {
		Graph<Double> g = new StandardGraph<Double>();
		
		g.add(1.0);
		g.add(2.0);
		g.add(3.0);
		g.add(4.0);
		
		g.addEdge(1.0, 2.0);
		g.addEdge(2.0, 3.0);
		g.addEdge(3.0, 4.0);
		g.addEdge(2.0, 4.0);
		g.addEdge(1.0, 4.0);
		
		assertEquals(5, g.edges());
	}
	
	@Test
	public void numberOfVerticies() {
		Graph<Double> g = new StandardGraph<Double>();
		g.add(1.0);
		g.add(2.0);
		g.add(3.0);
		g.add(4.0);
		g.add(5.0);
		
		assertEquals(5, g.vertices());
	}
	
	@Test
	public void connections() {
		Graph<Double> g = new StandardGraph<Double>();
		g.add(1.0);
		g.add(2.0);
		g.add(4.0);
	}
	
	@Test
	public void connectVertecies() {
		Graph<Double> g = new StandardGraph<Double>();
		g.add(1.0);
		g.add(2.0);
		
		assertFalse(g.isConnected(1.0, 2.0));
		
		g.addEdge(1.0, 2.0);

		assertTrue(g.isConnected(1.0, 2.0));
	}
	
	@Test
	public void simpleDistanceToVertex() {
		StandardGraph<Double> g = new StandardGraph<Double>();
		g.add(1.0);
		g.add(2.0);
		g.addEdge(1.0, 2.0);
		
		assertEquals(1.0, g.distance(1.0, 2.0), 9);
	}

}
