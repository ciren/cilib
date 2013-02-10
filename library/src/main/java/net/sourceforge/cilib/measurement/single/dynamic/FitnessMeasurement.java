/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.dynamic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * Calculate the actual fitness of the particle at their current position.
 * This measurement should be used on dynamic algorithms where the pbest of the
 * particles may be inaccurate (outdated).
 *
 *
 */
public class FitnessMeasurement implements Measurement {

    private static final long serialVersionUID = 2632671785674388015L;

    @Override
    public Type getValue(Algorithm algorithm) {
        return Real.valueOf(algorithm.getOptimisationProblem().getFitness(algorithm.getBestSolution().getPosition()).getValue());
    }

    @Override
    public Measurement getClone() {
        return this;
    }
}
