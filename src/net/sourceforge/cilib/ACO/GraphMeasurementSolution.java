/*
 * GraphMeasurementSolution.java
 *
 * Created on Jun 11, 2004
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

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Container.Graph.Edge;
import net.sourceforge.cilib.Measurement.Measurement;

/**
 * @author gpampara
 */
public class GraphMeasurementSolution implements Measurement {
	public String getDomain() {
		return "T";
	}
	
	public Object getValue() {
		System.out.println("Printing result");
		ACO aco = (ACO) Algorithm.get();
		TSPProblem problem = (TSPProblem) aco.getDiscreteOptimisationProblem();
		ArrayList<Edge> shortestPath = (ArrayList<Edge>) problem.getBestSolution();
		shortestPath.trimToSize();
				
		StringBuffer result = new StringBuffer();
		
		result.append("Number of edges: "+ shortestPath.size() + "\n\n");
		System.out.println("Number of edges: " + shortestPath.size());
		
		for (ListIterator<Edge> e = shortestPath.listIterator(); e.hasNext(); ) {
			Edge l = e.next();
			//System.out.println(l.toString());
			result.append(l.toString()+"\n");
		}
		
		result.append("\n\n\n\n");
		
		return result.toString();
	}
}
