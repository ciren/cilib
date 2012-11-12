/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;

/**
 *
 */
public class FitnessEvaluations implements Measurement<Int> {
    private static final long serialVersionUID = 8843539724541605245L;

    /**
     * {@inheritDoc}
     */
    @Override
    public FitnessEvaluations getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int getValue(Algorithm algorithm) {
        int evaluations = algorithm.getOptimisationProblem().getFitnessEvaluations();
        return Int.valueOf(evaluations);
    }

}
