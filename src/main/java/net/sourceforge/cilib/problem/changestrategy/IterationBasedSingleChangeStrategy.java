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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.cilib.problem.changestrategy;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.problem.Problem;

/**
 * Signal a change periodically based on the number of iteration.
 * At most one change will be signaled per iteration by an instance of this class.
 * @author Julien Duhain
 */
public class IterationBasedSingleChangeStrategy implements ChangeStrategy {

    private ControlParameter resolution;
    private int changeCounter=0;

    public IterationBasedSingleChangeStrategy(int numberOfIterationBetweenChanges) {
        this.resolution = new ConstantControlParameter(numberOfIterationBetweenChanges);
    }

    @Override
    public boolean shouldApply(Problem problem) {
        int iterations = AbstractAlgorithm.get().getIterations();
        if (iterations != 0 && iterations % Double.valueOf(resolution.getParameter()).intValue() == 0 && iterations!=changeCounter){
            changeCounter = iterations;
            return true;
        }

        return false;
    }

}
