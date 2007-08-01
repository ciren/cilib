/*
 * ParametricClusteringFunction.java
 * 
 * Created on July 18, 2007
 *
 * Copyright (C) 2003 - 2007
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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@linkplain ClusteringFitnessFunction} to calculate a parameterised fitness of a particular
 * clustering in the <tt>calculateFitness</tt> method. See:<br/>
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern Recognition
 *             and Image Processing", author = "Mahamed G.H. Omran", institution = "University Of
 *             Pretoria", school = "Computer Science", year = "2004", month = nov, pages = "105"
 *             address = "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht" }
 * @author Theuns Cloete
 */
public class ParametricClusteringFunction extends ClusteringFitnessFunction {
	private static final long serialVersionUID = 583965930447258179L;
	/** Specifies the weight that intra-cluster-distance will contribute to the final fitness */
	protected double w1 = 0.0;
	/** Specifies the weight that inter-cluster-distance will contribute to the final fitness */
	protected double w2 = 0.0;
	/** Stores the calculated zMax value */
	protected double zMax = -1.0;

	public ParametricClusteringFunction() {
		super();
		w1 = 0.5;
		w2 = 0.5;
		zMax = -1.0;
	}

	@Override
	public double calculateFitness() {
		// make sure the sum of the parameters equal 1.0
		if (w1 + w2 != 1.0)
			throw new IllegalArgumentException("The sum of w1 and w2 must equal 1.0");

		// zMax only needs to be calculated once; domain is not supposed to change during a simulation
		if (zMax < 0.0) {
			zMax = zMax();
		}

		return (w1 * calculateMaximumAverageDistance()) + (w2 * (zMax - calculateMinimumInterClusterDistance()));
	}

	/**
	 * Set the weight that the intra-cluster-distance will contribute to the final fitness
	 * @param w the weight to which w1 will be set
	 */
	public void setW1(double w) {
		w1 = w;
	}

	/**
	 * Set the weight that the inter-cluster-distance will contribute to the final fitness
	 * @param w the weight to which w2 will be set
	 */
	public void setW2(double w) {
		w2 = w;
	}

	/**
	 * Calculate the maximum distance possible between two {@linkplain Vector}s. In other words,
	 * calculate the distance between two {@linkplain Vector}s; where all the elements in the first
	 * {@linkplain Vector} is set to that element's upper bound and all the elements in the second
	 * {@linkplain Vector} is set to that element's lower bound.
	 * @return the maximum distance possible between two {@linkplain Vector}s
	 */
	protected double zMax() {
		// first, get the build representation from the domain
		Vector prototype = (Vector) ((DataSetBuilder)dataset).getProblem().getDomain().getBuiltRepresenation();
		Vector upper = prototype.clone();
		Vector lower = prototype.clone();

		// then, set the elements of these Vectors to the upper and lower bounds respectively
		for (int i = 0; i < upper.size(); i++) {
			upper.setReal(i, prototype.getNumeric(i).getUpperBound());
			lower.setReal(i, prototype.getNumeric(i).getLowerBound());
		}

		// lastly, calculate the distance between the two Vectors
		return dataset.calculateDistance(upper, lower);
	}
}
