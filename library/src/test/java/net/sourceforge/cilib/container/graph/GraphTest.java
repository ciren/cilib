/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.container.graph;

import org.junit.Test;




/**
 * This Unit test tests all the needed operations of the Queue container class.
 *
 */
public class GraphTest {

    public GraphTest() {
    }

    @Test
    public void testGraphCreation() {
    //    Graph<Vertex, Edge> g = new Graph<Vertex, Edge>();
        //assertNotNull(g);
    }
/*
    @Test
    public void testGraphAddVerticies() {
        Graph<Vertex, Edge> g = new Graph<Vertex, Edge>();
        g.addVertex(new Vertex(1));
        g.addVertex(new Vertex(2));

        assertEquals(2, g.getVertexCount());
    }

    @Test
    public void testGraphAddEdges() {
        Graph<Vertex, Edge> g = new Graph<Vertex, Edge>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        Edge e = new Edge(v1, v2);

        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(e);

        assertEquals(2, g.getVertexCount());
        assertEquals(1, g.getEdgeCount());
    }

    @Test
    public void testVertexSelection() {
        Graph<Vertex, Edge> g = new Graph<Vertex, Edge>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addVertex(v1);
        g.addVertex(v2);

        assertSame(v1, g.getVertexAt(0));
        assertSame(v2, g.getVertexAt(1));
    }

    @Test
    public void testEdgeSelection() {
        Graph<Vertex, Edge> g = new Graph<Vertex, Edge>();
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        g.addVertex(v1);
        g.addVertex(v2);

        Edge e = new Edge(v1, v2);

        g.addEdge(e);

        assertSame(e, g.getEdgeBetweenVerticies(v1, v2));
    }
    */
}
