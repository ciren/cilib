/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 *
 */
public class Fitness implements Measurement<Real> {
    private static final long serialVersionUID = 4152219744331703008L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        double fitness = algorithm.getBestSolution().getFitness().getValue();
        return Real.valueOf(fitness);
    }
}
