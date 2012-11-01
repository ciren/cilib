/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 *
 */
public class PercentageComplete implements Measurement<Real> {
    private static final long serialVersionUID = 552272710698138639L;

    @Override
    public PercentageComplete getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        AbstractAlgorithm alg = (AbstractAlgorithm) algorithm;
        return Real.valueOf(alg.getPercentageComplete());
    }

}
