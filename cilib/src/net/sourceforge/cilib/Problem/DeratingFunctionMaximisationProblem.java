/*
 * DeratingFunctionMaximisationProblem.java
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

import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.Functions.Function;
import net.sourceforge.cilib.Functions.MaximumDeratingFunction1;

public class DeratingFunctionMaximisationProblem
    extends FunctionMaximisationProblem
    implements OptimisationProblem {
    private double solutionThreshold = Double.POSITIVE_INFINITY;
    private Vector vectorSolutions = new Vector();
    private Function deratingFunction = new MaximumDeratingFunction1();

    public DeratingFunctionMaximisationProblem() {
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
        // set the initial fitness to the actual fitness value without modification.
        double fitness = getFunction().evaluate(solution);

        // iterate through the number of solutions found that are above the solution
        // threshold and modify the fitness using the derating function.
        Iterator iterator = vectorSolutions.iterator();
        while (iterator.hasNext()) {
            // calculate the distance between the solution and the previousely found
            // solution.
            // get the solution object from the iterator.
            Double[] d_solution = (Double[]) iterator.next();

            // convert the object into a double array that we can use.
            double[] t_solution = new double[d_solution.length];
            for (int i = 0; i < d_solution.length; i++) {
                t_solution[i] = d_solution[i].doubleValue();
            }
            // calcaulate the distance between the solution and the previousely found
            // solution.
            double distance = distance(solution, t_solution);

            // normalise the distance in the range [0..1].
            distance = normalise(distance);

            // inorder to evaluate the derating function the distance needs to be
            // a array.
            double[] dist = { distance };

            // modify the fitness.
            fitness = fitness * getDeratingFunction().evaluate(dist);
        }

        // determine if the individuals fitness is better than the solution threshold.
        if (fitness > solutionThreshold) {
            // add the position of the solution to the vector of solutions.
            // because the solutions are stored in a Vector, the solution needs to be
            // converted into a object of Double[].
            Double[] t_solution = new Double[solution.length];
            for (int i = 0; i < t_solution.length; i++) {
                // create memory for the dimesion.
                t_solution[i] = new Double(solution[i]);
            }
            // add the solution object to the solution to the vector of solution.
            vectorSolutions.add(t_solution);
        }

        return fitness;
    }

    public Function getDeratingFunction() {
        return deratingFunction;
    }

    public void setDeratingFunction(Function deratingFunction) {
        this.deratingFunction = deratingFunction;
    }

    public double normalise(double distance) {
        // as the distance is not expected out of the functions range
        // the maximum expected distance will be from the lower bound to
        // the upper bound of the function.
        double min = getFunction().getDomain().getLowerBound();
        double max = getFunction().getDomain().getUpperBound();

        // calculate the maximum distance.
        double max_distance = Math.abs(min - max);

        // normalise the distace in the range [0..1].
        return distance / max_distance;
    }

    public double distance(double[] d1, double[] d2) {
        double distance = 0.0;
        for (int i = 0; i < d1.length; i++) {
            // calculate the sum of the difference between each of the dimensions
            distance += Math.pow(d1[i] - d2[i], 2.0);
        }
        // return the square root of the sum.
        return Math.sqrt(distance);
    }

    public void setSolutionThreshold(double solutionThreshold) {
        this.solutionThreshold = solutionThreshold;
    }

    public double getSolutionThreshold() {
        return solutionThreshold;
    }
}
