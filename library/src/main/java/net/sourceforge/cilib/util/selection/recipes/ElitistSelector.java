/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Ordering;
import java.util.Comparator;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.ReverseArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;

/**
 * A recipe for Elitist selection.
 * <p>
 * Elitist selection is performed by:
 * <ol>
 *   <li>Sorting the list of elements in a natural ordering.</li>
 *   <li>Selecting the last element from the list as it is the "best".</li>
 *   <li>Return the result.</li>
 * </ol>
 * @param <E> The selection type.
 */
public class ElitistSelector<E extends Comparable> implements Selector<E> {

    private static final long serialVersionUID = -5432603299031620114L;
    private Comparator<E> comparator;

    /**
     * Create a new instance with a defined comparator being
     * {@link Ordering#natural()}.
     */
    public ElitistSelector() {
        this.comparator = Ordering.natural();
    }

    /**
     * Create a new instance with the provided {@link Comparator}.
     * @param comparator The comparator to set.
     */
    public ElitistSelector(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public ElitistSelector(ElitistSelector<E> copy) {
        this.comparator = copy.comparator;
    }

    /**
     * Set the comparator to be used.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the current comparator.
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
