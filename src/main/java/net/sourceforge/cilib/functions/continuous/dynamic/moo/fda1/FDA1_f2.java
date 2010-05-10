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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

/**
 * This function is the g*h function of the FDA1 problem defined on page 428 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 * R(-1, 1)^20
 *
 * @author Marde Greeff
 */
public class FDA1_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 6369118486095689078L;
    //member
    private Function<Vector, ? extends Number> fda1_g;
    private Function<Vector, ? extends Number> fda1_h;
    private FunctionMinimisationProblem fda1_g_problem;
    private FunctionMinimisationProblem fda1_h_problem;

    /**
     * Default constructor
     */
    public FDA1_f2() {
        super();
    }

    /**
     * Copy constructor.
     * @param copy
     */
    @SuppressWarnings("unchecked")
    public FDA1_f2(FDA1_f2 copy) {
//        this.setDomain(copy.getDomain());
        this.setFDA1_g(copy.getFDA1_g());
        this.setFDA1_g(copy.getFDA1_g_problem());
        this.setFDA1_h(copy.getFDA1_h());
        this.setFDA1_h(copy.getFDA1_h_problem());
    }

    /**
     * Returns a clone
     */
    @Override
    public FDA1_f2 getClone() {
        return new FDA1_f2(this);
    }

    /**
     * Sets the g function
     * @param problem
     */
    public void setFDA1_g(FunctionMinimisationProblem problem) {
        this.fda1_g_problem = problem;
        this.fda1_g = problem.getFunction();
//        this.fda1_g.setDomain(fda1_g.getDomainRegistry().getDomainString());
    }

    /**
     * returns the problem used to set the g function
     * @return
     */
    public FunctionMinimisationProblem getFDA1_g_problem() {
        return this.fda1_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA1 problem
     * @param fda1_g
     */
    public void setFDA1_g(Function<Vector, ? extends Number> fda1_g) {
        this.fda1_g = fda1_g;
//        this.setDomain(fda1_g.getDomainRegistry().getDomainString());
    }

    /**
     * Returns the g function that is used in the FDA1 problem
     * @return
     */
    public Function getFDA1_g() {
        return this.fda1_g;
    }

    /**
     * Sets the h function
     * @param problem
     */
    public void setFDA1_h(FunctionMinimisationProblem problem) {
        this.fda1_h_problem = problem;
        this.fda1_h = problem.getFunction();
//        this.fda1_h.setDomain(fda1_h.getDomainRegistry().getDomainString());
    }

    /**
     * returns the problem used to set the h function
     * @return
     */
    public FunctionMinimisationProblem getFDA1_h_problem() {
        return this.fda1_h_problem;
    }

    /**
     * Sets the f1 function that is used in the FDA1 problem
     * @param fda1_h
     */
    public void setFDA1_h(Function<Vector, ? extends Number> fda1_h) {
        this.fda1_h = fda1_h;
//        this.setDomain(fda1_h.getDomainRegistry().getDomainString());
    }

    /**
     * Gets the f1 function that is used in the FDA1 problem
     * @return
     */
    public Function getFDA1_h() {
        return this.fda1_h;
    }

    /**
     * Evaluates the function
     * g*h
     */
    @Override
    public Double apply(Vector input) {
        Vector y = input.copyOfRange(1, input.size());
        double g = this.fda1_g.apply(y).doubleValue();
        double h = this.fda1_h.apply(input).doubleValue();
        return g * h;
    }
}
