/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.weighting;

import net.sourceforge.cilib.util.selection.WeightedObject;

/**
 *
 */
public interface Weighting {

    <T> Iterable<WeightedObject> weigh(Iterable<T> iterable);
}
