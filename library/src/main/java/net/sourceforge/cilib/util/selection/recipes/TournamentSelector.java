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
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.util.selection.PartialSelection;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;
import net.sourceforge.cilib.util.selection.arrangement.ReverseArrangement;
import net.sourceforge.cilib.util.selection.arrangement.SortedArrangement;

/**
 * A recipe for Tournament selection.
 * <p>
 * Tournament selection is performed by:
 * <ol>
 *   <li>Randomly ordering a list of elements.</li>
 *   <li>Selecting a sublist of {@code tournamentProportion}.</li>
 *   <li>Sorting the created sublist.</li>
 *   <li>Selecting the best performing element.</li>
 *   <li>Return the result.</li>
 * </ol>
 *
 * @param <E> The selection type.
 */
public class TournamentSelector<E extends Comparable> implements Selector<E> {

    private static final long serialVersionUID = -6689673224380247931L;
    private ControlParameter tournamentProportion;
    private Comparator<E> comparator;

    /**
     * Create a new instance.
     */
    public TournamentSelector() {
        this.tournamentProportion = new ProportionalControlParameter();
        this.comparator = Ordering.natural();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public TournamentSelector(TournamentSelector<E> copy) {
        this.tournamentProportion = copy.tournamentProportion.getClone();
        this.comparator = copy.comparator;
    }

    /**
     * Get the proportion of the tournament.
     * @return The proportion of the tournament.
     */
    public ControlParameter getTournamentProportion() {
        return this.tournamentProportion;
    }

    /**
     * Set the proportion of the tournament.
     * @param tournamanetProportion The value to set.
     */
    public void setTournamentProportion(ControlParameter tournamentProportion) {
        this.tournamentProportion = tournamentProportion;
    }

    /**
     * Set the comparator for the selection.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the comparator for the selection.
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
        
        /**
         * Here we can reference to tournamentSize,
         * as the size = TournamentProportion*List.size
         */
        int tournamentSize = Double.valueOf(this.tournamentProportion.getParameter() * size).intValue();
        List<E> intermediate = Selection.copyOf(iterable).orderBy(new RandomArrangement()).select(Samples.last(tournamentSize));
        return Selection.copyOf(intermediate).orderBy(new SortedArrangement(comparator)).orderBy(new ReverseArrangement());
    }
}
