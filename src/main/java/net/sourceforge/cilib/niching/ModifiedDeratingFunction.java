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
package net.sourceforge.cilib.niching;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.functions.ContinuousFunction;
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
public class ModifiedDeratingFunction implements ContinuousFunction {

    private List<Vector> solutions;
    private DeratingFunction deratingFunction;
    private DistanceMeasure distanceMeasure;
    private ContinuousFunction originalFunction;

    /**
     * The default constructor.
     */
    public ModifiedDeratingFunction() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.deratingFunction = new PowerDeratingFunction();
        this.solutions = Lists.<Vector>newLinkedList();
    }

    @Override
    public Double apply(Vector solution) {
        double fitness = originalFunction.apply(solution);
        double radius = deratingFunction.getRadius();

        // iterate through the number of solutions found that are above the solution
        // threshold and modify the fitness using the derating function.
        for (Vector v : solutions) {
            double distance = distanceMeasure.distance((Vector) solution, v);

            //TODO: normalise the distance in the range [0..1].
            
            if (distance < radius) {
                fitness *= getDeratingFunction().apply(Vector.of(distance));
            }
        }
        
        return fitness;
    }
    
    public void addSolution(Vector solution) {
        solutions.add(solution);
    }

    public DeratingFunction getDeratingFunction() {
        return deratingFunction;
    }

    public void setDeratingFunction(DeratingFunction deratingFunction) {
        this.deratingFunction = deratingFunction;
    }
    
    public ContinuousFunction getOriginalFunction() {
        return originalFunction;
    }

    public void setOriginalFunction(ContinuousFunction originalFunction) {
        this.originalFunction = originalFunction;
    }
}
