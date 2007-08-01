/*
 * ParametricWithQuantisationErrorFunction.java
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

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@linkplain ClusteringFitnessFunction) to calculate an improved parameterised fitness of a
 * particular clustering in the <tt>calculateFitness</tt> method. See:<br/>
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern Recognition
 *             and Image Processing", author = "Mahamed G.H. Omran", institution = "University Of
 *             Pretoria", school = "Computer Science", year = "2004", month = nov, pages = "114 &
 *             115" address = "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht" }
 * @author Theuns Cloete
 */
public class ParametricWithQuantisationErrorFunction extends ParametricClusteringFunction {
	private static final long serialVersionUID = -2022785065235231801L;

	/** Specifies the weight that the Quantisation Error will contribute to the final fitness */
	protected double w3 = 0.0;

	public ParametricWithQuantisationErrorFunction() {
		super();
		w1 = 0.3;
		w2 = 0.3;
		w3 = 0.4;
	}

	@Override
	public double calculateFitness() {
		// make sure the sum of the parameters equal 1.0
		if (w1 + w2 + w3 != 1.0)
			throw new IllegalArgumentException("The sum of w1, w2 and w3 must equal 1.0");

		// zMax only needs to be calculated once; domain is not supposed to change during a simulation
		if (zMax < 0.0) {
			zMax = zMax();
		}

		return (w1 * calculateMaximumAverageDistance()) + (w2 * (zMax - calculateMinimumInterClusterDistance())) + (w3 * calculateQuantisationError());
	}

	/**
	 * Set the weight that the Quantisation Error will contribute to the final fitness
	 * @param w the weight to which w3 will be set
	 */
	public void setW3(double w) {
		w3 = w;
	}
}
