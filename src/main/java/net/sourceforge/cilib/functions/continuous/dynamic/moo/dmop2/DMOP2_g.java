/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dmop2;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the DMOP2 problem defined on page 119 in the following article:
 * C-K. Goh and K.C. Tan. A competitive-cooperative coevolutionary paradigm for dynamic multiobjective 
 * optimization, IEEE Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 * 
 * @author Marde Greeff
 */

public class DMOP2_g implements ContinuousFunction {

	private static final long serialVersionUID = -7743860912793353675L;
	
	//members
	//number of generations for which t remains fixed 
	private int tau_t;
	//generation counter
	private int tau;
	//number of distinct steps in t
	private int n_t;
	
	/**
	 * Creates a new instance of DMOP2_g.
	 */
	public DMOP2_g() {		
		//initialize the members
		this.tau_t =  5;
		this.tau = 1;
		this.n_t = 10;
	}
		
	/**
	 * Sets the iteration number.
	 * @param tau Iteration number.
	 */
	public void setTau(int tau) {
		this.tau = tau;
	}
	
	/**
	 * Returns the iteration number.
	 * @return tau Iteration number.
	 */
	public int getTau() {
		return this.tau;
	}
	
	/**
	 * Sets the frequency of change.
	 * @param tau Change frequency.
	 */
	public void setTau_t(int tau_t) {
		this.tau_t = tau_t;
	}
	
	/**
	 * Returns the frequency of change.
	 * @return tau_t Change frequency.
	 */
	public int getTau_t() {
		return this.tau_t;
	}
	
	/**
	 * Sets the severity of change.
	 * @param n_t Change severity.
	 */
	public void setN_t(int n_t) {
		this.n_t = n_t;
	}
	
	/**
	 * Returns the severity of change.
	 * @return n_t Change severity.
	 */
	public int getN_t() {
		return this.n_t;
	}
	
	/**
	 * Evaluates the function.
	 */
    @Override
	public Double apply(Vector x) {
		this.tau = AbstractAlgorithm.get().getIterations();
		return apply(this.tau, x);
	}
	
	/**
	 * Evaluates the function for a specific iteration.
	 */
	public Double apply(int iteration, Vector x) {
		double t = (1.0/(double)n_t)*Math.floor((double)iteration/(double)this.tau_t); 
		double G = Math.sin(0.5*Math.PI*t);
		
		double sum = 1.0;
		for (int k=0; k < x.size(); k++) {
			sum += Math.pow(x.doubleValueOf(k) - G, 2);
		}
		return sum;
	}
}
