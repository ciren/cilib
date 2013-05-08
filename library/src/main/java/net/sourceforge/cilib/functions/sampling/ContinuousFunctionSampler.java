/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.sampling;

import java.util.ArrayList;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Abstract class for continuous function sampling.
 */
public abstract class ContinuousFunctionSampler implements Sampler<ContinuousFunction, Vector> {
    protected Vector constructInputVector(double x, Vector values) {
        ArrayList<Numeric> inputValues = new ArrayList<Numeric>(values);

        inputValues.add(0, Real.valueOf(x));
        Numeric[] arr = inputValues.toArray(new Numeric[0]);
        
        return Vector.of(arr);
    }
}
