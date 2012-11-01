/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Type;

/**
 * Give the current value of the global optimum of the
 * function.
 *
 *
 */
public class GlobalMaximum implements Measurement {

    private static final long serialVersionUID = 2658868675629949642L;

    public GlobalMaximum() {
    }

    public GlobalMaximum(GlobalMaximum rhs) {
    }

    @Override
    public GlobalMaximum clone() {
        return new GlobalMaximum(this);
    }

    @Override
    public Type getValue(Algorithm algorithm) {
//        FunctionOptimisationProblem problem = (FunctionOptimisationProblem) algorithm.getOptimisationProblem();
//        double value = problem.getFunction().getMaximum().doubleValue();
//        return new Real(value);
        throw new UnsupportedOperationException("Implementation is required... this is not correct");
    }

    @Override
    public Measurement getClone() {
        return new GlobalMaximum(this);
    }
}
