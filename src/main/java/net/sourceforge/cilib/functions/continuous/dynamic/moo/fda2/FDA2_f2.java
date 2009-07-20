/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g*h function of the FDA2 problem defined on page 428 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 * @author Marde Greeff
 */

public class FDA2_f2 extends ContinuousFunction {

    private static final long serialVersionUID = 7814549850032093196L;

    //member
    ContinuousFunction fda2_g;
    ContinuousFunction fda2_h;
    FunctionMinimisationProblem fda2_g_problem;
    FunctionMinimisationProblem fda2_h_problem;

    /**
     * Default constructor
     */
    public FDA2_f2() {
        super();
        setDomain("R(-1, 1)^31"); //verander hier
    }

    /**
     * copy constructor
     * @param copy
     */
    public FDA2_f2(FDA2_f2 copy) {
        super(copy);
        this.fda2_g = copy.fda2_g;
        this.fda2_g_problem = copy.fda2_g_problem;
        this.fda2_h = copy.fda2_h;
        this.fda2_h_problem = copy.getFDA2_h_problem();
    }

    /**
     * return a clone
     */
    public FDA2_f2 getClone() {
        return new FDA2_f2(this);
    }

    /**
     * Sets the g function
     * @param problem
     */
    public void setFDA2_g(FunctionMinimisationProblem problem) {
        this.fda2_g_problem = problem;
        this.fda2_g = (ContinuousFunction)problem.getFunction();
        this.fda2_g.setDomain(fda2_g.getDomainRegistry().getDomainString());
    }

    /**
     * returns the problem used to set the g function
     * @return
     */
    public FunctionMinimisationProblem getFDA2_g_problem() {
        return this.fda2_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA2 problem
     * @param fda1_g
     */
    public void setFDA2_g(ContinuousFunction fda2_g) {
        this.fda2_g = fda2_g;
        this.setDomain(fda2_g.getDomainRegistry().getDomainString());
    }

    /**
     * Returns the g function that is used in the FDA2 problem
     * @return
     */
    public ContinuousFunction getFDA2_g() {
        return this.fda2_g;
    }

    /**
     * Sets the h function
     * @param problem
     */
    public void setFDA2_h(FunctionMinimisationProblem problem) {
        this.fda2_h_problem = problem;
        this.fda2_h = (ContinuousFunction)problem.getFunction();
        this.fda2_h.setDomain(fda2_h.getDomainRegistry().getDomainString());
    }

    /**
     * returns the problem used to set the h function
     * @return
     */
    public FunctionMinimisationProblem getFDA2_h_problem() {
        return this.fda2_h_problem;
    }

    /**
     * Sets the f1 function that is used in the FDA2 problem
     * @param fda1_f1
     */
    public void setFDA2_h(ContinuousFunction fda2_h) {
        this.fda2_h = fda2_h;
        this.setDomain(fda2_h.getDomainRegistry().getDomainString());
    }

    /**
     * Gets the f1 function that is used in the FDA2 problem
     * @return
     */
    public ContinuousFunction getFDA2_h() {
        return this.fda2_h;
    }

    /**
     * Evaluates the function
     * g*h
     */
    public Double evaluate(Vector input) {
        Vector y = input;
        if (input.getDimension() > 1)
            y = input.subList(1, fda2_g.getDimension()); //-1
        double g = this.fda2_g.evaluate(y);
        double h = this.fda2_h.evaluate(input);

        double value = g*h;
        return value;
    }
}