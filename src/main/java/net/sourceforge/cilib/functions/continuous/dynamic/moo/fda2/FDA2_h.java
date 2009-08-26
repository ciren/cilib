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

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

/**
 * This function is the h function of the FDA2 problem defined on page 429 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 * @author Marde Greeff
 */

public class FDA2_h extends ContinuousFunction {

    private static final long serialVersionUID = -637862405309737323L;

    //members
    ContinuousFunction fda2_f;
    ContinuousFunction fda2_g;
    FunctionMinimisationProblem fda2_f_problem;
    FunctionMinimisationProblem fda2_g_problem;

    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;

    /**
     * Default constructor
     */
    public FDA2_h() {
        super();
        setDomain("R(-1, 1)^31"); //verander hier
        //initialize the members
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 10;
    }

    /**
     * copy constructor
     * @param copy
     */
    public FDA2_h(FDA2_h copy) {
        super(copy);
        this.tau = copy.tau;
        this.tau_t = copy.tau_t;
        this.n_t = copy.n_t;
        this.fda2_f = copy.fda2_f;
        this.fda2_f_problem = copy.fda2_f_problem;
        this.fda2_g = copy.fda2_g;
        this.fda2_g_problem = copy.fda2_g_problem;
    }

    /**
     * return a clone
     */
    public FDA2_h getClone() {
        return new FDA2_h(this);
    }

    /**
     * Sets the f function used with FDA2 problem
     * @param problem
     */
    public void setFDA2_f(FunctionMinimisationProblem problem) {
        this.fda2_f_problem = problem;
        this.fda2_f = (ContinuousFunction)problem.getFunction();
        this.setDomain(fda2_f.getDomainRegistry().getDomainString());
    }

    /**
     * Returns the problem used to set the f function
     * @return
     */
    public FunctionMinimisationProblem getFDA2_f_problem() {
        return this.fda2_f_problem;
    }

    /**
     * sets the f function that is used the FDA2 function
     * @param fda1_f
     */
    public void setFDA2_f(ContinuousFunction fda2_f) {
        this.fda2_f = fda2_f;
        this.setDomain(fda2_f.getDomainRegistry().getDomainString());
    }

    /**
     * Returns the f function that is used in the FDA2 function
     * @return
     */
    public ContinuousFunction getFDA2_f() {
        return this.fda2_f;
    }

    /**
     * Sets the g function used in FDA2 problem
     * @param problem
     */
    public void setFDA2_g(FunctionMinimisationProblem problem) {
        this.fda2_g_problem = problem;
        this.fda2_g = (ContinuousFunction)problem.getFunction();
        this.setDomain(fda2_g.getDomainRegistry().getDomainString());
    }

    /**
     * Returns function used to set g function
     * @return
     */
    public FunctionMinimisationProblem getFDA2_g_problem() {
        return this.fda2_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA2 function
     * @param fda1_g
     */
    public void setFDA2_g(ContinuousFunction fda2_g) {
        this.fda2_g = fda2_g;
        this.setDomain(fda2_g.getDomainRegistry().getDomainString());
    }

    /**
     * Returns the g function that is used in the FDA2 function
     * @return
     */
    public ContinuousFunction getFDA2_g() {
        return this.fda2_g;
    }

    /**
     * sets the iteration number
     * @param tau
     */
    public void setTau(int tau) {
        this.tau = tau;
    }

    /**
     * returns the iteration number
     * @return tau
     */
    public int getTau() {
        return this.tau;
    }

    /**
     * sets the frequency of change
     * @param tau
     */
    public void setTau_t(int tau_t) {
        this.tau_t = tau_t;
    }

    /**
     * returns the frequency of change
     * @return tau_t
     */
    public int getTau_t() {
        return this.tau_t;
    }

    /**
     * sets the severity of change
     * @param n_t
     */
    public void setN_t(int n_t) {
        this.n_t = n_t;
    }

    /**
     * returns the severity of change
     * @return n_t
     */
    public int getN_t() {
        return this.n_t;
    }

    /**
     * Evaluates the function
     * h(X_III, f_1, g) = 1-(f_1/g)^(H(t) + sum(x_i-H(t))^2)^(-1)
     */
    @Override
    public Double evaluate(Vector input) {
        this.tau = AbstractAlgorithm.get().getIterations();

        double t = (1.0/(double)n_t)*Math.floor((double)this.tau/(double)this.tau_t);
        double H = 0.75 + 0.7*(Math.sin(0.5*Math.PI*t));

        Vector xI = input;
        Vector xII = input;
        Vector xIII = input;
        if (input.getDimension() > 1) {
            xI = input.subList(0, 0);
            xII = input.subList(1, 15);
            xIII = input.subList(16, input.getDimension()-1);
        }

        double f = this.fda2_f.evaluate(xI);
        double g = this.fda2_g.evaluate(xII);

        double value = 1.0;
        double power = H;

        for (int k=0; k < xIII.getDimension(); k++) {
            power += Math.pow(xIII.getReal(k) - H, 2);
        }

        power = Math.pow(power, -1);
        double f_div_g = f / g;
        value -= Math.pow(f_div_g, power);

        return value;
    }
}
