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
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.functions.KnownOptimum;
import net.sourceforge.cilib.functions.Function;

/*
 * Measurement to calculate the difference between the best fitness
 * and the known optimum value of a function if it exists.
 */
public class FitnessMinusOptimum implements Measurement<Real> {

    private FunctionOptimum optimum;
    private Fitness fitness;

    public FitnessMinusOptimum() {
        optimum = new FunctionOptimum();
        fitness = new Fitness();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FitnessMinusOptimum getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        return Real.valueOf(fitness.getValue(algorithm).doubleValue()
                - optimum.getValue(algorithm).doubleValue());
    }

}
