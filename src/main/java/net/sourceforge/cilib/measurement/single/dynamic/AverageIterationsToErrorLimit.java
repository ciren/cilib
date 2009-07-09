/*
 * AverageIterationsToErrorLimit.java
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

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionMaximisationProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * AverageIterationsToErrorLimit computes the average number of iterations needed to reach
 * an acceptable error value after a change.
 * The output for a given iteration is the average of all the measurements taken so far.
 *
 * NOTE: for this measurement to be used, a resolution of 1 as to be used by the measurement
 * @author  Julien Duhain
 */
public class AverageIterationsToErrorLimit extends DynamicMeasurement {

    private static final long serialVersionUID = -2848258016113713942L;
    private int cycleSize = 50; //period between 2 changes in the environment
    private int cycleNr = 0;
    private double limit; //the acceptable error limit
    private boolean flag = true; //false if the limit has been reach for the current cycle

    public AverageIterationsToErrorLimit(){super();}

    public AverageIterationsToErrorLimit(AverageIterationsToErrorLimit copy) {
        setStateAware(copy.isStateAware());
        this.cycleSize = copy.cycleSize;
        this.cycleNr = copy.cycleNr;
        this.avg = copy.avg;
        this.limit = copy.limit;
        this.flag = true;
    }

    public int getCycleSize() {
        return cycleSize;
    }

    public void setCycleSize(int cycleSize) {
        this.cycleSize = cycleSize;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public AverageIterationsToErrorLimit getClone() {
        return new AverageIterationsToErrorLimit(this);
    }

    public Type getValue(Algorithm algorithm) {
        int iteration = algorithm.getIterations();
        double n = algorithm.getBestSolution().getFitness().getValue();
        ContinuousFunction func = (ContinuousFunction)((FunctionMaximisationProblem)(algorithm.getOptimisationProblem())).getFunction();
        double err = (Double)func.getMaximum() - n;
        int score = this.cycleSize;

        if(flag &&(err <= limit || (iteration+1)%cycleSize == 0)){
            score = iteration-this.cycleNr*this.cycleSize;
            avg = (avg * this.cycleNr + score) / (this.cycleNr+1);
            cycleNr++;
            flag = false;
        }//if
        if((iteration+1)%cycleSize == 0){
            flag = true;
        }//if

        return new Real(avg);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        this.cycleNr = in.readInt();
        this.avg = in.readDouble();
        this.limit = in.readDouble();
        this.flag = in.readBoolean();

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(cycleNr);
        out.writeDouble(avg);
        out.writeDouble(limit);
        out.writeBoolean(flag);
    }
}
