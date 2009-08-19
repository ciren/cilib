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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the FDA1 problem defined on page 428 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 * @author Marde Greeff
 */

public class FDA1_g extends ContinuousFunction {

    private static final long serialVersionUID = 1721209032942724811L;

    //members
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;

    /**
     * Default constructor
     */
    public FDA1_g() {
        super();
        setDomain("R(-1, 1)^19");
        //initialize the members
        this.tau_t =  5;
        this.tau = 1;
        this.n_t = 10;
    }

    /**
     * Copy constructor
     * @param copy
     */
    public FDA1_g(FDA1_g copy){
        super(copy);
        this.setDomain(copy.getDomain());
        this.tau_t = copy.tau_t;
        this.tau = copy.tau;
        this.n_t = copy.n_t;
    }

    /**
     * return a clone
     */
    public FDA1_g getClone() {
        return new FDA1_g(this);
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
     * f(XII) = 1 + sum ( x_i - G(t))^2
     */
    @Override
    public Double evaluate(Vector input) {
        this.tau = AbstractAlgorithm.get().getIterations();

        double t = (1.0/(double)n_t)*Math.floor((double)this.tau/(double)this.tau_t);
        double G = Math.sin(0.5*Math.PI*t);

        double sum = 1.0;
        for (int k=0; k < input.getDimension(); k++) {
            sum += Math.pow(input.getReal(k) - G, 2);
        }

        return sum;
    }
}
