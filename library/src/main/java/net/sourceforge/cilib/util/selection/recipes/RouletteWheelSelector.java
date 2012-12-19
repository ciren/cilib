/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.ProportionalArrangement;
import net.sourceforge.cilib.util.selection.arrangement.ReverseArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;
import net.sourceforge.cilib.util.selection.weighting.LinearWeighting;
import net.sourceforge.cilib.util.selection.weighting.Weighting;

/**
 * A recipe for Roulette wheel selection.
 * <p>
 * Roulette wheel selection is performed by:
 * <ol>
 *   <li>Weighing the elements of a selection.</li>
 *   <li>Performing a proportional ordering of the weighed elements.</li>
 *   <li>Returning the best result.</li>
 * </ol>
 * @param <E> The selection type.
 */
public class RouletteWheelSelector<E extends Comparable> implements Selector<E> {

    private static final long serialVersionUID = 4194450350205390514L;
    private Weighting weighting;

    /**
     * Create a new instance.
     */
    public RouletteWheelSelector() {
        this.weighting = new LinearWeighting();
    }

    /**
     * Create a new instance with the provided weighing strategy.
     * @param weighing The weighing strategy to set.
     */
    public RouletteWheelSelector(Weighting weighing) {
        this.weighting = weighing;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RouletteWheelSelector(RouletteWheelSelector<E> copy) {
        this.weighting = copy.weighting;
    }

    /**
     * Set the weighing strategy
     * @param weighing The strategy to set.
     */
    public void setWeighing(Weighting weighing) {
        this.weighting = weighing;
    }

    /**
     * Get the current weighing strategy.
     * @return The current weighing strategy.
     */
    public Weighting getWeighing() {
        return this.weighting;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartialSelection<E> on(Iterable<E> iterable) {
        return Selection.copyOf(iterable).weigh(weighting)
                .orderBy(new SortedArrangement())
                .orderBy(new ProportionalArrangement())
                .orderBy(new ReverseArrangement());
    }
}
