/*
 * CollectiveMeanError.java
 *
 * Created on February 4, 2003, 8:25 PM
 *
 *
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
 *
 */

package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * CollectiveMeanError computes the average of the differences between best fitness of the swarm
 * and the maximum over all iterations so far.
 * The output for a given iteration is the average of all the errors so far.
 *
 * NOTE: for this measurement to be used, a resolution of 1 as to be used by the measurement
 * @author  Julien Duhain
 */
public class CollectiveMeanError extends DynamicMeasurement {

    private static final long serialVersionUID = -2848258016113713942L;

    public CollectiveMeanError(){super();}

    public CollectiveMeanError(CollectiveMeanError copy) {
        this.setStateAware(copy.isStateAware());
        this.avg = copy.avg;
    }

    public CollectiveMeanError getClone() {
        return new CollectiveMeanError(this);
    }

    public Type getValue(Algorithm algorithm) {
        int iteration = algorithm.getIterations();
        double n = algorithm.getBestSolution().getFitness().getValue();
        ContinuousFunction func = (ContinuousFunction)((FunctionMaximisationProblem)(algorithm.getOptimisationProblem())).getFunction();
        double err = (Double)func.getMaximum() - n;
        avg = (avg * (iteration-1) + err) / (iteration);
        return new Real(avg);
    }
}
