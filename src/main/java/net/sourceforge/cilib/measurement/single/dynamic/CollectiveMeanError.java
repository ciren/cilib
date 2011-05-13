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
package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.DynamicOptimizationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * CollectiveMeanError computes the average of the differences between best
 * fitness of the swarm and the maximum over all iterations so far.
 * The output for a given iteration is the average of all the errors so far.
 *
 * NOTE: For this measurement to be used, a resolution of 1 has to be set for
 * the measurement.
 *
 * @author  Julien Duhain
 */
public class CollectiveMeanError extends DynamicMeasurement {

    private static final long serialVersionUID = -2848258016113713942L;

    public CollectiveMeanError() {
        super();
    }

    public CollectiveMeanError(CollectiveMeanError copy) {
        this.setStateAware(copy.isStateAware());
        this.avg = copy.avg;
    }

    @Override
    public CollectiveMeanError getClone() {
        return new CollectiveMeanError(this);
    }

    @Override
    public Type getValue(Algorithm algorithm) {
        int iteration = algorithm.getIterations();
        DynamicOptimizationProblem function = (DynamicOptimizationProblem) algorithm.getOptimisationProblem();
        double error = function.getError(algorithm.getBestSolution().getPosition());
        avg = (avg * (iteration - 1) + error) / (iteration);
        return Real.valueOf(avg);
    }
}
