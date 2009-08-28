/*
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

package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * Calculate the error between the current best value of the swarm and the global optimum of the
 * function.
 *
 * @author Anna Rakitianskaia, Julien Duhain
 *
 */
public class ErrorMeasurement implements Measurement {
    private static final long serialVersionUID = 2632671785674388015L;

    @Override
    public String getDomain() {
        return "R";
    }

    @Override
    public Type getValue(Algorithm algorithm) {
        double n = algorithm.getBestSolution().getFitness().getValue();
           ContinuousFunction func = ((FunctionMaximisationProblem) (AbstractAlgorithm.get().getOptimisationProblem())).getFunction();
           Real err = new Real(func.getMaximum() - n);
        return err;
    }

    @Override
    public Measurement getClone() {
        return this;
    }
}
