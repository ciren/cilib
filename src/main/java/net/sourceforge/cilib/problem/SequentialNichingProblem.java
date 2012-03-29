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
package net.sourceforge.cilib.problem;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.niching.PowerDeratingFunction;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * <p>Title: SequentialNichingProblem </p>
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
 */
public class SequentialNichingProblem extends FunctionMaximisationProblem {

    private static final long serialVersionUID = 6513411928705015979L;
    
    private List<Vector> solutions;
    private ContinuousFunction deratingFunction;
    private DistanceMeasure distanceMeasure;

    /**
     * The default constructor.
     */
    public SequentialNichingProblem() {
        super();

        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.deratingFunction = new PowerDeratingFunction();
        this.solutions = Lists.<Vector>newLinkedList();
    }

    /**
     * Calculates the fitness of a solution with respect to the
     * modifications to the search space.
     * @param solution The solution to calculate the fitness.
     * @return The fitness of the solution.
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        // set the initial fitness to the actual fitness value without modification.
        double fitness = (super.calculateFitness(solution).getValue()).doubleValue();
        double radius = ((PowerDeratingFunction) deratingFunction).getRadius();

        // iterate through the number of solutions found that are above the solution
        // threshold and modify the fitness using the derating function.
        for (Vector v : solutions) {
            // calcaulate the distance between the solution and the previousely found
            // solution.
            double distance = distanceMeasure.distance((Vector) solution, v);

            // normalise the distance in the range [0..1].
            // modify the fitness.
            if (distance < radius) {
                fitness *= getDeratingFunction().apply(Vector.of(distance));
            }
        }
        
        return new MaximisationFitness(new Double(fitness));
    }

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
}
