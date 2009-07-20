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
package net.sourceforge.cilib.problem;

//TODO: Add domain validators to check that this is working on ContinuousFunctions

import java.util.Iterator;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.MaximumDeratingFunction1;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * <p>Title: DeratingFunctionMaximisationProblem </p>
 * <p>Description: This class provides a way to modify the search
 * space such that a solution could be removed from the search
 * space and a new solution can be searched for.
 * </p>
 * <p>
 * The interested reader is referred to:
 * Beasley, D., Bull, D. R. & Martin R. R. (1993).
 * A Sequential Niche Technique for Multimodal Function Optimization.
 * Evolutionary Computation 1(2), MIT Press, pp.101-125.
 * </p>
 * @author Clive Naicker
 */
public class DeratingFunctionMaximisationProblem extends FunctionMaximisationProblem {
    private static final long serialVersionUID = 6513411928705015979L;

    /**
     * This vector contains all the solutions (or positions) in the search
     * space that will be modified using the deratingFunction.
     */
    private java.util.Vector<Double[]> vectorSolutions = new java.util.Vector<Double[]>();
    /**
     * This is the Derating Function that will be used to modify the
     * search at every position that is in the vectorSolutions
     */
    private ContinuousFunction deratingFunction = new MaximumDeratingFunction1();
    /**
     * This hashtable is more convenience rather than a necessity.
     * It used to add a solution to the vectorSolutions only if the
     * solution is not already in the hashtable.
     */
//    private Hashtable hashtableSolutions = new Hashtable();
    /**
     * This is used to calculate the Euclidean distance between the
     * solution that is current being calculated and a solution in the
     * vectorSolutions
     * @see #calculateFitness(net.sourceforge.cilib.type.types.Type)
     */
    private DistanceMeasure distanceMeasure;

    /**
     * The default constuctor.  Intialises the distanceMeasure to
     * the EuclideanDistanceMeasure.
     */
    public DeratingFunctionMaximisationProblem() {
        super();

        distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Calculates the fitness of a solution with respect to the
     * modifications to the search space.
     * @param solution The solution to calculate the fitness.
     * @return The fitness of the solution.
     */
    protected Fitness calculateFitness(Type solution) {
        // set the initial fitness to the actual fitness value without modification.
        double fitness = (super.calculateFitness(solution).getValue()).doubleValue();
        double radius = ((MaximumDeratingFunction1) deratingFunction).getRadius();

        // iterate through the number of solutions found that are above the solution
        // threshold and modify the fitness using the derating function.
        Iterator<Double[]> iterator = vectorSolutions.iterator();
        while (iterator.hasNext()) {
            // calculate the distance between the solution and the previousely found
            // solution.
            Double[] distanceSolution = iterator.next();

            // convert the object into a double array that we can use.
            /*double[] t_solution = new double[d_solution.length];
            for (int i = 0; i < d_solution.length; i++) {
                t_solution[i] = d_solution[i].doubleValue();
            }*/
            Vector tSolution = new Vector(distanceSolution.length);
            for (int i = 0; i < distanceSolution.length; i++) {
                tSolution.set(i, new Real(distanceSolution[i].doubleValue()));
            }

            // calcaulate the distance between the solution and the previousely found
            // solution.
            //double distance = distanceMeasure.distance((double[]) solution, t_solution);
            double distance = distanceMeasure.distance((Vector) solution, tSolution);

            // normalise the distance in the range [0..1].
            distance = normalise(distance);

            // inorder to evaluate the derating function the distance needs to be
            // a array.
            Vector dist = new Vector();
            dist.add(new Real(distance));

            // modify the fitness.
            if (distance < radius) {
                fitness = fitness * getDeratingFunction().evaluate(dist);
            }
        }
        return new MaximisationFitness(new Double(fitness));
    }

    /**
     * This method finds the fitness of the solution without applying the
     * derating function.
     * @param solution The solution to find the fitness off.
     * @return The fitness of the solution[]
     */
    public double getRawFitness(Type solution) {
        return (super.calculateFitness(solution).getValue()).doubleValue();
    }

    /**
     * This method will add a solution to the vectorSolutions.  The
     * solution added will be used to create depression in the search
     * using the deratingFunction.
     * @param solution The position in the search to modify.
     */
    public void addSolution(double[] solution) {
        // add the position of the solution to the vector of solutions.
        // because the solutions are stored in a Vector, the solution needs to be
        // converted into a object of Double[].
        Double[] tSolution = new Double[solution.length];
        for (int i = 0; i < tSolution.length; i++) {
            // create memory for the dimesion.
            tSolution[i] = new Double(solution[i]);
        }
        // add the solution object to the solution to the vector of solution.
        vectorSolutions.add(tSolution);
    }

    /**
     * This is a utility method that can be used to only unique
     * solutions.
     * @param key The key into the hashtable
     * @param solution The solution to add
     */
    /*
    public void addSolution(Object key, double[] solution) {
        if (!hashtableSolutions.containsKey(key)) {
            addSolution(solution);
        }
        else {
            hashtableSolutions.put(key, solution);
            addSolution(solution);
        }
    }
    */

    /**
     * This method gets the function that is used to modify the
     * search space, i.e. the derating function
     * @return The deratingFunction
     */
    public ContinuousFunction getDeratingFunction() {
        return deratingFunction;
    }

    /**
     * This is an accessor method that can be used to set the
     * deratingFunction that is used to modify the search space.
     * @param deratingFunction The derating function to use
     * when modifying the search space.
     */
    public void setDeratingFunction(ContinuousFunction deratingFunction) {
        this.deratingFunction = deratingFunction;
    }

    /**
     * This method is used to calculate the normalised distance
     * of a value in the domain of the problem.
     * @param distance The distance to normalise
     * @return A value in the domain [0..1]
     */
    public double normalise(double distance) {
        // as the distance is not expected out of the functions range
        // the maximum expected distance will be from the lower bound to
        // the upper bound of the function.

        //double min = ((Quantitative) getFunction().getDomainComponent().getComponent(0)).getLowerBound().doubleValue();
        //double max = ((Quantitative) getFunction().getDomainComponent().getComponent(0)).getUpperBound().doubleValue();

        //double min = Domain.getInstance().getLowerBound(); // TODO: Should this be here?
        //double max = Domain.getInstance().getUpperBound(); // TODO: Should this be here?

        // calculate the maximum distance.
        //double max_distance = Math.abs(min - max);

        // normalise the distace in the range [0..1].
        //return distance / max_distance;
        return 0.0;
    }

    /**
     * Removes all solutions from the vectorSolutions.
     */
    public void clear() {
        vectorSolutions.clear();
    }

    /**
     * This method sets the function to evaluate (that is the search space).
     * @param function The function to evaluate.
     */
    public void setFunction(ContinuousFunction function) {
        super.setFunction(function);
    }
}
