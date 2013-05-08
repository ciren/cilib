/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.sampling;

import net.sourceforge.cilib.functions.Function;

/**
 * An interface for function sampling.
 */
public interface Sampler<F extends Function, T> {
    T getSamples(F function, T values, int samples);
}
