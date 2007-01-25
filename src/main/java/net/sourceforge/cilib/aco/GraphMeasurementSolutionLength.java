/*
 * GraphMeasurement.java
 *
 * Created on Jun 11, 2004
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
 */
package net.sourceforge.cilib.aco;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * Measurement to return the length of a solution based on some <code>Graph</code> data structure
 * 
 * @author Gary Pampara
 */
public class GraphMeasurementSolutionLength implements Measurement {
	
	public GraphMeasurementSolutionLength(GraphMeasurementSolutionLength copy) {
	}
	
	public GraphMeasurementSolutionLength clone() {
		return new GraphMeasurementSolutionLength(this);
	}

	/**
	 * Get the type of Domain
	 * @return A <code>String</code> representing the type of the domain 
	 */
	public String getDomain() {
		return "R";
	}
	
	/**
	 * Get the value of the path length of the solution from a TSP-like solution
	 * @returns The result referenced as an <code>Object</code>
	 */
	public Type getValue() {	
		/*ASTSP a = (ASTSP) Algorithm.get();
		TSPProblem problem = (TSPProblem) a.getDiscreteOptimisationProblem();
		return new Double(problem.getBestSolutionLength());*/
		
		ASTSP a = (ASTSP) Algorithm.get();
		TSPProblem problem = (TSPProblem) a.getOptimisationProblem();
		return new Real(problem.getBestSolutionLength());
	}

}
