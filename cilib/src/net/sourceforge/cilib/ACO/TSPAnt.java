/*
 * TSPAntDecorator.java
 *
 * Created on Jul 25, 2004
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
package net.sourceforge.cilib.ACO;

import java.util.ArrayList;
import java.util.ListIterator;

import net.sourceforge.cilib.Container.Graph.Edge;
import net.sourceforge.cilib.Container.Graph.Graph;
import net.sourceforge.cilib.Container.Graph.Vertex;

/**
 * @author gpampara
 */
public class TSPAnt extends Ant {
	private Vertex currentVertex = null;
	private ArrayList tabuList = null;
	private ArrayList currentTour = null;
	private double currentTourLength = -1.0;
	
	public TSPAnt(Object identity) {
		super(identity);
		tabuList = new ArrayList();
		currentTour = new ArrayList();
	}
	
	public TSPAnt(Object identity, Vertex currentNode) {
		super(identity);
		this.currentVertex = currentNode;
		tabuList = new ArrayList();
		currentTour = new ArrayList();
	}
	
	public void setCurrentNode(Vertex v) {
		currentVertex = v;
	}
	
	public Vertex getCurrentNode() {
		return currentVertex;
	}
	
	public void setTabuList(Graph graph) {
		for (ListIterator l = graph.getVertices(); l.hasNext(); )
			tabuList.add((Vertex) l.next());
		
		tabuList.remove(currentVertex);
	}
	
	public ArrayList getTabuList() {
		return tabuList;
	}
	
	public void removeFromTabuList(Vertex v) {
		if (tabuList.contains(v)) {
			tabuList.remove(v);
		}
	}
	
	public ArrayList getCurrentTour() {
		return currentTour;
	}
	
	public double getCurrentTourLength() {
		if (currentTourLength > -1)
		  return currentTourLength;
		else throw new ACOException("Error - the currrentTourLengthmust be calculated first!");
	}
	
	public void addToCurrentTour(Edge e) {
	  currentTour.add(e);
	}
	
	public void emptyCurrentTour() {
		tabuList.clear();
		currentTour.clear();
		currentTourLength = -1.0;
	}
	
	public void calculateCurrentTourLength() {
		currentTourLength = 0.0;
		for (ListIterator l = currentTour.listIterator(); l.hasNext(); ) {
			Edge edge = (Edge) l.next();
			currentTourLength += edge.getCost();
		}
	}

	public String toString() {
		return "TSPAntDecorator: " + identity.toString() + " currentVertex: " + currentVertex;
	}
}
