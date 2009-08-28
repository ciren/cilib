/*
 * AverageErrorEndOfCycleError.java
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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * AverageBestErrorBeforeChange computes the average of the differences between best fitness of the swarm
 * and the maximum selected from specific iterations.
 * In an environment that changes periodically, this class selects the best fitness
 * at the end of each cycle averages them.
 * The output for a given iteration is the average of all the errors selected
 * so far.
 *
 * NOTE: for this measurement to be used, a resolution of 1 as to be used by the measurement
 * @author  Julien Duhain
 */
public class AverageBestErrorBeforeChange extends DynamicMeasurement{

    private static final long serialVersionUID = -2848258016113713942L;

    private int cycleSize = 50; //period between 2 changes in the environment
    private int cycleNr;

    public AverageBestErrorBeforeChange(){super();}

    public AverageBestErrorBeforeChange(AverageBestErrorBeforeChange copy) {
        this.setStateAware(copy.isStateAware());
        this.cycleSize = copy.cycleSize;
        this.cycleNr = copy.cycleNr;
        this.avg = copy.avg;
    }

    public AverageBestErrorBeforeChange getClone() {
        return new AverageBestErrorBeforeChange(this);
    }

    public synchronized Type getValue(Algorithm algorithm) {
        if((AbstractAlgorithm.get().getIterations()+1)%cycleSize == 0){
            double n = algorithm.getBestSolution().getFitness().getValue();
            ContinuousFunction func = ((FunctionMaximisationProblem) (algorithm.getOptimisationProblem())).getFunction();
            Real err = new Real(func.getMaximum() - n);

            avg = (avg * this.cycleNr + err.getReal()) / (this.cycleNr + 1);
            this.cycleNr++;
        }
        return new Real(avg);
    }

    public int getCycleSize() {
        return cycleSize;
    }

    public void setCycleSize(int cycleSize) {
        this.cycleSize = cycleSize;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        this.avg = in.readDouble();
        this.cycleNr = in.readInt();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(avg);
        out.writeInt(cycleNr);
    }

}
