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
 * Measurement to obtain the known optimum value of a function
 * if it exists.
 */
public class FunctionOptimum implements Measurement<Real> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionOptimum getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Function f = ((FunctionOptimisationProblem)algorithm.getOptimisationProblem()).getFunction();
        return Real.valueOf(((KnownOptimum<Double>)f).getOptimum());
    }

}
