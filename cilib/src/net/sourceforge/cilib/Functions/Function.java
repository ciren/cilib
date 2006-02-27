/*
 * Function.java
 *
 * Created on January 11, 2003, 1:36 PM
 *
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 *
 */

package net.sourceforge.cilib.Functions;

import java.io.Serializable;

import net.sourceforge.cilib.Problem.Domain;

/**
 * All functions should inherit from <code>Function</code>
 *
 * @author  espeer
 */
public abstract class Function implements Serializable {

    /**
     * @param dimension The number of dimensions of the function.
     * @param domain The domain for the function.
     * @param minimum The minimum of the function.
     * @param maximum The maximum of the function.
     */
    protected Function(int dimension, Domain domain, double minimum, double maximum)
    {
        this.dimension = dimension;
        this.domain = domain;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Accessor for the number of dimensions of the function
     *
     * @return The function dimension/
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Sets the number of dimensions for the function.
     *
     * @param dimension The function dimension.
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Accessor for the domain of the function. Currently, all dimensions
     * are assumed to have the same domain.
     *
     * @returns The search domain.
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * Sets the domain of the function. Currently, all dimensions are assumed to
     * have the same domain.
     *
     * @param domain The function domain.
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    /**
     * Accessor for the function minimum. This is the minimum value of the function
     * in the given domain.
     *
     * @return The minimum function value.
     */
    public double getMinimum() {
        return minimum;
    }

    /**
     * Edited by C. Naicker for the implementation of FunctionMaximisationProblem.
     * @param maximum The maximum of the function.
     *
     */
    public void setMaximum(double maximum) {
      this.maximum = maximum;
    }

    /**
     * @return The maximum of the function. This needs to be set by the user.
     *
     */
    public double getMaximum() {
      return maximum;
    }

    /**
     * Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     */
    public abstract double evaluate(double[] x);

    protected int dimension;
    protected Domain domain;
    protected double minimum;
    protected double maximum;
}
