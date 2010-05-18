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
package net.sourceforge.cilib.problem.changestrategy;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.problem.Problem;

/**
 * An {@code IterationBasedChangeStrategy} is a test to ensure that a problem is
 * altered or changed at a predefined frequency.
 * <p>
 * The default behaviour is to return {@code true} for all iterations. ie: the frequency
 * paramater {@code resolution} is set to have a value of {@code 1.0}.
 * </p>
 * @see ControlParameter
 *
 * @author gpampara
 */
public class IterationBasedChangeStrategy implements ChangeStrategy {

    private ControlParameter resolution;

    /**
     * Create a new instance.
     */
    public IterationBasedChangeStrategy() {
        this.resolution = new ConstantControlParameter(1.0);
    }

    /**
     * Determine if a change should be applied to the current problem, based
     * on the current number of iterations that have been completed.
     * @param problem The problem to query.
     * @return {@code true} if a change should be performed, {@code false} otherwise.
     */
    @Override
    public boolean shouldApply(Problem problem) {
        int iterations = AbstractAlgorithm.get().getIterations();
        if (iterations != 0 && iterations % Double.valueOf(resolution.getParameter()).intValue() == 0)
           return true;

        return false;
    }

}
