/*
 * GraphTest.java
 * JUnit based test
 *
 * Created on January 04, 2005, 4:45 PM
 *
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
 *   
 */

package net.sourceforge.cilib.Container;

import net.sourceforge.cilib.Container.Graph.Graph;
import net.sourceforge.cilib.Container.Graph.Vertex;
import net.sourceforge.cilib.Container.Graph.Edge;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * This Unit test tests all the needed operations of the Queue container class.
 *
 * @author gpampara
 */
public class GraphTest extends TestCase {

	public GraphTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(GraphTest.class);

		return suite;
	}

	public void setUp() {
	}

	public void testGraphCreation() {
		Graph g = new Graph();
		assertNotNull(g);
	}

	public void testGraphAddVerticies() {
		Graph g = new Graph();
		g.addVertex(new Vertex(1));
		g.addVertex(new Vertex(2));

		assertEquals(2, g.getVertexCount());
	}

	public void testGraphAddEdges() {
		Graph g = new Graph();
		Vertex v1 = new Vertex(1);
		Vertex v2 = new Vertex(2);

		Edge e = new Edge(v1, v2);

		g.addVertex(v1);
		g.addVertex(v2);
		g.addEdge(e);

		assertEquals(2, g.getVertexCount());
		assertEquals(1, g.getEdgeCount());
	}

	public void testVertexSelection() {
		Graph g = new Graph();
		Vertex v1 = new Vertex(1);
		Vertex v2 = new Vertex(2);

		g.addVertex(v1);
		g.addVertex(v2);

		assertSame(v1, g.getVertexAt(0));
		assertSame(v2, g.getVertexAt(1));
	}

	public void testEdgeSelection() {
		Graph g = new Graph();
		Vertex v1 = new Vertex(1);
		Vertex v2 = new Vertex(2);

		g.addVertex(v1);
		g.addVertex(v2);

		Edge e = new Edge(v1, v2);

		g.addEdge(e);

		assertSame(e, g.getEdgeBetweenVerticies(v1, v2));
	}
}
