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
import java.util.Collection;
import java.util.List;
import net.sourceforge.cilib.functions.continuous.derating.DeratingFunction;
import net.sourceforge.cilib.functions.continuous.derating.PowerDeratingFunction;
import net.sourceforge.cilib.problem.objective.Maximise;
import net.sourceforge.cilib.problem.solution.Fitness;
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
public class DeratingOptimisationProblem extends FunctionOptimisationProblem {

    private List<Vector> solutions;
    private DeratingFunction deratingFunction;
    private DistanceMeasure distanceMeasure;

    /**
     * The default constructor.
     */
    public DeratingOptimisationProblem() {
        this.objective = new Maximise();
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.deratingFunction = new PowerDeratingFunction();
        this.solutions = Lists.<Vector>newLinkedList();
    }

    /**
     * Copy constructor.
     */
    public DeratingOptimisationProblem(DeratingOptimisationProblem copy) {
        super(copy);
        this.distanceMeasure = copy.distanceMeasure;
        this.deratingFunction = copy.deratingFunction;
        this.solutions = Lists.<Vector>newLinkedList(copy.solutions);
    }

    @Override
    public DeratingOptimisationProblem getClone() {
        return new DeratingOptimisationProblem(this);
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        Vector input = (Vector) solution;
        double fitness = super.calculateFitness(input).getValue();

        for (Vector v : solutions) {
            double distance = distanceMeasure.distance(input.normalize(), v.normalize());

            if (distance < deratingFunction.getRadius()) {
                fitness *= getDeratingFunction().apply(Vector.of(distance));
            }
        }

        return objective.evaluate(fitness);
    }

    public void addSolution(Vector solution) {
        solutions.add(solution);
    }

    public void addSolutions(Collection<Vector> solutions) {
        this.solutions.addAll(solutions);
    }

    public List<Vector> getSolutions() {
        return solutions;
    }

    public void clearSolutions() {
        solutions.clear();
    }

    public DeratingFunction getDeratingFunction() {
        return deratingFunction;
    }

    public void setDeratingFunction(DeratingFunction deratingFunction) {
        this.deratingFunction = deratingFunction;
    }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }
}
