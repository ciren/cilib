/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.sampling;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Standard sampling method for angle modulation.
 */
public class StandardAngleModulationSampler extends ContinuousFunctionSampler {
    public Vector getSamples(ContinuousFunction f, Vector values, int samples) {
        Double[] sampleValues = new Double[samples];
        
        for (int i = 0; i < samples; i++) {
            Vector inputVector = constructInputVector(i, values);
            sampleValues[i] = f.apply(inputVector);
        }
        
        return Vector.of(sampleValues);
    }
}
