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

import java.util.ArrayList;
import java.util.ListIterator;

import net.sourceforge.cilib.container.Queue;
import net.sourceforge.cilib.container.visitor.PrePostVisitor;
import net.sourceforge.cilib.container.visitor.Visitor;


/**
 * <p>
 * An implementation of a Acyclic Graph Data structure.
 *  
 * @author Gary Pampara
 * @param <V> v.
 * @param <E> e.
 * @deprecated This implementation is no longer valid. Please look at {@link StandardGraph}
 */
@Deprecated
public class Graph<V extends Vertex, E extends Edge> {
	protected ArrayList<V> vertexSet;
	protected ArrayList<E> edgeSet;
	protected String name;
	
	/**
	 * Create a <code>Graph</code> data structure without a name.
	 */
	public Graph() {
		this.name = null;
		vertexSet = new ArrayList<V>();
		edgeSet = new ArrayList<E>();
	}
	
	/**
	 * Create a <code>Graph</code> data structure and associate it with the given name.
	 * @param name The name to be given to this <code>Graph</code>.
	 */
	public Graph(String name) {
		this.name = name;
		vertexSet = new ArrayList<V>();
		edgeSet = new ArrayList<E>();
	}
	
	/**
	 * Add a <code>Vertex</code> to the current <code>Graph</code>.
	 * @param v The Vertex to be inserted into the Graph.
	 */
	public void addVertex(V v) {
		for (V currentVertex : vertexSet) {
			if (v.getIdentity() == currentVertex.getIdentity())
				throw new RuntimeException("Error cannont insert vertex into graph - Unique Identity already exists");
		}
		
		vertexSet.add(v);
	}
	
	/**
	 * Add an edge to the <code>Graph</code>.
	 * @param e The edge to be added to the <code>Graph</code>.
	 */
	public void addEdge(E e) {
		if (e == null)
			throw new RuntimeException("Cannot add a null edge to a graph");
		
		edgeSet.add(e);
		e.addVertexInformation(true); // Add the bidirectional edge
	}
	
	/**
	 * Get the associated name of the <code>Graph</code>, if any.
	 * @return A <code>String</code> containing the name or <code>null</code> if there is no name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the number of verticies contained in the <code>Graph</code>.
	 * @return The number of verticies.
	 */
	public int getVertexCount() {
		return vertexSet.size();
	}
	
	/**
	 * Get the number of edges currently in the <code>Graph</code>.
	 * @return The number of edges in the <code>Graph</code>.
	 */
	public int getEdgeCount() {
		return edgeSet.size();
	}
	
	/**
	 * Get the <code>Vertex</code> at a specific position in the list based on the index.
	 * @param index The index at which to return the Vertex.
	 * @return A referance to the Vertex object at the required index.
	 */
	public Vertex getVertexAt(int index) {
		if (index > vertexSet.size()) 
			throw new RuntimeException("Invalid index for Graph Vertex selection: " + index);
		
		return vertexSet.get(index);
	}
	
	/**
	 * Get the index of the vertex as contained in the internal container.
	 * This value will always be between 0 and getNumberOfVerticies().
	 * This is an accessor method to give the same functionality as operator[].
	 * 
	 * @param v The <code>Vertex</code> to have it's index returned.
	 * @return The index of the <code>Vertex</code> in the internal container.
	 */
	public int indexOf(Vertex v) {
		return vertexSet.indexOf(v);
	}
	
	/**
	 * Get an <code>Iterator</code> to the edge set.
	 * @return An <code>Iterator</code> to the edge set.
	 */
	public ListIterator<E> getEdges() {
		return edgeSet.listIterator(0);
	}
	
	/**
	 * Get the associated edge between two verticies.
	 * @param v1 A vertex to reference the edge from.
	 * @param v2 A vertex to reference the edge to.
	 * @return The <code>Edge</code> created by the two verticies v1 and v2.
	 */
	public Edge getEdgeBetweenVerticies(Vertex v1, Vertex v2) {
		Edge returnEdge = null;
		
		for (ListIterator<Edge> l = v1.getEmanatingEdges(); l.hasNext();) {
			Edge edge = l.next();
			if (edge.getOtherAssociatedVertex(v1) == v2 && 
					edge.getOtherAssociatedVertex(v2) == v1)
				returnEdge = edge;
		}
		return returnEdge;
	}
	
	/**
	 * Get an iterator to the verticies contained within the graph.
	 * @return A <code>ListIterator</code> starting at index 0.
	 */
	public ListIterator<V> getVertices() {
		return vertexSet.listIterator(0);
	}
	
