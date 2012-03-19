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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dimp1;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

/**
 * This function is the h function of the DIMP1 problem defined in the following paper:
 * W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien strategy for multiobjective
 * evolutionary algorithms in a fast changing environment, Memetic Computing, 2:87-110,
 * 2010.
 *
 * @author Marde Greeff
 */

public class DIMP1_h implements ContinuousFunction {

    
    //members
    private ContinuousFunction dimp1_g;
    private ContinuousFunction dimp1_f1;
    private FunctionMinimisationProblem dimp1_f1_problem;
    private FunctionMinimisationProblem dimp1_g_problem;

    //Domain("R(-1, 1)^20");

    /**
     * Sets the g function with a specified problem.
	 * @param problem FunctionMinimisationProblem used for the g function.
     */
    public void setDIMP1_g(FunctionMinimisationProblem problem) {
        this.dimp1_g_problem = problem;
        this.dimp1_g = (ContinuousFunction)problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
	 * @return dimp1_g_problem FunctionMinimisationProblem used for the g function.
     */
    public FunctionMinimisationProblem getDIMP1_g_problem() {
        return this.dimp1_g_problem;
    }

    /**
     * Sets the g function that is used in the DIMP1 problem without specifying the problem.
	 * @param dimp1_g ContinuousFunction used for the g function.
     */
    public void setDIMP1_g(ContinuousFunction dimp1_g) {
        this.dimp1_g = dimp1_g;
    }

    /**
     * Returns the g function that is used in the DIMP1 problem.
	 * @return dimp1_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getDIMP1_g() {
        return this.dimp1_g;
    }

    /**
     * Sets the f1 function with a specified problem.
	 * @param problem FunctionMinimisationProblem used for the f1 function.
     */
    public void setDIMP1_f(FunctionMinimisationProblem problem) {
        this.dimp1_f1_problem = problem;
        this.dimp1_f1 = (ContinuousFunction)problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
	 * @return dimp1_f1_problem FunctionMinimisationProblem used for the f1 function.
     */
    public FunctionMinimisationProblem getDIMP1_f_problem() {
        return this.dimp1_f1_problem;
    }

    /**
     * Sets the f1 function that is used in the DIMP1 problem without specifying the problem.
	 * @param dimp1_f ContinuousFunction used for the f1 function.
     */
    public void setDIMP1_f(ContinuousFunction dimp1_f1) {
        this.dimp1_f1 = dimp1_f1;
    }

    /**
     * Returns the f1 function that is used in the DIMP1 problem.
	 * @return dimp1_f1 ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getDIMP1_f() {
        return this.dimp1_f1;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return apply(iteration, x);
    }
    
    /**
     * Evaluates the function for a specific iteration.
     */
    public Double apply(int iteration, Vector x) {

        //only the first element
        Vector y = x.copyOfRange(0, 1);
        //all the elements except the first element
        Vector z = x.copyOfRange(1, x.size());
        //evaluate the dimp1_g function
        double g = ((DIMP1_g)this.dimp1_g).apply(iteration, z);
        //evaluate the dimp1_f1 function
        double f1 = this.dimp1_f1.apply(y);

        double value = 1.0;
        value -= Math.pow((double)f1 / (double)g, 2);

        return value;
    }

}
