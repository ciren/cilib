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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda4;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

/**
 * This function is the fk function of the FDA4 problem defined on page 430 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations 
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442
 * 
 * @author Marde Greeff
 */

public class FDA4_fk implements ContinuousFunction {

	private static final long serialVersionUID = -7426719045212043996L;
	
	//members
	FunctionMinimisationProblem fda4_g_problem;
	ContinuousFunction fda4_g;
	int M; 
	int n;
	int K;
	
	/**
	 * Creates a new instance of FDA4_k.
	 */
	public FDA4_fk() {
		this.K = 2;
		this.M = 3;
		this.n = this.M + 9;
	}
	
	
	/**
	 * Sets the number of objectives.
	 * @param m Number of objectives.
	 */
	public void setM(int m) {
		this.M = m;
		this.n = this.M + 9;
	}
	
	/**
	 * Returns the number of objectives.
	 * @return M Number of objectives.
	 */
	public int getM() {
		return this.M;
	}
	
	/**
	 * Sets the number of variables.
	 * @param n Number of variables.
	 */
	public void setN(int n) {
		this.n = n;
		this.M = this.n - 9;
	}
	
	/**
	 * Returns the number of variables.
	 * @return n Number of variables.
	 */
	public int getN() {
		return this.n;
	}
	
	/**
	 * Sets the k value of the objective.
	 * @param k Objective number.
	 */
	public void setK(int k) {
		this.K = k;
	}
	
	/**
	 * Returns the k value of the objective.
	 * @return K Objective number.
	 */
	public int getK() {
		return this.K;
	}
	
	/**
	 * Sets the g function with a specified problem.
	 * @param problem FunctionMinimisationProblem used for the g function.
	 */
	public void setFDA4_g(FunctionMinimisationProblem problem) {
		this.fda4_g_problem = problem;
		this.fda4_g = (ContinuousFunction)problem.getFunction();
	}
	
	/**
	 * Returns the problem used to set the g function.
	 * @return fda4_g_problem FunctionMinimisationProblem used for the g function.
	 */
	public FunctionMinimisationProblem getFDA4_g_problem() {
		return this.fda4_g_problem;
	}
	
	/**
	 * Sets the g function that is used in the FDA4 problem without specifying the problem.
	 * @param fda4_g ContinuousFunction used for the g function.
	 */
	public void setFDA4_g(ContinuousFunction g) {
		this.fda4_g = g;
	}
	
	/**
	 * Returns the g function that is used in the FDA4 problem.
	 * @return fda4_g ContinuousFunction used for the g function.
	 */
	public ContinuousFunction getFDA4_g() {
		return this.fda4_g;
	}
	
	/**
	 * Evaluates the function.
	 */
    @Override
	public Double apply(Vector x) {
        int iteration  = AbstractAlgorithm.get().getIterations();
		return this.apply(iteration, x);
	}
	
	/**
	 * Evaluates the function for a specific iteration.
	 */
	public double apply(int iteration, Vector x) {
		Vector y = x;
		
		if (x.size() > 1)
			y = x.copyOfRange(this.M-1, this.n);
		
		double g = ((FDA4_g)this.fda4_g).apply(iteration, y);
		double value = 1.0 + g;
		double mult = 1.0;
                
		for (int i=1; i <= (this.M-this.K); i++) {
			mult *= Math.cos(x.doubleValueOf(i-1)*Math.PI/2.0);                        
		}
		
		mult *= Math.sin(x.doubleValueOf(this.M-this.K)*Math.PI/2.0);
		mult *= value;
                
		return mult;
	}
}
