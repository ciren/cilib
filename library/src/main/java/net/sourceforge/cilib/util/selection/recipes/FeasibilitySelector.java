/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.UnconstrainedBoundary;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;

public class FeasibilitySelector <E extends Individual> implements Selector<E>{
    private BoundaryConstraint constraint;

    /*
     * Default constructor for FeasibilitySelector
     */
    public FeasibilitySelector() {
        constraint = new UnconstrainedBoundary();
    }

    /*
     * Copy constructor for FeasibilitySelector
     * @param new instance of FeasibilitySelector
     */
    public FeasibilitySelector(FeasibilitySelector copy) {
        constraint = copy.constraint;
    }

    /*
     * Clone method of FeasibilitySelector
     * @return The new instance of FeasibilitySelector
     */
    public FeasibilitySelector getClone() {
        return new FeasibilitySelector(this);
    }

    /*
     * Inner class holding an individual and its sum of constraintViolation
     */
    protected class ExtendedIndividual {
        private E individual;
        private double sumOfConstrainViolation;

        /*
         * Gets the Individual
         * @return The individual
         */
        public E getIndividual() {
            return individual;
        }

        /*
         * Sets the Individual to the one received as a parameter
         * @param individual The Individual to be returned
         */
        public void setIndividual(E individual) {
            this.individual = individual;
        }

        /*
         * Gets the sum of constraint violation calculated during the separation between feasible and infeasible solutions
         * @return sumOfConstraintViolation The distance between where the vector is and where the vector can be
         */
        public double getSumOfConstrainViolation() {
            return sumOfConstrainViolation;
        }

        /*
         * Sets the sum of constraint violation calculated during the separation between feasible and infeasible solutions
         * @return sumOfConstraintViolation The distance between where the vector is and where the vector can be
         */
        public void setSumOfConstrainViolation(double sumOfConstrainViolation) {
            this.sumOfConstrainViolation = sumOfConstrainViolation;
        }
    }

    /*
     * Selects among the feasible and infeasible solutions
     * @param iterable This is the iterable container holding the solutions among which one must be selected
     * @return a PartialSelection holding the objects retrieved
     */
    public PartialSelection<E> on(Iterable<E> iterable) {
        E bestIndividual = iterable.iterator().next();
        E temp;
        double difference;
        ArrayList feasibleEntities = new ArrayList();
        ArrayList inFeasibleEntities = new ArrayList();
        E winningIndividual;
        EuclideanDistanceMeasure euclideanDistanceMeasure = new EuclideanDistanceMeasure();

        for(E current : iterable) {
           temp = (E) current.getClone();

           constraint.enforce(temp);
           difference = euclideanDistanceMeasure.distance(temp.getCandidateSolution(), current.getCandidateSolution());

           if(difference == 0) {
               feasibleEntities.add(current);
           } else {
               ExtendedIndividual newIndividual = new ExtendedIndividual();
               newIndividual.setIndividual(current);
               newIndividual.setSumOfConstrainViolation(difference);
               inFeasibleEntities.add(newIndividual);
           }
        }

        if(feasibleEntities.size() > 0) {
            return Selection.copyOf(Arrays.asList(selectBestOfFeasible(feasibleEntities)));
        }

        return Selection.copyOf(Arrays.asList(selectBestOfInfeasible(inFeasibleEntities)));

    }

    /*
     * Selects the best among a set of feasible solutions
     * @param iterable The set of feasible solutions
     * @return The best individual among the feasible solutions
     */
    protected E selectBestOfFeasible(Iterable<E> iterable) {
        E bestIndividual = iterable.iterator().next();

        for(E current : iterable) {
           if(current.getFitness().compareTo(bestIndividual.getBestFitness()) > 0) {
               bestIndividual = (E) current.getClone();
           }
        }

        return bestIndividual;
    }

    /*
     * Selects the individual with the lowest sum of constraint violation among a set of infeasible solutions
     *@param iterable The set of infeasible solutions
     *@return The best individual among these infeasible solutions
     */
    protected E selectBestOfInfeasible(Iterable<ExtendedIndividual> iterable) {
        E bestIndividual = iterable.iterator().next().getIndividual();
        ExtendedIndividual temp;
        double sumOfConstraintViolation = Double.POSITIVE_INFINITY;
        double sumPerDimension;
        Vector difference;

        for(ExtendedIndividual current : iterable) {
            sumPerDimension = current.getSumOfConstrainViolation();
            if(sumPerDimension < sumOfConstraintViolation ) {
               bestIndividual = (E) current.getIndividual().getClone();
               sumOfConstraintViolation = sumPerDimension;
            }

        }

        return bestIndividual;
    }

    /*
     * Gets the Boundary Constraint currently being used
     * @return The boundary constraint
     */
    public BoundaryConstraint getConstraint() {
        return constraint;
    }

    /*
     * Sets the boundary constraint currently being used to the one received as a parameter
     * @param constraint The new constraint
     */
    public void setConstraint(BoundaryConstraint constraint) {
        this.constraint = constraint;
    }


}
