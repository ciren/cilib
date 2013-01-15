/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.dynamic;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.problem.DynamicOptimisationProblem;
import net.sourceforge.cilib.type.types.Real;

/**
 * AverageIterationsToErrorLimit computes the average number of iterations
 * needed to reach an acceptable error value after a change.
 * The output for a given iteration is the average so far.
 *
 * NOTE: For this measurement to be used, a resolution of 1 has to be set for
 * the measurement.
 */
public class AverageIterationsToErrorLimit extends DynamicMeasurement<Real> {

    private static final long serialVersionUID = -2848258016113713942L;
    private int cycleSize = 50; //period between 2 changes in the environment
    private int cycleNr = 0;
    private double limit; //the acceptable error limit
    private boolean flag = true; //false if the limit has been reach for the current cycle

    public AverageIterationsToErrorLimit() {
        super();
    }

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

    @Override
    public AverageIterationsToErrorLimit getClone() {
        return new AverageIterationsToErrorLimit(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        int iteration = algorithm.getIterations();

        DynamicOptimisationProblem function = (DynamicOptimisationProblem) algorithm.getOptimisationProblem();
        double error = function.getError(algorithm.getBestSolution().getPosition());
        int score = this.cycleSize;

        if (flag && (error <= limit || (iteration) % cycleSize == 0)) {
            score = iteration - this.cycleNr * this.cycleSize;
            avg = (avg * this.cycleNr + score) / (this.cycleNr + 1);
            cycleNr++;
            flag = false;
        }//if

        if ((iteration) % cycleSize == 0 && !algorithm.isFinished()) {
            flag = true;
        }//if

        return Real.valueOf(avg);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        this.cycleNr = in.readInt();
        this.avg = in.readDouble();
        this.flag = in.readBoolean();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(cycleNr);
        out.writeDouble(avg);
        out.writeBoolean(flag);
    }
}
