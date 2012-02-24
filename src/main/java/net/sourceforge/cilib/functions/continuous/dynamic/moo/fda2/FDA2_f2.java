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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the FDA2 problem defined on page 428 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 * @author Marde Greeff
 */

public class FDA2_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 7814549850032093196L;

    //member
    ContinuousFunction fda2_g;
    ContinuousFunction fda2_h;
    FunctionMinimisationProblem fda2_g_problem;
    FunctionMinimisationProblem fda2_h_problem;

    //Domain("R(-1, 1)^31");


    /**
     * Sets the g function with a specified problem.
	 * @param problem FunctionMinimisationProblem used for the g function.
     */
    public void setFDA2_g(FunctionMinimisationProblem problem) {
        this.fda2_g_problem = problem;
        this.fda2_g = (ContinuousFunction)problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
	 * @return fda2_g_problem FunctionMinimisationProblem used for the g function.
     */
    public FunctionMinimisationProblem getFDA2_g_problem() {
        return this.fda2_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA2 problem without specifying the problem.
	 * @param fda2_g ContinuousFunction used for the g function.
     */
    public void setFDA2_g(ContinuousFunction fda2_g) {
        this.fda2_g = fda2_g;
    }

    /**
     * Returns the g function that is used in the FDA2 problem.
	 * @return fda2_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getFDA2_g() {
        return this.fda2_g;
    }

    /**
     * Sets the h function with a specified problem.
	 * @param problem FunctionMinimisationProblem used for the h function.
     */
    public void setFDA2_h(FunctionMinimisationProblem problem) {
        this.fda2_h_problem = problem;
        this.fda2_h = (ContinuousFunction)problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
	 * @return fda2_h_problem FunctionMinimisationProblem used for the h function.
     */
    public FunctionMinimisationProblem getFDA2_h_problem() {
        return this.fda2_h_problem;
    }

    /**
     * Sets the h function that is used in the FDA2 problem.
     * @param fda2_h ContinuousFunction used for the h function.
     */
    public void setFDA2_h(ContinuousFunction fda2_h) {
        this.fda2_h = fda2_h;
    }

    /**
     * Returns the h function that is used in the FDA2 problem.
     * @return fda2_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getFDA2_h() {
        return this.fda2_h;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return this.apply(iteration, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public double apply(int iteration, Vector x) {
        Vector y = x;
        if (x.size() > 1)
            y = x.copyOfRange(1, 16);

        double g = this.fda2_g.apply(y);
        double h = ((FDA2_h)this.fda2_h).apply(iteration, x);

        double value = g*h;
        return value;
    }
}