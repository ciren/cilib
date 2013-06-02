/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.am.samplingstrategies;

import fj.data.Array;
import net.sourceforge.cilib.type.types.container.Vector;

public interface SamplingStrategy {
    Array<Vector> getSamplePoints(int samples, Vector values);
}
