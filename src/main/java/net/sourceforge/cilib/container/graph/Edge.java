/*
 * Edge.java
 * 
 * Created on Mar 25, 2004
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
package net.sourceforge.cilib.container.graph;

import net.sourceforge.cilib.util.Cloneable;


/**
 * This class represents an <code>edge</code> connecting two <code>Vertex</code> nodes in a Graph
 * The Edge can be a uni-directional and well as a bi-directional edge.
 * @author Gary Pampara
 */
public class Edge implements Cloneable {
	private double weight;
	private double cost;
	private Vertex firstVertex;  // This is the vertex that starts the edge
	private Vertex secondVertex; // This is the vertex the ends the edge
	
	/**
	 * <code>Edge</code> constructor to create an edge given a starting and ending <code>Vertex</code>
	 * 
	 * @param startVertex The Vertex objec the Edge starts at for a connection
	 * @param endVertex The Vertex objec the Edge ends at for a connection
	 */
	public Edge(Vertex startVertex, Vertex endVertex) {
		cost = 1.0;
		weight = 1.0;
		this.firstVertex = startVertex;
		this.secondVertex = endVertex;
	}
	
	/**
	 * <code>Edge</code> constructor to create an edge given a starting and ending <code>Vertex</code> given
	 * a value for cost of this <code>Edge</code>
	 * 
	 * @param startVertex The Vertex object the Edge starts at for a connection
	 * @param endVertex The Vertex object the Edge ends at for a connection
	 * @param cost The cost associated with this <code>Edge</code>
	 */
	public Edge(Vertex startVertex, Vertex endVertex, double cost) {
		this.firstVertex = startVertex;
		this.secondVertex = endVertex;
		this.cost = cost;
		this.weight = 1.0;
	}
	
	/**
	 * <code>Edge</code> constructor to create an edge given a starting and ending <code>Vertex</code> given
	 * a value for cost of this <code>Edge</code>
	 * 
	 * @param startVertex The Vertex object the Edge starts at for a connection
	 * @param endVertex The Vertex object the Edge ends at for a connection
	 * @param cost The cost associated with this <code>Edge</code>
	 * @param weight The weight associated with this <code>Edge</code>
	 */
	public Edge(Vertex startVertex, Vertex endVertex, double cost, double weight) {
		this.firstVertex = startVertex;
		this.secondVertex = endVertex;
		this.cost = cost;
		System.out.println("Received Weight: " + weight);
		this.weight = weight;
	}
	
	/**
	 * Add the needed information about the vertex to the Vertices. This call is implicitly handled by the
	 * Graph and DirectedGraph with addEdge(Edge)
	 * @see net.sourceforge.cilib.container.graph.Graph#addEdge
	 * @see net.sourceforge.cilib.container.graph.DirectedGraph#addEdge
	 * @param bidirectional It <code>true</code> the edge is an edge on an undirected graph, if <code>false</code>
	 *                      it is the edge on a directed graph.
	 */
	public void addVertexInformation(boolean bidirectional) {
		if (bidirectional) {
			firstVertex.addEminatingEdge(this);
			secondVertex.addIncidentEdge(this);
			firstVertex.addIncidentEdge(this);
			secondVertex.addEminatingEdge(this);
		}
		else {
			firstVertex.addEminatingEdge(this);
			secondVertex.addIncidentEdge(this);
		}
	}
	
	/**
	 * Get the "cost" of the relavant edge. Based on the application, this 
	 * could be travelling costs, etc...
	 * @return related cost of this edge
	 */
	public double getCost() {
		return cost;
	}
	
	/**
	 * Get the vertex this edge's begining point is attached to
	 * @return Referance to the edges's starting vertex
	 */
	public Vertex getFirstVertex() {
		return firstVertex;
	}
	
	/**
	 * Get the vertex this edge's ending point is attached to
	 * @return Referance to the edge's ending vertex 
	 */
	public Vertex getSecondVertex() {
		return secondVertex;
	}
	
	/**
	 * Given the Vertex <code>v</code> associated with this <code>Edge</code>, return the "partner" Vertex
	 * of the Edge. This is the other vertex associated with this edge, given one of ther verticies.
	 * @param v The <code>Vertex</code> for the edge to return it's partner <code>Vertex</code>
	 * @return A Vertex which is the partner vertex of <code>v</code>
	 */
	public Vertex getOtherAssociatedVertex(Vertex v) {
		if (v == getFirstVertex())
			return getSecondVertex(); // The first vertex we have, return the second
		else return getFirstVertex(); // We have the second Vertex, return the first
	}
	
	/**
	 * Set the weight to be associated with this Edge object
	 * @param weight The weight to be assigned
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * Get the weight associated with this egde object
	 * @return The weight associated with this edge object
	 */
	public double getWeight() {
		return weight;
	}
	
	
	public Object getClone() {
		Edge clone = null;
		
		try {
			clone = (Edge) super.clone();
			clone.cost = this.cost;
			clone.weight = this.weight;
			clone.firstVertex = this.firstVertex;
			clone.secondVertex = this.secondVertex;
			
			System.out.println("Clone: " + clone.weight);
			System.out.println("orig: " + weight);
		}
		catch (CloneNotSupportedException c) {
			c.printStackTrace();
		}
		
		return clone;
	}
	
	
	/**
	 * Return a <code>String</code> representation of the the associated <code>Edge</code> object
	 * @return The String representing the <code>Edge</code>
	 */
	public String toString() {
		return firstVertex.toString() + "->" + secondVertex.toString() + " => cost: " + cost + " weight: " + weight;
	}
}
