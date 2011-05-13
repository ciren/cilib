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
package net.sourceforge.cilib.functions.continuous.dynamic;

import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import org.junit.Test;

/**
 *
 * @author bennie
 */
public class GeneralizedMovingPeaksTest {

    @Test
    public void generalizedMovingPeaks() {
        int frequency = 1;
        int peaks = 3;
        double widthSeverity = 0.1;
        double heightSeverity = 3.0;
        double shiftSeverity = 2;
        double lambda = 1.0;

        FunctionMaximisationProblem problem = new FunctionMaximisationProblem();
        problem.setDomain("R(0.0, 100.0)^2");
        problem.setFunction(new GeneralizedMovingPeaks(frequency, peaks, widthSeverity, heightSeverity, shiftSeverity, lambda));

        PSO pso = new PSO();
        pso.setOptimisationProblem(problem);
        pso.addStoppingCondition(new MaximumIterations(100));

        pso.initialise();
        pso.run();
    }
}
