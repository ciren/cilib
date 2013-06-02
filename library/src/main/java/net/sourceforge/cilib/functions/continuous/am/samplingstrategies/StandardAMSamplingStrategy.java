/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.am.samplingstrategies;

import fj.data.Array;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Generates the standard sampling points for angle modulation.
 */
public class StandardAMSamplingStrategy extends AbstractSamplingStrategy {
    @Override
    public Array<Vector> getSamplePoints(int samples, Vector values) {
        Vector[] points = new Vector[samples];
        
        for (int i = 0; i < samples; i++) {
            points[i] = constructInputVector(i, values);
        }
        
        return Array.array(points);
    }
}
