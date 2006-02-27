/*
 * FunctionMaximisationProblem.java
 *
 * Created on June 24, 2003, 21:00 PM
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
package net.sourceforge.cilib.Problem;

public class FunctionMaximisationProblem
    extends FunctionMinimisationProblem
    implements OptimisationProblem {

    public FunctionMaximisationProblem() {
        super();
    }

    public double getFitness(double[] solution) {
        int d = getDimension();
        for (int i = 0; i < d; i++) {
            Domain domain = getDomain(i);
            if (solution[i] < domain.getLowerBound()
                || solution[i] > domain.getUpperBound()) {
                return Double.NEGATIVE_INFINITY;
            }
        }
        return getFunction().evaluate(solution);
    }

    /**
     * <p>
     * Returns the error for the given solution. That is, a lower error value
     * is returned if the given solution is a better maximiser for the function.
     * </p>
     * <p>
     * The lowest possible error (corresponding to the best solution) should be 0. However,
     * if the function incorrectly reports its maximum value then it is possible for error
     * values to be negative.
     * </p>
     *
     * @param The solution for which an error is saught.
     * @return The error.
     */
    public double getError(double[] solution) {
        return getFunction().getMaximum() - getFunction().evaluate(solution);
    }
}