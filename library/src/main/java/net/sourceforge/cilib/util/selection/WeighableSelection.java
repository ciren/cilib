/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection;

import net.sourceforge.cilib.util.selection.weighting.Weighting;

/**
 *
 */
public interface WeighableSelection<T> {

    WeightedSelection<T> weigh(Weighting weighing);
}
