/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import net.sourceforge.cilib.util.selection.PartialSelection;


/**
 * A recipe is a series of steps that need to be followed to achieve a
 * selection.
 * @param <E> The selection type.
 */
public interface Selector<E> {

    /**
     * Perform the selection process.
     * @param iterable The elements to perform the selection on.
     * @return The selected element.
     */
    PartialSelection<E> on(Iterable<E> iterable);
}
