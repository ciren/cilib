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
 * AverageBestErrorBeforeChange computes the average of the differences between
 * best fitness of the swarm and the maximum selected from specific iterations.
 * In an environment that changes periodically, this class selects the best
 * fitness at the end of each cycle averages them.
 * The output for a given iteration is the average of all the errors selected
 * so far.
 *
 * NOTE: For this measurement to be used, a resolution of 1 has to be set for
 * the measurement
 */
public class AverageBestErrorBeforeChange extends DynamicMeasurement<Real> {

    private static final long serialVersionUID = -2848258016113713942L;
    private int cycleSize = 50; //period between 2 changes in the environment
    private int cycleNr;

    public AverageBestErrorBeforeChange() {
        super();
    }

    public AverageBestErrorBeforeChange(AverageBestErrorBeforeChange copy) {
        this.setStateAware(copy.isStateAware());
        this.cycleSize = copy.cycleSize;
        this.cycleNr = copy.cycleNr;
        this.avg = copy.avg;
    }

    @Override
    public AverageBestErrorBeforeChange getClone() {
        return new AverageBestErrorBeforeChange(this);
    }

    @Override
    public synchronized Real getValue(Algorithm algorithm) {
        if ((algorithm.getIterations() + 1) % cycleSize == 0) {
            DynamicOptimisationProblem function = (DynamicOptimisationProblem) algorithm.getOptimisationProblem();
            double error = function.getError(algorithm.getBestSolution().getPosition());
            this.avg = (this.avg * this.cycleNr + error) / (this.cycleNr + 1);
            this.cycleNr++;
        }
        return Real.valueOf(avg);
    }

    public int getCycleSize() {
        return cycleSize;
    }

    public void setCycleSize(int cycleSize) {
        this.cycleSize = cycleSize;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.avg = in.readDouble();
        this.cycleNr = in.readInt();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(avg);
        out.writeInt(cycleNr);
    }
}
