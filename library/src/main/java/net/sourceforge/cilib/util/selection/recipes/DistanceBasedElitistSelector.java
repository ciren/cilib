/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import net.sourceforge.cilib.util.selection.arrangement.DistanceComparator;

/**
 * This class is similar to {@link ElitistSelector}, but where a
 * {@link DistanceComparator} is used. This class is necessary to select a
 * different comparator than the default one, due to the way in which the
 * archive has been implemented.
 *
 * @param <E> The selection type.
 */
public class DistanceBasedElitistSelector<E extends Comparable<? super E>> extends ElitistSelector<E> {

    /**
     * Create a new instance with a defined comparator being
     * {@link DistanceComparator}.
     */
    public DistanceBasedElitistSelector() {
        this.comparator = new DistanceComparator();
    }

}
