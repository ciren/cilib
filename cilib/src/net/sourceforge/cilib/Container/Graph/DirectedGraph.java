/*
 * DiGraph.java
 *
 * Created on May 31, 2004
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
package net.sourceforge.cilib.Container.Graph;

import java.util.ListIterator;

import net.sourceforge.cilib.Container.Queue;
import net.sourceforge.cilib.Container.Visitor;

/**
 * An implementation of a Directed Graph data structure
 * 
 * @author gpampara
 */
public class DirectedGraph extends Graph {

	/**
	 * Constructor to create a Direceted Graph class
	 * @param name The unique name to be give to the Vertex
	 */
	public DirectedGraph(String name) {
		super(name);
	}
	
	public void addEdge(Edge e) {
		if (e == null)
			throw new GraphException("Cannot add a null edge to the graph");
		
		edgeSet.add(e);
		e.addVertexInformation(false); // Add the Directed Edge
	}
	
	public void topologicalOrderTraversal(Visitor visitor) {
		int degree [] = new int[getVertexCount()];
		for (int i = 0; i < getVertexCount(); i++)
			degree[i] = 0;
		
		for (ListIterator<Edge> l = getEdges(); l.hasNext(); ) {
			Edge edge = l.next();
			degree[indexOf(edge.getSecondVertex())]++;
		}
		
		Queue<Vertex> queue = new Queue<Vertex>();
		for (int i = 0; i < getVertexCount(); i++)
			if (degree[i] == 0)
				queue.enqueue(getVertexAt(i));
			
		while (!queue.isEmpty() && !visitor.isDone()) {
			Vertex vertex = queue.dequeue();
			visitor.visit(vertex);
			
			for (ListIterator<Edge> l = vertex.getEmanatingEdges(); l.hasNext(); ) {
				Edge edge = l.next();
				Vertex tmpVertex = edge.getSecondVertex();
				
				if (--degree[indexOf(tmpVertex)] == 0)
					queue.add(tmpVertex);
			}
		}
	}
}
