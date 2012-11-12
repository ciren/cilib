/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.util.selection;

import com.google.common.base.Predicate;

/**
 *
 */
public interface FunctionalSelection<T> {

    PartialSelection<T> exclude(T... items);

    PartialSelection<T> exclude(Iterable<T> items);

    PartialSelection<T> filter(Predicate<? super T> predicate);
}
