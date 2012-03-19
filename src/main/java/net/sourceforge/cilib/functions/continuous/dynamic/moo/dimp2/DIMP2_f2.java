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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dimp2;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

/**
 *  This function is the f2 function of the DIMP2 problem defined in the following paper:
 * W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien strategy for multiobjective
 * evolutionary algorithms in a fast changing environment, Memetic Computing, 2:87-110,
 * 2010.
 *
 * @author Marde Greeff
 */

public class DIMP2_f2 implements ContinuousFunction {

    
    //member
    ContinuousFunction dimp2_g;
    ContinuousFunction dimp2_h;
    FunctionMinimisationProblem dimp2_g_problem;
    FunctionMinimisationProblem dimp2_h_problem;

    //Domain("R(-1, 1)^20");
    

    /**
     * Sets the g function with a specified problem.
	 * @param problem FunctionMinimisationProblem used for the g function.
     */
    public void setDIMP2_g(FunctionMinimisationProblem problem) {
        this.dimp2_g_problem = problem;
        this.dimp2_g = (ContinuousFunction)problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
	 * @return dimp2_g_problem FunctionMinimisationProblem used for the g function.
     */
    public FunctionMinimisationProblem getDIMP2_g_problem() {
        return this.dimp2_g_problem;
    }

    /**
     * Sets the g function that is used in the DIMP2 problem without specifying the problem.
	 * @param dimp2_g ContinuousFunction used for the g function.
     */
    public void setDIMP2_g(ContinuousFunction dimp2_g) {
        this.dimp2_g = dimp2_g;
    }

    /**
     * Returns the g function that is used in the DIMP2 problem.
	 * @return dimp2_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getDIMP2_g() {
        return this.dimp2_g;
    }

    /**
     * Sets the h function with a specified problem.
	 * @param problem FunctionMinimisationProblem used for the h function.
     */
    public void setDIMP2_h(FunctionMinimisationProblem problem) {
        this.dimp2_h_problem = problem;
        this.dimp2_h = (ContinuousFunction)problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
	 * @return dimp2_h_problem FunctionMinimisationProblem used for the h function.
     */
    public FunctionMinimisationProblem getDIMP2_h_problem() {
        return this.dimp2_h_problem;
    }

    /**
     * Sets the h function that is used in the DIMP2 problem without specifying the problem.
	 * @param dimp2_h ContinuousFunction used for the h function.
     */
    public void setDIMP2_h(ContinuousFunction dimp2_h) {
        this.dimp2_h = dimp2_h;
    }

    /**
     * Sets the f1 hunction that is used in the DIMP2 problem without specifying the problem.
	 * @param dimp2_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getDIMP2_h() {
        return this.dimp2_h;
    }

    /**
     * Evaluates the function.
     * g*h
     */
    @Override
    public Double apply(Vector input) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return apply(iteration, input);
    }

    /**
     * Evaluates the function for a specific iteration.
     * g*h
     */
    public Double apply(int iteration, Vector input) {
        Vector y = input.copyOfRange(1, input.size());
        double g = ((DIMP2_g)this.dimp2_g).apply(iteration, y);
        double h = ((DIMP2_h)this.dimp2_h).apply(iteration, input);

        double value = g*h;

        return value;
    }
}