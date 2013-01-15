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
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.Real;

/**
 * AverageBestFitnessBeforeChange computes the average of the best fitness of
 * the swarm selected from specific iterations.
 * In an environment that changes periodically, this class selects the best
 * fitness at the end of each cycle and averages them.
 * The output for a given iteration is the average of all the fitness's selected
 * so far.
 *
 * NOTE: For this measurement to be used, a resolution of 1 has to be set for
 * the measurement.
 *
 */
public class AverageBestFitnessBeforeChange extends DynamicMeasurement<Real> {

    private static final long serialVersionUID = -2848258016113713942L;
    private int cycleSize = 50; //period between 2 changes in the environment
    private int cycleNr = 0;

    public AverageBestFitnessBeforeChange() {
        super();
    }

    public AverageBestFitnessBeforeChange(AverageBestFitnessBeforeChange copy) {
        this.setStateAware(copy.isStateAware());
        this.cycleSize = copy.cycleSize;
        this.cycleNr = copy.cycleNr;
        this.avg = copy.avg;
    }

    @Override
    public AverageBestFitnessBeforeChange getClone() {
        return new AverageBestFitnessBeforeChange(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        if ((algorithm.getIterations() + 1) % cycleSize == 0) {
            Problem function = algorithm.getOptimisationProblem();
            double fitness = function.getFitness(algorithm.getBestSolution().getPosition()).getValue();
            avg = (avg * cycleNr + fitness) / (cycleNr + 1);
            cycleNr++;
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
