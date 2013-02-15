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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.hef3;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the adapted F3 problem defined in the
 * following paper: H. Li and Q. Zhang. Multiobjective optimization problems
 * with complicated Pareto sets, MOEA/D and NSGA-II, IEEE Transactions on
 * Evolutionary Computation, 13(2):284-302, 2009.
 *
 * The problem has been adapted by Helbig and Engelbrecht to make it a DMOOP.
 *
 * @author Marde Greeff
 */
public class HEF3_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 6369118486095689078L;
    //member
    ContinuousFunction hef3_g;
    ContinuousFunction hef3_h;
    FunctionOptimisationProblem hef3_g_problem;
    FunctionOptimisationProblem hef3_h_problem;

    //Domain("R(-1, 1)^20")
    
    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setHEF3_g(FunctionOptimisationProblem problem) {
        this.hef3_g_problem = problem;
        this.hef3_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return hef3_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getHEF3_g_problem() {
        return this.hef3_g_problem;
    }

    /**
     * Sets the g function that is used in the HEF3 problem without specifying
     * the problem.
     * @param hef3_g ContinuousFunction used for the g function.
     */
    public void setHEF3_g(ContinuousFunction hef3_g) {
        this.hef3_g = hef3_g;
    }

    /**
     * Returns the g function that is used in the HEF3 problem.
     * @return hef3_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getHEF3_g() {
        return this.hef3_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setHEF3_h(FunctionOptimisationProblem problem) {
        this.hef3_h_problem = problem;
        this.hef3_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return hef3_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getHEF3_h_problem() {
        return this.hef3_h_problem;
    }

    /**
     * Sets the h function that is used in the HEF3 problem without specifying
     * the problem.
     * @param hef3_h ContinuousFunction used for the h function.
     */
    public void setHEF3_h(ContinuousFunction hef3_h) {
        this.hef3_h = hef3_h;
    }

    /**
     * Sets the f1 function that is used in the HEF3 problem without specifying
     * the problem.
     * @param hef3_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getHEF3_h() {
        return this.hef3_h;
    }

    /**
     * Evaluates the function. g*h
     */
    @Override
    public Double apply(Vector input) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return apply(iteration, input);
    }

    /**
     * Evaluates the function for a specific iteration. g*h
     */
    public Double apply(int iteration, Vector input) {
        double g = ((HEF3_g) this.hef3_g).apply(input);
        double h = ((HEF3_h) this.hef3_h).apply(iteration, input);

        double value = g * h;

        return value;
    }
}