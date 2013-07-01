/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.util.selection.weighting;

import net.cilib.util.selection.WeightedObject;

/**
 *
 */
public interface Weighting {

    <T> Iterable<WeightedObject> weigh(Iterable<T> iterable);
}