	/**
	 * Get an iterator to the verticies contained within the <code>Graph</code>.
	 * @param index The position to start the interator.
	 * @return A <code>ListIterator</code> starting at the specified index.
	 */
	public ListIterator<V> getVertices(int index) {
		return vertexSet.listIterator(index);
	}	
	
	/**
	 * A traversal of the <code>Graph</code> starting at a specific <code>Vertex</code>.
	 * @param visitor The <code>PrePostVisitor</code> to apply to the traversal.
	 * @param startVertex The <code>Vertex</code> to start the traversal at.
	 */
	public void depthFirstTraversal(PrePostVisitor<Vertex> visitor, Vertex startVertex) {
		boolean [] visited = new boolean[getVertexCount()];
		for (int i = 0; i < visited.length; i++)
			visited[i] = false;
		
		depthFirstTraversal(visitor, startVertex, visited);
	}
		
	/**
	 * This is the internal private traversal method which is applied recursuvely. This method
	 * was created in order to facilitate the recursive nature of the depth-first search or
	 * traversal algorithm.
	 * 
	 * Please use the method depthFirstTraversal(PrePostVisitor, Vertex) to perform the algorithm.
	 * 
	 * @param visitor The <code>PrePostVisitor</code> to apply to this traversal.
	 * @param startVertex The <code>Vertex</code> to start the traversal from.
	 * @param visited The array defining if the specific vertex has already been visited.
	 */
	private void depthFirstTraversal(PrePostVisitor<Vertex> visitor, Vertex vertex, boolean [] visited) {
		if (visitor.isDone())
			return;
		
		visitor.preVisit(vertex);
		visited[indexOf(vertex)] = true; // Get the index and make the associated array index have value true
		
		ListIterator<Edge> l = vertex.getEmanatingEdges();
		while (l.hasNext()) {
			Edge edge = l.next();
			Vertex oppositeVertexOfEdge = edge.getOtherAssociatedVertex(vertex);
			
			if (!visited[indexOf(oppositeVertexOfEdge)]) {
				depthFirstTraversal(visitor, oppositeVertexOfEdge, visited);
			}
		}
		
		visitor.postVisit(vertex);
	}
	
	/**
	 * Performs a breadth first traversal on the graph. At each iteration, the visit() method of
	 * the Vistor is applied to the currently traversing <code>Vertex</code>.
	 * @param startVertex The vertex to start the traversal from.
	 */
	public void breadthFirstTraversal(Visitor<Vertex> visitor, Vertex startVertex) {
		boolean [] enqueued = new boolean[getVertexCount()];
		for (int i = 0; i < getVertexCount(); i++)
			enqueued[i] = false;
		
		Queue<Vertex> queue = new Queue<Vertex>();
		queue.enqueue(startVertex);
		enqueued[indexOf(startVertex)] = true;
		
		while (!queue.isEmpty() && !visitor.isDone()) {
			Vertex vertex = queue.dequeue();
			visitor.visit(vertex);
			ListIterator<Edge> l = vertex.getEmanatingEdges();
			while (l.hasNext()) {
				Edge edge = l.next();
				Vertex tmpVertex = edge.getOtherAssociatedVertex(vertex);
				
				if (!enqueued[indexOf(tmpVertex)]) {
					enqueued[indexOf(tmpVertex)] = true;
					queue.enqueue(tmpVertex);
				}
			}
		}
	}
	
	/**
	 * Print a summary of the <code>Graph</code> structure. The summary is 2 fold. First the summary is based on
	 * incident edges and then the summary is based on eminating edges.
	 */
	public void printGraphSummary() {
		System.out.println(name + ":");

		if (vertexSet.size() == 0 | edgeSet.size() == 0)
			System.out.println("  contains no vertices or edges.");
		else { 
			System.out.println("  # of vertices: " + vertexSet.size());
			System.out.println("  # of edges: " + edgeSet.size());

			System.out.println("  Vertices and incident edges:");
			ListIterator<V> l = vertexSet.listIterator(0);
			while (l.hasNext()) {
				Vertex v = (Vertex) l.next();
				System.out.print("\t" + v.toString() + ":");
				ListIterator<Edge> e = v.getIncidentEdges();
				while (e.hasNext()) {
					System.out.print(" " + e.next().toString() + "\n\t  ");
				}
				System.out.println();
			}
			
			System.out.println("  Vertices and out-going edges:");
			ListIterator<V> m = vertexSet.listIterator(0);
			while (m.hasNext()) {
				V v = m.next();
				System.out.print("\t" + v.toString());
				ListIterator<Edge> e = v.getEmanatingEdges();
				while (e.hasNext()) {
					System.out.print("   " + e.next().toString() + "\n\t  ");
				}
				System.out.println();
			}
		}	
	}
}
