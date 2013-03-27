/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import java.util.Comparator;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.DistanceComparator;
import net.sourceforge.cilib.util.selection.arrangement.ReverseArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;

/**
 * This class is similar to {@link ElitistSelector}, but where a
 * {@link DistanceComparator} is used. This class is necessary to select a
 * different comparator than the default one, due to the way in which the
 * archive has been implemented.
 *
 * @param <E> The selection type.
 */
public class DistanceBasedElitistSelector<E extends Comparable<? super E>> implements Selector<E> {

    private static final long serialVersionUID = -5432603299031620114L;
    private Comparator<E> comparator;

    /**
     * Create a new instance with a defined comparator being
     * {@link DistanceComparator}.
     */
    public DistanceBasedElitistSelector() {
        this.comparator = new DistanceComparator();
    }

    /**
     * Create a new instance with the provided {@link Comparator}.
     *
     * @param comparator The comparator to set.
     */
    public DistanceBasedElitistSelector(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Create a copy of the provided instance.
     *
     * @param copy The instance to copy.
     */
    public DistanceBasedElitistSelector(ElitistSelector<E> copy) {
        this.comparator = copy.getComparator();
    }

    /**
     * Set the comparator to be used.
     *
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the current comparator.
     *
     * @return The current comparator instance.
     */
    public Comparator<E> getComparator() {
        return this.comparator;
    }

    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        return Selection.copyOf(iterable).orderBy(new SortedArrangement())
                .orderBy(new ReverseArrangement());
    }
}
