/*
 * Vertex.java
 * 
 * Created on Mar 25, 2004
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
package net.sourceforge.cilib.Container.Graph;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class represents a <code>Vertex</code> as defined in a <code>Graph</code> in modern Graph theory.
 * The vertex has incident and eminating edges.
 * 
 * @author Gary Pampara
 */
public class Vertex {
	private int degree; 
	private Object identity;
	private ArrayList inEdge;
	private ArrayList outEdge;
	
	/**
	 * Constructor. Performs no real functionality and assigns all data members to null\	 *
	 */
	public Vertex() {
		this.identity = null;
		inEdge = null;
		outEdge = null;
	}
	
	/**
	 * Constructor that specifies a value for the <code>Vertex</code> identity
	 * @param identity The object that will give the vertex an identity
	 */
	public Vertex(Object identity) {
		this.identity = identity;
		inEdge = new ArrayList();
		outEdge = new ArrayList();
	}
	
	/**
	 * Add an <code>Edge</code> to the eminaining edge set of the <code>Vertex</code>
	 * @param e The <code>Edge</code> to be added
	 */
	public void addEminatingEdge(Edge e) {
		outEdge.add(e);
		degree++;
	}
	
	/**
	 * Add an <code>Edge</code> to the incident edge set of the <code>Vertex</code>
	 * @param e The <code>Edge</code> to be added
	 */
	public void addIncidentEdge(Edge e) {
		inEdge.add(e);
		degree++;
	}
	
	/**
	 * Get the identity of this <code>Vertex</code>
	 * @return An <code>Object</code> representing the <code>Vertex</code> identity
	 */
	public Object getIdentity() {
		return identity;
	}
	
	/**
	 * Get an iterator that can cycle through all the incident edges on this <code>Vertex</code>
	 * @return An iterator for all the incident edges
	 */
	public ListIterator getIncidentEdges() {
		return inEdge.listIterator();
	}
	
	/**
	 * Get an iterator that can cycle through all the eminating edges on this <code>Vertex</code>
	 * @return An iterator for all the eminating edges
	 */
	public ListIterator getEmanatingEdges() {
		return outEdge.listIterator();
	}
	
	/**
	 * Get the degree associated with this vertex
	 * @return The degree of the vertex
	 */
	public int getDegree() {
		return degree;
	}
	
	/**
	 * Returns the number of eminating edges from this <code>vertex</code>
	 * @return The number of eminating edges from the <code>Vertex</code>
	 */
	public int getEminatingEdgeCount() {
		return outEdge.size();
	}
	
	/**
	 * Returns the number of incident edges on this <code>Vertex</code> 
	 * @return The number of incident edges on this <code>Vertex</code>
	 */
	public int getIncidentEdgeCount() {
		return inEdge.size();
	}
	
	public String toString() {
		return "Vertex(" + identity.toString() + ")";
	}
}