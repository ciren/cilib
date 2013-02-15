/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dmop3;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the DMOP3 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 * @author Marde Greeff
 */
public class DMOP3_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 1534041495354849624L;
    //member
    ContinuousFunction dmop3_g;
    ContinuousFunction dmop3_h;
    FunctionOptimisationProblem dmop3_g_problem;
    FunctionOptimisationProblem dmop3_h_problem;

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setDMOP3_g(FunctionOptimisationProblem problem) {
        this.dmop3_g_problem = problem;
        this.dmop3_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return dmop3_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getDMOP3_g_problem() {
        return this.dmop3_g_problem;
    }

    /**
     * Sets the g function that is used in the DMOP3 problem without specifying
     * the problem.
     * @param dmop3_g ContinuousFunction used for the g function.
     */
    public void setDMOP3_g(ContinuousFunction dmop3_g) {
        this.dmop3_g = dmop3_g;
    }

    /**
     * Returns the g function that is used in the DMOP3 problem.
     * @return dmop3_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getDMOP3_g() {
        return this.dmop3_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setDMOP3_h(FunctionOptimisationProblem problem) {
        this.dmop3_h_problem = problem;
        this.dmop3_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return dmop3_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getDMOP3_h_problem() {
        return this.dmop3_h_problem;
    }

    /**
     * Sets the h function that is used in the DMOP3 problem.
     * @param dmop3_h ContinuousFunction used for the h function.
     */
    public void setDMOP3_h(ContinuousFunction dmop3_h) {
        this.dmop3_h = dmop3_h;
    }

    /**
     * Returns the h function that is used in the DMOP3 problem.
     * @return dmop3_h Function used for the h function.
     */
    public ContinuousFunction getDMOP3_h() {
        return this.dmop3_h;
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
    public Double apply(int iteration, Vector x) {
        double g = ((DMOP3_g) this.dmop3_g).apply(iteration, x);
        double h = ((DMOP3_h) this.dmop3_h).apply(iteration, x);

        double value = g * h;

        return value;
    }
}
