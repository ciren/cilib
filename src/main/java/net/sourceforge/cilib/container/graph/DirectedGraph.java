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
package net.sourceforge.cilib.container.graph;

import java.util.ListIterator;

import net.sourceforge.cilib.container.Queue;
import net.sourceforge.cilib.container.visitor.Visitor;

/**
 * An implementation of a Directed Graph data structure.
 * 
 * @author Gary Pampara
 * @param <V> The {@code Vertex} type.
 * @param <E> The {@code Edge} type.
 * @deprecated This class is no longer used.
 */
@Deprecated
public class DirectedGraph<V extends Vertex, E extends Edge> extends Graph<V, E> {

	/**
	 * Constructor to create a Direceted Graph class.
	 * @param name The unique name to be give to the Vertex.
	 */
	public DirectedGraph(String name) {
		super(name);
	}
	
	//public void addEdge(Edge e) {
	public void addEdge(E e) {
		if (e == null)
			throw new RuntimeException("Cannot add a null edge to the graph");
		
		edgeSet.add(e);
		e.addVertexInformation(false); // Add the Directed Edge
	}
	
	public void topologicalOrderTraversal(Visitor<Vertex> visitor) {
		int [] degree = new int[getVertexCount()];
		for (int i = 0; i < getVertexCount(); i++)
			degree[i] = 0;
		
		ListIterator<E> edgeIterator = getEdges();
		while (edgeIterator.hasNext()) {
			Edge edge = edgeIterator.next();
			degree[indexOf(edge.getSecondVertex())]++;
		}
		
		Queue<Vertex> queue = new Queue<Vertex>();
		for (int i = 0; i < getVertexCount(); i++)
			if (degree[i] == 0)
				queue.enqueue(getVertexAt(i));
			
		while (!queue.isEmpty() && !visitor.isDone()) {
			Vertex vertex = queue.dequeue();
			visitor.visit(vertex);
			
			for (ListIterator<Edge> l = vertex.getEmanatingEdges(); l.hasNext();) {
				Edge edge = l.next();
				Vertex tmpVertex = edge.getSecondVertex();
				
				if (--degree[indexOf(tmpVertex)] == 0)
					queue.add(tmpVertex);
			}
		}
	}
}
