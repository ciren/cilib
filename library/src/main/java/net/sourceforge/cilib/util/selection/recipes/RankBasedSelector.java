/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;

/**
 * A recipe for Rank based selection.
 * <p>
 * Rank based selection is performed by:
 * <ol>
 *   <li>Sorting the list of elements in a natural ordering.</li>
 *   <li>Selecting portion of the elements that are better than the majority.</li>
 *   <li>Randomizing the sub list of elements and selecting an element from the randomised list.</li>
 *   <li>Return the result.</li>
 * </ol>
 * @param <E>
 */
public class RankBasedSelector<E extends Comparable> implements Selector<E> {

    private static final long serialVersionUID = -2387196820773731607L;
    private Comparator<E> comparator;

    /**
     * Create a new instance.
     */
    public RankBasedSelector() {
        this.comparator = Ordering.natural();
    }

    /**
     * Create a new instance with the provided {@link Comparator}.
     * @param comparator The comparator to use.
     */
    public RankBasedSelector(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RankBasedSelector(RankBasedSelector<E> copy) {
        this.comparator = copy.comparator;
    }

    /**
     * Set the comparator to use.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the comparator.
     * @return The current comparator.
     */
    public Comparator<E> getComparator() {
        return this.comparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        int size = Iterables.size(iterable);
        List<E> list = Selection.copyOf(iterable).orderBy(new SortedArrangement()).select(Samples.last(Rand.nextInt(size)+1));
        return Selection.copyOf(list).orderBy(new RandomArrangement());
    }
}
